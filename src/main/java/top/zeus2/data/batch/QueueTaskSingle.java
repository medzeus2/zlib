package top.zeus2.data.batch;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

/**
 * 每次处理一个任务的队列任务
 * 
 * @author Zeus2 代码调用示例
 * 
 *         <pre>
 *         {
 * 
 *         	QueueTaskSingle<String> s = new QueueTaskSingle<String>(new ConsumerIF<String>() {
 * 
 *         		public void consume(String data) {
 *         			System.out.println(data + Thread.currentThread());
 * 
 *         		}
 *         	});
 *         	s.setMaxConsumeThreads(10);
 *         	s.start();
 * 
 *         	for (int i = 0; i < 1000; i++) {
 * 
 *         		s.offer(String.valueOf(i));
 *         	}
 *         }
 *         </pre>
 * 
 * @param <T>
 *            处理一个任务的对象类型
 * @see AbstractQueueTask
 */
public class QueueTaskSingle<T> extends AbstractQueueTask<T, T> {

	private static final Logger log = LoggerFactory.getLogger(QueueTaskSingle.class);

	/**
	 * 创建一个每次消费一个任务的异步队列
	 * 
	 * @param queue
	 *            设置阻塞队列的队列，
	 * @param conumserif
	 *            设置队列的消费逻辑
	 */
	public QueueTaskSingle(BlockingQueue<T> queue, ConsumerIF<T> conumserif) {
		super(queue, conumserif);
	}

	/**
	 * 采用系统默认的LinkedBlockingQueue创建异步队列
	 * 
	 * @param conumserif
	 *            设置队列的消费逻辑
	 */
	public QueueTaskSingle(ConsumerIF<T> conumserif) {
		super(conumserif);
	}

	/**
	 * 单个队列的消费逻辑
	 */
	@Override
	public void run() {
		while (!stopped) {
			T data = null;
			try {

				// 从队列获取一个数据，如果100毫秒获取不到数据，那么返回空进入下一个循环
				data = blockqueue.poll(maxDelayMS, TimeUnit.MILLISECONDS);
				if (data != null) {
					getConumser().consume(data);
				}
			} catch (InterruptedException e) {
				break;
			} catch (Exception e) {

				// 其他异常时 需要打印日志
				log.warn("consuming object error, drop it. data is: " + String.valueOf(data), e);
			}
		}

	}

}
