package com.github.ontio.model.dto;

import lombok.Data;

import javax.persistence.Table;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/4/26
 */
@Data
@Table(name = "tbl_current")
public class CurrentDto extends com.github.ontio.model.dao.Current {
}
