package com.github.ontio.schedule;

import com.alibaba.fastjson.JSONObject;
import com.github.ontio.mapper.Oep4Mapper;
import com.github.ontio.mapper.Oep5Mapper;
import com.github.ontio.mapper.Oep8Mapper;
import com.github.ontio.model.dao.Oep4;
import com.github.ontio.model.dao.Oep5;
import com.github.ontio.model.dao.Oep8;
import com.github.ontio.utils.ConstantParam;
import com.github.ontio.utils.Helper;
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

    @Autowired
    public InitOepInfoSchedule(Oep4Mapper oep4Mapper, Oep5Mapper oep5Mapper, Oep8Mapper oep8Mapper) {
        this.oep4Mapper = oep4Mapper;
        this.oep5Mapper = oep5Mapper;
        this.oep8Mapper = oep8Mapper;
    }


    /**
     * 初始化OEP全局变量
     */
    @Scheduled(initialDelay = 1 * 100, fixedDelay = 5 * 60 * 1000)
    public void initOepInfo() {
        log.info("####{}.{} begin....", CLASS_NAME, Helper.currentMethod());
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
    }


}
