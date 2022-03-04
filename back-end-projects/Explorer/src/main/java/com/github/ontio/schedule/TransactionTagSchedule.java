package com.github.ontio.schedule;

import com.github.ontio.mapper.*;
import com.github.ontio.model.dao.*;
import com.github.ontio.util.ConstantParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EnableScheduling
public class TransactionTagSchedule {

    private String CLASS_NAME = this.getClass().getSimpleName();

    @Autowired
    private ContractTagMapper contractTagMapper;
    @Autowired
    private Oep4Mapper oep4Mapper;
    @Autowired
    private Oep5Mapper oep5Mapper;
    @Autowired
    private Oep8Mapper oep8Mapper;
    @Autowired
    private Orc20Mapper orc20Mapper;
    @Autowired
    private Orc721Mapper orc721Mapper;
    @Autowired
    private Orc1155Mapper orc1155Mapper;

    /**
     * 初始化所有 转入地址的信息   5 * 60 * 1000
     */
    @Scheduled(initialDelay = 3 * 1000, fixedDelay = 5 * 60 * 1000)
    public void initToAddressTag() {
        log.info("####{}.{} begin....", CLASS_NAME, com.github.ontio.util.Helper.currentMethod());

        List<ContractTag> contractTags = contractTagMapper.selectAll();
        contractTags.forEach(item -> {
            ConstantParam.CONTRACT_TAG.put(item.getContractHash().toLowerCase(), item.getName());
        });

        Oep4 oep4 = new Oep4();
        oep4.setAuditFlag(true);
        List<Oep4> oep4s = oep4Mapper.select(oep4);
        oep4s.forEach(item -> {
            ConstantParam.CONTRACT_TAG.put(item.getContractHash().toLowerCase(), item.getName());
        });

        Oep5 oep5 = new Oep5();
        oep5.setAuditFlag(true);
        List<Oep5> oep5s = oep5Mapper.select(oep5);
        oep5s.forEach(item -> {
            ConstantParam.CONTRACT_TAG.put(item.getContractHash().toLowerCase(), item.getName());
        });

        Oep8 oep8 = new Oep8();
        oep8.setAuditFlag(true);
        List<Oep8> oep8s = oep8Mapper.select(oep8);
        oep8s.forEach(item -> {
            ConstantParam.CONTRACT_TAG.put(item.getContractHash().toLowerCase(), item.getCollection());
        });

        Orc20 orc20 = new Orc20();
        orc20.setAuditFlag(true);
        List<Orc20> orc20s = orc20Mapper.select(orc20);
        orc20s.forEach(item -> {
            ConstantParam.CONTRACT_TAG.put(item.getContractHash().toLowerCase(), item.getName());
        });

        Orc721 orc721 = new Orc721();
        orc721.setAuditFlag(true);
        List<Orc721> orc721s = orc721Mapper.select(orc721);
        orc721s.forEach(item -> {
            ConstantParam.CONTRACT_TAG.put(item.getContractHash().toLowerCase(), item.getName());
        });

        Orc1155 orc1155 = new Orc1155();
        orc1155.setAuditFlag(true);
        List<Orc1155> orc1155s = orc1155Mapper.select(orc1155);
        orc1155s.forEach(item -> {
            ConstantParam.CONTRACT_TAG.put(item.getContractHash().toLowerCase(), item.getCollection());
        });
    }

}
