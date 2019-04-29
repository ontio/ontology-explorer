package com.github.ontio.blocksync.mapper;

import com.github.ontio.blocksync.model.Oep4;
import com.github.ontio.blocksync.model.Oep4Example;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

public interface Oep4Mapper {
    long countByExample(Oep4Example example);

    int deleteByExample(Oep4Example example);

    @Delete({
        "delete from tbl_oep4",
        "where contract_hash = #{contractHash,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String contractHash);

    @Insert({
        "insert into tbl_oep4 (contract_hash, name, ",
        "total_supply, symbol, ",
        "decimals, create_time, ",
        "audit_flag, update_time)",
        "values (#{contractHash,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, ",
        "#{totalSupply,jdbcType=VARCHAR}, #{symbol,jdbcType=VARCHAR}, ",
        "#{decimals,jdbcType=INTEGER}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{auditFlag,jdbcType=BIT}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(Oep4 record);

    int insertSelective(Oep4 record);

    List<Oep4> selectByExampleWithRowbounds(Oep4Example example, RowBounds rowBounds);

    List<Oep4> selectByExample(Oep4Example example);

    @Select({
        "select",
        "contract_hash, name, total_supply, symbol, decimals, create_time, audit_flag, ",
        "update_time",
        "from tbl_oep4",
        "where contract_hash = #{contractHash,jdbcType=VARCHAR}"
    })
    @ResultMap("com.github.ontio.blocksync.mapper.Oep4Mapper.BaseResultMap")
    Oep4 selectByPrimaryKey(String contractHash);

    int updateByExampleSelective(@Param("record") Oep4 record, @Param("example") Oep4Example example);

    int updateByExample(@Param("record") Oep4 record, @Param("example") Oep4Example example);

    int updateByPrimaryKeySelective(Oep4 record);

    @Update({
        "update tbl_oep4",
        "set name = #{name,jdbcType=VARCHAR},",
          "total_supply = #{totalSupply,jdbcType=VARCHAR},",
          "symbol = #{symbol,jdbcType=VARCHAR},",
          "decimals = #{decimals,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "audit_flag = #{auditFlag,jdbcType=BIT},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where contract_hash = #{contractHash,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(Oep4 record);

    // Self-defined mapper

    @Select({
        "select name, symbol, decimals",
        "from tbl_oep4",
        "where audit_flag = 1"
    })
    List<Map> selectApprovedKeyInfo();
}