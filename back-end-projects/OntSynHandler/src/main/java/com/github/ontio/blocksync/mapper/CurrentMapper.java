package com.github.ontio.blocksync.mapper;

import com.github.ontio.blocksync.model.Current;
import com.github.ontio.blocksync.model.CurrentExample;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface CurrentMapper {
    long countByExample(CurrentExample example);

    int deleteByExample(CurrentExample example);

    @Insert({
        "insert into tbl_current (block_height, tx_count, ",
        "ontid_count, nonontid_tx_count)",
        "values (#{blockHeight,jdbcType=INTEGER}, #{txCount,jdbcType=INTEGER}, ",
        "#{ontidCount,jdbcType=INTEGER}, #{nonontidTxCount,jdbcType=INTEGER})"
    })
    int insert(Current record);

    int insertSelective(Current record);

    List<Current> selectByExampleWithRowbounds(CurrentExample example, RowBounds rowBounds);

    List<Current> selectByExample(CurrentExample example);

    int updateByExampleSelective(@Param("record") Current record, @Param("example") CurrentExample example);

    int updateByExample(@Param("record") Current record, @Param("example") CurrentExample example);
}