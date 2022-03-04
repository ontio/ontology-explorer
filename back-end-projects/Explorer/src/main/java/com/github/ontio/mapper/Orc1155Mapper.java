package com.github.ontio.mapper;

import com.github.ontio.model.dao.Orc1155;
import com.github.ontio.model.dto.Orc1155DetailDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;


@Repository
public interface Orc1155Mapper extends Mapper<Orc1155> {

    List<Map<String, String>> selectAuditPassedOrc1155(@Param("symbol") String symbol);

    List<Orc1155DetailDto> selectOrc1155Tokens(@Param("ascending") List<String> ascending, @Param("descending") List<String> descending);

    Orc1155DetailDto selectOrc1155TokenDetail(@Param("contractHash") String contractHash);
}