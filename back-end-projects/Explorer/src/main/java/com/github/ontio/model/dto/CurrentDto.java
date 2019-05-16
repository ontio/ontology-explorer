package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.dao.Current;
import lombok.Data;

import javax.persistence.Table;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/4/26
 */
@Data
@Table(name = "tbl_current")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrentDto extends Current {

    private Integer nodeCount;

    private Integer addressCount;
}
