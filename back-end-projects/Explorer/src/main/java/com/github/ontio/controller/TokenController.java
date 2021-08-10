package com.github.ontio.controller;

import com.github.ontio.aop.RequestLimit;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.service.ITokenService;
import com.github.ontio.util.Helper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/8
 */
@Validated
@RestController
@Slf4j
@RequestMapping(value = "/v2/tokens")
public class TokenController {

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private final ITokenService tokenService;

    @Autowired
    public TokenController(ITokenService tokenService) {
        this.tokenService = tokenService;
    }


    @ApiOperation(value = "Get token list by token type")
    @GetMapping(value = "/{token_type}")
    public ResponseBean queryTokensByPage(
            @PathVariable("token_type") @Pattern(regexp = "oep4|OEP4|oep5|OEP5|oep8|OEP8|orc20|ORC20", message = "Incorrect token type") String tokenType,
            @RequestParam("page_size") @Min(1) @Max(20) Integer pageSize,
            @RequestParam("page_number") @Min(1) Integer pageNumber,
            @RequestParam(name = "sort", required = false) List<String> sorts
    ) {

        log.info("###{}.{} begin...token_type:{}", CLASS_NAME, Helper.currentMethod(), tokenType);

        ResponseBean rs = tokenService.queryTokensByPage(tokenType, pageNumber, pageSize, sorts);
        return rs;
    }


    @ApiOperation(value = "Get token detail by token type and contracthash")
    @GetMapping(value = "/{token_type}/{contract_hash}")
    public ResponseBean queryTokenDetail(@PathVariable("token_type") @Pattern(regexp = "oep4|OEP4|oep5|OEP5|oep8|OEP8|orc20|ORC20", message = "Incorrect token type") String tokenType,
                                         @PathVariable("contract_hash") @Length(min = 40, max = 42, message = "Incorrect contract hash") String contractHash) {

        log.info("###{}.{} begin...token_type:{}，contract_hash:{}", CLASS_NAME, Helper.currentMethod(), tokenType, contractHash);

        ResponseBean rs = tokenService.queryTokenDetail(tokenType, contractHash);
        return rs;
    }

    @RequestLimit(count = 120)
    @ApiOperation(value = "Get oep8 token transaction list by token name")
    @GetMapping(value = "/oep8/{contract_hash}/{token_name}/transactions")
    public ResponseBean queryOep8TxsByPage(@PathVariable("contract_hash") @Length(min = 40, max = 40, message = "Incorrect " +
            "contract hash") String contractHash,
                                           @PathVariable("token_name") String tokenName,
                                           @RequestParam("page_size") @Min(1) @Max(20) Integer pageSize,
                                           @RequestParam("page_number") @Min(1) Integer pageNumber) {

        log.info("###{}.{} begin...contract_hash:{},token_name:{}", CLASS_NAME, Helper.currentMethod(), contractHash, tokenName);

        ResponseBean rs = tokenService.queryOep8TxsByPage(contractHash, tokenName, pageNumber, pageSize);
        return rs;
    }

    @RequestLimit(count = 120)
    @ApiOperation(value = "Get token daily aggregations for specific token type")
    @GetMapping(value = "/{token_type}/{contract_hash}/daily")
    public ResponseBean queryDailyAggregation(
            @PathVariable("token_type") @Pattern(regexp = "oep4|OEP4|orc20|ORC20", message = "Incorrect token type") String tokenType,
            @PathVariable("contract_hash") @Length(min = 40, max = 42, message = "Incorrect contract hash") String contractHash,
            @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date to
    ) {
        log.info("###{}.{} begin...token_type:{}，contract_hash:{}", CLASS_NAME, Helper.currentMethod(), tokenType, contractHash);

        return tokenService.queryDailyAggregations(tokenType, contractHash, from, to);
    }

    @RequestLimit(count = 120)
    @ApiOperation(value = "Get token rankings")
    @GetMapping(value = "/rankings")
    public ResponseBean queryRankings(
            @RequestParam(name = "ranking_id", required = false) List<Short> rankingIds,
            @RequestParam("duration") short duration
    ) {
        log.info("###{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        return tokenService.queryRankings(rankingIds, duration);
    }

    @RequestLimit(count = 120)
    @ApiOperation(value = "Get token price in fiat")
    @GetMapping(value = "/prices")
    public ResponseBean queryPrice(
            @RequestParam("token") @Pattern(regexp = "ont|ONT|ong|ONG", message = "Incorrect token") String token,
            @RequestParam("fiat") @Pattern(regexp = "usd|USD", message = "Incorrect fiat") String fiat
    ) {
        log.info("###{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        return tokenService.queryPrice(token, fiat);
    }


    @ApiOperation(value = "Get oep logos")
    @GetMapping(value = "/logos")
    public ResponseBean queryOepLogos(@RequestParam("contract_hash") @Length(min = 40, max = 42, message = "Incorrect contract hash") String contractHash,
                                      @RequestParam("page_size") @Min(1) @Max(50) int pageSize,
                                      @RequestParam("page_number") @Min(1) int pageNumber) {
        log.info("###{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        return tokenService.queryOepLogos(contractHash, pageSize, pageNumber);
    }

}
