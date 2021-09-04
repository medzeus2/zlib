package top.zeus2.data.batch;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.zeus2.exception.ConsumeException;

/**
 * 支持多个消费者的的列表，每个消费者消费一次
 * 
 * @author Zeus2
 *
 * @param <T>
 */
public class ConsumerList<T> implements ConsumerIF<T> {

	private List<ConsumerIF<T>> list = new CopyOnWriteArrayList<ConsumerIF<T>>();

	private static Logger logger = LoggerFactory.getLogger(ConsumerList.class);

	/**
	 * 消费对象
	 */
	@Override
	public void consume(T data) {
		for (ConsumerIF<T> consumerIF : list) {
			try {
				consumerIF.consume(data);
			} catch (ConsumeException e) {
				throw e;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		}
	}

	public void insert(ConsumerIF<T> consumerif) {
		list.add(consumerif);
	}

	public void insert(int index, ConsumerIF<T> consumerif) {
		list.add(index, consumerif);
	}

	public void remove(ConsumerIF<T> consumerif) {

		list.remove(consumerif);
	}

}
