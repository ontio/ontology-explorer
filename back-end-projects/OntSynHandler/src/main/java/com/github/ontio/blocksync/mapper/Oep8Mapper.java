package com.github.ontio.blocksync.mapper;

import com.github.ontio.blocksync.model.Oep8;
import com.github.ontio.blocksync.model.Oep8Example;
import com.github.ontio.blocksync.model.Oep8Key;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

public interface Oep8Mapper {
    long countByExample(Oep8Example example);

    int deleteByExample(Oep8Example example);

    @Delete({
        "delete from tbl_oep8",
        "where contract_hash = #{contractHash,jdbcType=VARCHAR}",
          "and token_id = #{tokenId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(Oep8Key key);

    @Insert({
        "insert into tbl_oep8 (contract_hash, token_id, ",
        "name, total_supply, ",
        "symbol, create_time, ",
        "audit_flag, update_time)",
        "values (#{contractHash,jdbcType=VARCHAR}, #{tokenId,jdbcType=VARCHAR}, ",
        "#{name,jdbcType=VARCHAR}, #{totalSupply,jdbcType=VARCHAR}, ",
        "#{symbol,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{auditFlag,jdbcType=BIT}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(Oep8 record);

    int insertSelective(Oep8 record);

    List<Oep8> selectByExampleWithRowbounds(Oep8Example example, RowBounds rowBounds);

    List<Oep8> selectByExample(Oep8Example example);

    @Select({
        "select",
        "contract_hash, token_id, name, total_supply, symbol, create_time, audit_flag, ",
        "update_time",
        "from tbl_oep8",
        "where contract_hash = #{contractHash,jdbcType=VARCHAR}",
          "and token_id = #{tokenId,jdbcType=VARCHAR}"
    })
    @ResultMap("com.github.ontio.blocksync.mapper.Oep8Mapper.BaseResultMap")
    Oep8 selectByPrimaryKey(Oep8Key key);

    int updateByExampleSelective(@Param("record") Oep8 record, @Param("example") Oep8Example example);

    int updateByExample(@Param("record") Oep8 record, @Param("example") Oep8Example example);

    int updateByPrimaryKeySelective(Oep8 record);

    @Update({
        "update tbl_oep8",
        "set name = #{name,jdbcType=VARCHAR},",
          "total_supply = #{totalSupply,jdbcType=VARCHAR},",
          "symbol = #{symbol,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "audit_flag = #{auditFlag,jdbcType=BIT},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where contract_hash = #{contractHash,jdbcType=VARCHAR}",
          "and token_id = #{tokenId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(Oep8 record);

    // Self-defined mapper

    @Update({
            "update tbl_oep8",
            "set total_supply = #{totalSupply,jdbcType=DECIMAL},",
            "where contract_hash = #{contractHash,jdbcType=VARCHAR}",
            "and token_id = #{tokenId,jdbcType=VARCHAR}"
    })
    int updateTotalSupply(Oep8 oep8);

    @Select({
            "select name, symbol, decimals",
            "from tbl_oep8",
            "where audit_flag = 1"
    })
    List<Map> selectApprovedKeyInfo();
}