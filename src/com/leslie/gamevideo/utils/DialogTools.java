package com.leslie.gamevideo.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.leslie.gamevideo.AppConnect;
import com.leslie.gamevideo.R;
import com.leslie.gamevideo.activities.LogoActivity;
import com.leslie.gamevideo.activities.Main;

public class DialogTools {
	public static interface DialogOnClickListener {
		public abstract void onDialogClick(DialogInterface dialoginterface,
				int i, int j);
	}

	public static interface RatingListener {
		public abstract void afterRateVideo();
	}

	public static final int SOURCE_NEGATIVE = -7;
	public static final int SOURCE_NEUTRAL = -6;
	public static final int SOURCE_POSITIVE = -5;

	public DialogTools() {
	}

	// 设置网络方法
	public static Dialog buildNetworkErrorDialog(final Activity activity) {
		Builder b = new AlertDialog.Builder(activity).setTitle(
				"初始化失败，请检查网络是否正常")
				.setMessage("没有找到可用的网络，请确认WLAN、3G、2G至少一项可以使用");
		b.setCancelable(false);
		b.setIcon(R.drawable.icon_tang_new);
		b.setPositiveButton("网络设置", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				startWireLessSettings(activity);
			}
		}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
				Main main = (Main) activity;
				main.init();
			}
		});
		return b.create();
	}
	
	public static void startWireLessSettings(Activity activity){
		if(android.os.Build.VERSION.SDK_INT > 10 ){  
			activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS),0);  
		}else {  
			activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS),0);  
		} 
	}

	// 提示3g_2g网络
	public static Dialog build3G_2GDialog(final Activity activity) {
		Builder b = new AlertDialog.Builder(activity).setTitle("友好提示")
				.setMessage("您正在使用3G/2G网络访问应用，建议使用WLAN减少流量费用！");
		b.setIcon(R.drawable.icon_tang_new);
		b.setCancelable(false);
		b.setPositiveButton("继续使用", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Main main = (Main) activity;
				main.init();
				dialog.cancel();
			}
		}).setNeutralButton("切换网络", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				startWireLessSettings(activity);
				dialog.cancel();
			}
		}).setNegativeButton("退出应用", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				for (int i = 0; i < LogoActivity.activities.size(); i++) {
					if (null != LogoActivity.activities.get(i)) {
						LogoActivity.activities.get(i).finish();
					}
				}
				ActivityManager activityManager = (ActivityManager) activity
						.getSystemService(Context.ACTIVITY_SERVICE);
				activityManager.restartPackage("com.leslie.gamevideo");
				AppConnect.getInstance(activity).close();
				System.exit(0);
				dialog.cancel();
			}
		});
		return b.create();
	}

	public static Dialog buildDialog(Context context, String s, String s1) {
		Builder builder = new Builder(context);
		builder.setTitle(s);
		builder.setCancelable(false);
		builder.setOnKeyListener(new android.content.DialogInterface.OnKeyListener() {

			public boolean onKey(DialogInterface dialoginterface, int i,
					KeyEvent keyevent) {
				return true;
			}

		});
		builder.setPositiveButton(s1,
				new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialoginterface, int i) {
						dialoginterface.dismiss();
					}
				});
		return builder.create();
	}

	public static Dialog buildDisclaimerDialog(Context context,
			android.content.DialogInterface.OnClickListener onclicklistener,
			android.content.DialogInterface.OnClickListener onclicklistener1) {
		Builder builder = new Builder(context);
		builder.setTitle(0x7f080050);
		builder.setIcon(0x1080027);
		builder.setMessage(0x7f080051);
		builder.setPositiveButton(0x7f080052, onclicklistener);
		builder.setNegativeButton(0x7f080053, onclicklistener1);
		builder.setCancelable(false);
		builder.setOnKeyListener(new android.content.DialogInterface.OnKeyListener() {

			public boolean onKey(DialogInterface dialoginterface, int i,
					KeyEvent keyevent) {
				return true;
			}

		});
		return builder.create();
	}

	public static AlertDialog dialogCustom(Context context, String s, int i,
			View view, String s1, String s2, String s3,
			final DialogOnClickListener onClickListener) {
		Builder builder = new Builder(new ContextThemeWrapper(context,
				R.style.Dialog));
		builder.setView(view);
		builder.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialoginterface, int j,
					KeyEvent keyevent) {
				return true;
			}
		});
		if (s != null && s.trim().length() > 0)
			builder.setTitle(s);
		if (i > 0)
			builder.setIcon(i);
		if (s1 != null && s1.trim().length() > 0)
			builder.setPositiveButton(s1, new OnClickListener() {

				public void onClick(DialogInterface dialoginterface, int j) {
					if (onClickListener != null)
						onClickListener.onDialogClick(dialoginterface, j, -5);
				}

			});
		if (s2 != null && s2.trim().length() > 0)
			builder.setNeutralButton(s2, new OnClickListener() {

				public void onClick(DialogInterface dialoginterface, int j) {
					if (onClickListener != null)
						onClickListener.onDialogClick(dialoginterface, j, -6);
				}

			});
		if (s3 != null && s3.trim().length() > 0)
			builder.setNegativeButton(s3, new OnClickListener() {

				public void onClick(DialogInterface dialoginterface, int j) {
					if (onClickListener != null)
						onClickListener.onDialogClick(dialoginterface, j, -7);
				}

			});
		return builder.create();
	}

	public static AlertDialog getMessageDialog(Context context, String s,
			String s1, String s2, String s3, String s4,
			DialogOnClickListener dialogonclicklistener) {
		Builder builder = new Builder(context);
		builder.setMessage(s1);
		setButtons(s, builder, s2, s3, s4, dialogonclicklistener);
		return builder.create();
	}

	public static void safeShowDialog(Dialog dialog) {
		if (dialog != null) {
			dialog.show();
		}
	}

	private static void setButtons(String s, Builder builder, String s1,
			String s2, String s3, final DialogOnClickListener onClickListener) {
		if (s != null && s.trim().length() > 0)
			builder.setTitle(s);
		if (s1 != null && s1.trim().length() > 0)
			builder.setPositiveButton(s1, new OnClickListener() {

				public void onClick(DialogInterface dialoginterface, int i) {
					if (onClickListener != null)
						onClickListener.onDialogClick(dialoginterface, i, -5);
				}

			});
		if (s2 != null && s2.trim().length() > 0)
			builder.setNeutralButton(s2, new OnClickListener() {

				public void onClick(DialogInterface dialoginterface, int i) {
					if (onClickListener != null)
						onClickListener.onDialogClick(dialoginterface, i, -6);
				}

			});
		if (s3 != null && s3.trim().length() > 0)
			builder.setNegativeButton(s3, new OnClickListener() {

				public void onClick(DialogInterface dialoginterface, int i) {
					if (onClickListener != null)
						onClickListener.onDialogClick(dialoginterface, i, -7);
				}

			});
		builder.create().show();
	}

	public static void showToast(Context context, int i) {
		Toast.makeText(context, i, Toast.LENGTH_SHORT).show();
	}

	public static void showToast(Context context, CharSequence charsequence) {
		Toast.makeText(context, charsequence, Toast.LENGTH_SHORT).show();
	}

	public static void alertCommonDialog(Context context, String s) {
		android.app.AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(s);
		builder.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						dialoginterface.dismiss();
					}
				});
		Dialog dialog = builder.create();
		DialogTools.safeShowDialog(dialog);
	}

}
