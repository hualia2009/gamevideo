package com.leslie.gamevideo.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageThreadPoolWrap {
	private static final int DEFAULT_COREPOOLSIZE = 10;
	private static final long DEFAULT_KEEP_ALIVE_TIME = 30L;
	private static final int DEFAULT_MAXIMUM_POOLSIZE = 30;
	private static ImageThreadPoolWrap instance;
	private LinkedBlockingQueue<Runnable> bq;
	private ThreadPoolExecutor executor;

	private ImageThreadPoolWrap() {
		executor = null;
		bq = new LinkedBlockingQueue<Runnable>();
		executor = new ThreadPoolExecutor(DEFAULT_COREPOOLSIZE,
				DEFAULT_MAXIMUM_POOLSIZE, DEFAULT_KEEP_ALIVE_TIME,
				TimeUnit.SECONDS, bq);
	}

	public static ImageThreadPoolWrap getThreadPool() {
		if (instance == null) {
			instance = new ImageThreadPoolWrap();
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
