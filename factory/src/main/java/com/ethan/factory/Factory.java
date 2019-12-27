package com.ethan.factory;

import com.ethan.common.app.MyApplication;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Factory {

    // 单例模式
    private static final Factory instance;
    // 全局的线程池
    private final Executor executor;

    static {
       instance = new Factory();
    }
    public Factory() {
        executor = Executors.newFixedThreadPool(4);
    }

    public static MyApplication app(){
        return MyApplication.getInstance();
    }

    /**
     * 异步运行的方法
     * @param runnable
     */
    public static void runOnAsync(Runnable runnable){
        instance.executor.execute(runnable);
    }

}
