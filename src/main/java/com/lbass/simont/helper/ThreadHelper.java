package com.lbass.simont.helper;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ThreadHelper {
    public static ScheduledExecutorService getScheduledExecutorService(String namingPattern, int workerSize) {
        BasicThreadFactory factory =
                new BasicThreadFactory.Builder().
                        namingPattern(namingPattern).
                        daemon(true).
                        priority(Thread.MAX_PRIORITY).build();
       return Executors.newScheduledThreadPool(workerSize, factory);
    }
}
