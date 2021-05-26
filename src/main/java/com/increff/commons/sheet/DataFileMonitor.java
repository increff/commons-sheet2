package com.increff.commons.sheet;

import org.apache.log4j.Logger;

public class DataFileMonitor implements IProgressMonitor {

    private static final int MB = 1024 * 1024;

    private static Logger logger = Logger.getLogger(DataFileMonitor.class);
    private static final int BATCH = 1_000_000;

    @Override
    public void process(int count) {
        if (count % BATCH != 0) {
            return;
        }
        logger.info("Lines: " + count + ", " + getMemoryString());
    }

    private static String getMemoryString() {
        // Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();
        long m = runtime.maxMemory() / MB;
        long t = runtime.totalMemory() / MB;
        long u = (runtime.totalMemory() - runtime.freeMemory()) / MB;
        long f = runtime.freeMemory() / MB;
        // Print used memory
        return "Memory[MB] Max:" + m + ", Total:" + t + ", Used:" + u + ", Free:" + f;
    }
}
