package com.github.ontio.controller;

import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.service.IAddressService;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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


    @ApiOperation(value = "Get address balance")
    @GetMapping(value = "/{address}/balances")
    public ResponseBean queryAddressBalance(@PathVariable("address") @Length(min = 34, max = 34, message = "Incorrect address format") String address) {

        log.info("####{}.{} begin...address:{}", CLASS_NAME, Helper.currentMethod(), address);

        ResponseBean rs = addressService.queryAddressBalance(address);
        return rs;
    }

    @ApiOperation(value = "Get address transfer transaction list by params", notes = "(begin_time+end_time) or (page_number+page_size)")
    @GetMapping(value = "/{address}/transactions")
    public ResponseBean queryAddressTransferTxsByPage(@PathVariable("address") @Length(min = 34, max = 34, message = "Incorrect address format") String address,
                                                      @RequestParam(name = "page_size",required = false) @Min(1) @Max(20) Integer pageSize,
                                                      @RequestParam(name = "page_number",required = false) @Min(1) Integer pageNumber,
                                                      @RequestParam(name = "begin_time",required = false) Long beginTime,
                                                      @RequestParam(name = "end_time",required = false) Long endTime) {

        log.info("####{}.{} begin...address:{}", CLASS_NAME, Helper.currentMethod(), address);

        ResponseBean rs = new ResponseBean();
        if (Helper.isNotEmptyOrNull(pageNumber, pageSize)) {

            rs = addressService.queryTransferTxsByPage(address, "", pageNumber, pageSize);
        } else if (Helper.isNotEmptyOrNull(beginTime, endTime)) {

            rs = addressService.queryTransferTxsByTime(address, "", beginTime, endTime);
        }
        return rs;
    }

    @ApiOperation(value = "Get address transfer transaction list by params+assetName", notes = "(begin_time+end_time) or (page_number+page_size)")
    @GetMapping(value = "/{address}/{asset_name}/transactions")
    public ResponseBean queryAddressTransferTxsByPageAndAssetName(@PathVariable("address") @Length(min = 34, max = 34, message = "error address format") String address,
                                                                  @PathVariable("asset_name") String assetName,
                                                                  @RequestParam(name = "page_size",required = false) @Min(1) @Max(20) Integer pageSize,
                                                                  @RequestParam(name = "page_number",required = false) @Min(1) Integer pageNumber,
                                                                  @RequestParam(name = "begin_time",required = false) Long beginTime,
                                                                  @RequestParam(name = "end_time",required = false) Long endTime) {

        log.info("###{}.{} begin...address:{}", CLASS_NAME, Helper.currentMethod(), address);

        ResponseBean rs = new ResponseBean();
        if (Helper.isNotEmptyOrNull(pageNumber, pageSize)) {

            rs = addressService.queryTransferTxsByPage(address, assetName, pageNumber, pageSize);
        } else if (Helper.isNotEmptyOrNull(beginTime, endTime)) {

            if(Helper.isTimeRangeExceedLimit(beginTime, endTime)){
                return  new ResponseBean(ErrorInfo.PARAM_ERROR.code(), ErrorInfo.PARAM_ERROR.desc(), false);
            }
            rs = addressService.queryTransferTxsByTime(address, assetName, beginTime, endTime);
        }
        return rs;
    }


}
