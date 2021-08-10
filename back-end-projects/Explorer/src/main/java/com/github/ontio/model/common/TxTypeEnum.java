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
    // todo 加上枚举的方式 ERC20 和ERC721
    ERC20("09"),
    ERC721("10")

    ;

    @Getter
    @JsonValue
    private final String code;

}
