import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class WebServer {
	private HttpServer server;

	public WebServer(int port) throws Exception {
		server = HttpServer.create(new InetSocketAddress(port), 0);
	}

	public void start() {
		server.createContext("/get", new GetHandler());
		server.createContext("/set", new SetHandler());
		server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool()); // creates a default executor
		server.start();
		System.out.println("The HTTP server is running");

	}

	public void stop() {
		server.stop(1);
	}

	/**
	 * http://localhost:8000/get?key=X GET request used for key lookup. The only
	 * request parameter should be key=X where X is the name of the key.
	 * 
	 * It will first check the cache for the key. If the key is not found in the
	 * cache, it will then search redis for the key. If the key does not exist
	 * in either, it will return null.
	 * 
	 */
	static class GetHandler implements HttpHandler {
		public void handle(HttpExchange httpExchange) throws IOException {
			Map<String, String> parms = WebServer.queryToMap(httpExchange
					.getRequestURI().getQuery());
			String key = parms.get("key");
			String value = LRUCache.getInstance().get(key);
			if (value == null) {
				value = RedisClient.getInstance().get(key);
				LRUCache.getInstance().set(key, value);
			}
			if (value == null)
				value = "Key does not exist";
			writeResponse(httpExchange, value);
		}
	}

	/**
	 * http://localhost:8000/set?key=X&value=Y GET request used for key
	 * insertion into the redis db. The request parameters should be
	 * key=X&value=Y where X is the name of the key and Y is the value.
	 * 
	 * This will first add the key to the redis db and then add it to the cache.
	 * 
	 */
	static class SetHandler implements HttpHandler {
		public void handle(HttpExchange httpExchange) throws IOException {
			Map<String, String> parms = WebServer.queryToMap(httpExchange
					.getRequestURI().getQuery());
			String key = parms.get("key");
			String value = parms.get("value");

			RedisClient.getInstance().set(key, value);

			LRUCache.getInstance().set(key, value);
			writeResponse(httpExchange, "Successfully added " + key + "/"
					+ value);
		}
	}

	public static void writeResponse(HttpExchange httpExchange, String response)
			throws IOException {
		httpExchange.sendResponseHeaders(200, response.length());
		OutputStream os = httpExchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
		
	}

	/**
	 * returns the url parameters in a map
	 * 
	 * @param query
	 * @return map
	 */
	public static Map<String, String> queryToMap(String query) {
		Map<String, String> result = new HashMap<String, String>();
		for (String param : query.split("&")) {
			String pair[] = param.split("=");
			if (pair.length > 1) {
				result.put(pair[0], pair[1]);
			} else {
				result.put(pair[0], "");
			}
		}
		return result;
	}

}
