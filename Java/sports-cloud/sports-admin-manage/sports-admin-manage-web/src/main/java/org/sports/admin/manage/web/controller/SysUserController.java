package org.sports.admin.manage.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.DigestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.dao.entity.SysDept;
import org.sports.admin.manage.dao.entity.SysMenu;
import org.sports.admin.manage.dao.entity.SysRole;
import org.sports.admin.manage.dao.entity.SysUser;
import org.sports.admin.manage.dao.req.UserPasswdUpdateRequest;
import org.sports.admin.manage.service.constant.UserConstant;
import org.sports.admin.manage.service.rycore.AjaxResult;
import org.sports.admin.manage.service.rycore.BaseController;
import org.sports.admin.manage.service.rycore.page.TableDataInfo;
import org.sports.admin.manage.service.rycore.utils.StringUtils;
import org.sports.admin.manage.service.service.*;
import org.sports.springboot.starter.base.exception.enums.GlobalErrorCodeConstants;
import org.sports.springboot.starter.base.exception.enums.ServiceErrorCodeRange;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.satoken.enums.UserChannelEnum;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.sports.springboot.starter.web.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户信息
 * 

 */
@RestController
@RequestMapping("/system/user")
@Tag(name = "系统admin用户管理")
public class SysUserController extends BaseController
{
    @Resource
    private ISysUserService userService;
    @Resource
    private ISysRoleService roleService;
    @Autowired
    private ISysDeptService deptService;
    @Resource
    private ISysPostService postService;
    @Resource
    private ISysMenuService menuService;
    @Resource
    private SysPermissionService permissionService;

    /**
     * 获取用户列表
     */
    @SaCheckPermission("system:user:list")
    @GetMapping("/list")
    @Operation(summary = "获取系统用户列表")
    public TableDataInfo list(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @SaCheckPermission("system:user:query")
    @GetMapping(value = { "/", "/{userId}" })
    @Operation(summary = "根据用户ID获取系统用户信息")
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId)
    {
        userService.checkUserDataScope(userId);
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId))
        {
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put(AjaxResult.DATA_TAG, sysUser);
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    @SaCheckPermission("system:user:add")
    @PostMapping
    @Operation(summary = "新增系统用户")
    public AjaxResult add(@Validated @RequestBody SysUser user)
    {
        if (!userService.checkUserNameUnique(user))
        {
            return error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user))
        {
            return error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
        {
            return error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        if(Strings.isBlank(user.getPassword())){
            user.setPassword(UserConstant.INIT_PASSWD);
        }
        user.setCreateBy(SotokenUtil.getBGUserName());
        user.setPassword(DigestUtil.md5Hex(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @SaCheckPermission("system:user:edit")
    @PutMapping
    @Operation(summary = "修改系统用户信息")
    public AjaxResult edit(@Validated @RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (!userService.checkUserNameUnique(user))
        {
            return error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user))
        {
            return error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
        {
            return error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(SotokenUtil.getBGUserName());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @SaCheckPermission("system:user:remove")
    @DeleteMapping("/{userIds}")
    @Operation(summary = "根据用户ID删除系统用户")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        if (ArrayUtils.contains(userIds, SotokenUtil.getCheckUserId()))
        {
            return error("当前用户不能删除");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @SaCheckPermission("system:user:resetPwd")
    @PutMapping("/resetPwd")
    @Operation(summary = "重置系统用户密码")
    public AjaxResult resetPwd(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setPassword(DigestUtil.md5Hex(user.getPassword()));
        user.setUpdateBy(SotokenUtil.getBGUserAccount());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @SaCheckPermission("system:user:edit")
    @PutMapping("/changeStatus")
    @Operation(summary = "修改系统用户状态")
    public AjaxResult changeStatus(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(SotokenUtil.getBGUserName());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * 根据用户编号获取授权角色
     */
    @SaCheckPermission("system:user:query")
    @GetMapping("/authRole/{userId}")
    @Operation(summary = "根据用户ID获取授权角色")
    public AjaxResult authRole(@PathVariable("userId") Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * 用户授权角色
     */
    @SaCheckPermission("system:user:edit")
    @PutMapping("/authRole")
    @Operation(summary = "用户授权")
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds)
    {
        userService.checkUserDataScope(userId);
        userService.insertUserAuth(userId, roleIds);
        return success();
    }

    /**
     * 获取部门树列表
     */
    @SaCheckPermission("system:user:list")
    @GetMapping("/deptTree")
    @Operation(summary = "获取部门树列表")
    public AjaxResult deptTree(SysDept dept)
    {
        return success(deptService.selectDeptTreeList(dept));
    }


    @GetMapping("/getMyUserInfo")
    @Operation(summary = "获取当前自己信息new")
    @MLog
    public Result<SysUser> getMyUserInfo(){
        Long userId = SotokenUtil.getCheckUserId();
        SysUser sysUser = userService.selectUserById(userId);
        sysUser.setPassword(null);
        return Results.success(sysUser);
    }

    @PutMapping("/updateMyPasswd")
    @Operation(summary = "修改当前自己密码new")
    @MLog
    public Result updateMyPasswd(@Validated @RequestBody UserPasswdUpdateRequest passwdUpdateRequest){
        Long userId = SotokenUtil.getCheckUserId();
        if(!passwdUpdateRequest.getNewPasswd().equals(passwdUpdateRequest.getRePasswd())){
            return Results.failure(GlobalErrorCodeConstants.BAD_REQUEST);
        }
        SysUser sysUser = userService.selectUserById(userId);
        if(!sysUser.getPassword().equals(DigestUtil.md5Hex(passwdUpdateRequest.getOldPasswd()))){
            return Results.failure(ServiceErrorCodeRange.PASSWD_FAIL);
        }
        sysUser.setPassword(DigestUtil.md5Hex(passwdUpdateRequest.getNewPasswd()));
        sysUser.setUpdateTime(DateUtil.date());
        userService.resetPwd(sysUser);
        SotokenUtil.logout(userId, UserChannelEnum.ADMIN, null);
        return Results.success();
    }

    @DeleteMapping("/myLogout")
    @Operation(summary = "当前用户自己退出new")
    @MLog
    public Result myLogout(){
        SotokenUtil.logout();
        return Results.success();
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    @Operation(summary = "获取当前用户路由信息new")
    public AjaxResult getRouters()
    {
        Long userId = SotokenUtil.getCheckUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    /**
     * 获取用户角色权限信息
     */
    @GetMapping("/getPermissonsInfo")
    @Operation(summary = "获取用户角色权限信息new")
    public AjaxResult getPermissonsInfo() {
        SysUser user = userService.selectUserById(SotokenUtil.getCheckUserId());
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }
}
