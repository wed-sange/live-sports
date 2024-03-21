/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sports.chat.web;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jim.core.packets.Command;
import org.jim.core.utils.PropUtil;
import org.jim.server.JimServer;
import org.jim.server.command.CommandManager;
import org.jim.server.command.handler.*;
import org.jim.server.config.ImServerConfig;
import org.jim.server.config.PropertyImServerConfigBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.sports.chat.service.util.PushMessageThreadPool;
import org.sports.chat.web.command.MyWsHandshakeProcessor;
import org.sports.chat.web.command.MysqlAsyncChatMessageProcessor;
import org.sports.chat.web.helper.GroupCmdJoinProcessor;
import org.sports.chat.web.helper.db.RedisAndMysqlMessageHelper;
import org.sports.chat.web.listener.GroupListener;
import org.sports.chat.web.listener.UserListener;
import org.sports.chat.web.service.LoginServiceProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.tio.core.ssl.SslConfig;

import java.net.InetAddress;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"org.sports.chat", "org.sports.admin.manage"})
@MapperScan({"org.sports.chat.service.mapper","org.sports.admin.manage.dao.mapper"})
public class SportsChatApplication {
    @SneakyThrows
    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication app = new SpringApplication(SportsChatApplication.class);
        ConfigurableApplicationContext application = app.run(args);
        Environment env = application.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "External: \thttp://{}:{}\n\t" +
                        "Doc: \thttp://{}:{}/apis/doc.html\n" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
        /**
         * 启动服务后启动IM
         */
        ImServerConfig imServerConfig = new PropertyImServerConfigBuilder("jim.properties").build();
        //初始化SSL;(开启SSL之前,你要保证你有SSL证书哦...)
        initSsl(imServerConfig);
        //设置群组监听器，非必须，根据需要自己选择性实现;
        imServerConfig.setImGroupListener(new GroupListener());
        //设置绑定用户监听器，非必须，根据需要自己选择性实现;
        imServerConfig.setImUserListener(new UserListener());
        //绑定消息存储处理使用mysql+redis的方式
        imServerConfig.setMessageHelper(new RedisAndMysqlMessageHelper());
        JimServer jimServer = new JimServer(imServerConfig);

        /*****************start 以下处理器根据业务需要自行添加与扩展，每个Command都可以添加扩展,此处为demo中处理**********************************/

        MyHandshakeReqHandler handshakeReqHandler = CommandManager.getCommand(Command.COMMAND_HANDSHAKE_REQ, MyHandshakeReqHandler.class);
        //添加自定义握手处理器;
        handshakeReqHandler.addMultiProtocolProcessor(new MyWsHandshakeProcessor());
        LoginReqHandler loginReqHandler = CommandManager.getCommand(Command.COMMAND_LOGIN_REQ, LoginReqHandler.class);
        //添加加入群聊处理器；
        JoinGroupReqHandler joinGroupReqHandler = CommandManager.getCommand(Command.COMMAND_JOIN_GROUP_REQ, JoinGroupReqHandler.class);
        GroupCmdJoinProcessor groupCmdJoinProcessor = new GroupCmdJoinProcessor();
        joinGroupReqHandler.setSingleProcessor(groupCmdJoinProcessor);
        //添加退出群聊处理器；
        OutGroupReqHandler outGroupReqHandler = CommandManager.getCommand(Command.COMMAND_EXIT_GROUP_RESP, OutGroupReqHandler.class);
        outGroupReqHandler.setSingleProcessor(groupCmdJoinProcessor);
        //添加登录业务处理器;
        loginReqHandler.setSingleProcessor(new LoginServiceProcessor());
        //添加用户业务聊天记录处理器，用户自己继承抽象类BaseAsyncChatMessageProcessor即可，以下为内置默认的处理器！
        ChatReqHandler chatReqHandler = CommandManager.getCommand(Command.COMMAND_CHAT_REQ, ChatReqHandler.class);
        chatReqHandler.setSingleProcessor(new MysqlAsyncChatMessageProcessor());
        /*****************end *******************************************************************************************/
        jimServer.start();
        new PushMessageThreadPool().start();
    }

    /**
     * 开启SSL之前，你要保证你有SSL证书哦！
     *
     * @param imServerConfig
     * @throws Exception
     */
    private static void initSsl(ImServerConfig imServerConfig) throws Exception {
        //开启SSL
        if (ImServerConfig.ON.equals(imServerConfig.getIsSSL())) {
            String keyStorePath = PropUtil.get("jim.key.store.path");
            String keyStoreFile = keyStorePath;
            String trustStoreFile = keyStorePath;
            String keyStorePwd = PropUtil.get("jim.key.store.pwd");
            if (StringUtils.isNotBlank(keyStoreFile) && StringUtils.isNotBlank(trustStoreFile)) {
                SslConfig sslConfig = SslConfig.forServer(keyStoreFile, trustStoreFile, keyStorePwd);
                imServerConfig.setSslConfig(sslConfig);
            }
        }
    }
}
