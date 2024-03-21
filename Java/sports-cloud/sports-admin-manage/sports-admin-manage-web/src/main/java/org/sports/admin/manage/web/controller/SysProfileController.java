package org.sports.admin.manage.web.controller;

import cn.hutool.crypto.digest.DigestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.dao.entity.SysUser;
import org.sports.admin.manage.service.rycore.AjaxResult;
import org.sports.admin.manage.service.rycore.BaseController;
import org.sports.admin.manage.service.service.ISysUserService;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 个人信息 业务处理
 * 

 */
@RestController
@RequestMapping("/system/user/profile")
@Tag(name = "ry个人信息 业务处理")
public class SysProfileController extends BaseController
{
    @Resource
    private ISysUserService userService;

    /**
     * 个人信息
     */
    @GetMapping
    @Operation(summary = "个人信息")
    public AjaxResult profile() {
        Long userId = SotokenUtil.getCheckUserId();
        SysUser user = userService.selectUserById(userId);
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(user.getUserName()));
        ajax.put("postGroup", userService.selectUserPostGroup(user.getUserName()));
        return ajax;
    }

    /**
     * 修改用户
     */
    @PutMapping
    @Operation(summary = "修改用户")
    public AjaxResult updateProfile(@RequestBody SysUser user)
    {
        Long userId = SotokenUtil.getCheckUserId();
        SysUser currentUser = userService.selectUserById(userId);
        currentUser.setNickName(user.getNickName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhonenumber(user.getPhonenumber());
        currentUser.setSex(user.getSex());
        if (Strings.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(currentUser))
        {
            return error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        if (Strings.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(currentUser))
        {
            return error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        if (userService.updateUserProfile(currentUser) > 0)
        {
            return success();
        }
        return error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @PutMapping("/updatePwd")
    @Operation(summary = "重置密码")
    public AjaxResult updatePwd(String oldPassword, String newPassword)
    {
        Long userId = SotokenUtil.getCheckUserId();
        SysUser sysUser = userService.selectUserById(userId);
        if (!DigestUtil.md5Hex(oldPassword).equals(sysUser.getPassword()))
        {
            return error("修改密码失败，旧密码错误");
        }
        if (DigestUtil.md5Hex(newPassword).equals(sysUser.getPassword()))
        {
            return error("新密码不能与旧密码相同");
        }
        if (userService.resetUserPwd(sysUser.getUserName(), DigestUtil.md5Hex(newPassword)) > 0)
        {
            return success();
        }
        return error("修改密码异常，请联系管理员");
    }

    /**
     * 修改头像
     */
    @Operation(summary = "修改头像")
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("file") String file) {
        if (Strings.isNotBlank(file)) {
            Long userId = SotokenUtil.getCheckUserId();
            SysUser sysUser = userService.selectUserById(userId);
            if (userService.updateUserAvatar(sysUser.getUserName(), file)) {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", file);
                return ajax;
            }
            return error("修改头像异常，请联系管理员");
        }
        return error("上传图片不能为空");
    }

}
