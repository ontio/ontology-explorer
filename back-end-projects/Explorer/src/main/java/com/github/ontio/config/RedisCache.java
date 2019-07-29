package com.github.ontio.config;

import com.github.ontio.ApplicationContextProvider;
import com.github.ontio.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.beans.BeansException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.github.ontio.ApplicationContextProvider.getProperty;
import static com.github.ontio.config.RedisCache.SpringAccessor.getRedisTemplate;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/7/16
 */
@Slf4j
public class RedisCache implements Cache {

    private static final String REDIS_LONG_EXPIRE_MINUTE = "redis.expire.long.minute";
    private static final String REDIS_MEDIUM_EXPIRE_SECOND = "redis.expire.medium.second";
    private static final String REDIS_SHORT_EXPIRE_SECOND= "redis.expire.short.second";

    private String CLASS_NAME = this.getClass().getSimpleName();

    // 读写锁
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    private String id;

    public RedisCache(final String id) {

        log.info("##init ExplorerRedisCache, Cache id:{}##", id);
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
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
            getRedisTemplate().opsForValue().set(key.toString(), value, getProperty(REDIS_LONG_EXPIRE_MINUTE, Integer.class), TimeUnit.MINUTES);
        } else if (Helper.isBelongRedisMediumExpireMapper(key.toString())) {
            getRedisTemplate().opsForValue().set(key.toString(), value, getProperty(REDIS_MEDIUM_EXPIRE_SECOND, Integer.class), TimeUnit.SECONDS);
        } else {
            getRedisTemplate().opsForValue().set(key.toString(), value, getProperty(REDIS_SHORT_EXPIRE_SECOND, Integer.class), TimeUnit.SECONDS);
        }
    }

    @Override
    public Object getObject(Object key) {
        log.info("##{}.{} key:{}##", CLASS_NAME, Helper.currentMethod(), key.toString());
        try {
            if (key != null) {
                return getRedisTemplate().opsForValue().get(key.toString());
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
                getRedisTemplate().delete(key.toString());
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void clear() {
        log.info("##{}.{} this.id:{}", CLASS_NAME, Helper.currentMethod(), this.id);
        try {
            Set<String> keys = getRedisTemplate().keys("*:" + this.id + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                log.info("keys:{}", keys);
                getRedisTemplate().delete(keys);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public int getSize() {
        log.info("##{}.{}", CLASS_NAME, Helper.currentMethod());
        Long size = (Long) getRedisTemplate().execute(new RedisCallback<Long>() {
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

    // RedisCache is instantiated by MyBatis, however we wish to use a Spring managed RedisTemplate.  To avoid race
    // conditions between Spring context initialization, and MyBatis, use getRedisTemplate() to access this.
    static final class SpringAccessor {
        private static RedisTemplate<String, Object> redisTemplate;

        static RedisTemplate<String, Object> getRedisTemplate() {
            // locally cache the RedisTemplate as that is not expected to change
            if (redisTemplate == null) {
                try {
                    redisTemplate = ApplicationContextProvider.getBean("redisTemplate");
                } catch (BeansException ex) {
                    log.warn("##Spring Redis template is currently not available.");
                }
            }
            return redisTemplate;
        }
    }
}