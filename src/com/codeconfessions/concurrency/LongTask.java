package com.codeconfessions.concurrency;

public class LongTask {

    public static void simulate(long waitTime) {
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
