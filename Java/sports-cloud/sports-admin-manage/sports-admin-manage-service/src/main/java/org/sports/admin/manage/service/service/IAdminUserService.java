//package org.sports.admin.manage.service.service;
//
//import com.baomidou.mybatisplus.extension.service.IService;
//import org.sports.admin.manage.dao.entity.AdminUserDO;
//import org.sports.admin.manage.dao.req.UserPageRequest;
//import org.sports.admin.manage.service.vo.AdminUserVO;
//import org.sports.springboot.starter.convention.page.PageResponse;
//
///**
// * <p>
// * 后台管理人员信息 服务类
// * </p>
// *
//
// * @since 2023-07-20
// */
//public interface IAdminUserService extends IService<AdminUserDO> {
//
//    /**
//     * 根据账号获取后台用户信息
//     * @param account
//     * @return
//     */
//    AdminUserDO getByAccount(String account);
//
//    /**
//     * 用户分页查询
//     * @param pageRequest
//     * @return
//     */
//    PageResponse<AdminUserVO> getAdminUserPage(UserPageRequest pageRequest);
//
//    /**
//     * 判断是否存在相同的账号
//     * @param account
//     * @return
//     */
//    boolean existUserByAccount(String account);
//
//}
