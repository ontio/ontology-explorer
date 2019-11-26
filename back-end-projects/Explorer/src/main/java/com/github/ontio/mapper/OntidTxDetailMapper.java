package com.github.ontio.mapper;

import com.github.ontio.model.dto.OntidTxDetailDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface OntidTxDetailMapper extends Mapper<OntidTxDetailDto> {

    List<OntidTxDetailDto> selectOntidTxsByPage(@Param("ontid") String ontid, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    Integer selectTxCountByOntid(@Param("ontid") String ontId);

    OntidTxDetailDto selectOneByTxHash(@Param("txHash") String txHash);

}