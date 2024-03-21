# **Deployment Document for Server**

## **Project Dependency Middleware**

|Middleware|Description|
| :- | :- |
|Nacos|Service registration and discovery|
|mysql|Data storage|
|redis|Cache middleware|
|oss|File storage|

## **Project List**

|Number|System|Function|Remarks|
| :- | :- | :- | :- |
|1|sports-admin-manage|Backend management||
|2|sports-live-manage|Live broadcast terminal||
|3|sports-app|User terminal||
|4|sports-chat|Message management terminal||
|5|sports-admin-gateway|Backend gateway|Proxy project 1|
|6|sports-app-gateway|Frontend gateway|Proxy projects 2, 3, 4|

## Database Initialization File [init_data.sql](sql%2Finit_data.sql)

## **Backend Management ([sports-admin-manage](sports-admin-manage)) Deployment**
### **Modify Project Startup Port [application.yaml](sports-admin-manage%2Fsports-admin-manage-web%2Fsrc%2Fmain%2Fresources%2Fapplication.yaml)**

```yaml 
server:
  port: 6001
```

### **Modify Configuration [application-dev.yaml](sports-admin-manage%2Fsports-admin-manage-web%2Fsrc%2Fmain%2Fresources%2Fapplication-dev.yaml)**
```yaml
spring:
  cloud:
    #nacos configuration
    nacos:
      discovery:
        server-addr: ******:8848
        enabled: true
        username: nacos
        password: nacos
        group: sports
        namespace: 082d2278-d937-48c5-97a1-e9408058e6e7
  #mysql configuration
  datasource:
    username: root
    password: root
    url: jdbc:mysql://******:3304/sports?characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&serverTimezone=GMT%2B8
  # redis configuration
  redis:
    # Address
    host: ******
    # Port, default is 6379
    port: 6379
    # Database index
    database: 6
    # Password (comment out if no password)
    # password:
    # Connection timeout
    timeout: 10s
    # Whether to enable SSL
    ssl: false
# Sports data source configuration 
edi:
  # Basketball data source
  basketball:
    user: ******
    secret: ******
    baseUrl: ******
  # Football data source
  football:
    user: ******
    secret: ******
    baseUrl: ******
# OSS configuration
storage:
  enabled: true
  config:
    # Storage type: AMAZON, ALIYUN, TENCENT, QINIU, HUAWEI, MINIO
    type: AMAZON
    domain: https://******.cloudfront.net
  amazon:
    accessKey: ******
    secretKey: ******
    bucketName: ******
    region: ******
```

## **Live Broadcast Terminal ([sports-live-manage](sports-live-manage)) Deployment**
### **Modify Project Startup Port [application.yaml](sports-live-manage%2Fsports-live-manage-web%2Fsrc%2Fmain%2Fresources%2Fapplication.yaml)**

```yaml 
server:
  port: 6001
```

### **Modify Configuration**

```yaml
spring:
  cloud:
    # Nacos configuration
    nacos:
      discovery:
        server-addr: ******:8848
        enabled: true
        username: nacos
        password: nacos
        group: sports
        namespace: 082d2278-d937-48c5-97a1-e9408058e6e7
  # mysql configuration
  datasource:
    username: ******
    password: ******
    url: jdbc:mysql://******:3304/sports?characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&serverTimezone=GMT%2B8
  # redis configuration
  redis:
    # Address
    host: ******
    # Port, default is 6379
    port: 6379
    # Database index
    database: 6
    # Password (comment out if no password)
    # password:
    # Connection timeout
    timeout: 10s
    # Whether to enable SSL
    ssl: false
# Sports data source configuration 
edi:
  # Basketball data source
  basketball:
    user: ******
    secret: ******
    baseUrl: ******
  # Football data source
  football:
    user: ******
    secret: ******
    baseUrl: ******
```

## **User Terminal ([sports-app](sports-app)) Deployment**
### **Modify Project Startup Port [application.yaml](sports-app%2Fsports-app-web%2Fsrc%2Fmain%2Fresources%2Fapplication.yaml)**

```yaml 
server.port:6001
```

### **Modify Configuration [application-dev.yaml](sports-app%2Fsports-app-web%2Fsrc%2Fmain%2Fresources%2Fapplication-dev.yaml)**

```yaml
spring:
  cloud:
    # Nacos configuration
    nacos:
      discovery:
        server-addr: ******:8848
        enabled: true
        username: nacos
        password: nacos
        group: sports
        namespace: 082d2278-d937-48c5-97a1-e9408058e6e7
  # mysql configuration
  datasource:
    username: root
    password: root
    url: jdbc:mysql://******:3304/sports?characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&serverTimezone=GMT%2B8
  # Email configuration
  mail:
    # Email server address (set hosts locally on the LAN)mail.abc.com
    host: ******
    # Email account
    username: ******
    # Email authorization code
    password: ******
    # Default encoding
    default-encoding: utf-8
    # Chinese subject
    zhSubject: "Sports Live Streaming Account Registration"
    # English subject
    enSubject: "Sports Live Streaming Account Registration"
  # redis configuration
  redis:
    # Address
    host: ******
    # Port, default is 6379
    port: 6379
    # Database index
    database: 6
    # Password (comment out if no password)
    # password:
    # Connection timeout
    timeout: 10s
    # Whether to enable SSL
    ssl: false
# Sports data source configuration 
edi:
  # Basketball data source
  basketball:
    user: ******
    secret: ******
    baseUrl: ******
  # Football data source
  football:
    user: ******
    secret: ******
    baseUrl: ******
```

## **Message Management ([sports-chat](sports-chat)) Deployment**
### **Modify Project Startup Port**
```yaml 
server.port:6001
```
### **Modify Configuration [application-dev.yaml](sports-chat%2Fsports-chat-web%2Fsrc%2Fmain/resources/application-dev.yaml)

```yaml 
spring:
  cloud:
    nacos:
      discovery:
        server-addr: ******:8848
        enabled: true
        username: nacos
        password: nacos
        group: sports
        namespace: 082d2278-d937-48c5-97a1-e9408058e6e7
  datasource:
    username: root
    password: root
    url: jdbc:mysql://******:3304/sports?characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&serverTimezone=GMT%2B8

  redis:
    # Address
    host: ******
    # Port, default is 6379
    port: 6379
    # Database index
    database: 6
    # Password (comment out if no password)
    # password:
    # Connection timeout
    timeout: 10s
    # Whether to enable SSL
    ssl: false
```
### **Jiguang Push Configuration [application-dev.yaml](sports-chat%2Fsports-chat-web%2Fsrc%2Fmain%2Fresources%2Fapplication-dev.yaml)**

```yaml
# Jiguang Push Configuration
jpush:
  appName: sports
  apiAddress: https://push.api.engagelab.cc/v4/push
  #appKey: Unique identifier for the Jiguang platform application
  appKey: ******
  #masterSecret: Key, used in conjunction with appKey for authentication
  masterSecret: ******
  #apnsProduction: Push environment, false: development, true: production; Note: This field is only valid for iOS notifications, invalid for Android, and invalid for iOS custom messages
  apnsProduction: false
```

### **Modify JIM to use Redis Configuration [jim.properties](sports-chat%2Fsports-chat-web%2Fsrc%2Fmain%2Fresources%2Fjim.properties)**

```properties
# When the connection pool is not enough, the retry connection times
jim.redis.retrynum = 50
# The maximum number of available connection instances, default value is 8;
jim.redis.maxactive = -1
# Control the maximum number of jedis instances in a pool that are idle (idle), the default value is also 8.
jim.redis.maxidle = 2000
# The maximum time to wait for an available connection, in milliseconds, the default value is -1, indicating never timeout.
jim.redis.maxwait = 5000
jim.redis.timeout = 2000
# Redis machine ip
jim.redis.host = 10.170.2.11
# Redis port number
jim.redis.port = 6379
# Redis password
jim.redis.auth =
```

## **Backend Gateway ([sports-admin-gateway](sports-admin-gateway)) Deployment**
### **Project Startup Port and Proxy Routing Configuration [application.yaml](sports-admin-gateway%2Fsrc%2Fmain%2Fresources%2Fapplication.yaml)**
```yaml
server:
  port: 6005
spring:
  profiles:
    active: test
  application:
    name: sports-admin-gateway
  cloud:
    gateway:
      routes:
        - id: sports-admin-manage //Backend management
          predicates:
            - Path=/apis/admin/**
          uri: lb://sports-admin-manage/apis/admin/**
```
### **Modify Nacos Configuration [application-dev.yaml](sports-admin-gateway%2Fsrc%2Fmain%2Fresources%2Fapplication-dev.yaml)**
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: ******:8848
        enabled: true
        username: nacos
        password: nacos
        group: sports
        namespace: 082d2278-d937-48c5-97a1-e9408058e6e7
```

## **Frontend Gateway ([sports-app-gateway](sports-app-gateway)) Deployment**
### **Project Startup Port and Proxy Routing Configuration**
```yaml
server:
  port: 6006
spring:
  profiles:
    active: test
  application:
    name: sports-app-gateway
  cloud:
    gateway:
      # Global CORS configuration
      globalcors:
        # Solve the problem of OPTIONS request interception
        add-to-simple-url-handler-mapping: true
        cors-configurations:
          # Intercepted request
          '[/**]':
            # Allowed cross-domain requests
            allowedOriginPatterns: "*" # spring boot2.4 and later configurations
            # Allowed headers in the request
            allowedHeaders: "*"
            # Cross-domain request method
            allowedMethods: "*"
            # Whether to allow carrying cookies
            allowCredentials: true
            # Effective period of cross-domain detection, in s
            maxAge: 3600
      routes:
        - id: sports-live-manage //Live management
          predicates:
            - Path=/apis/live/**```yaml
          uri: lb://sports-live-manage/apis/live/**
          #uri: http://192.168.102.6:6002
        - id: sports-app //User terminal
          predicates:
            - Path=/apis/app/**
          uri: lb://sports-app/apis/app/**
          #uri: http://192.168.102.6:6003
        - id: sports-chat //IM terminal
          predicates:
            - Path=/ws-sports-chat
          uri: ws://10.170.2.11:8888/
          filters:
            - StripPrefix=1
            - DedupeResponseHeader=Access-Control-Allow-Credentials Access-Control-Allow-Origin
```
### **Modify Nacos Configuration**
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: ******:8848
        enabled: true
        username: nacos
        password: nacos
        group: sports
        namespace: 082d2278-d937-48c5-97a1-e9408058e6e7
```

# **Setting Up the Postfix Mail Server**
## Step 1: Check if Postfix is Installed on the Server (Postfix is generally installed on CentOS 7)
```bash
Check status
sudo systemctl status postfix
Start postfix
systemctl start postfix
Check if it started
ps -ef |grep postfix
If postfix exists, skip step 2, no need to install Postfix
```
## Step 2: Install Postfix
```bash
sudo yum install postfix
```
## Step 3: Configure Postfix
Edit the configuration file `/etc/postfix/main.cf` and add or modify the following:
```plaintext
myhostname = mail.yourdomain.com
mydomain = yourdomain.com
myorigin = $mydomain
inet_interfaces = all
inet_protocols = ipv4
mydestination = $myhostname, $mydomain
home_mailbox = Maildir/
mynetworks = 0.0.0.0/0
```
## Step 4: Check for Postfix Configuration Errors
```bash
Execute without prompts indicating no configuration exceptions
sudo postfix check
View postfix non-default and effective configurations
postconf -n 
```
## Step 5: Verify Support for Cyrus and Dovecot Features
```bash
sudo postconf -a
If cyrus and dovecot do not exist, postfix is not installed properly
```
## Step 6: Restart the Postfix Service
```bash
sudo systemctl restart postfix
```
## Step 7: Create Email Sender
```bash
-------------Add Email Test Account--------------
Create a group for the test account
groupadd mailusers
Create user1 account belonging only to the mailusers group and unable to log in to the system, used for testing email
useradd -g mailusers -s /sbin/nologin user1@domain.com
Create a password for user1@domain.com
passwd user1@domain.com
```
## Step 8: Domain Name Resolution Configuration
```bash
Record type: A    Host record: mail    Record value: Server IP
Record type: MX   Host record: @       Record value: mail.domain.com
```
## Step 9: Check Firewall Settings to Open Port 25 (for cloud services, configure inbound rules in the security group directly, skip the following commands)
```bash
sudo firewall-cmd --zone=public --add-port=25/tcp --permanent
sudo firewall-cmd --reload
```

