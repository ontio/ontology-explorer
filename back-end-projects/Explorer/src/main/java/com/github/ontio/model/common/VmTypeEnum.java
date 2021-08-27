package com.github.ontio.model.common;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum VmTypeEnum {

    DEPLOYMENT_CODE("208"),
    INVOKE_NEOVM("209"),
    INVOKE_WASMVM("210"),
    INVOKE_EVM("211");

    @Getter
    @JsonValue
    private final String code;

}
