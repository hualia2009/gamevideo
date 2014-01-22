package com.leslie.gamevideo.application;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

/**
 * 所有应用程序退出： 1.全局的application单例 2.activitylist用来存所有createed的Activity
 * 3.退出时，循环finish
 * @author huangl
 * 
 */
public class ExitApplication extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();
	private static ExitApplication instance;

	private ExitApplication() {
	}

	/**
	 * 单例模式中获取唯一的ExitApplication实例
	 * 
	 * @return
	 */
	public static ExitApplication getInstance() {
		if (null == instance) {
			instance = new ExitApplication();
		}
		return instance;

	}

	/**
	 * 添加Activity到容器中
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public int getSize() {
		return this.activityList.size();
	}

	/**
	 * 遍历所有Activity并finish
	 */
	public void exit() {

		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);

	}
}