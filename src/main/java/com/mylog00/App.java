package com.mylog00;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 */
public class App {
    private final AtomicInteger threadCount = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        Runnable runnable = () -> {
            System.out.println("Hello from: " + Thread.currentThread().toString());
            latch.countDown();
        };
        App app = new App();
        for (int i = 0; i < 5; i++) {
            app.newThread(runnable).start();
        }
        latch.await();
    }

    private Thread newThread(Runnable runnable) {
        final String threadName = "my-virtual-thread-" + threadCount.incrementAndGet();
        return new ReflectiveVirtualThreadBuilder()
                .name(threadName)
                .unstarted(runnable);
    }


}
