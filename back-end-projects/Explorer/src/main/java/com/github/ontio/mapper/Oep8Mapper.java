package com.github.ontio.mapper;

import com.github.ontio.model.dao.Oep8;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface Oep8Mapper extends Mapper<Oep8> {

    List<Map<String,String>> selectAuditPassedOep8();

}