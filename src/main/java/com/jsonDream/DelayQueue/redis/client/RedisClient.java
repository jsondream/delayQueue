package com.jsonDream.DelayQueue.redis.client;


import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * redisAPi接口
 * </p>
 *
 * @author wangguangdong
 * @version 1.0
 * @Date 15/12/24
 */
public class RedisClient {

    public static <T extends Object> T domain(RedisDomainInterface<T> interfaces) {
        T Object;
        Jedis jedis = RedisPoolClient.getInstance().getJedis();
        try {
            Object = interfaces.domain(jedis);
        } finally {
            RedisPoolClient.getInstance().returnResource(jedis);
        }
        return Object;
    }

    public static void doWithOut(RedisDoWithOutInterface interfaces) {
        Jedis jedis = RedisPoolClient.getInstance().getJedis();
        try {
            interfaces.domain(jedis);
        } finally {
            RedisPoolClient.getInstance().returnResource(jedis);
        }
    }
}