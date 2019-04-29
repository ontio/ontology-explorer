package com.github.ontio.blocksync.mapper;

import com.github.ontio.blocksync.model.Contract;
import com.github.ontio.blocksync.model.ContractExample;
import com.github.ontio.blocksync.model.ContractWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

public interface ContractMapper {
    long countByExample(ContractExample example);

    int deleteByExample(ContractExample example);

    @Delete({
        "delete from tbl_contract",
        "where contract_hash = #{contractHash,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String contractHash);

    @Insert({
        "insert into tbl_contract (contract_hash, name, ",
        "create_time, update_time, ",
        "audit_flag, contact_info, ",
        "description, type, ",
        "logo, creator, address_count, ",
        "tx_count, ont_sum, ",
        "ong_sum, token_sum, ",
        "category, dapp_name, ",
        "dappstore_flag, total_reward, ",
        "lastweek_reward, abi, ",
        "code, source_code)",
        "values (#{contractHash,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, ",
        "#{createTime,jdbcType=INTEGER}, #{updateTime,jdbcType=INTEGER}, ",
        "#{auditFlag,jdbcType=INTEGER}, #{contactInfo,jdbcType=VARCHAR}, ",
        "#{description,jdbcType=VARCHAR}, #{type,jdbcType=VARCHAR}, ",
        "#{logo,jdbcType=VARCHAR}, #{creator,jdbcType=VARCHAR}, #{addressCount,jdbcType=INTEGER}, ",
        "#{txCount,jdbcType=INTEGER}, #{ontSum,jdbcType=DECIMAL}, ",
        "#{ongSum,jdbcType=DECIMAL}, #{tokenSum,jdbcType=VARCHAR}, ",
        "#{category,jdbcType=VARCHAR}, #{dappName,jdbcType=VARCHAR}, ",
        "#{dappstoreFlag,jdbcType=INTEGER}, #{totalReward,jdbcType=DECIMAL}, ",
        "#{lastweekReward,jdbcType=DECIMAL}, #{abi,jdbcType=LONGVARCHAR}, ",
        "#{code,jdbcType=LONGVARCHAR}, #{sourceCode,jdbcType=LONGVARCHAR})"
    })
    int insert(ContractWithBLOBs record);

    int insertSelective(ContractWithBLOBs record);

    List<ContractWithBLOBs> selectByExampleWithBLOBsWithRowbounds(ContractExample example, RowBounds rowBounds);

    List<ContractWithBLOBs> selectByExampleWithBLOBs(ContractExample example);

    List<Contract> selectByExampleWithRowbounds(ContractExample example, RowBounds rowBounds);

    List<Contract> selectByExample(ContractExample example);

    @Select({
        "select",
        "contract_hash, name, create_time, update_time, audit_flag, contact_info, description, ",
        "type, logo, creator, address_count, tx_count, ont_sum, ong_sum, token_sum, category, ",
        "dapp_name, dappstore_flag, total_reward, lastweek_reward, abi, code, source_code",
        "from tbl_contract",
        "where contract_hash = #{contractHash,jdbcType=VARCHAR}"
    })
    @ResultMap("com.github.ontio.blocksync.mapper.ContractMapper.ResultMapWithBLOBs")
    ContractWithBLOBs selectByPrimaryKey(String contractHash);

    int updateByExampleSelective(@Param("record") ContractWithBLOBs record, @Param("example") ContractExample example);

    int updateByExampleWithBLOBs(@Param("record") ContractWithBLOBs record, @Param("example") ContractExample example);

    int updateByExample(@Param("record") Contract record, @Param("example") ContractExample example);

    int updateByPrimaryKeySelective(ContractWithBLOBs record);

    @Update({
        "update tbl_contract",
        "set name = #{name,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=INTEGER},",
          "update_time = #{updateTime,jdbcType=INTEGER},",
          "audit_flag = #{auditFlag,jdbcType=INTEGER},",
          "contact_info = #{contactInfo,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "type = #{type,jdbcType=VARCHAR},",
          "logo = #{logo,jdbcType=VARCHAR},",
          "creator = #{creator,jdbcType=VARCHAR},",
          "address_count = #{addressCount,jdbcType=INTEGER},",
          "tx_count = #{txCount,jdbcType=INTEGER},",
          "ont_sum = #{ontSum,jdbcType=DECIMAL},",
          "ong_sum = #{ongSum,jdbcType=DECIMAL},",
          "token_sum = #{tokenSum,jdbcType=VARCHAR},",
          "category = #{category,jdbcType=VARCHAR},",
          "dapp_name = #{dappName,jdbcType=VARCHAR},",
          "dappstore_flag = #{dappstoreFlag,jdbcType=INTEGER},",
          "total_reward = #{totalReward,jdbcType=DECIMAL},",
          "lastweek_reward = #{lastweekReward,jdbcType=DECIMAL},",
          "abi = #{abi,jdbcType=LONGVARCHAR},",
          "code = #{code,jdbcType=LONGVARCHAR},",
          "source_code = #{sourceCode,jdbcType=LONGVARCHAR}",
        "where contract_hash = #{contractHash,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKeyWithBLOBs(ContractWithBLOBs record);

    @Update({
        "update tbl_contract",
        "set name = #{name,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=INTEGER},",
          "update_time = #{updateTime,jdbcType=INTEGER},",
          "audit_flag = #{auditFlag,jdbcType=INTEGER},",
          "contact_info = #{contactInfo,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "type = #{type,jdbcType=VARCHAR},",
          "logo = #{logo,jdbcType=VARCHAR},",
          "creator = #{creator,jdbcType=VARCHAR},",
          "address_count = #{addressCount,jdbcType=INTEGER},",
          "tx_count = #{txCount,jdbcType=INTEGER},",
          "ont_sum = #{ontSum,jdbcType=DECIMAL},",
          "ong_sum = #{ongSum,jdbcType=DECIMAL},",
          "token_sum = #{tokenSum,jdbcType=VARCHAR},",
          "category = #{category,jdbcType=VARCHAR},",
          "dapp_name = #{dappName,jdbcType=VARCHAR},",
          "dappstore_flag = #{dappstoreFlag,jdbcType=INTEGER},",
          "total_reward = #{totalReward,jdbcType=DECIMAL},",
          "lastweek_reward = #{lastweekReward,jdbcType=DECIMAL}",
        "where contract_hash = #{contractHash,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(Contract record);
}