package com.codeconfessions.concurrency.project;

import com.codeconfessions.concurrency.LongTask;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class FlightService {

    private static final List<String> SITES = List.of("site1", "site2", "site3");

    public CompletableFuture<Quote> getQuote(String site) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println(String.format("Getting quote from %s", site));
            Random random = new Random();
            LongTask.simulate(1_000 + random.nextInt(2_000));
            Integer price = 100 + random.nextInt(10);
            //System.out.println(String.format("Quote{site=%s, price=%d}", site, price));
            return new Quote(site, price);
        });
    }

    public Stream<CompletableFuture<Quote>> getQuotes() {
        return SITES
                    .stream()
                    .map(this::getQuote);
    }
}
