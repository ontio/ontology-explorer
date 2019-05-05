package com.github.ontio.mapper;

import com.github.ontio.model.dao.Block;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface BlockMapper extends Mapper<Block> {
}