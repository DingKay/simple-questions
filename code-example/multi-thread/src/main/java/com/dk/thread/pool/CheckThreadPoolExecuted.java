package com.dk.thread.pool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dkay
 * @version 1.0
 */
public class CheckThreadPoolExecuted {
    private static final String THREAD_NAME = "ThreadPoolTest";
    private static final Integer CYCLE_TIMES = 5;
    private static final AtomicInteger ATOMICINTEGER = new AtomicInteger(0);
    private static final ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(5, 5, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000), (runnable) -> {
        Thread thread = new Thread(runnable);
        thread.setName(THREAD_NAME + " - " + ATOMICINTEGER.getAndIncrement());
        return thread;
    });

    public static void main(String[] args) {
        // 使用 CountDownLatch
        // 优点: 使用简单
        // 缺点: 不可复用
        countDownLatchTest();

        // 使用CyclicBarrier
        // 优点: 可复用
        // 缺点: 使用复杂度较高
        cyclicBarrierTest();

        THREAD_POOL.shutdown();
    }

    private static void cyclicBarrierTest() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(CYCLE_TIMES, () -> System.out.println("cyclicBarrierTest " +
                "任务执行完毕"));
        for (int i = 0; i < CYCLE_TIMES; i++) {
            THREAD_POOL.execute(() -> {
                task();
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static void countDownLatchTest() {
        CountDownLatch countDownLatch = new CountDownLatch(CYCLE_TIMES);
        Runnable task = () -> {
            task();
            countDownLatch.countDown();
        };

        for (int i = 0; i < CYCLE_TIMES; i++) {
            THREAD_POOL.execute(task);
        }

        try {
            System.out.println("countDownLatchTest 任务执行中...");
            countDownLatch.await();
            System.out.println("countDownLatchTest 任务执行完毕...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void task() {
        System.out.println(Thread.currentThread().getName() + "开始执行任务");
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "任务执行完毕");
    }
}
