import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class MainTest {
	static int httpPort;
	static long cacheSize;
	static long maxTime;

	public static void main(String[] args) throws Exception {

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
			cacheSize = 100;
			maxTime = 12000;
			httpPort = 8080;
			redisPort = 6379;
		}

		LRUCache cache = LRUCache.getInstance();
		cache.init(cacheSize, maxTime);

		RedisClient redis = RedisClient.getInstance();
		redis.init("localhost", redisPort);

		WebServer ws = null;
		ws = new WebServer(httpPort);
		ws.start();

		Result result = JUnitCore.runClasses(LRUCacheTest.class);
		result.createListener();
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}

		System.out.println(result.wasSuccessful());

		Result result2 = JUnitCore.runClasses(WebAPITest.class);
		result2.createListener();

		for (Failure failure : result2.getFailures()) {
			System.out.println(failure.toString());
		}

		System.out.println(result2.wasSuccessful());
		int total = result.getRunCount() + result2.getRunCount();
		int failed = result.getFailureCount() + result2.getFailureCount();
		int passed = total - failed;

		System.out.println("Total tests: " + total);
		System.out.println("Passed: " + passed);
		System.out.println("Failed: " + failed);
		System.out.println("Runtime: " + result.getRunTime()
				+ result2.getRunTime() + "ms");
		ws.stop();

		RedisClient.getInstance().close();

	}

}
