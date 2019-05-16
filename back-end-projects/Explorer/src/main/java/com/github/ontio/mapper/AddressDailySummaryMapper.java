package com.github.ontio.mapper;

import com.github.ontio.model.dao.AddressDailySummary;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface AddressDailySummaryMapper extends Mapper<AddressDailySummary> {

    Integer selectAllAddressCount();

}