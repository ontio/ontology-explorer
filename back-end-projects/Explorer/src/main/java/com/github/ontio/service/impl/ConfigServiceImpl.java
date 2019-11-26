package com.github.ontio.service.impl;

import com.github.ontio.common.ParamsConfig;
import com.github.ontio.mapper.ConfigMapper;
import com.github.ontio.model.dao.Config;
import com.github.ontio.service.IConfigService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@NoArgsConstructor
public class ConfigServiceImpl implements IConfigService {

    private ConfigMapper configMapper;

    @Autowired
    public ConfigServiceImpl(ConfigMapper configMapper) {
        this.configMapper = configMapper;
    }

    @Override
    public Long getMaxStakingChangeCount() {
        Config config = configMapper.selectByPrimaryKey(ParamsConfig.Field.maxStakingChangeCount);
        return config == null ? 0L : Long.parseLong(config.getValue());
    }

}
