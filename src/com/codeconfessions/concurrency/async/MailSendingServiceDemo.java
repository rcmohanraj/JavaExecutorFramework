package com.codeconfessions.concurrency.async;

import com.codeconfessions.concurrency.LongTask;
import com.codeconfessions.concurrency.mail.MailService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class MailSendingServiceDemo {

    public static void main(String[] args) {
        callMail();
    }

    private static void callMail() {
        MailService service = new MailService();
        //service.sendMail();
        service.sendMailAsyn();
        System.out.println("Hello World...");
        LongTask.simulate(5000);
    }

}
