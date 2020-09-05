package com.rcmcode.concurrency.project;

import com.rcmcode.concurrency.LongTask;

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
            System.out.println(String.format("Quote{site=%s, price=%d}", site, price));
            return new Quote(site, price);
        });
    }

    public Stream<CompletableFuture<Quote>> getQuotes() {
        return SITES
                    .stream()
                    .map(s -> getQuote(s));
    }

    private void test() {
        List<String> list = List.of("Search1", "Search2", "Search3");

        list.forEach(this::convert);

    }

    private void convert(String s) {
        System.out.println("s==>"+s);
    }

    public static void main(String[] args) {
        FlightService service = new FlightService();
        service.test();
    }

}
