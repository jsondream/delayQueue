package com.jsonDream.DelayQueue.redis;

import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.jsonDream.DelayQueue.redis.client.RedisClient;
import com.jsonDream.DelayQueue.redis.common.BaseRedisApi;

import redis.clients.jedis.Tuple;

/**
 * <p>
 * redis的延迟队列
 * </p>
 *
 * @author wangguangdong
 * @version 1.0
 * @Date 15/12/24
 */

public class RedisDelayQueueApi extends BaseRedisApi {

	/**
	 * 用单例的操作队列
	 */
	private RedisDelayQueueApi(){}
	
	private static class LazyHolder{
		private static RedisDelayQueueApi instance = new RedisDelayQueueApi();
	}
	
	public static RedisDelayQueueApi getInstance(){
		return LazyHolder.instance;
	}
	
	/**
	 * 借鉴delayQueue的锁机制
	 */
	private final transient ReentrantLock lock = new ReentrantLock();

	/**
	 * Condition signalled when a newer element becomes available at the head of
	 * the queue or a new thread may need to become leader.
	 */
	private final Condition available = lock.newCondition();

	/**
	 * 参考了delayQueue的内部结构
	 */
	private Thread leader = null;

	/**
	 * 存储延迟任务的队列
	 */
	private static final String delayedQueueKeyName = "DelayedQueueSet";

	/**
	 * 真正需要执行的队列
	 */
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

	public void take_queue() throws InterruptedException {

		final ReentrantLock lock = this.lock;
		lock.lockInterruptibly();
		try {
			for (;;) {
				// 查询队首
				Tuple tuple = peek();
				if (tuple == null) {
					// 阻塞当前线程
					available.await();
				}
				// 获取业务对象信息
				String elementValue = peek().getElement();
				// 执行的时间
				double execTime = tuple.getScore();
				// 当前系统时间
				long concurrentTime = System.currentTimeMillis();
				if (execTime - concurrentTime <= 0) {
					// TODO:执行真正的队列任务
				}
			}
		} finally {
			if (leader == null && peek() != null)
				available.signal();
			lock.unlock();
		}
	}

	/**
	 * 队首查询
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	private Tuple peek() throws InterruptedException {
		Set<Tuple> set = RedisClient.domain(redis -> redis.zrangeWithScores(delayedQueueKeyName, 0, 0));
		if (set.isEmpty()) {
			return null;
		} else {
			return set.iterator().next();
		}
	}

	public static void main(String[] a) throws Exception {
		RedisDelayQueueApi redisDelayQueueApi = new RedisDelayQueueApi();
		redisDelayQueueApi.execute_later("sss", 60l * 1000l * 60l * 24l * 30l * 3l);
		redisDelayQueueApi.take_queue();
	}

}
