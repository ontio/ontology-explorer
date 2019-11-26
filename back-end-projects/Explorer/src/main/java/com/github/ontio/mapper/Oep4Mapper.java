package com.github.ontio.mapper;

import com.github.ontio.model.dao.Oep4;
import com.github.ontio.model.dto.Oep4DetailDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface Oep4Mapper extends Mapper<Oep4> {

    List<Oep4DetailDto> selectOep4Tokens();

    Oep4DetailDto selectOep4TokenDetail(@Param("contractHash") String contractHash);

}