import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisClient {

	private static RedisClient redisClient = null;
	private static JedisPool pool = null;

	public RedisClient() {
		// empty singleton instantiation
	}

	public static RedisClient getInstance() {
		if (redisClient == null)
			redisClient = new RedisClient();

		return redisClient;
	}

	public void init(String redisHost, int redisPort) {
		pool = new JedisPool(new JedisPoolConfig(), "localhost");

		System.out.println("Connected to Redis sucessfully");

	}

	public void set(String key, String value) {
		System.out.println("Adding " + key + "/" + value + " to Redis db");
		try (Jedis jedis = pool.getResource()) {
			jedis.set(key, value);
		}

	}

	public String get(String key) {
		String value = null;
		try (Jedis jedis = pool.getResource()) {
			value = jedis.get(key);
			System.out.println("Retrieved " + key + "/" + value + " from Redis db");
		}

		return value;
	}

	public void del(String key) {
		try (Jedis jedis = pool.getResource()) {
			jedis.del(key);
		}
	}

	public boolean exists(String key) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.exists(key);
		}
	}
	
	public void close() {
		pool.close();
	}

}
