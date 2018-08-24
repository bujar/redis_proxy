import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

public class RedisClient {

	private static RedisClient redisClient = null;
	private static JedisPool pool = null;

	public RedisClient() {
		// empty singleton instantiation
	}

	/**
	 * Creates Singleton class using inner static helper class. This 
	 * gets loaded into memory when getInstance() is first called. This 
	 * method avoids slowdown of having to use synchronization.
	 * 
	 * Known as the Bill Pugh Singleton Implementation
	 * Reference: https://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
	 * 
	 */
	private static class RedisClientHelper {
		private static final RedisClient redisClient = new RedisClient();
	}
	public static RedisClient getInstance() {
		return RedisClientHelper.redisClient;
	}

	public void init(int redisPort) throws Exception {
		if (pool == null) {
			pool = new JedisPool(new JedisPoolConfig(), System.getenv("REDIS_HOST"));

			try (Jedis jedis = pool.getResource()) {
				System.out.println("Connected to Redis sucessfully");
			} catch (JedisException e) {
				System.out.println("Failed to connect to Redis");
				throw e;
			}
		} else {
			throw new Exception(
					"An instance of RedisClient already exists. You may only have one instance");
		}

	}

	public void set(String key, String value) {
		try (Jedis jedis = pool.getResource()) {
			jedis.set(key, value);
			System.out.println("Adding " + key + "/" + value + " to Redis db");
		} catch (JedisException e) {
			if (pool != null) {
				pool.close();
			}
			throw e;
		}

	}

	public String get(String key) throws JedisException {
		String value = null;
		try (Jedis jedis = pool.getResource()) {
			value = jedis.get(key);
			System.out.println("Retrieved " + key + "/" + value
					+ " from Redis db");
		} catch (JedisException e) {
			if (pool != null) {
				pool.close();
			}
			throw e;
		}

		return value;
	}

	public void del(String key) throws JedisException {
		try (Jedis jedis = pool.getResource()) {
			jedis.del(key);
		} catch (JedisException e) {
			if (pool != null) {
				pool.close();
			}
			throw e;
		}
	}

	public boolean exists(String key) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.exists(key);
		} catch (JedisException e) {
			if (pool != null) {
				pool.close();
			}
			throw e;
		}
	}

	public void close() {
		pool.close();
	}

}
