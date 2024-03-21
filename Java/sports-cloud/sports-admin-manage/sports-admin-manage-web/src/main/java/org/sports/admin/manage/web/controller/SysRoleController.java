package org.sports.admin.manage.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sports.admin.manage.dao.entity.SysDept;
import org.sports.admin.manage.dao.entity.SysRole;
import org.sports.admin.manage.dao.entity.SysUser;
import org.sports.admin.manage.dao.entity.SysUserRole;
import org.sports.admin.manage.service.rycore.AjaxResult;
import org.sports.admin.manage.service.rycore.BaseController;
import org.sports.admin.manage.service.rycore.page.TableDataInfo;
import org.sports.admin.manage.service.service.ISysDeptService;
import org.sports.admin.manage.service.service.ISysMenuService;
import org.sports.admin.manage.service.service.ISysRoleService;
import org.sports.admin.manage.service.service.ISysUserService;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息
 * 

 */
@RestController
@RequestMapping("/system/role")
@Tag(name = "系统权限管理")
public class SysRoleController extends BaseController
{
    @Resource
    private ISysRoleService roleService;
    @Resource
    private ISysUserService userService;
    @Resource
    private ISysDeptService deptService;
    @Resource
    private ISysMenuService sysMenuService;

    @SaCheckPermission("system:role:list")
    @GetMapping("/list")
    @Operation(summary = "系统权限列表")
    public TableDataInfo list(SysRole role)
    {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    /**
     * 根据角色编号获取详细信息
     */
    @SaCheckPermission("system:role:query")
    @GetMapping(value = "/{roleId}")
    @Operation(summary = "根据权限ID获取系统权限信息")
    public AjaxResult getInfo(@PathVariable Long roleId)
    {
        roleService.checkRoleDataScope(roleId);
        return success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @SaCheckPermission("system:role:add")
    @PostMapping
    @Operation(summary = "新增系统权限信息")
    public AjaxResult add(@Validated @RequestBody SysRole role)
    {
        if (!roleService.checkRoleNameUnique(role))
        {
            return error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        else if (!roleService.checkRoleKeyUnique(role))
        {
            return error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy(SotokenUtil.getBGUserName());
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改保存角色
     */
    @SaCheckPermission("system:role:edit")
    @PutMapping
    @Operation(summary = "修改系统权限信息")
    public AjaxResult edit(@Validated @RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        if (!roleService.checkRoleNameUnique(role))
        {
            return error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        else if (!roleService.checkRoleKeyUnique(role))
        {
            return error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy(SotokenUtil.getBGUserName());
        
        if (roleService.updateRole(role) > 0) {
            SysUser sysUser = new SysUser();
            sysUser.setRoleId(role.getRoleId());
            List<SysUser> userList = userService.selectAllocatedList(sysUser);
            for (SysUser user : userList) {
                sysMenuService.cacheUserPerms(user.getUserId());
            }
            return success();
        }
        return error("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }

    /**
     * 修改保存数据权限
     */
    @SaCheckPermission("system:role:edit")
    @PutMapping("/dataScope")
    @Operation(summary = "修改数据权限")
    public AjaxResult dataScope(@RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        return toAjax(roleService.authDataScope(role));
    }

    /**
     * 状态修改
     */
    @SaCheckPermission("system:role:edit")
    @PutMapping("/changeStatus")
    @Operation(summary = "修改系统权限状态")
    public AjaxResult changeStatus(@RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        role.setUpdateBy(SotokenUtil.getBGUserName());
        return toAjax(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     */
    @SaCheckPermission("system:role:remove")
    @DeleteMapping("/{roleIds}")
    @Operation(summary = "删除角色")
    public AjaxResult remove(@PathVariable Long[] roleIds)
    {
        List<SysUser> userList = new ArrayList<>();
        for (Long roleId : roleIds) {
            SysUser sysUser = new SysUser();
            sysUser.setRoleId(roleId);
            List<SysUser> list = userService.selectAllocatedList(sysUser);
            if(!CollectionUtils.isEmpty(list)){
                userList.addAll(list);
            }
        }
        roleService.deleteRoleByIds(roleIds);
        if(!CollectionUtils.isEmpty(userList)){
            for (SysUser user : userList) {
                sysMenuService.cacheUserPerms(user.getUserId());
            }
        }
        return success();
    }

    /**
     * 获取角色选择框列表
     */
    @SaCheckPermission("system:role:query")
    @GetMapping("/optionselect")
    @Operation(summary = "获取角色选择框列表")
    public AjaxResult optionselect()
    {
        return success(roleService.selectRoleAll());
    }

    /**
     * 查询已分配用户角色列表
     */
    @SaCheckPermission("system:role:list")
    @GetMapping("/authUser/allocatedList")
    @Operation(summary = "查询已分配用户角色列表")
    public TableDataInfo allocatedList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * 查询未分配用户角色列表
     */
    @SaCheckPermission("system:role:list")
    @GetMapping("/authUser/unallocatedList")
    @Operation(summary = "查询未分配用户角色列表")
    public TableDataInfo unallocatedList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * 取消授权用户
     */
    @SaCheckPermission("system:role:edit")
    @PutMapping("/authUser/cancel")
    @Operation(summary = "取消授权用户")
    public AjaxResult cancelAuthUser(@RequestBody SysUserRole userRole)
    {
        try {
            roleService.deleteAuthUser(userRole);
            sysMenuService.cacheUserPerms(userRole.getUserId());
            return success();
        }catch (Exception e){
            return error();
        }
    }

    /**
     * 批量取消授权用户
     */
    @SaCheckPermission("system:role:edit")
    @PutMapping("/authUser/cancelAll")
    @Operation(summary = "批量取消授权用户")
    public AjaxResult cancelAuthUserAll(Long roleId, Long[] userIds)
    {
        try {
            roleService.deleteAuthUsers(roleId, userIds);
            for (Long userId : userIds) {
                sysMenuService.cacheUserPerms(userId);
            }
            return success();
        }catch (Exception e){
            return error();
        }
    }

    /**
     * 批量选择用户授权
     */
    @SaCheckPermission("system:role:edit")
    @PutMapping("/authUser/selectAll")
    @Operation(summary = "批量选择用户授权")
    public AjaxResult selectAuthUserAll(Long roleId, Long[] userIds)
    {
        try {
            roleService.checkRoleDataScope(roleId);
            roleService.insertAuthUsers(roleId, userIds);
            for (Long userId : userIds) {
                sysMenuService.cacheUserPerms(userId);
            }
            return success();
        }catch (Exception e){
            return error();
        }
    }

    /**
     * 获取对应角色部门树列表
     */
    @SaCheckPermission("system:role:query")
    @GetMapping(value = "/deptTree/{roleId}")
    @Operation(summary = "获取对应角色部门树列表")
    public AjaxResult deptTree(@PathVariable("roleId") Long roleId)
    {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.selectDeptTreeList(new SysDept()));
        return ajax;
    }
}
