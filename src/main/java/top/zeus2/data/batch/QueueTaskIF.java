package top.zeus2.data.batch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public interface QueueTaskIF<T, C> {

	/**
	 * 当设置完消费者后，应立即启动队列等待产品到来，目前无需执行
	 */
	void start();

	/**
	 * 停止队列消费，停止后需要运行start才能继续启动。
	 */
	void stop();

	/**
	 * 
	 * @return
	 */
	ExecutorService getConsumerThreadPool();

	/**
	 * 设置消费线程池，默认会创建MaxConsumeThreads数量的线程，用来一起消费数据。
	 * 
	 * @param consumerThreadPool
	 */
	void setConsumerThreadPool(ExecutorService consumerThreadPool);

	/**
	 * 增加一个对象
	 * 
	 * @param data
	 */
	void offer(T data);

	/**
	 * 获取最大消费线程数，注意改值必须在start之前设置才有效，否则无效。
	 * 此外在setConsumerThreadPool后，消费线程数以ConsumerThreadPool中的数值为准。
	 * 
	 * @return 获取当前设置的最大消费线程数
	 */
	int getMaxConsumeThreads();

	/**
	 * 设置最大消费者线程，一般要求不高的任务选1，当1不能满足性能要求时可以适当提高，最大不能超过CPU内核数。
	 * 
	 * @param maxConsumeThreads
	 *            设置最大的消费者线程数
	 */
	void setMaxConsumeThreads(int maxConsumeThreads);

	/**
	 * 设置批量获取数据最大延时，当等待最大延时后依然没有新数据，执行队列将已有的数据进行一次批量执行<br />
	 * 需要注意的是，如果调用stop也会自动等待最大延时时间后才能生效。
	 * 
	 * 因此推荐采用低于1秒的延时时间，越小对CPU性能影响越大，建议采用默认值100毫秒。
	 * 
	 * @param time
	 *            延时时间
	 * @param unit
	 *            延时单位
	 */
	void setMaxDelayTime(long time, TimeUnit unit);

	/**
	 * @return the blocksize
	 */
	int getBlockingsize();

	/**
	 * Set the blocking size, when the buffer exceed the max blocking size, it will
	 * block the offer method.
	 * 
	 * @param blocksize
	 *            the blocksize to set
	 */
	void setBlockingsize(int blocksize);

	/**
	 * 获取当前队列长度
	 * 
	 * @return
	 */
	int size();

	/**
	 * @return the name
	 */
	String getName();

	/**
	 * @param name
	 *            the name to set
	 */
	void setName(String name);

}