package com.jsonDream.DelayQueue.redis;

import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.jsonDream.DelayQueue.redis.client.RedisClient;
import com.jsonDream.DelayQueue.redis.common.BaseRedisApi;

import redis.clients.jedis.Tuple;

/**
 * <p>
 * redis的authKey接口
 * </p>
 *
 * @author wangguangdong
 * @version 1.0
 * @Date 15/12/24
 */

public class RedisDelayQueueApi extends BaseRedisApi {
	
	private final transient ReentrantLock lock = new ReentrantLock();

	 /**
     * Condition signalled when a newer element becomes available
     * at the head of the queue or a new thread may need to
     * become leader.
     */
    private final Condition available = lock.newCondition();
    
    
	private static final String delayedQueueKeyName = "DelayedQueueSet";

	private static final String executeQueueKeyName = "DelayedQueueExecuteSet";

	/**
	 * 入队
	 *
	 * @param businessObjectString
	 * @param delay
	 */
	public void execute_later(String businessObjectString, final long delay) {
		// 生成延迟队列时间
		long currentTimeMillis = System.currentTimeMillis();
		final long key = currentTimeMillis + delay + 1;
		RedisClient.doWithOut(redis -> {
			if (delay > 0) {
				redis.zadd(delayedQueueKeyName, key, businessObjectString);
			} else {
				redis.rpush(executeQueueKeyName, businessObjectString);
			}
		});

	}

	public void poll_queue() {
		while (true) {
			Set<Tuple> set = RedisClient.domain(redis -> redis.zrangeWithScores(delayedQueueKeyName, 0, 0));
			if (set.isEmpty()) {
				// TODO:阻塞当前线程
			} else {
				Tuple tuple = set.iterator().next();
				// 获取业务字段
				String elementValue = tuple.getElement();
				// 时间
				double sys = tuple.getScore();

			}
		}
	}

	public static void main(String[] a) {
		RedisDelayQueueApi redisDelayQueueApi = new RedisDelayQueueApi();
		redisDelayQueueApi.execute_later("sss", 60l * 1000l * 60l * 24l * 30l * 3l);
		redisDelayQueueApi.poll_queue();
	}

}
