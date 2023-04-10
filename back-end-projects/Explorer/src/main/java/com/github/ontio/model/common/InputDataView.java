package com.github.ontio.model.common;

import lombok.Data;

import java.util.List;

@Data
public class InputDataView {

    private String originalView;

    private String abi;

    private DefaultView defaultView;

    private List<InputDataDecode> decode;
}
