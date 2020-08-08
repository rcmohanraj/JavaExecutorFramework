package com.codeconfessions.concurrency.waiting;

import com.codeconfessions.concurrency.LongTask;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureWaitingDemo {

    public static void main(String[] args) {
        waitForAllTaskCompletion();
        waitForAnyoneTaskCompleted();
    }

    public static void waitForAllTaskCompletion() {
        CompletableFuture<Integer> first = CompletableFuture.supplyAsync(() -> 1);
        CompletableFuture<Integer> second = CompletableFuture.supplyAsync(() -> 2);
        CompletableFuture<Integer> third = CompletableFuture.supplyAsync(() -> 3);

        CompletableFuture<Void> all = CompletableFuture.allOf(first, second, third);
        all.thenRun(() -> {
            try {
                Integer firstResult = first.get();
                System.out.println("first result:"+firstResult);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("All Tasks are completed");
        });
    }

    public static void waitForAnyoneTaskCompleted() {
        CompletableFuture<Integer> slowService =
                CompletableFuture.supplyAsync(() -> {
                    LongTask.simulate(2000);
                    return 20;
                });
        CompletableFuture<Integer> fastService = CompletableFuture.supplyAsync(() -> 20);

        CompletableFuture.anyOf(slowService, fastService)
                .thenAccept(result -> System.out.println(result));
    }
}
