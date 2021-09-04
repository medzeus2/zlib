package top.zeus2.data.batch;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 线程关闭事件分发器
 * 
 * @author Zeus2
 *
 */
public final class ThreadShutdownEventDispatcher {

	private static ThreadShutdownEventDispatcher _instance;
	private List<ThreadShutdownListener> listeners = new CopyOnWriteArrayList<ThreadShutdownListener>();

	private ThreadShutdownEventDispatcher() {

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				ThreadShutdownEventDispatcher.this.dispatchEvent();
			}
		}));
	}

	/**
	 * 获取单例
	 * 
	 * @return
	 */
	public static synchronized ThreadShutdownEventDispatcher getInstance() {
		if (_instance == null) {

			_instance = new ThreadShutdownEventDispatcher();
		}
		return _instance;
	}

	/**
	 * Registers a listener to receive events.
	 *
	 * @param listener
	 *            the listener.
	 */
	public void addListener(ThreadShutdownListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener cannnot be null.");
		}
		listeners.add(listener);
	}

	/**
	 * Unregisters a listener to receive events.
	 *
	 * @param listener
	 *            the listener.
	 */
	public void removeListener(ThreadShutdownListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Dispatches an event to all listeners.
	 *
	 * @param session
	 *            the session.
	 * @param eventType
	 *            the event type.
	 */
	public void dispatchEvent() {
		for (ThreadShutdownListener threadShutdown : listeners) {
			threadShutdown.close();
		}
	}
}
