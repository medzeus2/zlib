package top.zeus2.data.batch;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeus2.data.util.StringUtils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 异步队列任务
 * <p>
 * <p>
 * 代码调用示例
 *
 * <pre>
 * {
 * 	&#64;code
 * 	QueueTaskSingle<String> s = new QueueTaskSingle<String>(new ConsumerIF<String>() {
 *
 * 		public void consume(String data) {
 * 			System.out.println(data + Thread.currentThread());
 *
 *        }
 *    });
 * 	s.setMaxConsumeThreads(10);
 * 	s.start();
 *
 * 	for (int i = 0; i < 1000; i++) {
 *
 * 		s.offer(String.valueOf(i));
 *    }
 * }
 * </pre>
 *
 * @author Zeus2
 */
public abstract class AbstractQueueTask<T, C> implements Runnable, QueueTaskIF<T, C> {

    private static final Logger log = LoggerFactory.getLogger(AbstractQueueTask.class);
    /**
     * 阻塞消息队列
     */
    protected final BlockingQueue<T> blockqueue;
    /**
     * 队列消费者
     */
    @Getter
    private final ConsumerIF<C> conumser;
    /**
     * 辅助Offer判断线程的锁定对象
     */
    private final Object lockobj = new Object();
    /**
     * 最大延迟时间默认100毫秒
     */
    protected long maxDelayMS = 100L;
    /**
     * volatile 标记，标记是否开启处理队列循环。
     */
    protected volatile boolean stopped = true;
    /**
     * 本地线程池
     */
    protected ExecutorService consumerThreadPool = null;
    /**
     * 最大消费线程数，默认1个线程。
     */
    private int maxConsumeThreads = 1;
    /**
     * 设置阻塞大小。 当队列存储到blocksize时，队列自动是的offer的线程阻塞，以防止出现OOM的情况。
     * 增加volatile，确保多线程看到的值一致。
     */
    private volatile int blockingsize = -1;
    /**
     * 缓冲队列满处理规则，默认阻塞off线程，可选择抛出异常。
     */
    @Setter
    @Getter
    private PoolFullMode poolFullMode = PoolFullMode.Blocking;
    /**
     * 上次打印堆栈移端口
     */
    private volatile long t1;
    /**
     * 设置默认的线程名称
     */
    private String name;

    /**
     * 构造函数
     *
     * @param conumserif
     */
    public AbstractQueueTask(ConsumerIF<C> conumserif) {
        this(new LinkedBlockingQueue<T>(), conumserif);

    }

    /**
     * 构造函数
     *
     * @param queue
     * @param conumserif
     */
    public AbstractQueueTask(BlockingQueue<T> queue, ConsumerIF<C> conumserif) {
        this.conumser = conumserif;
        this.blockqueue = queue;

    }

    /*
     * (non-Javadoc)
     *
     * @see info.zeus2.concurrent.QueueTaskIF#start()
     */
    @Override
    public synchronized void start() {
        if (!this.stopped) {
            // The threads already started. do nothing
            return;
        }

        this.stopped = false;

        for (int i = 0; i < this.maxConsumeThreads; i++) {

            getThreadPool().execute(this);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see info.zeus2.concurrent.QueueTaskIF#stop()
     */
    @Override
    public synchronized void stop() {
        this.stopped = true;
    }

    /*
     * (non-Javadoc)
     *
     * @see info.zeus2.concurrent.QueueTaskIF#run()
     */
    @Override
    public abstract void run();

    /*
     * (non-Javadoc)
     *
     * @see info.zeus2.concurrent.QueueTaskIF#getConsumerThreadPool()
     */
    @Override
    public ExecutorService getConsumerThreadPool() {
        return consumerThreadPool;
    }

    /*
     * (non-Javadoc)
     *
     * @see info.zeus2.concurrent.QueueTaskIF#setConsumerThreadPool(java.tools.
     * concurrent.ExecutorService)
     */
    @Override
    public void setConsumerThreadPool(ExecutorService consumerThreadPool) {
        this.consumerThreadPool = consumerThreadPool;
    }

    /*
     * (non-Javadoc)
     *
     * @see info.zeus2.concurrent.QueueTaskIF#offer(T)
     */

    @Override
    public void offer(T data) {

        // 如果没有自动启动消费线程 则启动消费线程
        if (this.stopped) {
            // 同步一下 防止第二个线程offer时 多次启动消费者
            synchronized (lockobj) {
                // 再次判断
                if (this.stopped) {
                    for (int i = 0; i < this.maxConsumeThreads; i++) {

                        getThreadPool().execute(this);
                    }
                    this.stopped = false;
                }
            }

        }

        // 当队列数据大于阻塞大小时，拒绝新的信息，以保护JVM。
        while (this.blockingsize != -1 && this.blockqueue.size() > this.blockingsize) {
            if (poolFullMode == PoolFullMode.Exception) {
                throw new QueueFullException();
            }

            // 控制在5秒打印一次
            if (System.currentTimeMillis() - t1 > 5000L) {
                log.info("{} exceed maxblocksize {}", Thread.currentThread().getName(), this.blockingsize);

                t1 = System.currentTimeMillis();
            }
            try {
                Thread.sleep(20L);
            } catch (InterruptedException e) {
                // Ignore
            }
        }
        try {
            this.blockqueue.put(data);
        } catch (InterruptedException e) {
            // Ignore
        }
    }

    /**
     * 获取执行线程池
     *
     * @return
     */
    private synchronized ExecutorService getThreadPool() {

        if (consumerThreadPool == null) {

            final String name_in = AbstractQueueTask.this.name;
            consumerThreadPool = ThreadExecutors.newFixedThreadPool(this.maxConsumeThreads, 60, TimeUnit.SECONDS,
                    new ThreadFactory() {
                        private AtomicInteger index = new AtomicInteger(1);

                        @Override
                        public Thread newThread(Runnable r) {

                            Thread t = new Thread(r);
                            // 使用TypeReference 获取泛型的真实类型
                            if (StringUtils.isBlank(name_in)) {

                                t.setName(this.getClass().getName() + "-" + index.getAndIncrement());
                            } else {
                                t.setName(name_in + "-" + index.getAndIncrement());
                            }

                            t.setDaemon(true);
                            return t;
                        }
                    });
        }

        return this.consumerThreadPool;
    }

    /*
     * (non-Javadoc)
     *
     * @see info.zeus2.concurrent.QueueTaskIF#getMaxConsumeThreads()
     */
    @Override
    public int getMaxConsumeThreads() {
        return maxConsumeThreads;
    }

    /*
     * (non-Javadoc)
     *
     * @see info.zeus2.concurrent.QueueTaskIF#setMaxConsumeThreads(int)
     */
    @Override
    public void setMaxConsumeThreads(int maxConsumeThreads) {
        if (maxConsumeThreads <= 0) {
            maxConsumeThreads = 1;
        }
        this.maxConsumeThreads = maxConsumeThreads;
    }

    /*
     * (non-Javadoc)
     *
     * @see info.zeus2.concurrent.QueueTaskIF#setMaxDelayTime(long,
     * java.tools.concurrent.TimeUnit)
     */
    @Override
    public void setMaxDelayTime(long time, TimeUnit unit) {

        this.maxDelayMS = unit.toMillis(time);
    }

    /*
     * (non-Javadoc)
     *
     * @see info.zeus2.concurrent.QueueTaskIF#getBlocksize()
     */
    @Override
    public int getBlockingsize() {
        return blockingsize;
    }

    /*
     * (non-Javadoc)
     *
     * @see info.zeus2.concurrent.QueueTaskIF#setBlocksize(int)
     */
    @Override
    public void setBlockingsize(int blockingsize) {
        this.blockingsize = blockingsize;
    }

    /*
     * (non-Javadoc)
     *
     * @see info.zeus2.concurrent.QueueTaskIF#size()
     */
    @Override
    public int size() {

        return this.blockqueue.size();
    }

    /*
     * (non-Javadoc)
     *
     * @see info.zeus2.concurrent.QueueTaskIF#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     *
     * @see info.zeus2.concurrent.QueueTaskIF#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

}
