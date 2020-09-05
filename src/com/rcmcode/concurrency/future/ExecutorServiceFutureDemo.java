package com.rcmcode.concurrency.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceFutureDemo {

    public static void main(String[] args) {
        callableAndFutureDemo();
    }

    private static void callableAndFutureDemo() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        try {
            Future<Integer> future = executorService.submit(() -> {
                LongTask.simulateApiCall();
                return 1;
            });
            System.out.println("simulate call happening in new thread");
            Integer result = future.get();
            System.out.println("simulate call completed:"+result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

}
