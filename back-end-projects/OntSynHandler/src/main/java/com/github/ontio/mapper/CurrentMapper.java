package com.github.ontio.mapper;

import com.github.ontio.model.dao.Current;
import tk.mybatis.mapper.common.Mapper;

public interface CurrentMapper extends Mapper<Current> {

    void update(Current current);

    Integer selectBlockHeight();

}