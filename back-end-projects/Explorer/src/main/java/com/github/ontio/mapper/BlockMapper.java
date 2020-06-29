package com.github.ontio.mapper;

import com.github.ontio.model.dto.BlockDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface BlockMapper extends Mapper<BlockDto> {

    List<BlockDto> selectBlocksByPage(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    List<Map> selectHeightAndTime(@Param("count") int count);

    BlockDto selectOneByHeight(@Param("blockHeight") int blockHeight);

    BlockDto selectOneByHash(@Param("blockHash") String hash);

}