package com.mylog00;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class VThreadReflect {
    private static final Logger log = LoggerFactory.getLogger(VThreadReflect.class);
    private final MethodHandle OF_VIRTUAL;
    private final MethodHandle NAME;
    private final MethodHandle UNSTARTED;

    public VThreadReflect() {
        try {
            var builderClass = Class.forName("java.lang.Thread$Builder$OfVirtual");
            log.info("Builder class: {}", builderClass);
            var handles = MethodHandles.publicLookup();
            log.info("Handles: {}", handles);
            OF_VIRTUAL = handles.findStatic(Thread.class, "ofVirtual", MethodType.methodType(builderClass));
            log.info("OF_VIRTUAL: {}", OF_VIRTUAL);
            NAME = handles.findVirtual(builderClass, "name", MethodType.methodType(builderClass, String.class));
            log.info("NAME: {}", NAME);
            UNSTARTED = handles.findVirtual(builderClass, "unstarted", MethodType.methodType(Thread.class, Runnable.class));
            log.info("UNSTARTED: {}", UNSTARTED);
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public Thread unstarted2(String name, Runnable runnable) {
        Thread.Builder.OfVirtual builder = Thread.ofVirtual();
        builder = builder.name(name);
        return builder.unstarted(runnable);
    }

    public Thread unstarted(String name, Runnable runnable) {
        try {
            Object virtualBuilder = OF_VIRTUAL.invoke();
            log.info("Virtual builder invoke: {}", virtualBuilder);
            virtualBuilder = NAME.invoke(virtualBuilder, name);
            log.info("Virtual builder name: {}", virtualBuilder);
            return (Thread) UNSTARTED.invoke(virtualBuilder, runnable);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
