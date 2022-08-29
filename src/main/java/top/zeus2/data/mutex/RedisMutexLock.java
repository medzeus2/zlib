package top.zeus2.data.mutex;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisMutexLock {

    // redis 模板
    @Getter
    @Setter
    private StringRedisTemplate stringRedisTemplate;
 

    @Getter
    @Setter
    private MutexBlockMode mutexBlockMode = MutexBlockMode.Block;


    private static final String RELEASE_LOCK_LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return " + "redis.call('del', KEYS[1]) else return 0 end";

    private ThreadLocal<String> threadUuid = new ThreadLocal<String>();


    public boolean tryLock(String key, long time, TimeUnit unit) {

        if (threadUuid.get() == null) {
            threadUuid.set(UUID.randomUUID().toString().replace("-", ""));
        }
        String lockid = threadUuid.get();

        log.info("acquire lock for key " + key);
        if (time < 0) {
            throw new IllegalArgumentException("timeoutMsecs is less than zero");
        }

        try {
            if (stringRedisTemplate.opsForValue().setIfAbsent(key, lockid, time, unit)) {
                log.info("lock key {} get success", key);
                return true;
            }

            log.info("cannot lock key {}, retry again", key);
            if (this.mutexBlockMode == MutexBlockMode.Exception) {
                throw new MutexTaskRunningException("task is running cannot acquire lock . key : " + key);
            }
            // wait for 0.5s. prevent circular calls acquire lock.
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {

            }
            return false;

        } finally {

        }

    }


    public void release(String key) {


        try {

            String lockid = threadUuid.get();

            if (lockid == null) {

                throw new RuntimeException("usage error : muster lock then release..");
            }

            // 指定 lua 脚本，并且指定返回值类型
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<Long>(RELEASE_LOCK_LUA_SCRIPT, Long.class);
            // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）


            Long result = (Long) stringRedisTemplate.execute(redisScript, Collections.singletonList(key), lockid);

            if (result.equals(Long.valueOf("0"))) {

                log.warn("lockkey {} expired, cannot found it", key);
            } else {

                log.info("delete lockkey {} success", key);
            }

        } finally {

        }
    }

}
