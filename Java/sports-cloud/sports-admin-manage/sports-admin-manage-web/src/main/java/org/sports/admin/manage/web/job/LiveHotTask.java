package org.sports.admin.manage.web.job;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.entity.LiveDO;
import org.sports.admin.manage.service.service.*;
import org.sports.admin.manage.service.vo.LiveHeatConfigVO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 直播间热度值
 */
@Component
@Slf4j
public class LiveHotTask {

    @Resource
    private ILiveService liveService;
    @Resource
    private IChatGroupUserService chatGroupUserService;
    @Resource
    private ILiveHeatConfigService liveHeatConfigService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private IAppUserLiveShareService appUserLiveShareService;
    @Resource
    private IChatMessageService chatMessageService;


    /**
     * 批量计算直播间热度
     * 每5分钟执行一次
     */
    @Scheduled(initialDelay = 10000, fixedRate = 1000 * 60 * 5)
    void countLiveHotBatch() {
        String key = "job:countLiveHotBatch";
        RLock lock = redissonClient.getLock(key);
        if(lock.tryLock()){
            try {
                //查询当前直播中的live
                List<LiveDO> liveDoList = liveService.getAllLiveing(null);
                if(CollectionUtils.isEmpty(liveDoList)){
                    return;
                }
                log.info("【批量计算直播间热度开始】当前处理数量：" + liveDoList.size());
                //获取对应直播对应在线人数
                List<String> liveIdList = liveDoList.stream().map(item->item.getId().toString()).collect(Collectors.toList());
                Map<String, Integer> userCountMap = chatGroupUserService.getLiveUserByGroupIds(liveIdList);
                //获取对应直播发送消息数
                Map<String, Integer> msgSendCountMap = chatMessageService.getLiveMsgSendByGroupIds(liveIdList);
                //获取对应直播分享次数
                Map<String, Integer> shareCountMap = appUserLiveShareService.getLiveShareByGroupIds(liveIdList);
                //获取热度直播配置
                LiveHeatConfigVO liveHeatConfigVO = liveHeatConfigService.getLiveHeatConfig();
                LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
                List<LiveDO> updateList = new ArrayList<>();
                LiveDO sLiveDao;
                for (LiveDO liveDO : liveDoList) {
                    /**
                     * 总热度 = 初始热度+在线人数热度+直播间发送消息数热度+分享直播次数热度
                     * 初始热度获取：1假如缓存里面包含上一次直播热度，优先获取  2假如缓存里面没有 则获取初始热度（ LiveDO对象中总热度）
                     */
                    // 初始热度处理
                    if(liveDO.getHotInitValue() == null || liveDO.getHotInitValue() == 0L){
                        if(liveDO.getHotValue() > liveHeatConfigVO.getBaseHeatHot()){
                            //历史异常数据处理
                            liveDO.setHotInitValue((long)liveHeatConfigVO.getBaseHeatHot());
                        }else{
                            liveDO.setHotInitValue(liveDO.getHotValue());
                        }
                    }
                    //总热度初始化（初始热度赋值）
                    Long hotValue = liveDO.getHotInitValue();
//                    //当前直播时间热度：1获取有多少个30分钟 2根据次数 每次根据上下限范围随机获取热度叠加
//                    int totalNo = getThirtyNum(liveDO.getOpenTime(), nowTime);
//                    if(totalNo > 0){
//                        long hotTimeValue = 0;
//                        if(liveDO.getHotTimeNo() == null){
//                            //首次处理
//                            hotTimeValue = getThirtyHotValue(totalNo, liveHeatConfigVO);
//                            liveDO.setHotTimeNo(totalNo);
//                            liveDO.setHotTimeValue(hotTimeValue);
//                        }else if(totalNo > liveDO.getHotTimeNo()){
//                            hotTimeValue = getThirtyHotValue(totalNo - liveDO.getHotTimeNo(), liveHeatConfigVO) + liveDO.getHotTimeValue();
//                            liveDO.setHotTimeNo(totalNo);
//                            liveDO.setHotTimeValue(hotTimeValue);
//                        }else{
//                            hotTimeValue = liveDO.getHotTimeValue();
//                        }
//                        hotValue += hotTimeValue;
//                    }
                    //在线人数热度
                    if(userCountMap.containsKey(liveDO.getId().toString())){
                        hotValue += (long)userCountMap.get(liveDO.getId().toString()) * liveHeatConfigVO.getPeopleNumberRatio();
                    }
                    //直播间发送消息数热度
                    if(msgSendCountMap.containsKey(liveDO.getId().toString())){
                        hotValue += (long)msgSendCountMap.get(liveDO.getId().toString()) * liveHeatConfigVO.getMsgSendRatio();
                    }
                    //分享直播次数热度
                    if(shareCountMap.containsKey(liveDO.getId().toString())){
                        hotValue += (long)shareCountMap.get(liveDO.getId().toString()) * liveHeatConfigVO.getShareRatio();
                    }
//                    liveDO.setHotValue(hotValue);
                    sLiveDao = new LiveDO();
                    sLiveDao.setId(liveDO.getId());
                    sLiveDao.setHotInitValue(liveDO.getHotInitValue());
//                    sLiveDao.setHotTimeNo(liveDO.getHotTimeNo());
//                    sLiveDao.setHotTimeValue(liveDO.getHotTimeValue());
                    sLiveDao.setHotValue(hotValue);
                    updateList.add(sLiveDao);
                }
                liveService.updateLiveHotValueBatch(updateList);
                log.info("【批量计算直播间热度结束】");
            }finally {
                lock.unlock();
            }
        }

    }

//    /**
//     * 根据次数 每次随机获取热度
//     * @param thirtyNum
//     * @return
//     */
//    private Integer getThirtyHotValue(int thirtyNum, LiveHeatConfigVO liveHeatConfigVO){
//        if(thirtyNum < 1){
//            return 0;
//        }
//        int randomNum = RandomUtil.randomInt(liveHeatConfigVO.getThirtyAddLower(), liveHeatConfigVO.getThirtyAddUpper());
//        if(thirtyNum == 1){
//            return randomNum;
//        }
//        return randomNum + getThirtyHotValue(thirtyNum-1, liveHeatConfigVO);
//    }
//
//    /**
//     * 获取有多少个30分钟
//     * @param openTime
//     * @param nowTime
//     * @return
//     */
//    private int getThirtyNum(LocalDateTime openTime, LocalDateTime nowTime){
//        return  (int)((Timestamp.valueOf(nowTime).getTime() - Timestamp.valueOf(openTime).getTime())/1000/60/30);
//    }

}
