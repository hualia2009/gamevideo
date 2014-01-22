
package com.leslie.gamevideo.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leslie.gamevideo.R;

public class HotKeyWordsAdapter extends BaseAdapter
{
	private static class ViewHolder
	{
		TextView textView;
		private ViewHolder()
		{
		}
	}

	private Context context;
	@SuppressWarnings("rawtypes")
	private ArrayList mlist;

	@SuppressWarnings("rawtypes")
	public HotKeyWordsAdapter(ArrayList arraylist, Context context1)
	{
		mlist = arraylist;
		context = context1;
	}

	public int getCount()
	{
		int i;
		if (mlist != null)
			i = mlist.size();
		else
			i = 0;
		return i;
	}

	public Object getItem(int i)
	{
		Object obj;
		if (mlist == null || mlist.size() <= i)
			obj = null;
		else
			obj = mlist.get(i);
		return obj;
	}

	public long getItemId(int i)
	{
		return (long)i;
	}

	public View getView(int i, View view, ViewGroup viewgroup)
	{
		ViewHolder viewholder;
		if (view != null && view.getTag() != null)
		{
			viewholder = (ViewHolder)view.getTag();
		} else
		{
			viewholder = new ViewHolder();
			view = ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(R.layout.hotkey_item, null);
			viewholder.textView = (TextView)view.findViewById(R.id.hotkey_textView);
			view.setTag(viewholder);
		}
		if (mlist != null && mlist.size() > i)
			viewholder.textView.setText((CharSequence)mlist.get(i));
		return view;
	}
}