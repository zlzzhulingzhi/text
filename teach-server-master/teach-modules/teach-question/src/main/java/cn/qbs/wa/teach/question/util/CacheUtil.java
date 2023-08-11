package cn.qbs.wa.teach.question.util;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类
 *
 * @Author zcm
 * @Date 2021/11/18 09:17
 * @Version 1.0
 */
@Component
public class CacheUtil {

    /**
     * 默认有效期7天
     */
    private static final long DEFAULT_TIME_TO_LIVE = 60 * 60 * 24 * 7;

    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;


    @Resource
    private RedissonClient redissonClient;


    public <T> T getFromBucket(String key) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        return bucket != null ? bucket.get() : null;
    }

    public <T> T getFromBucket(String key, DataLoader<T> dataLoader) {
        return this.getFromBucket(key, dataLoader, DEFAULT_TIME_TO_LIVE, DEFAULT_TIME_UNIT);
    }

    public <T> T getFromBucket(String key, DataLoader<T> dataLoader, long timeToLive, TimeUnit timeUnit) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        T data = bucket.get();
        if (data == null) {
            data = dataLoader.load();
            bucket.set(data, timeToLive, timeUnit);
        }
        return (T) data;
    }


    public boolean deleteFromBucket(String key) {
        return redissonClient.getBucket(key).delete();
    }

}
