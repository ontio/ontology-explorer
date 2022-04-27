package com.github.ontio.model.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AbiParameters {

    private String name;

    private String type;

    private List<AbiParameters> subType;
}
