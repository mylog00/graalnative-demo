package com.mylog00;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.atomic.AtomicInteger;


public class VThreadReflectTest {
    private static final Logger log = LoggerFactory.getLogger(VThreadReflectTest.class);

    @DisplayName("Virtual threads with reflection")
    @Test
    public void testApp() throws InterruptedException {
        final var app = new VThreadReflect();
        final var i = new AtomicInteger(0);
        Runnable runnable = () -> {
            System.out.println("Test message: " + Thread.currentThread().toString());
            i.incrementAndGet();
        };
        Thread thread = app.unstarted("VTest", runnable);
        thread.start();
        thread.join(1000);
        Assertions.assertEquals(1, i.get());
    }

    @Test
    void testVTreads() throws Throwable {
        final var i = new AtomicInteger(0);
        Runnable runnable = () -> {
            System.out.println("Test message: " + Thread.currentThread().toString());
            i.incrementAndGet();
        };

        var builderClass = Class.forName("java.lang.Thread$Builder$OfVirtual");
        log.info("Builder class: {}", builderClass);
        var handles = MethodHandles.publicLookup();
        log.info("Handles: {}", handles);
        final MethodHandle OF_VIRTUAL = handles.findStatic(Thread.class, "ofVirtual", MethodType.methodType(builderClass));
        log.info("OF_VIRTUAL: {}", OF_VIRTUAL);
        final MethodHandle NAME = handles.findVirtual(builderClass, "name", MethodType.methodType(builderClass, String.class));
        log.info("NAME: {}", NAME);
        final MethodHandle UNSTARTED = handles.findVirtual(builderClass, "unstarted", MethodType.methodType(Thread.class, Runnable.class));
        log.info("UNSTARTED: {}", UNSTARTED);

        Object virtualBuilder = OF_VIRTUAL.invoke();
        log.info("Virtual builder invoke: {}", virtualBuilder);
        virtualBuilder = NAME.invoke(virtualBuilder, "VTest2");
        log.info("Virtual builder name: {}", virtualBuilder);
        Object thread = UNSTARTED.invoke(virtualBuilder, runnable);
        log.info("Thread: {}", thread);
        Assertions.assertInstanceOf(Thread.class, thread);
        ((Thread) thread).start();
        ((Thread) thread).join(1000);
        Assertions.assertEquals(1, i.get());
    }
}
