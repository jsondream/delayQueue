# 延时队列   

### 需求场景   

* 饿了吗下单60s后发送短信通知   
* 淘宝订单在某时间内不下单就关闭   
现在的互联网环境中大家几乎都用到了延时队列的功能、  




#### 目标   

1. 高可用(消息在泵机后不丢失)  
2. 分布式  


#### 分布式可靠性  

redis队列中结合了redisson来实现了分布式锁   

[一个redisson的使用和示例项目](https://github.com/sianlixxx/redisson/blob/master/src/test/java/com/cctv/redis/RedisClientTest.java)