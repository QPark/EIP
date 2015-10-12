package com.qpark.eip.core;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A {@link ThreadPoolExecutor} that logs at the start and stop the still to be
 * executed queue size.
 * 
 * @author bhausen
 */
public class ReportingThreadPoolExecutor extends ThreadPoolExecutor {
    /** The {@link Logger}. */
    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ReportingThreadPoolExecutor.class);

    /**
     * Create the thread pool like {@link Executors#newFixedThreadPool} does it
     * 
     * @param nThreads
     */
    public ReportingThreadPoolExecutor(final int nThreads) {
	super(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }

    /**
     * Create the thread pool like {@link Executors#newFixedThreadPool} does it
     * 
     * @param nThreads
     * @param threadFactory
     */
    public ReportingThreadPoolExecutor(final int nThreads, final ThreadFactory threadFactory) {
	super(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
    }

    /**
     * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread,
     *      java.lang.Runnable)
     */
    @Override
    protected void beforeExecute(final Thread t, final Runnable r) {
	this.logger.debug("Starting execute - {} threads, {} callables waiting", this.getCorePoolSize(),
		this.getQueue().size());
	super.beforeExecute(t, r);
    }

    /**
     * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable,
     *      java.lang.Throwable)
     */
    @Override
    protected void afterExecute(final Runnable r, final Throwable t) {
	super.afterExecute(r, t);
	if (t == null) {
	    this.logger.debug("Finished execute - {} threads, {} callables waiting", this.getCorePoolSize(),
		    this.getQueue().size());
	} else {
	    this.logger.warn("Finished execute with exception - {} threads, {} callables waiting: {}",
		    this.getCorePoolSize(), this.getQueue().size(), t.getMessage());
	}
    }
}
