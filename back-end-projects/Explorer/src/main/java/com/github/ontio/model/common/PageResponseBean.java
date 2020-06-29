package com.github.ontio.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/4/26
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponseBean {

    private Integer total;

    private List records;

    public PageResponseBean(List records, Integer total) {
        this.total = total == null ? 0 : total;
        this.records = records;
    }

    public PageResponseBean() {
    }
}
