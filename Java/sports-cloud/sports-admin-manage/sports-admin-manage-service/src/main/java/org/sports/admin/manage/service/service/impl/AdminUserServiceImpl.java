//package org.sports.admin.manage.service.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.apache.logging.log4j.util.Strings;
//import org.sports.admin.manage.dao.entity.AdminUserDO;
//import org.sports.admin.manage.dao.mapper.AdminUserMapper;
//import org.sports.admin.manage.dao.req.UserPageRequest;
//import org.sports.admin.manage.service.enums.YnEnum;
//import org.sports.admin.manage.service.service.IAdminUserService;
//import org.sports.admin.manage.service.vo.AdminUserVO;
//import org.sports.springboot.starter.convention.page.PageResponse;
//import org.sports.springboot.starter.database.mybatisplus.PageUtil;
//import org.springframework.stereotype.Service;
//
///**
// * <p>
// * 后台管理人员信息 服务实现类
// * </p>
// *
//
// * @since 2023-07-20
// */
//@Service
//public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUserDO> implements IAdminUserService {
//
//    @Override
//    public AdminUserDO getByAccount(String account){
//        return this.getOne(new LambdaQueryWrapper<AdminUserDO>().eq(AdminUserDO::getAccount, account));
//    }
//
//    @Override
//    public PageResponse<AdminUserVO> getAdminUserPage(UserPageRequest pageRequest){
//        LambdaQueryWrapper queryWrapper= new LambdaQueryWrapper<AdminUserDO>()
//                .select(AdminUserDO.class, i -> !i.getColumn().equals("passwd"))
//                .eq(AdminUserDO::getYnValid, YnEnum.ONE.getValue())
//                .like(Strings.isNotBlank(pageRequest.getAccount()), AdminUserDO::getAccount, pageRequest.getAccount())
//                .like(Strings.isNotBlank(pageRequest.getName()), AdminUserDO::getName, pageRequest.getName())
//                .orderByDesc(AdminUserDO::getCreateTime);
//
//        Page<AdminUserDO> page = this.baseMapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
//        return PageUtil.convert(page, AdminUserVO.class);
//    }
//
//    @Override
//    public boolean existUserByAccount(String account){
//        long count = this.count(new LambdaQueryWrapper<AdminUserDO>().eq(AdminUserDO::getAccount, account));
//        if(count > 0){
//            return true;
//        }
//        return false;
//    }
//
//}
