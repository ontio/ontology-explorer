package com.github.ontio.mapper;

import com.github.ontio.model.dao.Orc721;
import com.github.ontio.model.dto.Orc721DetailDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@Repository
public interface Orc721Mapper extends Mapper<Orc721> {

    List<Orc721DetailDto> selectOrc721Tokens(@Param("ascending") List<String> ascending, @Param("descending") List<String> descending);

    Orc721DetailDto selectOrc721TokenDetail(@Param("contractHash") String contractHash);
}