package top.zeus2.data.batch;

import java.util.concurrent.*;

import info.zeus2.util.JdkVersion;

public class ThreadExecutors {

	/**
	 * Create fixed size thread pool. And when idle for specified time , the
	 * threads will be exit. 创建固定大小线程池
	 * 
	 * @param nThreads
	 *            线程池的数量 The Thead's count
	 * @param keepAliveTime
	 *            Then keep alive time for thread, when out of alive time , the
	 *            thread will be stopped. 保持在线程池的时间，如果大于0，则自动超期清理线程池。
	 * 
	 * @param unit
	 *            Time Unit 时间单位
	 * @param threadFactory
	 *            The Factory for creating thread 创建的线程工厂
	 * @return reutrn the created thread poll.返回适用的线程池
	 */
	public static ExecutorService newFixedThreadPool(int nThreads, long keepAliveTime, TimeUnit unit,
			ThreadFactory threadFactory) {
		ThreadPoolExecutor pool = null;
		pool = new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(), threadFactory);
		if (keepAliveTime > 0L) {
			pool.setKeepAliveTime(keepAliveTime, unit);
			if (JdkVersion.getMajorJavaVersion() >= JdkVersion.JAVA_16) {
				pool.allowCoreThreadTimeOut(true);
			}
		}

		return pool;

	}
}
