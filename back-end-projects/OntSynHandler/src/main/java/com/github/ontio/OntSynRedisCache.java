package com.github.ontio;

import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class OntSynRedisCache implements Cache {

    private static final Logger logger = LoggerFactory.getLogger(OntSynRedisCache.class);

    // 读写锁
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    private RedisTemplate<String, Object> redisTemplate = ApplicationContextProvider.getBean("redisTemplate");

    private String id = "defaultrediscacheid002";

    public OntSynRedisCache(final String id) {

        logger.info("##init ExplorerRedisCache, Cache id:{}##", id);
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    public OntSynRedisCache() {
        logger.info("##init ExplorerRedisCache with default Cache id:{}##", this.id);
    }

    @Override
    public String getId() {
        logger.info("##get Redis Cache Id:{}##", this.id);
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
       // logger.info("##putObject. key:{}, value:{}##", key, value);
        if (value != null) {
            // 向Redis中添加数据，有效时间是2天
            redisTemplate.opsForValue().set(key.toString(), value, 2, TimeUnit.DAYS);
        }
    }

    @Override
    public Object getObject(Object key) {
        logger.info("##getObject. key:{}##", key);
        try {
            if (key != null) {
                Object obj = redisTemplate.opsForValue().get(key.toString());
              //  logger.info("##getObject. value:{}", obj);
                return obj;
            }
        } catch (Exception e) {
            logger.error("redis error... ", e);
        }
        return null;
    }

    /**
     * 批量删除对应的value
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            removeObject(key);
        }
    }

    @Override
    public Object removeObject(Object key) {
        logger.info("##removeObject. key:{}##", key);
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
        logger.info("clear Redis Cache,this.id:{}",this.id);
        try {
            Set<String> keys = redisTemplate.keys("*:" + this.id + "*");
            if (!CollectionUtils.isEmpty(keys)) {
             //   logger.info("keys:{}",keys);
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public int getSize() {
        logger.info("##get Redis Cache Size##");
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
        logger.info("##get Redis Cache ReadWriteLock##");
        return this.readWriteLock;
    }


}