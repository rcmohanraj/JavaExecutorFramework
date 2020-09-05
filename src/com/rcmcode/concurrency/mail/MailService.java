package com.rcmcode.concurrency.mail;

import com.rcmcode.concurrency.LongTask;

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
