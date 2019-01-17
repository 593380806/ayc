package com.ayc.framework.redis;



import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.RedisZSetCommands.Aggregate;
import org.springframework.data.redis.connection.RedisZSetCommands.Range;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Redis工具类
 * @author ysj
 * @Data 2019-01-15
 */
@Component
public class RedisUtil {
	
	@Resource
    RedisTemplate<Object, Object> redisTemplate;
	
	public void set(String key, String value) {
		final byte[] keyBytes = key.getBytes();
		final byte[] valueBytes = value.getBytes();
		redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) {
				connection.set(keyBytes, valueBytes);
				return null;
			}
		}, true);
	}
	
	public String get(String key) {
		final byte[] keyBytes = key.getBytes();
		return redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) {
				byte[] t = connection.get(keyBytes);
				return null == t? null : new String(t);
			}
		}, true);
	}
	
	/**
	 * 删除redis key
	 * @param key
	 * @return
	 */
	public Long del(String key) {
		final byte[] keyBytes = key.getBytes();
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) {
				return connection.del(keyBytes);
			}
		}, true);
	}
	
	public Boolean setNx(String key, String value) {
		final byte[] keyBytes = key.getBytes();
		final byte[] valueBytes = value.getBytes();
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) {
				return connection.setNX(keyBytes, valueBytes);
			}
		}, true);
	}
	
	public Boolean setNx(String key, String value, final long seconds) {
		final byte[] keyBytes = key.getBytes();
		final byte[] valueBytes = value.getBytes();
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) {
				Boolean flag = connection.setNX(keyBytes, valueBytes);
				connection.expire(keyBytes, seconds);
				return flag;
			}
		}, true);
	}
	
	
	public void set(String key, String value, final long seconds) {
		final byte[] keyBytes = key.getBytes();
		final byte[] valueBytes = value.getBytes();
		redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection){
				connection.set(keyBytes, valueBytes);
				connection.expire(keyBytes, seconds);
				return null;
			}
		}, true);
	}
	
	
	public Long incrBy(String key, final long value) {
		final byte[] keyBytes = key.getBytes();
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) {
				return connection.incrBy(keyBytes, value);
			}
		}, true);
	}
	
	public Long decrBy(String key, final long value) {
		final byte[] keyBytes = key.getBytes();
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) {
				return connection.decrBy(keyBytes, value);
			}
		}, true);
	}


	public <T> T get(String strKey, final Type returnType) {
		final byte[] keyBytes = strKey.getBytes();
		return redisTemplate.execute(new RedisCallback<T>() {
			@Override
			public  T doInRedis(RedisConnection connection) {
				byte[] resultbytes = connection.get(keyBytes);
				if(null == resultbytes)
					return null;
				return  JSONObject.parseObject(resultbytes, returnType);
			}
		}, true);
	}
	
	/*** redis set 操作 **/
	/**
	 * 删除redis key
	 * @param key
	 * @return
	 */
	public Long sAdd(String key,String val1) {
		final byte[] keyBytes = key.getBytes();
		final byte[] valBytes = val1.getBytes();
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) {
				return connection.sAdd(keyBytes, valBytes);
			}
		}, true);
	}
	
	public Boolean SisMember(String key , String val){
		final byte[] keyBytes = key.getBytes();
		final byte[] valBytes = val.getBytes();
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) {
				return connection.sIsMember(keyBytes, valBytes);
			}
		}, true);
	}
	
	/*** redis zset 操作 **/
    /**
     * 有序集合添加
     * @param key
     * @param value
     */
    public boolean zAdd(final String key,final double score,final String value){
        return redisTemplate.execute(new RedisCallback<Boolean>() {

			public Boolean  doInRedis(RedisConnection connection) {
				return connection.zAdd(key.getBytes(), score, value.getBytes());
			}
		}, true);
        
    }
    
    /**
     * 有序集合添加
     * @param key
     */
    public Long zCard(final String key){
    	   return redisTemplate.execute(new RedisCallback<Long>() {

   			public Long  doInRedis(RedisConnection connection) {
   				return connection.zCard(key.getBytes());
   			}
   		}, true);
    }
	
    /**
     * redis zset 差集运算 
     * @param destKey
     * @param key1
     * @param key2
     */
//	public TowTuple<Long, Long> zDiff(final String destKey, final String key1,final String key2){
//		TowTuple<Long, Long> tuple = redisTemplate.execute(new RedisCallback<TowTuple<Long, Long>>() {
//
//			public TowTuple<Long, Long>  doInRedis(RedisConnection connection) {
//				//S1 - S2
//				//S1交集S2
//				Long unionCount = connection.zUnionStore( destKey.getBytes(), Aggregate.MIN, new int[]{1,0}, key1.getBytes(),key2.getBytes());
//				Long delCount = connection.zRemRangeByScore(destKey.getBytes(), Range.range().lte(0).gte(0));
//				return Tuple.tuple(unionCount, delCount);
//			}
//		}, true);
//		return tuple;
//	}
	
	public static void main(String[] args) {
	
	}

	/**
	 * redis zcount
	 */
	public Long zCount(final String key,final double max, final double min) {
		return redisTemplate.execute(new RedisCallback<Long>() {

			public Long  doInRedis(RedisConnection connection) {
				Long zcount = connection.zCount(key.getBytes(), min, max);
				return zcount;
			}
		}, true);
	}

	/**
	 * redis zInterStore
	 * @param destKey
	 * @param key1
	 * @param key2
	 * @return
	 */
	public Long zInter(final String destKey,final String key1,final String key2) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long  doInRedis(RedisConnection connection) {
				Long zcount = connection.zInterStore(destKey.getBytes(), key1.getBytes(), key2.getBytes());
				return zcount;
			}
		}, true);
	}

	/**
	 * 获取zset中的元素
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<byte[]> zRange(final String key,final Long start, final Long end) {
		return redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
			public Set<byte[]>  doInRedis(RedisConnection connection) {
				Set<byte[]> zresult = connection.zRange(key.getBytes(), start, end);
				return zresult;
			}
		}, true);
	}
	
	public Set<RedisZSetCommands.Tuple> zRangeWithScores(final String key, final Long start, final Long end) {
		return redisTemplate.execute(new RedisCallback<Set<RedisZSetCommands.Tuple>>() {
			public Set<RedisZSetCommands.Tuple>  doInRedis(RedisConnection connection) {
				Set<RedisZSetCommands.Tuple> zresult = connection.zRangeWithScores(key.getBytes(), start, end);
				return zresult;
			}
		}, true);
	}
	
	/*********************************redis 操作List****************************************************/
	/**
	 * redis llen
	 * @param
	 */
	public Long llen(final String key) {
		return redisTemplate.execute(new RedisCallback<Long>() {

			public Long  doInRedis(RedisConnection connection) {
				Long zcount = connection.lLen(key.getBytes());
				return zcount;
			}
		}, true);
	}
	
	
	/**
	 * redis  lrange
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> lrange(final String key,final Long start, final Long end) {
		return redisTemplate.execute(new RedisCallback<List<String>>() {
			public List<String>  doInRedis(RedisConnection connection) {
				List<byte[]> list = connection.lRange(key.getBytes(), start, end);
				if(null != list){
					List<String> rangeList = new ArrayList<String>(list.size());
					for(byte[] b : list){
						rangeList.add(new String(b));	
					}
					return rangeList;
				}
				return null;
			}
		}, true);
	}
	
	/**
     * list添加 RPUSH 
     * @param key
     */
    public Long rpush(final String key, final String[] values){
    	final byte[][] rawValues = new byte[values.length][];
		int i = 0;
		for (String value : values) {
			if(StringUtils.isNotEmpty(value))
				rawValues[i++] = value.getBytes();
		}
        return redisTemplate.execute(new RedisCallback<Long>() {

			public Long  doInRedis(RedisConnection connection) {
				return connection.rPush(key.getBytes(), rawValues);
			}
		}, true);
    }
    
    /**
     * 设置redis key的过期时间
     * @param key
     * @param seconds
     * @return
     */
    public Boolean setExpire(final String key,final Long seconds){
        return redisTemplate.execute(new RedisCallback<Boolean>() {

			public Boolean  doInRedis(RedisConnection connection) {
				return connection.expire(key.getBytes(), seconds);
			}
		}, true);
    }
	
    /*************************************redis Map 操作 ****************************************/
    /**
     * 删除MAP k-v
     * @param key
     * @param fieldStr
     */
	public void hdel(String key, String fieldStr) {
		final byte[] keyBytes = key.getBytes();
		final byte[] fieldStrBytes = fieldStr.getBytes();
		redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) {
				return connection.hDel(keyBytes, fieldStrBytes);
			}
		}, true);
	}

	/**
	 * 
	 * @param key
	 * @param fieldStr
	 * @param genericReturnType
	 * @return
	 */
	public <T> T hget(String key, String fieldStr,final  Type genericReturnType) {
		final byte[] keyBytes = key.getBytes();
		final byte[] fieldStrBytes = fieldStr.getBytes();
		return redisTemplate.execute(new RedisCallback<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T doInRedis(RedisConnection connection) {
				byte[] resultbytes = connection.hGet(keyBytes, fieldStrBytes);
				if(null == resultbytes)
					return null;
				
				return (T) JSONObject.parseObject(resultbytes, genericReturnType);
			}
		}, true);
	}
	
	/**
	 * 获取Map k-V
	 * @param key
	 * @param fieldStr
	 * @return
	 */
	public String hgetString(String key, String fieldStr) {
		final byte[] keyBytes = key.getBytes();
		final byte[] fieldStrBytes = fieldStr.getBytes();
		return redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) {
				byte[] resultbytes = connection.hGet(keyBytes, fieldStrBytes);
				if(null == resultbytes)
					return null;
				
				return new String(resultbytes);
			}
		}, true);
	}

	/**
	 * 设置Map k-v 并设置超时时间
	 * @param key
	 * @param fieldStr
	 * @param jsonString
	 * @param expireTime
	 */
	public void hset(String key, String fieldStr, String jsonString,
			final int expireTime) {
		final byte[] keyBytes = key.getBytes();
		final byte[] fieldStrBytes = fieldStr.getBytes();
		final byte[] jsonStringBytes = jsonString.getBytes();
		redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) {
				connection.hSet(keyBytes, fieldStrBytes, jsonStringBytes);
				if(expireTime <= 0){
					return true;
				}
				return connection.expire(keyBytes, expireTime);
			}
		
		}, true);
	}
	
}
