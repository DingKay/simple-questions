package com.dk.thread.print;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 优化 {@link ThreeThread} 功能
 *
 * @author dkay
 * @version 1.0
 * @see ThreeThread
 */
@Slf4j
public class ThreeThreadOpt {

    public static class TaskTemple<C, S> extends Thread {
        // 执行判断策略
        private final Predicate<C> predicate;
        private final C condition;
        // 任务结束判断
        private final Predicate<S> endPoint;
        private final S strategy;
        // 任务执行消费
        private final Runnable job;
        // 同步锁对象
        private final Object monitor;

        public TaskTemple(Predicate<C> predicate, C condition, Predicate<S> endPoint, S strategy,
                          Runnable job, Object monitor) {
            this.predicate = predicate;
            this.condition = condition;
            this.endPoint = endPoint;
            this.strategy = strategy;
            this.job = job;
            this.monitor = monitor;
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            log.info("{} thread 开始执行", threadName);

            while (true) {
                synchronized (monitor) {
                    if (predicate.test(condition)) {
                        job.run();
                        monitor.notifyAll();
                        if (endPoint.test(strategy)) {
                            log.debug("{} thread 超过设定执行次数, 退出执行", threadName);
                            return;
                        }
                        log.debug("{} thread 执行结束, 唤醒其他线程", threadName);
                    } else {
                        try {
                            monitor.wait();
                            log.debug("{} thread 执行条件不满足, 等待唤醒", threadName);
                        } catch (Exception e) {
                            log.error("occur an error", e);
                        }
                    }
                }
            }
        }
    }

    public static class EndPointStrategy {
        ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

        private final Integer endValue;
        private final Integer startValue;
        private Boolean flag = Boolean.FALSE;

        public EndPointStrategy(Integer startValue, Integer endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }

        public boolean checkEnd() {
            Integer current = threadLocal.get();
            if (Objects.isNull(current)) {
                current = startValue;
                threadLocal.set(current);
            }

            if (current >= endValue) {
                threadLocal.remove();
                flag = Boolean.TRUE;
            } else {
                threadLocal.set(++current);
            }

            return flag;
        }
    }

    /**
     * 全局策略实现
     * 底层部分逻辑委托map实现
     */
    public static class GlobalStrategy {
        // GlobalStrategy 委托全局策略map
        private final Map<Integer, Boolean> globalLoopStrategy;

        public GlobalStrategy(Map<Integer, Boolean> globalLoopStrategy) {
            this.globalLoopStrategy = globalLoopStrategy;
        }

        private void nullCheck() {
            if (Objects.isNull(globalLoopStrategy)) {
                throw new RuntimeException("循环策略标识器为空");
            }
        }

        public boolean checkReady() {
            nullCheck();
            return Objects.nonNull(globalLoopStrategy);
        }

        public boolean check(Integer key) {
            nullCheck();
            Boolean aBoolean = globalLoopStrategy.get(key);
            if (Objects.nonNull(aBoolean) && aBoolean) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }

        public void done(Integer key) {
            nullCheck();
            globalLoopStrategy.put(key, Boolean.FALSE);
        }

        public void nextExecutor(Integer nextKey) {
            nullCheck();
            globalLoopStrategy.put(nextKey, Boolean.TRUE);
        }
    }

    /**
     * 缺点：任务调度只支持环形线性指派，即：只支持串行调度
     * 如果存在多个执行者指派同一个被执行人(nextExecutorKey>= 2),
     * 那么被执行人(nextExecutorKey所映射selfKey的执行者)的执行次数会出现问题
     */
    public static class ConditionStrategy {
        private final GlobalStrategy strategy;
        private final Integer selfKey;
        private final Integer nextExecutorKey;

        public ConditionStrategy(GlobalStrategy strategy, Integer selfKey, Integer nextExecutorKey) {
            this.strategy = strategy;
            this.selfKey = selfKey;
            this.nextExecutorKey = nextExecutorKey;
        }

        public boolean checkCondition() {
            if (!strategy.checkReady() || Objects.isNull(selfKey)) {
                throw new RuntimeException("自身标识符为空");
            }

            if (strategy.check(selfKey)) {
                strategy.done(selfKey);
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }

        public void doneCallback() {
            strategy.nextExecutor(nextExecutorKey);
        }
    }

    /**
     * 缺点：
     * 委托执行严重，模板太多，不够精炼
     */
    public static class WorkJob implements Runnable {
        private final ConditionStrategy strategy;
        private final Runnable actualWork;

        public WorkJob(ConditionStrategy strategy, Runnable actualWork) {
            this.strategy = strategy;
            this.actualWork = actualWork;
        }

        @Override
        public void run() {
            actualWork.run();
            strategy.doneCallback();
        }
    }

}
