/**
 *
 */
package org.sports.chat.web.service;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.logging.log4j.util.Strings;
import org.jim.core.ImChannelContext;
import org.jim.core.packets.LoginReqBody;
import org.jim.core.packets.LoginRespBody;
import org.jim.core.packets.User;
import org.jim.core.packets.UserStatusType;
import org.jim.server.processor.login.LoginCmdProcessor;
import org.jim.server.protocol.AbstractProtocolCmdProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sports.admin.manage.dao.entity.LiveUserDO;
import org.sports.admin.manage.dao.enums.IdentityType;
import org.sports.admin.manage.dao.mapper.LiveUserMapper;
import org.sports.admin.manage.service.service.IAppUserService;
import org.sports.admin.manage.service.vo.AppUserVO;
import org.sports.chat.service.util.CheckLoginUtil;
import org.sports.chat.service.util.LRUCache;
import org.sports.chat.service.util.PushMessageUtil;
import org.sports.springboot.starter.satoken.enums.UserChannelEnum;

import java.util.Objects;

/**

 */
public class LoginServiceProcessor extends AbstractProtocolCmdProcessor implements LoginCmdProcessor {

    private Logger logger = LoggerFactory.getLogger(LoginServiceProcessor.class);

    /**
     * todo 后续用户量增加可优化缓存方案，在用户退出登录时再缓存信息，比现在这样命中率高很多。
     */
    private static final LRUCache<String, User> USER_LRU_CACHE = new LRUCache<>(10000);

    private IAppUserService appUserService = SpringUtil.getBean(IAppUserService.class);
    private LiveUserMapper liveUserMapper = SpringUtil.getBean(LiveUserMapper.class);
    private CheckLoginUtil checkLoginUtil = SpringUtil.getBean(CheckLoginUtil.class);

    private PushMessageUtil pushMessageUtil = SpringUtil.getBean(PushMessageUtil.class);

    /**
     * 根据用户名和密码获取用户
     *
     * @param loginReqBody
     * @param imChannelContext
     * @return

     */
    @Override
    public User getUser(LoginReqBody loginReqBody, ImChannelContext imChannelContext) {
        String userId = loginReqBody.getUserId();
        String loginType = loginReqBody.getLoginType();
        if (Strings.isBlank(userId)) {
            logger.error("userId是空，从token中获取用户信息，loginReqBody ={}", JSON.toJSONString(loginReqBody));
            if (Objects.isNull(loginReqBody.getToken())) {
                return null;
            }
            AppUserVO userByToken = checkLoginUtil.getUserByToken(loginReqBody.getToken());
            logger.info("根据token获取用户信息：{}", JSON.toJSONString(userByToken));
            if (Objects.isNull(userByToken)) {
                return null;
            }
            userId = String.valueOf(userByToken.getId());
            if (Objects.isNull(loginType)) {
                loginType = userByToken.getChannel();
            }
        }

        User user = USER_LRU_CACHE.get(userId);
        if (Objects.nonNull(user)) {
            return user;
        }
        // 判断用户类型
        if (UserChannelEnum.APP.getFlag().equals(loginType)) {
            logger.info("login userId = " + userId);
            AppUserVO appUserById = appUserService.getCacheAppUserById(Long.valueOf(userId));
            if (ObjectUtils.isNull(appUserById)) {
                logger.warn("未查询到该APP用户，请联系管理员重试！");
                return null;
            }
            user = User.builder()
                    .userId(userId)
                    .nick(appUserById.getName())
                    .avatar(appUserById.getHead())
                    .status(UserStatusType.ONLINE.getStatus())
                    .lvName(appUserById.getLvName())
                    .loginType(loginType)
                    .lvNum(appUserById.getLvNum())
                    .identity(IdentityType.APP_USER.getCode())
                    .build();
        } else if (UserChannelEnum.LIVE.getFlag().equals(loginType)) {
            LambdaQueryWrapper<LiveUserDO> select = Wrappers.lambdaQuery(LiveUserDO.class).eq(LiveUserDO::getId, userId).select(LiveUserDO::getIdentityType);
            LiveUserDO aDo = liveUserMapper.selectOne(select);
            if (Objects.isNull(aDo)) {
                logger.warn("未查询到该LIVE用户，请联系管理员重试！");
                return null;
            }
            user = User.builder()
                    .userId(userId)
                    .nick(aDo.getName())
                    .avatar(aDo.getHead())
                    .loginType(loginType)
                    .status(UserStatusType.ONLINE.getStatus())
                    .identity(aDo.getIdentityType())
                    .build();
        }
        USER_LRU_CACHE.put(userId, user);
        return user;
    }

    /**
     * @param builder
     */
//	public void initFriends(User.Builder builder){
//		Group myFriend = Group.newBuilder().groupId("1").name("我的好友").build();
//		List<User> myFriendGroupUsers = new ArrayList();
//		User user1 = User.newBuilder()
//				.userId(UUIDSessionIdGenerator.instance.sessionId(null))
//				.nick(familyName[RandomUtil.randomInt(0, familyName.length - 1)] + secondName[RandomUtil.randomInt(0, secondName.length - 1)])
//				.avatar(nextImg())
//				.build();
//		myFriendGroupUsers.add(user1);
//		myFriend.setUsers(myFriendGroupUsers);
//		builder.addFriend(myFriend);
//	}

//	public String nextImg() {
//		return ImgMnService.nextImg();
//	}

    /**
     * 登陆成功返回状态码:ImStatus.C10007
     * 登录失败返回状态码:ImStatus.C10008
     * 注意：只要返回非成功状态码(ImStatus.C10007),其他状态码均为失败,此时用户可以自定义返回状态码，定义返回前端失败信息
     */
    @Override
    public LoginRespBody doLogin(LoginReqBody loginReqBody, ImChannelContext imChannelContext) {
        if (checkLoginUtil.checkToken(loginReqBody.getToken())) {
            return LoginRespBody.success();
        } else {
            return LoginRespBody.failed();
        }
    }

    @Override
    public void onSuccess(User user, ImChannelContext channelContext) {
        if(Objects.isNull(user)){
            return;
        }
        //推送用户未读消息总数
        pushMessageUtil.pushUnReadMsgCount(user);
        //app用户登录成功后需要推送用户关注的主播中热度值最高的一个
        if(IdentityType.APP_USER.getCode().equals(user.getIdentity())){
            pushMessageUtil.pushAppUserMaxHotLive(user.getUserId());
        }
    }

    @Override
    public void onFailed(ImChannelContext imChannelContext) {
        logger.info("登录失败回调方法");
    }
}
