//package org.sports.admin.manage.web.controller;
//
//import cn.hutool.crypto.digest.DigestUtil;
//import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.apache.logging.log4j.util.Strings;
//import org.sports.admin.manage.dao.entity.AdminUserDO;
//import org.sports.admin.manage.dao.req.UserPageRequest;
//import org.sports.admin.manage.dao.req.UserPasswdUpdateRequest;
//import org.sports.admin.manage.service.constant.UserConstant;
//import org.sports.admin.manage.service.enums.YnEnum;
//import org.sports.admin.manage.service.service.IAdminUserService;
//import org.sports.admin.manage.service.vo.AdminUserAddVO;
//import org.sports.admin.manage.service.vo.AdminUserVO;
//import org.sports.springboot.starter.base.exception.enums.GlobalErrorCodeConstants;
//import org.sports.springboot.starter.base.exception.enums.ServiceErrorCodeRange;
//import org.sports.springboot.starter.base.utils.RandomUtils;
//import org.sports.springboot.starter.convention.page.PageResponse;
//import org.sports.springboot.starter.convention.result.Result;
//import org.sports.springboot.starter.log.annotation.MLog;
//import org.sports.springboot.starter.satoken.enums.UserChannelEnum;
//import org.sports.springboot.starter.satoken.util.SotokenUtil;
//import org.sports.springboot.starter.web.Results;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//
///**
// * 后台管理人员信息 前端控制器
// *
//
// * @since 2023-07-20
// */
//@RestController
//@RequestMapping("/admin/user")
//@Tag(name =  "ADMIN用户管理")
//public class AdminUserController {
//
//    @Autowired
//    private IAdminUserService adminUserService;
//
//    @PostMapping("/page")
//    @Operation(summary = "后台用户分页查询")
//    @MLog
//    public Result<PageResponse<AdminUserVO>> getAdminUserPage(@Validated @RequestBody UserPageRequest req) {
//        return Results.success(adminUserService.getAdminUserPage(req));
//    }
//
//    @GetMapping("/getUserInfo/{id}")
//    @Operation(summary = "获取后台用户信息")
//    @MLog
//    public Result<AdminUserVO> getBackgroundUserInfo(@PathVariable("id") Long id) {
//        AdminUserDO adminUserDO = adminUserService.getById(id);
//        AdminUserVO adminUserVO = new AdminUserVO();
//        BeanUtils.copyProperties(adminUserDO, adminUserVO);
//        return Results.success(adminUserVO);
//    }
//
//    @PostMapping("/add")
//    @Operation(summary = "添加后台用户信息")
//    @MLog
//    public Result add(@Validated @RequestBody AdminUserAddVO adminUserAddVO) {
//       if(adminUserAddVO.getAccount().length() < 1 || adminUserAddVO.getPasswd().length() > 16){
//            return Results.failure("账号1-16个字以内");
//        }
//        if(Strings.isEmpty(adminUserAddVO.getPasswd())){
//            adminUserAddVO.setPasswd(UserConstant.INIT_PASSWD);
//        }else if(adminUserAddVO.getPasswd().length() < 6 || adminUserAddVO.getPasswd().length() > 16){
//            return Results.failure("密码6-16个字以内");
//        }
//
//        if(adminUserService.existUserByAccount(adminUserAddVO.getAccount())){
//            return Results.failure("该账号已存在");
//        }
//        AdminUserDO adminUserDO = new AdminUserDO();
//        adminUserDO.setAccount(adminUserAddVO.getAccount());
//        if(Strings.isNotEmpty(adminUserAddVO.getName())){
//            adminUserDO.setName(adminUserAddVO.getName());
//        }else{
//            adminUserDO.setName(RandomUtils.buildNickName());
//        }
//        adminUserDO.setPasswd(DigestUtil.md5Hex(adminUserAddVO.getPasswd()));
//        adminUserDO.setYnValid(YnEnum.ONE.getValue());
//        adminUserDO.setCreateTime(LocalDateTime.now(ZoneOffset.UTC));
//        adminUserService.save(adminUserDO);
//        return Results.success();
//    }
//
//    @PutMapping("/update")
//    @Operation(summary = "修改后台用户信息[暂时只能修改姓名]")
//    @MLog
//    public Result update(@Validated @RequestBody AdminUserVO adminUserVO) {
//        if(Strings.isEmpty(adminUserVO.getName()) || adminUserVO.getId() == null){
//            return Results.failure("姓名不能为空");
//        }
//        AdminUserDO adminUserDO = adminUserService.getById(adminUserVO.getId());
//        adminUserDO.setName(adminUserVO.getName());
//        adminUserService.update(new LambdaUpdateWrapper<AdminUserDO>()
//                .eq(AdminUserDO::getId, adminUserVO.getId())
//                .set(AdminUserDO::getName, adminUserVO.getName())
//                .set(AdminUserDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
//        return Results.success();
//    }
//
//    @PutMapping("/resetPasswd/{id}")
//    @Operation(summary = "重置用户密码")
//    @MLog
//    public Result<String> resetPasswd(@PathVariable("id") Long id) {
//        adminUserService.update(new LambdaUpdateWrapper<AdminUserDO>()
//                .eq(AdminUserDO::getId, id)
//                .set(AdminUserDO::getPasswd, DigestUtil.md5Hex(UserConstant.INIT_PASSWD))
//                .set(AdminUserDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
//        SotokenUtil.logout(id, UserChannelEnum.ADMIN, null);
//        return Results.success(UserConstant.INIT_PASSWD);
//    }
//
//    @DeleteMapping("/cancel/{id}")
//    @Operation(summary = "注销")
//    @MLog
//    public Result cancel(@PathVariable("id") Long id) {
//        adminUserService.update(new LambdaUpdateWrapper<AdminUserDO>()
//                .eq(AdminUserDO::getId, id)
//                .set(AdminUserDO::getYnValid, YnEnum.ZERO.getValue())
//                .set(AdminUserDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
//        SotokenUtil.kickout(id, UserChannelEnum.ADMIN, null);
//        return Results.success();
//    }
//
//
//    @GetMapping("/getMyUserInfo")
//    @Operation(summary = "获取当前自己信息")
//    @MLog
//    public Result<AdminUserVO> getMyUserInfo(){
//        Long userId = SotokenUtil.getCheckUserId();
//        AdminUserDO adminUserDO = adminUserService.getById(userId);
//        AdminUserVO adminUserVO = new AdminUserVO();
//        BeanUtils.copyProperties(adminUserDO, adminUserVO);
//        return Results.success(adminUserVO);
//    }
//
//    @PutMapping("/updateMyPasswd")
//    @Operation(summary = "修改当前自己密码")
//    @MLog
//    public Result updateMyPasswd(@Validated @RequestBody UserPasswdUpdateRequest passwdUpdateRequest){
//        Long userId = SotokenUtil.getCheckUserId();
//        if(!passwdUpdateRequest.getNewPasswd().equals(passwdUpdateRequest.getRePasswd())){
//            return Results.failure(GlobalErrorCodeConstants.BAD_REQUEST);
//        }
//        AdminUserDO adminUserDO = adminUserService.getById(userId);
//        if(!adminUserDO.getPasswd().equals(DigestUtil.md5Hex(passwdUpdateRequest.getOldPasswd()))){
//            return Results.failure(ServiceErrorCodeRange.PASSWD_FAIL);
//        }
//        adminUserService.update(new LambdaUpdateWrapper<AdminUserDO>()
//                .eq(AdminUserDO::getId, userId)
//                .set(AdminUserDO::getPasswd,DigestUtil.md5Hex(passwdUpdateRequest.getNewPasswd()))
//                .set(AdminUserDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
//        SotokenUtil.logout(userId, UserChannelEnum.ADMIN, null);
//        return Results.success();
//    }
//
//    @DeleteMapping("/myLogout")
//    @Operation(summary = "当前用户自己退出")
//    @MLog
//    public Result myLogout(){
//        SotokenUtil.logout();
//        return Results.success();
//    }
//
//}
