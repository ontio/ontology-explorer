package com.github.ontio.blocksync.mapper;

import com.github.ontio.blocksync.model.OntId;
import com.github.ontio.blocksync.model.OntIdExample;
import java.util.List;

import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

public interface OntIdMapper {
    long countByExample(OntIdExample example);

    int deleteByExample(OntIdExample example);

    @Delete({
        "delete from tbl_ontid_tx_detail",
        "where tx_hash = #{txHash,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String txHash);

    @Insert({
        "insert into tbl_ontid_tx_detail (tx_hash, tx_type, ",
        "ontid, tx_time, block_height, ",
        "description, fee)",
        "values (#{txHash,jdbcType=VARCHAR}, #{txType,jdbcType=INTEGER}, ",
        "#{ontid,jdbcType=VARCHAR}, #{txTime,jdbcType=INTEGER}, #{blockHeight,jdbcType=INTEGER}, ",
        "#{description,jdbcType=VARCHAR}, #{fee,jdbcType=DECIMAL})"
    })
    int insert(OntId record);

    int insertSelective(OntId record);

    List<OntId> selectByExampleWithRowbounds(OntIdExample example, RowBounds rowBounds);

    List<OntId> selectByExample(OntIdExample example);

    @Select({
        "select",
        "tx_hash, tx_type, ontid, tx_time, block_height, description, fee",
        "from tbl_ontid_tx_detail",
        "where tx_hash = #{txHash,jdbcType=VARCHAR}"
    })
    @ResultMap("com.github.ontio.blocksync.mapper.OntIdMapper.BaseResultMap")
    OntId selectByPrimaryKey(String txHash);

    int updateByExampleSelective(@Param("record") OntId record, @Param("example") OntIdExample example);

    int updateByExample(@Param("record") OntId record, @Param("example") OntIdExample example);

    int updateByPrimaryKeySelective(OntId record);

    @Update({
        "update tbl_ontid_tx_detail",
        "set tx_type = #{txType,jdbcType=INTEGER},",
          "ontid = #{ontid,jdbcType=VARCHAR},",
          "tx_time = #{txTime,jdbcType=INTEGER},",
          "block_height = #{blockHeight,jdbcType=INTEGER},",
          "description = #{description,jdbcType=VARCHAR},",
          "fee = #{fee,jdbcType=DECIMAL}",
        "where tx_hash = #{txHash,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(OntId record);

    // Self-defined mapper

    @Select({
            "select count(*)",
            "from tbl_ontid_tx_detail",
            "where height = #{height,jdbcType=INTEGER}"
    })
    int selectCountByHeight(Integer height);

    @Delete({
        "delete from tbl_ontid_tx_detail",
        "where height = #{height,jdbcType=INTEGER}"
    })
    int deleteByHeight(Integer height);
}