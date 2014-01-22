package com.leslie.gamevideo.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BannerThreadPoolWrap {
	private static final int DEFAULT_COREPOOLSIZE = 5;
	private static final long DEFAULT_KEEP_ALIVE_TIME = 30L;
	private static final int DEFAULT_MAXIMUM_POOLSIZE = 5;
	private static BannerThreadPoolWrap instance;
	private LinkedBlockingQueue<Runnable> bq;
	private ThreadPoolExecutor executor;

	private BannerThreadPoolWrap() {
		executor = null;
		bq = new LinkedBlockingQueue<Runnable>();
		executor = new ThreadPoolExecutor(DEFAULT_COREPOOLSIZE,
				DEFAULT_MAXIMUM_POOLSIZE, DEFAULT_KEEP_ALIVE_TIME,
				TimeUnit.SECONDS, bq);
	}

	public static BannerThreadPoolWrap getThreadPool() {
		if (instance == null) {
			instance = new BannerThreadPoolWrap();
		}
		return instance;
	}

	public void executeTask(Runnable runnable) {
		executor.execute(runnable);
	}

	public void removeTask(Runnable runnable) {
		executor.remove(runnable);
	}

	public void shutdown() {
		executor.shutdown();
		instance = null;
	}

	public void showInfo() {

	}
}
