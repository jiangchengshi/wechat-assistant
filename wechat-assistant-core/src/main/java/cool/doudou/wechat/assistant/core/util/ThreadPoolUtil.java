package cool.doudou.wechat.assistant.core.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadPoolUtil
 *
 * @author jiangcs
 * @since 2022/08/13
 */
@Slf4j
public class ThreadPoolUtil {
    /**
     * 创建延迟和循环线程池
     */
    private static final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    static {
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2, new ScheduledThreadFactory());
    }

    public static ScheduledThreadPoolExecutor getScheduledInstance() {
        return scheduledThreadPoolExecutor;
    }

    private static class ScheduledThreadFactory implements ThreadFactory {
        private final AtomicInteger count = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            String threadName = "wechat_scheduled_" + count.addAndGet(1);

            Thread t = new Thread(r);
            t.setName(threadName);

            log.info("new scheduledThread[{}]: taskCount[{}] = queueSize[{}] + activeCount[{}] + completedTaskCount[{}] ",
                    threadName,
                    scheduledThreadPoolExecutor.getTaskCount(),
                    scheduledThreadPoolExecutor.getQueue().size(),
                    scheduledThreadPoolExecutor.getActiveCount(),
                    scheduledThreadPoolExecutor.getCompletedTaskCount()
            );

            return t;
        }
    }
}
