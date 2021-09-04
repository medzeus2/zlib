package top.zeus2.data.batch;

import java.util.List;

/**
 * 批量处理消费者接口
 * 
 * @author Zeus2
 *
 * @param <E>
 *            批量消费者的类型
 */
public interface ConsumerBatchIF<E> extends ConsumerIF<List<E>> {

}
