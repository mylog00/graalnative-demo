package com.mylog00;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class App {


    private Object virtualBuilder;
    private final AtomicInteger threadCount = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(5);
        Runnable runnable = () -> {
            System.out.println("Hello from: " + Thread.currentThread().toString());
            latch.countDown();
        };
        var app = new VThreadReflect();
        for (int i = 0; i < 5; i++) {
            app.unstarted(runnable).start();
        }
        latch.await();
    }
}
