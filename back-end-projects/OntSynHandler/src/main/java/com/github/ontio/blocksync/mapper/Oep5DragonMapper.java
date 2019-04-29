package com.github.ontio.blocksync.mapper;

import com.github.ontio.blocksync.model.Oep5Dragon;
import com.github.ontio.blocksync.model.Oep5DragonExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

public interface Oep5DragonMapper {
    long countByExample(Oep5DragonExample example);

    int deleteByExample(Oep5DragonExample example);

    @Delete({
        "delete from tbl_oep5_dragon",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into tbl_oep5_dragon (id, contract_hash, ",
        "asset_name, json_url)",
        "values (#{id,jdbcType=INTEGER}, #{contractHash,jdbcType=VARCHAR}, ",
        "#{assetName,jdbcType=VARCHAR}, #{jsonUrl,jdbcType=VARCHAR})"
    })
    int insert(Oep5Dragon record);

    int insertSelective(Oep5Dragon record);

    List<Oep5Dragon> selectByExampleWithRowbounds(Oep5DragonExample example, RowBounds rowBounds);

    List<Oep5Dragon> selectByExample(Oep5DragonExample example);

    @Select({
        "select",
        "id, contract_hash, asset_name, json_url",
        "from tbl_oep5_dragon",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ResultMap("com.github.ontio.blocksync.mapper.Oep5DragonMapper.BaseResultMap")
    Oep5Dragon selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Oep5Dragon record, @Param("example") Oep5DragonExample example);

    int updateByExample(@Param("record") Oep5Dragon record, @Param("example") Oep5DragonExample example);

    int updateByPrimaryKeySelective(Oep5Dragon record);

    @Update({
        "update tbl_oep5_dragon",
        "set contract_hash = #{contractHash,jdbcType=VARCHAR},",
          "asset_name = #{assetName,jdbcType=VARCHAR},",
          "json_url = #{jsonUrl,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Oep5Dragon record);
}