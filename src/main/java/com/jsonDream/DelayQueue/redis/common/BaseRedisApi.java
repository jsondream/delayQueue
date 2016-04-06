package com.jsonDream.DelayQueue.redis.common;


import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.jsonDream.DelayQueue.common.SerializeUtil;
import com.jsonDream.DelayQueue.redis.client.RedisClient;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author wangguangdong
 * @version 1.0
 * @Date 16/1/18
 */
public class BaseRedisApi {
    /**
     * 一天的时间
     */
    protected static final int DAY_SECONDS = 24 * 60 * 60;

    /**
     * <p>
     * 基础设置值
     * </p>
     *
     * @param key
     * @param value
     * @return
     */
    public static void setBaseObj(String key, Serializable value) {
        RedisClient.domain(redis -> redis.set(key.getBytes(), SerializeUtil.serialize(value)));
    }

    /**
     * <p>
     * 基础设置值
     * <br>
     * 默认的过期值为一天
     * </br>
     * </p>
     *
     * @param key
     * @param value
     * @return
     */
    public static void setBaseString(String key, String value) {
        RedisClient.domain(redis -> redis.set(key, value));
    }

    /**
     * <p>
     * 基础设置值
     * <br>
     * 默认的过期值为一天
     * </br>
     * </p>
     *
     * @param key
     * @param value
     * @return
     */
    public static void setBaseStringExp(String key, String value, int... expireTime) {
        // 默认过期时间为一天
        int expire = DAY_SECONDS;
        // 查看是否过期时间
        if (expireTime != null && expireTime.length > 0) {
            expire = expireTime[0];
        }
        // lambda需要一个final值
        final int expireSeconds = expire;
        RedisClient.domain(redis -> redis.setex(key, expireSeconds, value));
    }

    /**
     * <p>
     * 基础设置值
     * <br>
     * 默认的过期值为一天
     * </br>
     * </p>
     *
     * @param key
     * @param value
     * @return
     */
    public static void setBaseObjExp(String key, Serializable value, int... expireTime) {
        // 默认过期时间为一天
        int expire = DAY_SECONDS;
        // 查看是否过期时间
        if (expireTime != null && expireTime.length > 0) {
            expire = expireTime[0];
        }
        // lambda需要一个final值
        final int expireSeconds = expire;
        RedisClient.domain(
            redis -> redis.setex(key.getBytes(), expireSeconds, SerializeUtil.serialize(value)));
    }

    /**
     * <p>
     * 基础设置值
     * <br>
     * 默认的过期值为一天
     * </br>
     * </p>
     *
     * @param key
     * @param value
     * @return
     */
    public static void setBaseObjExp(byte[] key, byte[] value, int... expireTime) {
        // 默认过期时间为一天
        int expire = DAY_SECONDS;
        // 查看是否过期时间
        if (expireTime != null && expireTime.length > 0) {
            expire = expireTime[0];
        }
        // lambda需要一个final值
        final int expireSeconds = expire;
        RedisClient.domain(redis -> redis.setex(key, expireSeconds, value));
    }

    /**
     * 获取的对应的实体的二进制
     * <br>
     * 如果传递byteKey则代表byteKey为key
     * </br>
     *
     * @param key
     * @return
     */
    public static byte[] getBaseObj(String key, byte... byteKey) {
        if (byteKey != null && byteKey.length > 0) {
            return RedisClient.domain(redis -> redis.get(byteKey));
        }
        // 取值
        return RedisClient.domain(redis -> redis.get(key.getBytes()));
    }

    /**
     * 获取的对应的实体的二进制
     * <br>
     * 如果传递byteKey则代表byteKey为key
     * </br>
     *
     * @param key
     * @return
     */
    public static String getBaseString(String key) {
        // 取值
        return RedisClient.domain(redis -> redis.get(key));
    }

    /**
     * 删除对应的phoneCode
     * <br>
     * 如果传递byteKey则代表byteKey为key
     * </br>
     *
     * @param key
     * @return
     */
    public static void delBaseObj(String key, byte... byteKey) {
        if (byteKey != null && byteKey.length > 0) {
            RedisClient.domain(redis -> redis.del(byteKey));
            return;
        }
        RedisClient.domain(redis -> redis.del(key));
    }

    /**
     * 删除对应的phoneCode
     * <br>
     * 如果传递byteKey则代表byteKey为key
     * </br>
     *
     * @param key
     * @return
     */
    public static void expireTime(final String key, final int seconds) {
        RedisClient.domain(redis -> redis.expire(key, seconds));
    }

    /**
     * <p>
     * 用json的方式存储对象
     * </p>
     *
     * @param key
     * @param value
     */
    public static void setBaseJsonObject(String key, Object value) {
        RedisClient.domain(redis -> redis.set(key, JSON.toJSONString(value)));
    }

    /**
     * <p>
     * 带过期时间的设置值,默认过期时间为一天
     * </p>
     *
     * @param key
     * @param value
     */
    public static void setBaseJsonObjectExp(final String key, final Object value) {
        RedisClient.domain(redis -> {
                redis.set(key, JSON.toJSONString(value));
                redis.expire(key, DAY_SECONDS);
                return null;
            });
    }

    /**
     * <p>
     * 带过期时间的设置值,默认过期时间为一天
     * </p>
     *
     * @param key
     * @param value
     * @param second 过期时间,单位为秒
     */
    public static void setBaseJsonObjectExp(final String key, final Object value, int second) {
        RedisClient.domain(redis -> {
                redis.set(key, JSON.toJSONString(value));
                redis.expire(key, second);
                return null;
            });
    }
}

