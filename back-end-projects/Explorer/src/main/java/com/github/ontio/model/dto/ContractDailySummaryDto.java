package com.github.ontio.model.dto;

import com.github.ontio.model.dao.ContractDailySummary;

import javax.persistence.Table;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/9
 */
@Table(name = "tbl_contract_daily_summary")
public class ContractDailySummaryDto extends ContractDailySummary {
}
