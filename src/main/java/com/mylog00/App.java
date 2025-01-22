package com.mylog00;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(5);
        AtomicInteger counter = new AtomicInteger(0);
        Runnable runnable = () -> {
            System.out.println(counter.incrementAndGet() + " Hello from: " + Thread.currentThread().toString());
            latch.countDown();

        };
        var app = new VThreadReflect();
        for (int i = 0; i < 5; i++) {
            app.unstarted("VThread", runnable).start();
        }
        latch.await();
    }
}
