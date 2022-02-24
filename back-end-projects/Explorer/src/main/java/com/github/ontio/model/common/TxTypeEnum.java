package com.github.ontio.model.common;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author LiuQi
 */
@RequiredArgsConstructor
public enum TxTypeEnum {
    CONTRACT_DEPLOYMENT("01"),
    OEP4("02"),
    OEP5("03"),
    OEP8("04"),
    CONTRACT_CALL("05"),
    ONT_ID("06"),
    ONT_TRANSFER("07"),
    ONG_TRANSFER("08"),
    ORC20("09"),
    ORC721("10"),
    ORC1155("11")
    ;

    @Getter
    @JsonValue
    private final String code;

}
