package com.github.ontio.blocksync.mapper;

import com.github.ontio.blocksync.model.Oep5;
import com.github.ontio.blocksync.model.Oep5Example;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

public interface Oep5Mapper {
    long countByExample(Oep5Example example);

    int deleteByExample(Oep5Example example);

    @Delete({
        "delete from tbl_oep5",
        "where contract_hash = #{contractHash,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String contractHash);

    @Insert({
        "insert into tbl_oep5 (contract_hash, name, ",
        "total_supply, symbol, ",
        "create_time, audit_flag, ",
        "update_time)",
        "values (#{contractHash,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, ",
        "#{totalSupply,jdbcType=VARCHAR}, #{symbol,jdbcType=VARCHAR}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{auditFlag,jdbcType=BIT}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(Oep5 record);

    int insertSelective(Oep5 record);

    List<Oep5> selectByExampleWithRowbounds(Oep5Example example, RowBounds rowBounds);

    List<Oep5> selectByExample(Oep5Example example);

    @Select({
        "select",
        "contract_hash, name, total_supply, symbol, create_time, audit_flag, update_time",
        "from tbl_oep5",
        "where contract_hash = #{contractHash,jdbcType=VARCHAR}"
    })
    @ResultMap("com.github.ontio.blocksync.mapper.Oep5Mapper.BaseResultMap")
    Oep5 selectByPrimaryKey(String contractHash);

    int updateByExampleSelective(@Param("record") Oep5 record, @Param("example") Oep5Example example);

    int updateByExample(@Param("record") Oep5 record, @Param("example") Oep5Example example);

    int updateByPrimaryKeySelective(Oep5 record);

    @Update({
        "update tbl_oep5",
        "set name = #{name,jdbcType=VARCHAR},",
          "total_supply = #{totalSupply,jdbcType=VARCHAR},",
          "symbol = #{symbol,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "audit_flag = #{auditFlag,jdbcType=BIT},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where contract_hash = #{contractHash,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(Oep5 record);
}