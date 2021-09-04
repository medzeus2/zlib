package top.zeus2.data.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

public class QueueTaskBatch<T> extends AbstractQueueTask<T, List<T>> {

	private static final Logger log = LoggerFactory.getLogger(QueueTaskBatch.class);
	/**
	 * 每次批量执行最大数量
	 */
	private volatile int maxBatchSize = 100;

	public QueueTaskBatch(BlockingQueue<T> queue, ConsumerIF<List<T>> conumserif) {
		super(queue, conumserif);

	}

	public QueueTaskBatch(ConsumerIF<List<T>> conumserif) {
		super(conumserif);

	}

	@Override
	public void run() {
		// 一般情况 队列大小不会超过128
		List<T> list = new ArrayList<T>(128);
		while (!stopped) {
			// 使用一个对象，减少对象分配。
			list.clear();
			while (true) {
				try {
					T data = blockqueue.poll(maxDelayMS, TimeUnit.MILLISECONDS);
					if (data != null) {
						list.add(data);
						// 当达到最大批量执行数量时 自动退出获取循环
						if (list.size() >= maxBatchSize) {
							break;
						}
					} else {
						break;
					}
				} catch (InterruptedException e) {
					break;
				}
			}
			if (list.size() > 0) {
				try {
					getConumser().consume(list);
				} catch (Exception e) {
					// 当出现异常时，能够记录问题，并继续执行线程 不会造成线程 阻断。
					StringBuilder sb = new StringBuilder(1024);
					for (T t : list) {

						sb.append(t).append(" ");
					}

					log.warn("consuming objects error, objects is " + sb.toString(), e);
				}

			}
		}
	}

	/**
	 * 获取批量执行最大数量
	 * 
	 * @return
	 */
	public int getMaxBatchSize() {
		return maxBatchSize;
	}

	/**
	 * 设置每次批量执行的最大数量
	 * 
	 * @param maxBatchSize
	 */
	public void setMaxBatchSize(int maxBatchSize) {
		this.maxBatchSize = maxBatchSize;
	}

}
