# 体育直播Android

## 配置修改 [ApiComService.kt](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fxcjh%2Fapp%2Fnet%2FApiComService.kt)
```java
// API URL
const val SERVER_URL = "http://******:****/apis/"
// 比赛分享 URL
const val SHARE_IP = "http://******:****/"
// webSocket URL
const val WEB_SOCKET_URL = "ws://******:****/ws-sports-chat"
```
## 打包签名配置 innofun_live.jks文件需要自己去生成，放到根路径上
```base
signingConfigs {
    release {
        # 别名
        keyAlias 'innofun_live'
        # 别名密码
        keyPassword 'innofun123'
        # 签名文件路径
        storeFile file('innofun_live.jks')
        # 签名文件密码
        storePassword 'innofun123'
    }
    debug {
        # 别名
        keyAlias 'innofun_live'
        # 别名密码
        keyPassword 'innofun123'
        # 签名路径
        storeFile file('innofun_live.jks')
        # 签名密码
        storePassword 'innofun123'
    }
}
```

## 推送配置 JPush appKey ENGAGELAB_PRIVATES_APPKEY
```base
manifestPlaceholders = [
        app_name     : "@string/my_app_name",
        is_release   : true,
        networkConfig: "@xml/network_security_config",//https 协议 加密政策
        // appKey，需要与控制台上的一样，与packageName是一对一关系
        ENGAGELAB_PRIVATES_APPKEY : "******",
        // engagelab appChannel，用于渠道统计
        ENGAGELAB_PRIVATES_CHANNEL: "developer",
        // engagelab process，engagelab sdk工作所在的进程，注意:开头
        ENGAGELAB_PRIVATES_PROCESS: ":remote",
        //数据中心名称，填空""时，默认"Singapore"数据中心
        ENGAGELAB_PRIVATES_SITE_NAME: "Singapore",
        // mi client 配置，需要与小米控制台上的一样，还需要在Portal控制台配置 server 配置
        XIAOMI_APPID            : "",//MI-您的，对应平台信息
        XIAOMI_APPKEY           : "",//MI-您的，对应平台信息
        // mi global client 配置，需要与小米控制台上的一样，还需要在Portal控制台配置 server 配置
        XIAOMI_GLOBAL_APPID            : "",//MI-您的，对应平台信息
        XIAOMI_GLOBAL_APPKEY           : "",//MI-您的，对应平台信息
        // mz client 配置，需要与魅族控制台上的一样，还需要在Portal控制台配置 server 配置
        MEIZU_APPID             : "",//MZ-您的，对应平台信息
        MEIZU_APPKEY            : "",//MZ-您的，对应平台信息
        // oppo client 配置，需要与oppo控制台上的一样，还需要在Portal控制台配置 server 配置
        OPPO_APPID              : "",//OP-您的，对应平台信息
        OPPO_APPKEY             : "",//OP-您的，对应平台信息
        OPPO_APPSECRET          : "",//OP-您的，对应平台信息
        // vivo client 配置，需要与vivo控制台上的一样，还需要在Portal控制台配置 server 配置
        VIVO_APPID              : "",
        VIVO_APPKEY             : "",
        // honor client 配置，需要与honor控制台上的一样，还需要在Portal控制台配置 server 配置
        HONOR_APPID             : ""
]
```
