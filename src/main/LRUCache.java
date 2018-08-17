import java.util.LinkedHashMap;

public class LRUCache {
	// static variable single_instance of type Singleton
    private static LRUCache lruCache = null;
    
	private long maxSize;

	private long nItems;

	private long maxCacheTime;

	private LinkedHashMap<String, KVEntry> cache;

	private LRUCache() {
    	// empty singleton instantiation
	}
	
	public static LRUCache getInstance()
    {
        if (lruCache == null)
        	lruCache = new LRUCache();
 
        return lruCache;
    }
	
	public void init(long maxSize, long maxCacheTime){
		this.maxSize = maxSize;
		this.maxCacheTime = maxCacheTime;
		nItems = 0;
		cache = new LinkedHashMap<String, KVEntry>();
		
	}

	/**
	 * Performs a key lookup in our LinkedHashMap queue and returns the
	 * value. We remove this key from its current place in the queue and
	 * re-add it to the back of the queue. We consider a key lookup as 
	 * 'used' so it moves to the back of the LRU queue
	 * @param key
	 * @return
	 */
	public synchronized String get(String key) {
		KVEntry entry = cache.get(key);
		
		if (entry != null) {
//			System.out.println(entry.key);
			if (System.currentTimeMillis() - entry.timeInserted < maxCacheTime) {
				// if found, move from front of queue to back
				cache.remove(key);
				nItems--;
				set(key, entry.value);
				return entry.value;
			}
		}
		return null;

	}

	/**
	 * Inserts an KVEntry object into the back of the LRU queue.
	 * @param key		String representation of key used to create KVEntry object
	 * @param value		String representation of value used to create KVEntry object 
	 */
	public synchronized void set(String key, String value) {
		if (isFull()) {
			removeOldest();
		}
		KVEntry entry = new KVEntry(key, value);
		cache.put(key, entry);
		nItems += 1;

	}

	/**
	 * Removes the first element in the queue/linkedhashmap
	 * This equates to the least recently used element
	 */
	public synchronized void removeOldest() {
		// pop first element
		if (nItems != 0) {
			String key = cache.keySet().iterator().next();
			cache.remove(key);
			nItems -= 1;
		}
	}

	public void clearCache() {
		nItems = 0;
		cache.clear();
	}
	public boolean isEmpty() {
		return (nItems == 0);
	}

	public boolean isFull() {
		return (nItems == maxSize);
	}

	public long size() {
		return nItems;
	}
	
	public long getMaxSize() {
		return maxSize;
	}
	
	public long getMaxCacheTime() {
		return maxCacheTime;
	}
	
	public boolean exists(String key) {
		KVEntry entry = cache.get(key);
		if (entry != null) {
			if (System.currentTimeMillis() - entry.timeInserted < maxCacheTime) {
				return true;
			}
			else {
				cache.remove(key);
				return false;
			}
		}
		else return false;
	}
}