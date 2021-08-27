package com.github.ontio.schedule;

import com.github.ontio.mapper.ContractTagMapper;
import com.github.ontio.model.dao.ContractTag;
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


    /**
     * 初始化所有 转入地址的信息   5 * 60 * 1000
     */
    @Scheduled(initialDelay = 3 * 1000, fixedDelay = 5 * 60 * 1000)
    public void initToAddressTag() {
        log.info("####{}.{} begin....", CLASS_NAME, com.github.ontio.util.Helper.currentMethod());

        List<ContractTag> contractTags = contractTagMapper.selectAll();
        contractTags.forEach(item -> {
            ConstantParam.CONTRACT_TAG.put(item.getContractHash(), item.getName());
        });

    }

}
