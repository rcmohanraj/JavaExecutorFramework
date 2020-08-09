package com.codeconfessions.concurrency.project;

import com.codeconfessions.concurrency.LongTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FlightBookingDemo {

    public static void main(String[] args) {
        LocalDateTime start = LocalDateTime.now();
        FlightService flightService = new FlightService();
        /*CompletableFuture.allOf(flightService.getQuote("site1")), flightService.getQuote("site2"))
                .thenRun(() -> System.out.println("Retrieved all the quotes."));*/

        List<CompletableFuture<Void>> futures = flightService
                .getQuotes()
                .map(future -> future.thenAccept(result -> System.out.println(result)))
                .collect(Collectors.toList());
        CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun( () -> {
                    Duration duration = Duration.between(start, LocalDateTime.now());
                    System.out.println(String.format("Retrieved all quotes in %d msec", duration.toMillis()));
                });
        LongTask.simulate(10_000);
    }

}
