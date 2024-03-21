package org.sports.app.web.config;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.core.datainterface.SmsReadConfig;
import org.dromara.sms4j.provider.config.BaseConfig;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.sports.admin.manage.dao.entity.SmsDO;
import org.sports.admin.manage.service.constant.SMSConstant;
import org.sports.admin.manage.service.service.ISmsService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class Sms4jConfig implements SmsReadConfig {

    @Resource
    private ISmsService smsService;

    @Override
    public BaseConfig getSupplierConfig(String configId) {
        List<SmsDO> smsDOList = smsService.getUseSmsDOList();
        if(CollectionUtils.isNotEmpty(smsDOList)){
            SmsDO smsDO = smsDOList.stream()
                    .filter(smsDO1 -> Objects.equals(smsDO1.getId(),Long.parseLong(configId)))
                    .findFirst().orElse(null);
            if(Objects.equals(smsDO.getSupplier(), SMSConstant.SUPPLIER_ALI)){
                return getAlibabaConfig(smsDO);
            }
            if(Objects.equals(smsDO.getSupplier(),SMSConstant.SUPPLIER_TX)){
                return getTencentConfig(smsDO);
            }
        }
        return null;
    }

    @Override
    public List<BaseConfig> getSupplierConfigList() {
        List<SmsDO> smsDOList = smsService.getUseSmsDOList();
        if(CollectionUtils.isNotEmpty(smsDOList)){
            List<BaseConfig> configs =  new ArrayList<BaseConfig>();
            for (SmsDO smsDO : smsDOList) {
                if(Objects.equals(smsDO.getSupplier(),SMSConstant.SUPPLIER_ALI)){
                    configs.add(getAlibabaConfig(smsDO));
                }
                if(Objects.equals(smsDO.getSupplier(),SMSConstant.SUPPLIER_TX)){
                    configs.add(getTencentConfig(smsDO));
                }
            }
            return configs;
        }
        return null;

    }

    public AlibabaConfig getAlibabaConfig(SmsDO smsDO) {
        AlibabaConfig alibabaConfig = new AlibabaConfig();
        //alibabaConfig.setTemplateName();
        //alibabaConfig.setRegionId();
        alibabaConfig.setAccessKeyId(smsDO.getAccessKeyId());
        alibabaConfig.setSdkAppId(smsDO.getSdkAppId());
        alibabaConfig.setAccessKeySecret(smsDO.getAccessKeySecret());
        alibabaConfig.setSignature(smsDO.getSignature());
        alibabaConfig.setTemplateId(smsDO.getTemplateId());
        alibabaConfig.setConfigId(smsDO.getId()+"");
//        alibabaConfig.setMaximum(6);
        return alibabaConfig;
    }

    public TencentConfig getTencentConfig(SmsDO smsDO) {
        TencentConfig tencentConfig = new TencentConfig();
        tencentConfig.setAccessKeyId(smsDO.getAccessKeyId());
        tencentConfig.setSdkAppId(smsDO.getSdkAppId());
        tencentConfig.setAccessKeySecret(smsDO.getAccessKeySecret());
        tencentConfig.setSignature(smsDO.getSignature());
        tencentConfig.setTemplateId(smsDO.getTemplateId());
        tencentConfig.setConfigId(smsDO.getId()+"");
//        tencentConfig.setMaximum(6);
        return tencentConfig;
    }
}
