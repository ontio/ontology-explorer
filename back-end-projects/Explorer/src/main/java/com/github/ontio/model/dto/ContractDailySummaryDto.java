package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.dao.ContractDailySummary;

import javax.persistence.Table;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/9
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "tbl_contract_daily_summary")
public class ContractDailySummaryDto extends ContractDailySummary {
}
