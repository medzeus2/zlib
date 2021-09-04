package top.zeus2.data.batch;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 支持目标哈希的批量执行
 * 
 * @author Zeus2
 *
 * @param <T>
 */
public class QueueTaskHashBatch<T> implements QueueTaskIF<T, List<T>>, Runnable {

	private List<ConsumerBatchIF<T>> consumers = new CopyOnWriteArrayList<ConsumerBatchIF<T>>();

	private int consumer_count;

	private ExecutorService consumerThreadPool;

	private List<BlockingQueue<T>> blockqueueList;

	private int maxDelayMS;

	/**
	 * 标记状态是否停止
	 */
	private volatile boolean stopped = true;

	private int maxBatchSize = 100;

	public QueueTaskHashBatch() {
		// super(queue, conumserif);

	}

	@Override
	public void run() {

	}

	@Override
	public void start() {

		this.run();
	}

	@Override
	public void stop() {
		stopped = true;

	}

	@Override
	public ExecutorService getConsumerThreadPool() {
		return consumerThreadPool;
	}

	@Override
	public void setConsumerThreadPool(ExecutorService consumerThreadPool) {
		this.consumerThreadPool = consumerThreadPool;

	}

	@Override
	public void offer(T data) {
		// 当队列数据大于阻塞大小时，拒绝新的信息，以保护JVM。
		int index = data.hashCode() % consumer_count;

	}

	@Override
	public int getMaxConsumeThreads() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxConsumeThreads(int maxConsumeThreads) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaxDelayTime(long time, TimeUnit unit) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getBlockingsize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBlockingsize(int blocksize) {
		// TODO Auto-generated method stub

	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

}
