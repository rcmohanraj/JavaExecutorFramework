package com.codeconfessions.concurrency.completion;

import com.codeconfessions.concurrency.LongTask;

import java.util.concurrent.CompletableFuture;

public class CallUponFutureCompletion {

    public static void main(String[] args) {
        //thenRunBehavior();
        //thenRunAsyncBehavior();
        //thenAcceptBehavior();
        thenAcceptAsyncBehavior();
    }

    private static void thenRunBehavior() {
        CompletableFuture future = CompletableFuture.supplyAsync(() -> {
            LongTask.simulate(2000);
            System.out.println("Async call is processed by:"+Thread.currentThread().getName());
            return 1;
        });
        future.thenRun(()-> {
            System.out.println("Successfully completed Async call in:"+Thread.currentThread().getName());
        });
        System.out.println("Waiting for the result");
        LongTask.simulate(3000);
    }

    private static void thenRunAsyncBehavior() {
        CompletableFuture future = CompletableFuture.supplyAsync(() -> {
            //LongTask.simulate(2000);
            System.out.println("Async call is processed by:"+Thread.currentThread().getName());
            return 1;
        });
        future.thenRunAsync(()-> {
            System.out.println("Successfully completed Async call in:"+Thread.currentThread().getName());
        });
        System.out.println("Waiting for the result");
        LongTask.simulate(3000);
    }

    private static void thenAcceptBehavior() {
        CompletableFuture future = CompletableFuture.supplyAsync(() -> {
            //LongTask.simulate(2000);
            System.out.println("Async call is processed by:"+Thread.currentThread().getName());
            return 1;
        });
        future.thenAccept(result-> {
            System.out.println("Successfully completed Async call in:"+Thread.currentThread().getName());
            System.out.println("result:"+result);
        });
        System.out.println("Waiting for the result");
        LongTask.simulate(3000);
    }

    private static void thenAcceptAsyncBehavior() {
        CompletableFuture future = CompletableFuture.supplyAsync(() -> {
            //LongTask.simulate(2000);
            System.out.println("Async call is processed by:"+Thread.currentThread().getName());
            return 1;
        });
        future.thenAcceptAsync(result-> {
            System.out.println("Successfully completed Async call in:"+Thread.currentThread().getName());
            System.out.println("result:"+result);

        });
        System.out.println("Waiting for the result");
        LongTask.simulate(3000);
    }

}
