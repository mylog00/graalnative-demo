package com.mylog00;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;


public class VThreadReflectTest {
    @DisplayName("Virtual threads with reflection")
    @Test
    public void testApp() throws InterruptedException {
        final var app = new VThreadReflect();
        final var i = new AtomicInteger(0);
        Thread thread = app.name("VTest").unstarted(() -> {
            System.out.println("Test: " + Thread.currentThread().toString());
            i.incrementAndGet();
        });
        thread.start();
        thread.join(1000);
        Assertions.assertEquals(1, i.get());
    }
}
