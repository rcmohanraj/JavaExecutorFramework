package com.rcmcode.concurrency.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceThreadPoolDemo {

    public static void main(String[] args) {
        shutdownDemo();
        shutdownNowDemo();
    }

    private static void shutdownDemo() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        try {
            for (int i = 0; i < 10; i++) {
                executorService.submit(() -> System.out.println("shutdownDemo:"+Thread.currentThread().getName()));
            }
        } finally {
            executorService.shutdown();
        }
    }

    private static void shutdownNowDemo() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        try {
            for (int i = 0; i < 10; i++) {
                executorService.submit(() -> System.out.println("shutdownNowDemo:"+Thread.currentThread().getName()));
            }
        } finally {
            executorService.shutdownNow();
        }
    }
}
