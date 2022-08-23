package com.dk.thread.print;

import com.dk.config.InitConfig;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author dkay
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
        // 初始化log
        InitConfig.initializationLogger();

        // 初版
        // 创建同步监听器
        Object monitor = new Object();
        ThreeThread.flag = 0;
        ThreeThread.PrintA printA = new ThreeThread.PrintA(10, monitor);
        ThreeThread.PrintB printB = new ThreeThread.PrintB(10, monitor);
        ThreeThread.PrintC printC = new ThreeThread.PrintC(10, monitor);

        // 运行线程
        printA.start();
        printB.start();
        printC.start();

        try {
            Thread.sleep(4000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 优化版
        // 创建共用执行Condition谓词逻辑
        // 创建共用monitor
        Object aMonitor = new Object();
        Predicate<ThreeThreadOpt.ConditionStrategy> condition = ThreeThreadOpt.ConditionStrategy::checkCondition;
        // 创建共用检验EndPoint谓词逻辑
        Predicate<ThreeThreadOpt.EndPointStrategy> endPointCheck = ThreeThreadOpt.EndPointStrategy::checkEnd;
        // 创建全局共用策略容器
        Map<Integer, Boolean> synchronizedMap = Collections.synchronizedMap(new HashMap<>(16));
        ThreeThreadOpt.GlobalStrategy globalStrategy = new ThreeThreadOpt.GlobalStrategy(synchronizedMap);

        // 预设任务调度key
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        // 设置从A开始执行
        globalStrategy.nextExecutor(a);

        ThreeThreadOpt.TaskTemple<ThreeThreadOpt.ConditionStrategy, ThreeThreadOpt.EndPointStrategy> a1 =
                builderTask(aMonitor, condition, endPointCheck, globalStrategy, a, b, "A", false);
        ThreeThreadOpt.TaskTemple<ThreeThreadOpt.ConditionStrategy, ThreeThreadOpt.EndPointStrategy> b1 =
                builderTask(aMonitor, condition, endPointCheck, globalStrategy, b, c, "B", false);
        ThreeThreadOpt.TaskTemple<ThreeThreadOpt.ConditionStrategy, ThreeThreadOpt.EndPointStrategy> c1 =
                builderTask(aMonitor, condition, endPointCheck, globalStrategy, c, a, "C", true);

        a1.start();
        b1.start();
        c1.start();
    }

    private static ThreeThreadOpt.TaskTemple<ThreeThreadOpt.ConditionStrategy, ThreeThreadOpt.EndPointStrategy>
    builderTask(Object aMonitor, Predicate<ThreeThreadOpt.ConditionStrategy> condition,
                Predicate<ThreeThreadOpt.EndPointStrategy> endPointCheck,
                ThreeThreadOpt.GlobalStrategy globalStrategy, Integer self, Integer next,
                String outChar, boolean newLine) {
        // 创建打印线程x执行Condition实际调用类
        ThreeThreadOpt.ConditionStrategy printCondition = new ThreeThreadOpt.ConditionStrategy(globalStrategy, self,
                next);
        // 创建打印线程x检验EndPoint实际调用类
        ThreeThreadOpt.EndPointStrategy printEndPoint = new ThreeThreadOpt.EndPointStrategy(1, 10);
        StringBuilder outStr = new StringBuilder(outChar).append("\t");
        if (newLine) {
            outStr.append("\n");
        }
        // 创建打印线程x实际的“打印”操作
        Runnable printJob = () -> System.out.print(outStr);

        // 封装打印线程，其中调用callback调度下一个执行者
        ThreeThreadOpt.WorkJob job = new ThreeThreadOpt.WorkJob(printCondition, printJob);
        return new ThreeThreadOpt.TaskTemple<>(condition, printCondition, endPointCheck, printEndPoint, job,
                aMonitor);
    }
}
