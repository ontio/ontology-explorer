package com.github.ontio.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@Scope(value = "singleton")
@EnableConfigurationProperties
public class ParamsConfig {

    @Value("#{'${config.hosts}'.split(',')}")
    private List<String> hosts = new ArrayList<>();

    public interface Field {

        String maxStakingChangeCount = "maxStakingChangeCount";

    }

}