
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LRUCacheTest {
	
	long maxSize = MainTest.cacheSize;
	long expiryTime = MainTest.maxTime;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGet() {
		System.out.println("Test Get from cache:");

		LRUCache lruCache = LRUCache.getInstance();
		lruCache.init(maxSize,expiryTime);
		for (int i = 0; i < maxSize; i++) {
			lruCache.set(Integer.toString(i), Integer.toString(i * 2));
		}

		for (int i = 0; i < maxSize; i++) {
			String value = lruCache.get(Integer.toString(i));
			assertEquals(value, Integer.toString(i * 2));
		}

		lruCache.clearCache();
		System.out.println("SUCCESS\n\n");

}

	@Test
	public void testExpiry() throws InterruptedException {
		System.out.println("Test Get from cache after expired:");

		LRUCache lruCache = LRUCache.getInstance();
		lruCache.init(maxSize,expiryTime);
        
        lruCache.set("a", "1");
		assertTrue(lruCache.exists("a"));
		
		Thread.sleep(expiryTime + 1000);
		
		assertNull(lruCache.get("a"));
		assertFalse(lruCache.exists("a"));

		lruCache.clearCache();
		System.out.println("SUCCESS\n\n");

}
	
	@Test
	public void testRemoveOldest() {
		System.out.println("Test Cache Eviction:");

		LRUCache lruCache = LRUCache.getInstance();
		lruCache.init(maxSize,expiryTime);
		lruCache.set("firstitem", "evicted");
		for (int i = 0; i < maxSize - 1; i++) {
			lruCache.set(Integer.toString(i), Integer.toString(i * 2));
		}
		assertTrue(lruCache.exists("firstitem"));
		lruCache.set("next", "elem");
		
		// firstitem should get evicted
		assertFalse(lruCache.exists("firstitem"));
		
		lruCache.clearCache();
		
		System.out.println("SUCCESS\n\n");

		
	}

	@Test
	public void testClearCache() {
		System.out.println("Test Clear Cache:");

		LRUCache lruCache = LRUCache.getInstance();
		lruCache.init(maxSize,expiryTime);
		for (int i = 0; i < maxSize; i++) {
			lruCache.set(Integer.toString(i), Integer.toString(i * 2));
		}
		assertTrue(lruCache.isFull());
		
		lruCache.clearCache();
		assertTrue(lruCache.isEmpty());
		System.out.println("SUCCESS\n\n");

		
		
	}

	@Test
	public void testIsEmpty() {
		System.out.println("Tet Cache isEmpty method:");

		LRUCache lruCache = LRUCache.getInstance();
		lruCache.init(maxSize,expiryTime);
		assertTrue(lruCache.isEmpty());
		
		System.out.println("SUCCESS\n\n");

	}

	@Test
	public void testIsFull() {
		System.out.println("Test Cache isFull method:");

		LRUCache lruCache = LRUCache.getInstance();
		lruCache.init(maxSize,expiryTime);
		for (int i = 0; i < maxSize-1; i++) {
			lruCache.set(Integer.toString(i), Integer.toString(i * 2));
		}
		assertFalse(lruCache.isFull());
		
		lruCache.set("a", "1");
		assertTrue(lruCache.isFull());
		lruCache.clearCache();
		
		System.out.println("SUCCESS\n\n");

	}

	@Test
	public void testSize() {
		System.out.println("Test Cache size method:");

		LRUCache lruCache = LRUCache.getInstance();
		lruCache.init(maxSize,expiryTime);
		for (int i = 0; i < maxSize; i++) {
			lruCache.set(Integer.toString(i), Integer.toString(i * 2));
		}
		assertEquals(lruCache.size(), maxSize);
		lruCache.clearCache();
		
		System.out.println("SUCCESS\n\n");

	}


	@Test
	public void testContainsKey() {
		System.out.println("Test Cache containsKey method:");

		LRUCache lruCache = LRUCache.getInstance();
		lruCache.init(maxSize,expiryTime);
		assertFalse(lruCache.exists("key1"));
		lruCache.set("key1", "1");
		assertTrue(lruCache.exists("key1"));
		assertFalse(lruCache.exists("key2"));
		lruCache.clearCache();
		System.out.println("SUCCESS\n\n");

	}

}
