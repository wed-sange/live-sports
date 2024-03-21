package org.sports.app.web.config;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.sports.springboot.starter.web.config.MyNumberSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @描述: 统一时间格式
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/8/2
 * @创建时间: 14:37
 */
@Slf4j
@Configuration
public class JacksonConfig {

    @Bean
    public JavaTimeModule javaTimeModule() {
        // 全局配置序列化返回 JSON 处理
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        javaTimeModule.addSerializer(LocalDateTime.class, new CustomerLocalDateTimeSerializer());
        javaTimeModule.addSerializer(Number.class,  MyNumberSerializer.INSTANCE);
        javaTimeModule.addDeserializer(LocalDateTime.class, new CustomerLocalDateTimeDeserializer());
        log.info("初始化 jackson 配置");
        return javaTimeModule;
    }
    /**
     * description:序列化
     * LocalDateTime序列化为毫秒级时间戳
     */
    public static class CustomerLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            if (value != null) {
                long timestamp  = value.toInstant(ZoneOffset.UTC).toEpochMilli();
                gen.writeNumber(timestamp);
            }
        }
    }
    /**
     * description:反序列化
     * 毫秒级时间戳序列化为LocalDateTime
     */
    public static class CustomerLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext)
                throws IOException {
            long timestamp = p.getValueAsLong();
            if (timestamp > 0) {
                return LocalDateTimeUtil.of(timestamp, ZoneOffset.UTC);
            } else {
                return null;
            }
        }
    }


}
