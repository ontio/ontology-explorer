package com.github.ontio.mapper;

import com.github.ontio.model.dao.Oep8;
import com.github.ontio.model.dto.Oep8DetailDto;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface Oep8Mapper extends Mapper<Oep8> {

    // self-defined SQL

    List<Map<String,String>> selectAuditPassedOep8();

    List<Oep8DetailDto> selectOep8Tokens();

}