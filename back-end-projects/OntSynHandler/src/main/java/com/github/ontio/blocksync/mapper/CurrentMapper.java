package com.github.ontio.blocksync.mapper;

import com.github.ontio.blocksync.model.Current;
import com.github.ontio.blocksync.model.CurrentExample;
import java.util.List;
import java.util.Map;

import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

    // Self-defined mapper

    @Update({
        "update tbl_current",
        "set block_height = #{record.blockHeight,jdbcType=INTEGER},",
        "tx_count = #{record.txCount,jdbcType=INTEGER},",
        "ontid_count = #{record.ontidCount,jdbcType=INTEGER},",
        "nonontid_tx_count = #{record.nonontidTxCount,jdbcType=INTEGER}"
    })
    int update(Current current);

    @Select({
        "select",
        "tx_count, ontid_count, nonontid_tx_count, block_height",
        "from tbl_current"
    })
    Map<String, Integer> selectSummary();

    @Select({
        "select Height",
        "from tbl_current"
    })
    Integer selectDBHeight();
}