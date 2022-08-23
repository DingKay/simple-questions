package com.dk.thread.print;


import lombok.extern.slf4j.Slf4j;

/**
 * 启动三个线程，分别打印一个字母，每行输出 A B C，一共输出十行
 * 初级版
 * 缺点：
 *  1.线程承担的任务不够抽象化，写死在各个类中，输出A的任务必须要实例化 {@link PrintA}
 *  2.线程之间依次执行切换线程所需的判断策略固定 例如：flag % 3 == 1（只支持三个线程，多了或者少了不支持，需要修改各个类中的判断策略代码）
 *  3.线程的调度策略固定 例如：flag = 2;current++;
 *
 * @author dkay
 * @version 1.0
 */
@Slf4j
public class ThreeThread {
    public static Integer flag;
    public static class PrintA extends Thread {
        private final Integer cycle;
        private final Object monitor;

        public PrintA(Integer cycle, Object monitor) {
            this.cycle = cycle;
            this.monitor = monitor;
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            log.info("{} thread 开始执行", threadName);
            Integer current = 0;

            while (true) {
                if (cycle <= current) {
                    log.debug("{} thread 超过设定执行次数, 退出执行", threadName);
                    return;
                }

                synchronized (monitor) {
                    // or use equals of string
                    if (flag % 3 == 0) {
                        System.out.print("A\t");
                        flag = 1;
                        current++;
                        monitor.notifyAll();
                        log.debug("{} thread 执行结束, 唤醒其他线程", threadName);
                    } else {
                        try {
                            monitor.wait();
                            log.debug("{} thread 执行条件不满足, 等待唤醒", threadName);
                        } catch (java.lang.InterruptedException e) {
                            log.error("occur an error", e);
                        }
                    }
                }
            }
        }
    }

    public static class PrintB extends Thread {
        private final Integer cycle;
        private final Object monitor;

        public PrintB(Integer cycle, Object monitor) {
            this.cycle = cycle;
            this.monitor = monitor;
        }
        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            log.info("{} thread 开始执行", threadName);
            Integer current = 0;

            while (true) {
                if (cycle <= current) {
                    log.debug("{} thread 超过设定执行次数, 退出执行", threadName);
                    return;
                }

                synchronized (monitor) {
                    // or use equals of string
                    if (flag % 3 == 1) {
                        System.out.print("B\t");
                        flag = 2;
                        current++;
                        monitor.notifyAll();
                        log.debug("{} thread 执行结束, 唤醒其他线程", threadName);
                    } else {
                        try {
                            monitor.wait();
                            log.debug("{} thread 执行条件不满足, 等待唤醒", threadName);
                        } catch (java.lang.InterruptedException e) {
                            log.error("occur an error", e);
                        }
                    }
                }
            }
        }
    }

    public static class PrintC extends Thread {
        private final Integer cycle;
        private final Object monitor;

        public PrintC(Integer cycle, Object monitor) {
            this.cycle = cycle;
            this.monitor = monitor;
        }
        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            log.info("{} thread 开始执行", threadName);
            Integer current = 0;

            while (true) {
                if (cycle <= current) {
                    log.debug("{} thread 超过设定执行次数, 退出执行", threadName);
                    return;
                }

                synchronized (monitor) {
                    // or use equals of string
                    if (flag % 3 == 2) {
                        System.out.print("C\t\n");
                        flag = 0;
                        current++;
                        monitor.notifyAll();
                        log.debug("{} thread 执行结束, 唤醒其他线程", threadName);
                    } else {
                        try {
                            monitor.wait();
                            log.debug("{} thread 执行条件不满足, 等待唤醒", threadName);
                        } catch (java.lang.InterruptedException e) {
                            log.error("occur an error", e);
                        }
                    }
                }
            }
        }
    }
}
