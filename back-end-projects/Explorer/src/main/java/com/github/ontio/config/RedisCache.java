package com.github.ontio.config;

import com.github.ontio.ApplicationContextProvider;
import com.github.ontio.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/7/16
 */
@Component
@Slf4j
@DependsOn("applicationContextProviderExplorer")
public class RedisCache implements Cache {

    private String CLASS_NAME = this.getClass().getSimpleName();

    // 读写锁
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    private RedisTemplate<String, Object> redisTemplate = ApplicationContextProvider.getBean("redisTemplate");

    private String id = "defaultrediscacheid001";

    public RedisCache(final String id) {

        log.info("##init ExplorerRedisCache, Cache id:{}##", id);
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    public RedisCache() {
        log.info("##init ExplorerRedisCache with default Cache id:{}##", this.id);
    }

    @Override
    public String getId() {
        log.info("##{}.{} Id:{}", CLASS_NAME, Helper.currentMethod(), this.id);
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        log.info("##{}.{} key:{}, value:{}##", CLASS_NAME, Helper.currentMethod(), key, value);
        if (Helper.isBelongRedisLongExpireMapper(key.toString())) {
            redisTemplate.opsForValue().set(key.toString(), value, 2 * 60, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key.toString(), value, 6, TimeUnit.SECONDS);
        }
    }

    @Override
    public Object getObject(Object key) {
        log.info("##{}.{} key:{}##", CLASS_NAME, Helper.currentMethod(), key.toString());
        try {
            if (key != null) {
                Object obj = redisTemplate.opsForValue().get(key.toString());
                return obj;
            }
        } catch (Exception e) {
            log.error("redis error... ", e);
        }
        return null;
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            removeObject(key);
        }
    }


    @Override
    public Object removeObject(Object key) {
        log.info("##{}.{} key:{}##", CLASS_NAME, Helper.currentMethod(), key.toString());
        try {
            if (key != null) {
                redisTemplate.delete(key.toString());
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void clear() {
        log.info("##{}.{} this.id:{}", CLASS_NAME, Helper.currentMethod(), this.id);
        try {
            Set<String> keys = redisTemplate.keys("*:" + this.id + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                log.info("keys:{}", keys);
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public int getSize() {
        log.info("##{}.{}", CLASS_NAME, Helper.currentMethod());
        Long size = (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
        return size.intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        log.info("##get Redis Cache ReadWriteLock##");
        return this.readWriteLock;
    }


}