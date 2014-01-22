package com.leslie.gamevideo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images;
import android.util.Log;

import com.leslie.gamevideo.thread.ImageThreadPoolWrap;

public class AsyncImageLoader {

	private static ConcurrentHashMap<String, SoftReference<Bitmap>> imageCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>();
	private static int NUM = 200;
	private static boolean isDeling = false;

	public AsyncImageLoader() {
	}

	public static Bitmap loadDrawable(final String imageUrl,
			final ImageCallback imageCallback) {
		synchronized (imageCache) {
			if (imageCache.containsKey(imageUrl)) {
				Bitmap bitmap = imageCache.get(imageUrl).get();
				if (bitmap != null) {
					return bitmap;
				}
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Bitmap) message.obj, imageUrl);
			}
		};
		ImageThreadPoolWrap.getThreadPool().executeTask(new Runnable() {
			@SuppressLint("NewApi")
			public void run() {
				Bitmap bitmap = null;
				if (imageUrl.contains("http")) {
					bitmap = loadBitmapFromUrl(imageUrl);
				} else {
					bitmap = ThumbnailUtils.createVideoThumbnail(imageUrl,
							Images.Thumbnails.MINI_KIND);
				}
				AsyncImageLoader.putDrawable(imageUrl, bitmap);
				Message message = handler.obtainMessage(0, bitmap);
				handler.sendMessage(message);
			}
		});
		ImageThreadPoolWrap.getThreadPool().showInfo();
		return null;
	}

	public static void putDrawable(String imageUrl, Bitmap bitmap) {
		synchronized (imageCache) {
			imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
		}
	}

	public static Drawable loadImageFromUrl(String url) {
		boolean isRetry = true;
		URL m;
		InputStream i = null;
		Drawable d = null;
		while (isRetry) {
			try {
				m = new URL(url);
				HttpURLConnection hc = (HttpURLConnection) m.openConnection();
				hc.setDoInput(true);
				hc.setConnectTimeout(5000);
				hc.setRequestMethod("GET");
				hc.connect();
				if (hc.getResponseCode() == HttpURLConnection.HTTP_OK) {
					i = hc.getInputStream();
				} else {
					hc.disconnect();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				isRetry = false;
			} catch (IOException e) {
				e.printStackTrace();
				isRetry = false;
			}
			d = Drawable.createFromStream(i, "src");
			if (d != null) {
				isRetry = false;
			}
		}
		return d;
	}

	public static Bitmap loadBitmapFromUrl(String url) {
		URL m;
		boolean isRetry = true;
		InputStream in = null;
		ByteArrayInputStream bais = null;
		Bitmap bitmap = null;
		while (isRetry) {
			try {
				m = new URL(url);
				HttpURLConnection hc = (HttpURLConnection) m.openConnection();
				hc.setDoInput(true);
				hc.setConnectTimeout(5000);
				hc.setRequestMethod("GET");
				hc.connect();
				if (hc.getResponseCode() == HttpURLConnection.HTTP_OK) {
					in = hc.getInputStream();
				} else {
					hc.disconnect();
				}
				if (in == null) {
					break;
				}
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int byteread = 0;
				while ((byteread = in.read(buffer)) != -1) {
					out.write(buffer, 0, byteread);
				}
				bais = new ByteArrayInputStream(out.toByteArray());
				in.close();
				out.close();
				bitmap = Utils.readBitMap(bais);
				if (bitmap != null) {
					isRetry = false;
				}
				bais.close();
			} catch (MalformedURLException e1) {
				isRetry = false;
				e1.printStackTrace();
			} catch (IOException e) {
				isRetry = false;
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	public interface ImageCallback {
		public void imageLoaded(Bitmap bitmap, String imageUrl);
	}

	private static class Police extends Thread {
		public void run() {
			if (AsyncImageLoader.imageCache == null
					|| AsyncImageLoader.imageCache.size() <= AsyncImageLoader.NUM) {
				AsyncImageLoader.isDeling = false;
				return;
			} else {
				Enumeration<String> enumeration = AsyncImageLoader.imageCache
						.keys();
				while (enumeration.hasMoreElements()) {
					String s = (String) enumeration.nextElement();
					if (s != null && AsyncImageLoader.imageCache.containsKey(s)) {
						SoftReference<Bitmap> softreference = (SoftReference<Bitmap>) AsyncImageLoader.imageCache
								.get(s);
						if (softreference != null
								&& softreference.get() == null) {
							Log.i("AsyncImageLoader",
									"remove() is execute————————");
							AsyncImageLoader.imageCache.remove(s);
						}
					}
				}
				AsyncImageLoader.isDeling = false;
				return;
			}
		}
	}

	public static void delKey() {
		if (imageCache != null && imageCache.size() > NUM && !isDeling) {
			Log.i("AsyncImageLoader", "delKey() is execute————————");
			isDeling = true;
			ImageThreadPoolWrap.getThreadPool().executeTask(new Police());
		}
	}
}