package com.github.ontio.model.dto;

import com.github.ontio.model.dao.DailySummary;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "tbl_daily_summary")
public class DailySummaryDto extends DailySummary {

    private Integer ontidTotal;

    private Integer addressTotal;

}
