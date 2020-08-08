package com.codeconfessions.concurrency.combine;

import java.util.concurrent.CompletableFuture;

public class CompletableFeatureCombineDemo {

    public static void main(String[] args) {
        combineFutures();
    }

    public static void combineFutures() {
        //Getting item price
        CompletableFuture<Integer> firstTask = CompletableFuture.supplyAsync(() -> 20);

        //Getting conversion rate
        CompletableFuture<Double> secondTask = CompletableFuture.supplyAsync(() -> 0.9);

        firstTask
                .thenCombine(secondTask, (a, b) -> a*b)
                .thenAccept(System.out::println);

    }
}
