package com.ffan.qa.utils;

import java.io.IOException;
import com.ffan.qa.settings.TestConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtil {
    private static JedisPool jedisPool;

    static {
        if (jedisPool == null) {
            init();
        }
    }

    /**
     * 初始化Jedis连接池
     *
     * @throws IOException
     */
    private static void init() {
        //开始配置JedisPool
        String host = TestConfig.getCurrent().getRedisHost();
        int port = Integer.parseInt(TestConfig.getCurrent().getRedisPort());
        String auth = "";
        int poolTimeOut = 2000;
        JedisPoolConfig config = new JedisPoolConfig();
        //设置最大连接数（100个足够用了，没必要设置太大）
        config.setMaxTotal(100);
        //最大空闲连接数
        config.setMaxIdle(10);
        //获取Jedis连接的最大等待时间（50秒）
        config.setMaxWaitMillis(50 * 1000);
        //在获取Jedis连接时，自动检验连接是否可用
        config.setTestOnBorrow(true);
        //在将连接放回池中前，自动检验连接是否有效
        config.setTestOnReturn(true);
        //自动测试池中的空闲连接是否都是可用连接
        config.setTestWhileIdle(true);
        jedisPool = new JedisPool(config, host, port);
    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    public static void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

}
