public class KVEntry {

	public long timeInserted;
	public String key;
	public String value;

	public KVEntry(String k, String v) {
		timeInserted = System.currentTimeMillis();
		key = k;
		value = v;
	}

}
