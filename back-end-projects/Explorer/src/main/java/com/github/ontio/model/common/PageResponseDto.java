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
public class PageResponseDto {

    private Integer total;

    private List records;

    public PageResponseDto(List records, Integer total) {
        this.total = total;
        this.records = records;
    }
    public PageResponseDto(){}
}
