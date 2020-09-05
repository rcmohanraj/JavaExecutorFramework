package com.rcmcode.concurrency.timeout;

import com.rcmcode.concurrency.LongTask;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CompletableFutureTimeoutDemo {

    public static void main(String[] args) {
        //timeoutExceptionDemo();
        timeoutExceptionDefaultValueDemo();
    }

    public static void timeoutExceptionDemo() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Calling a slow service which will response after 3 seconds");
            LongTask.simulate(3000);
            return 10;
        });

        try {
            future
                    .orTimeout(1, TimeUnit.SECONDS)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void timeoutExceptionDefaultValueDemo() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Calling a slow service which will response after 3 seconds");
            LongTask.simulate(3000);
            return 10;
        });

        future
                .completeOnTimeout(20, 1, TimeUnit.SECONDS)
                .thenAccept(System.out::println);
        LongTask.simulate(3000);
    }
}
