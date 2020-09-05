package com.rcmcode.concurrency.async;

import com.rcmcode.concurrency.LongTask;
import com.rcmcode.concurrency.mail.MailService;

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
