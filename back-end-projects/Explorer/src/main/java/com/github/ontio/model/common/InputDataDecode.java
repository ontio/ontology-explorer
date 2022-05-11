package com.github.ontio.model.common;

import lombok.Data;

import java.util.List;

@Data
public class InputDataDecode {

    private String name;

    private String type;

    private List<Object> data;
}
