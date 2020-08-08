package com.codeconfessions.concurrency.compose;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureComposeDemo {

    public static void main(String[] args) {
        composeFutures();
    }

    public static void composeFutures() {
        //id-> fetch email from DB using the user id
        //with email fetch the playlist of the user from online stream application
        getUserEmailAsync()
                .thenCompose(email -> getPlaylistAsync(email))
                .thenAccept(System.out::println);
    }

    private static CompletableFuture<String> getUserEmailAsync() {
        System.out.println("Retrieving Email from DB");
        return CompletableFuture.supplyAsync(() -> "email");
    }

    private static CompletableFuture<String> getPlaylistAsync(String email) {
        System.out.println("Fetching playlist for the email id");
        return CompletableFuture.supplyAsync(() -> "playlist1");
    }
}
