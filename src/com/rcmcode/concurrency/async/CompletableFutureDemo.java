package com.rcmcode.concurrency.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CompletableFutureDemo {

    public static void main(String[] args) {
        completableFutureDemo();
    }

    private static void completableFutureDemo() {
        callRunAsync();
        callSupplyAsync();
    }

    private static void callRunAsync() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> System.out.println("Runnable Implementation"));
    }

    private static void callSupplyAsync() {
        Supplier<Integer> task = () -> 1;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(task);
        try {
            Integer result = future.get();
            System.out.println("Result:"+result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
