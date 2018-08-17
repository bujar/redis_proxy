
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

public class WebAPITest {

	@Test
	public void testHTTPSet() throws IOException {
		System.out.println("Testing HTTP Set");
		
		String key = "example";
		String value = "set";
		
	    String port = Integer.toString(MainTest.httpPort);
	    String url = "http://localhost:" + port + "/set?key=" + key + "&value=" + value;
		GETRequest(url);
		
		assertTrue(RedisClient.getInstance().exists(key));
		assertEquals(RedisClient.getInstance().get(key), value);
		assertEquals(LRUCache.getInstance().get(key), RedisClient.getInstance().get(key));

		System.out.println("SUCCESS\n\n");


	}
	
	@Test
	public void testHTTPGet() throws Exception {
	System.out.println("Testing HTTP GET:");
	String key = "example2";
	String value = "get";
    
    String port = Integer.toString(MainTest.httpPort);
    String url = "http://localhost:" + port + "/get?key=" + key;
    System.out.println("Requesting: " + url);
	GETRequest(url);

	assertTrue(RedisClient.getInstance().exists(key));
	assertEquals(RedisClient.getInstance().get(key), value);
	System.out.println("SUCCESS\n\n");

}
	@Test
	public void testGetCached() throws Exception {
	System.out.println("Testing HTTP GETCACHED:");
	String key = "example2";
	String value = "get";
    
    String port = Integer.toString(MainTest.httpPort);
    // adding kv pair to redis and cache
    String url = "http://localhost:" + port + "/set?key=" + key + "&value=" + value;
    System.out.println("Requesting: " + url);
	GETRequest(url);
	
	// requesting value for key a. This should return from cache
	url = "http://localhost:" + port + "/get?key=" + key;
    System.out.println("Requesting: " + url);
	GETRequest(url);

	// make sure that we get value from cache since it was just stored
	assertTrue(RedisClient.getInstance().exists(key));
	assertEquals(RedisClient.getInstance().get(key), value);
	assertTrue(LRUCache.getInstance().exists(key));
	assertEquals(LRUCache.getInstance().get(key), value);
	assertEquals(LRUCache.getInstance().get(key), RedisClient.getInstance().get(key));
	
	// make sure that it is no longer in the cache and that it still
	// returns from redis db
	Thread.sleep(MainTest.maxTime + 1000);
	assertFalse(LRUCache.getInstance().exists(key));
	assertTrue(RedisClient.getInstance().exists(key));
	assertEquals(RedisClient.getInstance().get(key), value);
	System.out.println("SUCCESS\n\n");

}
	private void GETRequest(String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

		} else {
			System.out.println("GET request didn't work");
		}
	}
}
