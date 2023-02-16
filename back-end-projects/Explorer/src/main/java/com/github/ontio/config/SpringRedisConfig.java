package com.github.ontio.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.protocol.ProtocolVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;

/**
 * @author li
 * @version 1.0
 * @date 2023/2/16
 */

@Configuration
@Slf4j
public class SpringRedisConfig implements LettuceClientConfigurationBuilderCustomizer {

    /**
     * SpringBoot2.5.1使用的lettuce版本是6.1.2，连接Redis时默认通过RESP3协议，此时会失败，需要手动改回RESP2
     * Redis5及以下默认使用RESP2进行连接，RESP3只能连接redis6
     * @param clientConfigurationBuilder
     */
    @Override
    public void customize(LettuceClientConfiguration.LettuceClientConfigurationBuilder clientConfigurationBuilder) {
        clientConfigurationBuilder.clientOptions(ClientOptions.builder()
                .protocolVersion(ProtocolVersion.RESP2)
                .build());
    }

}