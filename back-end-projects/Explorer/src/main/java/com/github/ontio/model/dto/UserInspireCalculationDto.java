package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInspireCalculationDto {

    private Long stakeAmount;

    private String publicKey;

}
