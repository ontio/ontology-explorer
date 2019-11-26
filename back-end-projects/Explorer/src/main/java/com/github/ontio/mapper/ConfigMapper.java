package com.github.ontio.mapper;

import com.github.ontio.model.dao.Config;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface ConfigMapper extends Mapper<Config> {
}