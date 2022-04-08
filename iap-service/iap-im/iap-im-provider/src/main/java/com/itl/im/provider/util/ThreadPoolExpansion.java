package com.itl.im.provider.util;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 自定义线程池
 *
 * @author tanq
 * @date 2020-10-10
 * @since jdk1.8
 */
public class ThreadPoolExpansion {
    /**
     * 队列阈值，超过此值则扩大线程池
     */
    private static final int MAX_QUEUE_SIZE = 100;

    /**
     * 每次扩容自动增加线程数
     */
    private static final int PER_ADD_THREAD = 10;

    /**
     * 监控积压时间频率
     */
    private static final int MONITOR_DELAY_TIME = 1;

    private ScheduledExecutorService scheduledExecutorService;

    private ThreadPoolExecutor executor;


    public ThreadPoolExecutor start() {
        executor = new ThreadPoolExecutor(10, 100, 60L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        scheduledExecutorService = new ScheduledThreadPoolExecutor(10, new BasicThreadFactory.Builder().namingPattern("mq-monitor-schedule-pool-%d").daemon(true).build());
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            //当队列大小超过限制，且jvm内存使用率小于80%时扩容，防止无限制扩容
            if (executor.getQueue().size() >= MAX_QUEUE_SIZE && executor.getPoolSize() < executor.getMaximumPoolSize() && getMemoryUsage() < 0.8) {
                executor.setCorePoolSize(executor.getPoolSize() + PER_ADD_THREAD);
            }
            //当队列大小小于限制的80%，线程池缩容
            if (executor.getPoolSize() > 0 && executor.getQueue().size() < MAX_QUEUE_SIZE * 0.8) {
                executor.setCorePoolSize(executor.getPoolSize() - PER_ADD_THREAD);
            }
        }, MONITOR_DELAY_TIME, MONITOR_DELAY_TIME, TimeUnit.SECONDS);
        return executor;
    }

    public void stop() throws InterruptedException {
        executor.shutdown();
        while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
            //等待线程池中任务执行完毕
        }
        scheduledExecutorService.shutdown();
    }

    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }

    public Future<?> submit(Runnable task) {
        return executor.submit(task);
    }


    /**
     * 获取jvm内存使用率
     *
     * @return
     */
    public static double getMemoryUsage() {
        return (double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Runtime.getRuntime().maxMemory();
    }


/*    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExpansion pool = new ThreadPoolExpansion();
        pool.start();
        pool.submit(new Runnable() {
            @Override
            public void run() {

            }
        });
        for (int i = 0; i < 1000; i++) {
            pool.submit(() -> {
                System.out.println(Thread.currentThread() + " execute!~~");
                try {
                    Thread.sleep(1500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        pool.stop();
    }*/
}
