package com.github.ontio.schedule;

import com.alibaba.fastjson.JSONObject;
import com.github.ontio.common.Helper;
import com.github.ontio.mapper.*;
import com.github.ontio.model.dao.*;
import com.github.ontio.utils.ConstantParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/6
 */
@Slf4j
@Component
@EnableScheduling
public class InitOepInfoSchedule {

    private String CLASS_NAME = this.getClass().getSimpleName();

    private final Oep4Mapper oep4Mapper;
    private final Oep5Mapper oep5Mapper;
    private final Oep8Mapper oep8Mapper;

    private final Orc20Mapper orc20Mapper;

    private final Orc721Mapper orc721Mapper;


    @Autowired
    public InitOepInfoSchedule(Oep4Mapper oep4Mapper, Oep5Mapper oep5Mapper, Oep8Mapper oep8Mapper, Orc20Mapper orc20Mapper, Orc721Mapper orc721Mapper) {
        this.oep4Mapper = oep4Mapper;
        this.oep5Mapper = oep5Mapper;
        this.oep8Mapper = oep8Mapper;
        this.orc20Mapper = orc20Mapper;
        this.orc721Mapper = orc721Mapper;
    }


    /**
     * 初始化OEP全局变量
     */
    @Scheduled(initialDelay = 1 * 10, fixedDelay = 5 * 60 * 1000)
    public void initOepInfo() {
        log.info("####{}.{} begin....", CLASS_NAME, com.github.ontio.utils.Helper.currentMethod());
        List<Oep4> oep4s = oep4Mapper.selectApprovedRecords();
        oep4s.forEach(item -> {
            JSONObject obj = new JSONObject();
            obj.put("symbol", item.getSymbol());
            obj.put("decimals", item.getDecimals());
            ConstantParam.OEP4MAP.put(item.getContractHash(), obj);
            ConstantParam.OEP4CONTRACTS.add(item.getContractHash());
        });


        List<Oep5> oep5s = oep5Mapper.selectApprovedRecords();
        oep5s.forEach(item -> {
            JSONObject obj = new JSONObject();
            obj.put("symbol", item.getSymbol());
            obj.put("name", item.getName());
            ConstantParam.OEP5MAP.put(item.getContractHash(), obj);
            ConstantParam.OEP5CONTRACTS.add(item.getContractHash());
        });

        List<Oep8> oep8s = oep8Mapper.selectApprovedRecords();
        oep8s.forEach(item -> {
            JSONObject obj = new JSONObject();
            obj.put("symbol", item.getSymbol());
            obj.put("name", item.getName());
            ConstantParam.OEP8MAP.put((String) item.getContractHash() + "-" + (String) item.getTokenId(), obj);
            ConstantParam.OEP8CONTRACTS.add((String) item.getContractHash());
        });

        List<Orc20> orc20s = orc20Mapper.selectApprovedRecords();
        orc20s.forEach(item -> {
            JSONObject obj = new JSONObject();
            obj.put("name", item.getName());
            obj.put("decimals", item.getDecimals());

            // 拿到数据库中的 "正的值" 现在使用反序的方式来进行判断
            String contractAddress = Helper.reverse(item.getContractHash().substring(2)).toLowerCase();
            ConstantParam.ORC20MAP.put(contractAddress, obj);
            ConstantParam.ORC20CONTRACTS.add(contractAddress);
        });


        List<Orc721> orc721s = orc721Mapper.selectApprovedRecords();
        orc721s.forEach(item -> {
            JSONObject obj = new JSONObject();
            obj.put("name", item.getName());

            //拿到数据库中的"正的值",现在使用反序的方式来进行判断
            String contractAddress = Helper.reverse(item.getContractHash().substring(2)).toLowerCase();
            ConstantParam.ORC721MAP.put(contractAddress, obj);
            ConstantParam.ORC721CONTRACTS.add(contractAddress);
        });


    }


}
