package com.codeconfessions.concurrency.mail;

import com.codeconfessions.concurrency.LongTask;

import java.util.concurrent.CompletableFuture;

public class MailService {

    public void sendMail() {
        System.out.println("Mail Initiated...");
        LongTask.simulate(3000);
        System.out.println("Mail Sent...");
    }

    public CompletableFuture sendMailAsyn() {
        return CompletableFuture.runAsync( () -> sendMail() );
    }
}
