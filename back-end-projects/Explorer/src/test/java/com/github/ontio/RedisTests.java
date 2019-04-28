package com.github.ontio;

import com.alibaba.fastjson.JSON;
import com.github.ontio.dao.BlockMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/7/16
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTests {

    @Autowired
    private BlockMapper blockMapper;


    @Test
    public void selectBlockByPage() {

        List<Map> rsList = blockMapper.selectBlockByPage(0, 10);

        log.debug(JSON.toJSONString(rsList));
    }
}