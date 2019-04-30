package com.github.ontio.mapper;

import com.github.ontio.model.dao.Oep5;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface Oep5Mapper extends Mapper<Oep5> {

    List<Oep5> selectApprovedRecords();


}