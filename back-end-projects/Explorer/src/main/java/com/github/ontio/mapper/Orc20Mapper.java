package com.github.ontio.mapper;

import com.github.ontio.model.dao.Orc20;
import com.github.ontio.model.dto.Orc20DetailDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface Orc20Mapper extends Mapper<Orc20> {

    List<Orc20DetailDto> selectOrc20Tokens(@Param("ascending") List<String> ascending, @Param("descending") List<String> descending);


    Orc20DetailDto selectOrc20TokenDetail(@Param("contractHash") String contractHash);
}