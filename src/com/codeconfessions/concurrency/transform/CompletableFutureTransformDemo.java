package com.codeconfessions.concurrency.transform;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureTransformDemo {

    public static void main(String[] args) {
        transformFuture();
    }

    public static void transformFuture() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 30);
        future
            .thenApply(CompletableFutureTransformDemo::toFahrenheit)
            .thenAccept(System.out::println);
    }

    private static double toFahrenheit(double celsius) {
        return (celsius * 1.8) + 32;
    }
}
