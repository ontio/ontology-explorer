package com.github.ontio.controller;

import com.github.ontio.paramBean.Result;
import com.github.ontio.service.impl.ContractServiceImpl;
import com.github.ontio.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.*;

/**
 * @author king
 * @version 1.0
 * @date 12.6
 */
@RestController
@EnableAspectJAutoProxy
@RequestMapping(value = "/api/v1/explorer")
public class ContractController {
    private static final Logger logger = LoggerFactory.getLogger(ContractController.class);

    private final String CLASS_NAME = this.getClass().getSimpleName();

    @Autowired
    private ContractServiceImpl contractService;

    /**
     * contract list
     * @param pagenumber the start page
     * @param pagesize   the amount of each page
     */
    @RequestMapping(value = "contract/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    public Result contracts(@PathVariable("pagesize") int pagesize,
                            @PathVariable("pagenumber") int pagenumber){
        Result rs = contractService.queryContract(pagesize, pagenumber);

        return rs;
    }

    /**
     * query contractTxs by page
     *
     * @param contractHash   contractHash
     * @param pageNumber the start page
     * @param pageSize   the amount of each page
     * @return
     */
    @RequestMapping(value = "/contract/{contracthash}/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryContractTxsByPage(@PathVariable("contracthash") String contractHash,
                                         @PathVariable("pagenumber") Integer pageNumber,
                                         @PathVariable("pagesize") Integer pageSize) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("pageSize:{}, pageNumber；{}", pageSize, pageNumber);
        if (contractHash.isEmpty()){
            return null;
        }

        Result rs = contractService.queryContractByHash(contractHash, pageSize, pageNumber);
        return rs;
    }

    /**
     * query OEP by type(oep4\oep8)
     * @param type type
     * @param pageNumber the start page
     * @param pageSize   the amount of each page
     * @return
     */
    @RequestMapping(value = "/oepcontract/{type}/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    public Result queryOEPContract(@PathVariable("type") String type,
                                    @PathVariable("pagenumber") Integer pageNumber,
                                    @PathVariable("pagesize") Integer pageSize){

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("pageSize:{}, pageNumber；{}", pageSize, pageNumber);
        if (type.isEmpty()){
            return null;
        }

        Result rs = contractService.queryOEPContract(type, pageSize, pageNumber);
        return rs;
    }

    /**
     * query OEP by type(oep4\oep8)
     * @param contracthash contracthash
     * @param type type
     * @param pageNumber the start page
     * @param pageSize   the amount of each page
     * @return
     */
    @RequestMapping(value = "/oepcontract/{type}/{contracthash}/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    public Result queryOEPContractByHash(@PathVariable("contracthash") String contracthash,
                                         @PathVariable("type") String type,
                                         @PathVariable("pagenumber") Integer pageNumber,
                                         @PathVariable("pagesize") Integer pageSize){

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("pageSize:{}, pageNumber；{}", pageSize, pageNumber);
        if (type.isEmpty() || contracthash.isEmpty()){
            return null;
        }

        Result rs = contractService.queryOEPContractByHashAndTokenName(contracthash, type, "", pageSize, pageNumber);
        return rs;
    }

    /**
     * query OEP by type(oep4\oep8)
     * @param contracthash contracthash
     * @param type type
     * @param tokenname tokenname
     * @param pageNumber the start page
     * @param pageSize   the amount of each page
     * @return
     */
    @RequestMapping(value = "/oepcontract/{type}/{contracthash}/{tokenname}/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    public Result queryOEPContractByHashAndSymbol(@PathVariable("contracthash") String contracthash,
                                                   @PathVariable("type") String type,
                                                   @PathVariable("tokenname") String tokenname,
                                                   @PathVariable("pagenumber") Integer pageNumber,
                                                   @PathVariable("pagesize") Integer pageSize){

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("pageSize:{}, pageNumber；{}", pageSize, pageNumber);
        if (type.isEmpty() || contracthash.isEmpty()){
            return null;
        }

        Result rs = contractService.queryOEPContractByHashAndTokenName(contracthash, type, tokenname, pageSize, pageNumber);
        return rs;
    }


    /**
     * 查询dappstore里的合约列表信息
     * @param pageSize
     * @param pageNumber
     * @return
     */
    @GetMapping(value = "/contract/dappstore/{pagesize}/{pagenumber}")
    public Result queryDappstoreContractInfo(@PathVariable("pagesize") Integer pageSize, @PathVariable("pagenumber") Integer pageNumber){

        logger.info("####{}.{} begin...pagesize:{},pagenumber:{}", CLASS_NAME, Helper.currentMethod(),pageSize, pageNumber);

        Result rs = contractService.queryDappstoreContractInfo(pageSize, pageNumber);
        return rs;
    }


    /**
     * 查询dappstore里合约汇总信息
     * @return
     */
    @GetMapping(value = "/contract/dappstore/24h/summary")
    public Result queryDappstoreContract24hSummary(){

        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        Result rs = contractService.queryDappstore24hSummary();
        return rs;
    }


}
