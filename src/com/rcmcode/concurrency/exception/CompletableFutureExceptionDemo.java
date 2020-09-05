package com.rcmcode.concurrency.exception;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureExceptionDemo {

    public static void main(String[] args) {
        //waitForAllTaskCompletion();
        exceptionWithoutBreaking();
    }

    public static void exceptionDemo() {
        CompletableFuture future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Calling weather service:" + Thread.currentThread().getName());
            throw new IllegalStateException();
        });
        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void exceptionWithoutBreaking() {
        CompletableFuture future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Calling weather service:" + Thread.currentThread().getName());
            throw new IllegalStateException();
        });
        try {
            Object result = future.exceptionally(ex -> {
                System.out.println("The error is occurred, so we will send the default response");
                return 1;
            }).get();
            System.out.println("result:"+result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
