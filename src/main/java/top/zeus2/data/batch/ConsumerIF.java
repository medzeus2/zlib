package top.zeus2.data.batch;

/**
 * 
 * 消费接口逻辑
 * 
 * 实现方法： 实现consume函数，并正确处理Consume对象。
 * 
 * @author Zeus2
 *
 * @param <E>
 * @see AbstractQueueTask
 */
public interface ConsumerIF<T> {

	/**
	 * 消费某个对象
	 * 
	 * @param data
	 */
	public void consume(T data);
}
