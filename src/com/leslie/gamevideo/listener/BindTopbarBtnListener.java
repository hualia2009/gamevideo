
package com.leslie.gamevideo.listener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import com.leslie.gamevideo.R;
import com.leslie.gamevideo.activities.History;

public class BindTopbarBtnListener
{
	public static ImageButton imgbtnSearch;
	public static ImageButton imgbtnHistory;

	public BindTopbarBtnListener()
	{
	}

	public static void bindAllBtnListener(final Activity activity)
	{
		imgbtnHistory = (ImageButton)activity.findViewById(R.id.imgbtnHistory);
		imgbtnHistory.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View view)
			{
				Intent intent = new Intent(activity,History.class);
				view.getContext().startActivity(intent);
			}

		});
		
	}
}
