package com.henry.mediaaction.manager;

import android.os.Handler;
import android.os.Looper;
import com.henry.mediaaction.utils.Logger;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
public class AppThreadExecutor {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    // We want at least 2 threads and at most 4 threads in the core pool,
    // preferring to have 1 less than the CPU count to avoid saturating
    // the CPU with background work
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    //线程池大小
    private static final int MAX_THREAD_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static volatile AppThreadExecutor instance;
    //创建一个大小为3的线程池, 主要用于网络请求等频繁任务的耗时线程
    private final Executor networkIO;
    //创建一个用于本地IO的线程池
    private final Executor diskIO;
    //创建一个用于主线程执行任务的线程池
    private final Executor mainThread;

    private final AtomicInteger mCount = new java.util.concurrent.atomic.AtomicInteger(1);

    class AppThreadFactory implements ThreadFactory {
        private String threadType = "default";

        public AppThreadFactory(String type) {
            this.threadType = type;
        }

        public void setThreadType(String type) {
            this.threadType = type;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("AppPool-" + threadType + "-" + mCount.getAndIncrement());
            Logger.e("AppThreadExecutor", thread.getName() + "-->" + instance.toString());
            return thread;
        }
    };

    private AppThreadExecutor() {
        diskIO = new ThreadPoolExecutor(1, MAX_THREAD_POOL_SIZE,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(2 * MAX_THREAD_POOL_SIZE), new AppThreadFactory("IO"), new ThreadPoolExecutor.DiscardOldestPolicy());
        networkIO = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_THREAD_POOL_SIZE,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(5 * MAX_THREAD_POOL_SIZE), new AppThreadFactory("NET"), new ThreadPoolExecutor.DiscardOldestPolicy());
        mainThread = new MainThreadExecutor();
    }

    public static AppThreadExecutor getInstance() {
        if (instance == null) {
            synchronized (AppThreadExecutor.class) {
                if (instance == null) {
                    instance = new AppThreadExecutor();
                }
            }
        }
        return instance;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    /**
     * 用于主线程执行任务的Executor
     */
    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
