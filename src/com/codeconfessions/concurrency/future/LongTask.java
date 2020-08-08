package com.codeconfessions.concurrency.future;

public class LongTask {

    public static void simulateApiCall() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
