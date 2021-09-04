package top.zeus2.data.batch;

import java.util.HashMap;
import java.util.Map;

/**
 * 队列上下文
 * 
 * @author Zeus2
 *
 */
public final class QueueContext {

	private Map<String, Object> mapdata;

	public QueueContext() {
		mapdata = new HashMap<String, Object>();
	}

	public void put(String key, Object value) {

		mapdata.put(key, value);
	}

	public Object get(String key) {

		return mapdata.get(key);
	}
}
