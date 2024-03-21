package org.sports.admin.manage.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.sports.admin.manage.service.constant.UserConstant;
import org.sports.admin.manage.service.rycore.AjaxResult;
import org.sports.admin.manage.service.rycore.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 参数配置 信息操作处理
 * 

 */
@RestController
@RequestMapping("/system/config")
@Tag(name = "系统ry固定配置入口")
public class SysConfigController extends BaseController
{


    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public AjaxResult getConfigKey(@PathVariable String configKey) {
        if("sys.user.initPassword".equals(configKey)){
            return success(UserConstant.INIT_PASSWD);
        }else{
            return error("参数不识别，请联系管理员");
        }
    }

}
