package com.mylog00;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class ReflectiveVirtualThreadBuilder {

    private final MethodHandle OF_VIRTUAL;
    private final MethodHandle NAME;
    private final MethodHandle UNSTARTED;

    private Object virtualBuilder;

    public ReflectiveVirtualThreadBuilder() {
        try {
            var builderClass = Class.forName("java.lang.Thread$Builder$OfVirtual");
            var handles = MethodHandles.publicLookup();
            OF_VIRTUAL = handles.findStatic(Thread.class, "ofVirtual", MethodType.methodType(builderClass));
            NAME = handles.findVirtual(builderClass, "name", MethodType.methodType(builderClass, String.class));
            UNSTARTED = handles.findVirtual(builderClass, "unstarted", MethodType.methodType(Thread.class, Runnable.class));
            this.virtualBuilder = OF_VIRTUAL.invoke();
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public ReflectiveVirtualThreadBuilder name(String name) {
        try {
            this.virtualBuilder = NAME.invoke(virtualBuilder, name);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public Thread unstarted(Runnable runnable) {
        try {
            return (Thread) UNSTARTED.invoke(virtualBuilder, runnable);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
