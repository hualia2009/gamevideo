package com.leslie.gamevideo.activities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leslie.gamevideo.AppConnect;
import com.leslie.gamevideo.R;
import com.leslie.gamevideo.mail.MailSenderInfo;
import com.leslie.gamevideo.mail.SimpleMailSender;
import com.leslie.gamevideo.thread.ThreadPoolWrap;
import com.leslie.gamevideo.utils.Utils;

/**
 * 我的糖豆-》反馈
 * 
 * @author huangl
 * 
 */
public class FeedBack extends BaseActivity {

	private Button sendMailBtn;
	private EditText userInfo;
	private EditText content;
	private String userInfoStr, contentStr;
	private String email;
	private TextView appName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_activity);
		sendMailBtn = (Button) findViewById(R.id.btnSubmit);
		userInfo = (EditText) findViewById(R.id.txtTelephone);
		content = (EditText) findViewById(R.id.txtContent);
		appName = (TextView) findViewById(R.id.appname);
		setTopMenu();
		Intent intent = getIntent();
		email = AppConnect.getInstance(this).getConfig("feedback_email",
				"412091963@qq.com");
		appName.setText("感谢您使用本客户端，如果您在使用过程中遇到任何不便或意见和建议都欢迎您反馈给我们。我们由衷的感谢，并将认真考虑您的意见和建议。");
		sendMailBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				userInfoStr = userInfo.getText().toString().trim();
				contentStr = content.getText().toString().trim();
				if (!Utils.checkNetworkAvalible(FeedBack.this)) {
					Toast.makeText(view.getContext(), "网络不可用，请检查网络设置！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (contentStr != null && contentStr != ""
						&& contentStr.length() > 0) {

					// if(!isNumeric(userInfoStr)&&!isEmail(userInfoStr)){
					// alerDialog("手机或邮箱格式不正确");
					// return;
					// }
					// if(isNumeric(userInfoStr)&&!isMobileNO(userInfoStr)){
					// alerDialog("手机或邮箱格式不正确");
					// return;
					// }
					ThreadPoolWrap.getThreadPool().executeTask(runnable);
					Toast.makeText(view.getContext(), "提交成功，感谢支持！",
							Toast.LENGTH_SHORT).show();
					userInfo.setText("");
					content.setText("");
				} else {
					Toast.makeText(view.getContext(), "请输入反馈内容",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private Runnable runnable = new Runnable() {

		public void run() {

			MailSenderInfo mailInfo = new MailSenderInfo();
			mailInfo.setMailServerHost("smtp.126.com");
			mailInfo.setMailServerPort("25");
			mailInfo.setValidate(true);
			mailInfo.setUserName("bokecctangdou@126.com");
			mailInfo.setPassword("bokecc09");
			mailInfo.setFromAddress("bokecctangdou@126.com");
			mailInfo.setToAddress(email);
			mailInfo.setSubject("用户使用游戏视频Android客户端反馈邮件");
			mailInfo.setContent("用户信息：" + userInfoStr + "<br>反馈内容："
					+ contentStr);
			SimpleMailSender.sendHtmlMail(mailInfo);
		}
	};

	// @Override
	// public void onBackPressed() {
	// super.onBackPressed();
	// finish();
	// }
	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);

		return m.matches();
	}

	public boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	private void alerDialog(String msg) {
		Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon_tang_new);
		Dialog dialog = builder.setTitle(msg)
				.setPositiveButton(R.string.ok, null).create();
		dialog.show();
	}

	@Override
	protected void setTitles() {
		title.setText("反馈");

	}
}
