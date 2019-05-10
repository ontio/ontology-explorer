package com.github.ontio.model.dto;

import com.github.ontio.model.dao.DailySummary;
import lombok.Data;

import javax.persistence.Table;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/9
 */
@Data
@Table(name = "tbl_daily_summary")
public class DailySummaryDto extends DailySummary {

    private Integer ontidTotal;

    private Integer addressTotal;

}
