package com.github.ontio.blocksync.mapper;

import com.github.ontio.blocksync.model.Block;
import com.github.ontio.blocksync.model.BlockExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

public interface BlockMapper {
    long countByExample(BlockExample example);

    int deleteByExample(BlockExample example);

    @Delete({
        "delete from tbl_block",
        "where block_height = #{blockHeight,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer blockHeight);

    @Insert({
        "insert into tbl_block (block_height, block_hash, ",
        "txs_root, block_time, ",
        "consensus_data, bookkeepers, ",
        "tx_count, block_size)",
        "values (#{blockHeight,jdbcType=INTEGER}, #{blockHash,jdbcType=VARCHAR}, ",
        "#{txsRoot,jdbcType=VARCHAR}, #{blockTime,jdbcType=INTEGER}, ",
        "#{consensusData,jdbcType=VARCHAR}, #{bookkeepers,jdbcType=VARCHAR}, ",
        "#{txCount,jdbcType=INTEGER}, #{blockSize,jdbcType=INTEGER})"
    })
    int insert(Block record);

    int insertSelective(Block record);

    List<Block> selectByExampleWithRowbounds(BlockExample example, RowBounds rowBounds);

    List<Block> selectByExample(BlockExample example);

    @Select({
        "select",
        "block_height, block_hash, txs_root, block_time, consensus_data, bookkeepers, ",
        "tx_count, block_size",
        "from tbl_block",
        "where block_height = #{blockHeight,jdbcType=INTEGER}"
    })
    @ResultMap("com.github.ontio.blocksync.mapper.BlockMapper.BaseResultMap")
    Block selectByPrimaryKey(Integer blockHeight);

    int updateByExampleSelective(@Param("record") Block record, @Param("example") BlockExample example);

    int updateByExample(@Param("record") Block record, @Param("example") BlockExample example);

    int updateByPrimaryKeySelective(Block record);

    @Update({
        "update tbl_block",
        "set block_hash = #{blockHash,jdbcType=VARCHAR},",
          "txs_root = #{txsRoot,jdbcType=VARCHAR},",
          "block_time = #{blockTime,jdbcType=INTEGER},",
          "consensus_data = #{consensusData,jdbcType=VARCHAR},",
          "bookkeepers = #{bookkeepers,jdbcType=VARCHAR},",
          "tx_count = #{txCount,jdbcType=INTEGER},",
          "block_size = #{blockSize,jdbcType=INTEGER}",
        "where block_height = #{blockHeight,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Block record);
}