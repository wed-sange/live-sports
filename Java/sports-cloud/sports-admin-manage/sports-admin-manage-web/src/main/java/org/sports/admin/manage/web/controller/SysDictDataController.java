package org.sports.admin.manage.web.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sports.admin.manage.dao.entity.SysDictData;
import org.sports.admin.manage.service.rycore.AjaxResult;
import org.sports.admin.manage.service.rycore.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息
 * 

 */
@RestController
@RequestMapping("/system/dict/data")
@Tag(name = "系统ry固定字典表入口")
public class SysDictDataController extends BaseController
{


    /**
     * 根据字典类型查询字典数据信息
     */
    @Operation(summary = "根据字典类型查询字典数据信息")
    @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType) {
        return success(getDataByType(dictType));
    }

    /**
     * ry字典表处理
     * @param dictType
     * @return
     */
    private List<SysDictData> getDataByType(String dictType){
        String[] dataStrs = null; //dataStrs = new String[]{",,,",",,,"}; code,sort,label,value
        if("sys_normal_disable".equals(dictType)){
            dataStrs = new String[]{"6,1,正常,1","7,2,停用,0"};
        }else if("sys_user_sex".equals(dictType)){
            dataStrs = new String[]{"1,1,男,0","2,2,女,1","3,3,未知,2"};
        }else if("sys_show_hide".equals(dictType)){
            dataStrs = new String[]{"4,1,显示,0","5,2,隐藏,1"};
        }
        return dealData(dataStrs);
    }

    /**
     * 数据组装
     * @param dataStrs
     * @return
     */
    private List<SysDictData> dealData(String[] dataStrs){
        List<SysDictData> dataList = new ArrayList<>();
        if(dataStrs == null || dataStrs.length < 1){
            return dataList;
        }
        SysDictData data;
        String[] strs; //code,sort,label,value
        for (String dataStr : dataStrs) {
            strs = dataStr.split(",");
            data = new SysDictData();
            data.setDictCode(Long.parseLong(strs[0]));
            data.setDictSort(Long.parseLong(strs[1]));
            data.setDictLabel(strs[2]);
            data.setDictValue(strs[3]);
           dataList.add(data);
        }
        return dataList;
    }


}
