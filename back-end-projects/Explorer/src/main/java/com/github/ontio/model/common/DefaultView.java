package com.github.ontio.model.common;

import lombok.Data;

import java.util.List;

@Data
public class DefaultView {

    private String function;

    private String methodId;

    private List<String> params;
}
