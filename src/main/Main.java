import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
	public LRUCache lruCache;

	public static void main(String[] args) {
		long cacheSize;
		long maxTime;
		int httpPort;
		int redisPort;

		// Read properties from config file
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("Resources/config.properties");

			// load a properties file
			prop.load(input);

			cacheSize = Long.parseLong(prop.getProperty("cacheSize"));
			maxTime = Long.parseLong(prop.getProperty("maxTime"));
			httpPort = Integer.parseInt(prop.getProperty("httpPort"));
			redisPort = Integer.parseInt(prop.getProperty("redisPort"));

		} catch (IOException ex) {
			// Default if not set
			cacheSize = 1000;
			maxTime = 60000;
			httpPort = 8080;
			redisPort = 6379;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		LRUCache cache = LRUCache.getInstance();
		cache.init(cacheSize, maxTime);

		RedisClient redis = null;
		try {
			redis = RedisClient.getInstance();
			redis.init("localhost", redisPort);
		} catch (Exception e) {
			System.out.println("Could not connect to redis server");
			e.printStackTrace();
		}

		WebServer ws = null;
		try {
			ws = new WebServer(httpPort);
		} catch (Exception e) {
			System.out.println("Could not launch web server");
			e.printStackTrace();
		}
		ws.start();
	}

}
