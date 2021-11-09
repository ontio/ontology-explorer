package com.github.ontio.controller;

import com.github.ontio.aop.RequestLimit;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.sdk.exception.SDKException;
import com.github.ontio.service.IAddressService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/4/28
 */
@Validated
@Slf4j
@RestController
@RequestMapping(value = "/v2/addresses")
public class AddressController {

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private final IAddressService addressService;

    @Autowired
    public AddressController(IAddressService addressService) {
        this.addressService = addressService;
    }


    @RequestLimit(count = 120)
    @ApiOperation(value = "Get address balance")
    @GetMapping(value = "/{address}/{token_type}/balances")
    public ResponseBean queryAddressBalance(@PathVariable("address") @Length(min = 34, max = 42, message = "Incorrect address " +
            "format") String address,
                                            @PathVariable("token_type") @Pattern(regexp = "oep4|OEP4|oep5|OEP5|oep8|OEP8|native|NATIVE|ALL|all|native-oep4|NATIVE-OEP4|orc20|ORC20|orc721|ORC721", message =
                                                    "Incorrect token type") String tokenType) {

//        log.info("####{}.{} begin...address:{},token_type:{}", CLASS_NAME, Helper.currentMethod(), address, tokenType);
        ResponseBean rs = null;
        if (address.startsWith(ConstantParam.EVM_ADDRESS_PREFIX)) {
            rs = addressService.queryEVMAddressBalance(address, tokenType);
        } else {
            rs = addressService.queryAddressBalance(address, tokenType);
        }
        return rs;
    }


    @RequestLimit(count = 120)
    @ApiOperation(value = "Get address balance by assetName")
    @GetMapping(value = "/{address}/balances")
    public ResponseBean queryAddressBalanceByAssetName(@PathVariable("address") @Length(min = 34, max = 34, message = "Incorrect " +
            "address format") String address,
                                                       @RequestParam(value = "asset_name", required = false) String assetName,
                                                       @RequestParam(value = "contract_hash", required = false) @Length(min = 40, max = 40, message = "Incorrect contract " +
                                                               "hash") String contractHash,
                                                       @RequestParam(value = "channel", required = false) String channel) {

//        log.info("####{}.{} begin...address:{},assetName:{}", CLASS_NAME, Helper.currentMethod(), address, assetName);

        ResponseBean rs = new ResponseBean();
        if (Helper.isNotEmptyAndNull(channel) && ConstantParam.CHANNEL_ONTO.equals(channel)) {
            //TODO abandon....
            rs = addressService.queryAddressBalanceByAssetName4Onto(address, assetName);
        } else if (Helper.isNotEmptyAndNull(assetName)) {
            rs = addressService.queryAddressBalanceByAssetName(address, assetName);
        } else if (Helper.isNotEmptyAndNull(contractHash)) {
            rs = addressService.queryAddressBalanceByContractHash(address, contractHash);
        }
        return rs;
    }


    @RequestLimit(count = 120)
    @ApiOperation(value = "Get address transfer transaction list by params", notes = "(begin_time+end_time) or " +
            "(page_number+page_size)")
    @GetMapping(value = "/{address}/transactions")
    public ResponseBean queryAddressTransferTxsByPage(@PathVariable("address") @Length(min = 34, max = 42, message = "Incorrect " +
            "address format") String address,
                                                      @RequestParam(name = "page_size", required = false) @Min(1) @Max(20) Integer pageSize,
                                                      @RequestParam(name = "page_number", required = false) @Min(1) Integer pageNumber,
                                                      @RequestParam(name = "begin_time", required = false) Long beginTime,
                                                      @RequestParam(name = "end_time", required = false) Long endTime) {

//        log.info("####{}.{} begin...address:{}", CLASS_NAME, Helper.currentMethod(), address);

        ResponseBean rs = new ResponseBean();
        if (Helper.isNotEmptyAndNull(pageNumber, pageSize)) {

            rs = addressService.queryTransferTxsByPage(address, "", pageNumber, pageSize);
        } else if (Helper.isNotEmptyAndNull(beginTime, endTime)) {
            //request time max range is one week
            if (Helper.isTimeRangeExceedWeek(beginTime, endTime)) {
                return new ResponseBean(ErrorInfo.TIME_RANGE_EXCEED.code(), ErrorInfo.TIME_RANGE_EXCEED.desc(), false);
            }
            rs = addressService.queryTransferTxsByTime(address, "", beginTime, endTime);
        }
        return rs;
    }

    @RequestLimit(count = 120)
    @ApiOperation(value = "Get address transfer transaction list by params+assetName", notes = "(begin_time+end_time) or " +
            "(page_number+page_size) or (end_time+page_size)")
    @GetMapping(value = "/{address}/{asset_name}/transactions")
    public ResponseBean queryAddressTransferTxsByPageAndAssetName(@PathVariable("address") @Length(min = 34, max = 42, message =
            "error address format") String address,
                                                                  @PathVariable("asset_name") String assetName,
                                                                  @RequestParam(name = "page_size", required = false) @Min(1) @Max(20) Integer pageSize,
                                                                  @RequestParam(name = "page_number", required = false) @Min(1) Integer pageNumber,
                                                                  @RequestParam(name = "begin_time", required = false) Long beginTime,
                                                                  @RequestParam(name = "end_time", required = false) Long endTime,
                                                                  @RequestParam(name = "channel", required = false) String channel,
                                                                  @RequestParam(name = "address_type", required = false) @Pattern(regexp = "fromAddress|toAddress") String addressType) {

//        log.info("###{}.{} begin...address:{}", CLASS_NAME, Helper.currentMethod(), address);

        ResponseBean rs = new ResponseBean();
        //ONTO request
        if (Helper.isNotEmptyAndNull(channel) && ConstantParam.CHANNEL_ONTO.equals(channel)) {

            if (Helper.isNotEmptyAndNull(beginTime, endTime)) {

                rs = addressService.queryTransferTxsByTime4Onto(address, assetName, beginTime, endTime, addressType);
            } else if (Helper.isNotEmptyAndNull(endTime, pageSize)) {

                rs = addressService.queryTransferTxsByTimeAndPage4Onto(address, assetName, endTime, pageSize, addressType);
            }
        } else {
            if (Helper.isNotEmptyAndNull(pageNumber, pageSize)) {

                rs = addressService.queryTransferTxsByPage(address, assetName, pageNumber, pageSize);
            } else if (Helper.isNotEmptyAndNull(beginTime, endTime)) {
                //request time max range is one week
                if (Helper.isTimeRangeExceedWeek(beginTime, endTime)) {
                    return new ResponseBean(ErrorInfo.TIME_RANGE_EXCEED.code(), ErrorInfo.TIME_RANGE_EXCEED.desc(), false);
                }
                rs = addressService.queryTransferTxsByTime(address, assetName, beginTime, endTime);
            }
        }
        return rs;
    }

    @RequestLimit(count = 120)
    @ApiOperation(value = "Get address daily aggregations by ont/ong token")
    @GetMapping(value = "/{address}/daily")
    public ResponseBean queryDailyAggregation(
            @PathVariable("address") @Length(min = 34, max = 42, message = "Incorrect address format") String address,
            @RequestParam(value = "token") @Pattern(regexp = "ont|ONT|ong|ONG", message = "Incorrect token") String token,
            @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date to
    ) throws SDKException {
//        log.info("###{}.{} begin...address:{}", CLASS_NAME, Helper.currentMethod(), address);

        return addressService.queryDailyAggregation(address, token, from, to);
    }

    @RequestLimit(count = 120)
    @ApiOperation(value = "Get address daily aggregations by specific token type")
    @GetMapping(value = "/{address}/{token_type}/daily")
    public ResponseBean queryDailyAggregationOfTokenType(
            @PathVariable("address") @Length(min = 34, max = 42, message = "Incorrect address format") String address,
            @PathVariable(value = "token_type") @Pattern(regexp = "oep4|OEP4|native|NATIVE|orc20|ORC20", message = "Incorrect token type") String tokenType,
            @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date to
    ) {
//        log.info("###{}.{} begin...address:{}", CLASS_NAME, Helper.currentMethod(), address);

        return addressService.queryDailyAggregationOfTokenType(address, tokenType, from, to);
    }

    @RequestLimit(count = 120)
    @ApiOperation(value = "Get address daily rankings by ranking ids and ranking duration")
    @GetMapping(value = "/rankings")
    public ResponseBean queryRankings(
            @RequestParam(name = "ranking_id", required = false) List<Short> rankingIds,
            @RequestParam("duration") short duration
    ) {
//        log.info("###{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        return addressService.queryRankings(rankingIds, duration);
    }

    @RequestLimit(count = 120)
    @ApiOperation(value = "Get address transfer transaction list by params", notes = "(page_number+page_size)")
    @GetMapping(value = "/{address}/txs")
    public ResponseBean queryAddressTransferTxsWithTotal(
            @PathVariable("address") @Length(min = 34, max = 42, message = "Incorrect address format") String address,
            @RequestParam(name = "asset_name", required = false) String assetName,
            @RequestParam(name = "page_size") @Min(1) @Max(20) Integer pageSize,
            @RequestParam(name = "page_number") @Min(1) Integer pageNumber
    ) {
//        log.info("####{}.{} begin...address:{}", CLASS_NAME, Helper.currentMethod(), address);

        return addressService.queryTransferTxsWithTotalByPage(address, assetName, pageNumber, pageSize);
    }

    @RequestLimit(count = 120)
    @ApiOperation(value = "Get address transfer transaction list of token type by params", notes = "(page_number+page_size)")
    @GetMapping(value = "/{address}/{token_type}/txs")
    public ResponseBean queryAddressTransferTxsOfTokenType(
            @PathVariable("address") @Length(min = 34, max = 42, message = "Incorrect address format") String address,
            @PathVariable("token_type") @Pattern(regexp = "oep4|OEP4|oep5|OEP5|oep8|OEP8|orc20|ORC20|orc721|ORC721", message = "Incorrect token type") String tokenType,
            @RequestParam(name = "page_size") @Min(1) @Max(20) Integer pageSize,
            @RequestParam(name = "page_number") @Min(1) Integer pageNumber
    ) {
//        log.info("####{}.{} begin...address:{};token_type:{}", CLASS_NAME, Helper.currentMethod(), address, tokenType);

        return addressService.queryTransferTxsOfTokenTypeByPage(address, tokenType, pageNumber, pageSize);
    }

}
