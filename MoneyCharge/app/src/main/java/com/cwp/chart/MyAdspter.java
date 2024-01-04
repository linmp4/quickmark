package com.cwp.chart;

import java.util.List;
import java.util.Map;

import com.cwp.cmoneycharge.R;

import android.R.bool;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyAdspter extends BaseAdapter {

	private List<Map<String, Object>> data;
	private LayoutInflater layoutInflater;
	private Context context;
	private Boolean time;
	private String textaddress;

	public MyAdspter(Context context, List<Map<String, Object>> data,
			Boolean time) {
		this.context = context;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(context);
		this.time = time;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	/**
	 * 获得某一位置的数据
	 */
	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	/**
	 * 获得唯一标识
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 组件集合，对应list.xml中的控件
	 * 
	 * @author Administrator
	 */
	public final class Zujian {
		public ImageView image;
		public TextView no;
		public TextView info;
		public TextView title;
		public TextView kind;
		public TextView money;
		public TextView address;
		public RelativeLayout titlebar;
		public TextView search_date;
		public TextView search_date2;
		public RelativeLayout search_img2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Zujian zujian = null;
		if (convertView == null) {
			zujian = new Zujian();
			// 获得组件，实例化组件
			convertView = layoutInflater.inflate(R.layout.list, null);
			zujian.image = (ImageView) convertView
					.findViewById(R.id.search_img);
			zujian.no = (TextView) convertView.findViewById(R.id.no);
			zujian.info = (TextView) convertView.findViewById(R.id.info);
			zujian.title = (TextView) convertView.findViewById(R.id.title);
			zujian.kind = (TextView) convertView.findViewById(R.id.kind);
			zujian.money = (TextView) convertView.findViewById(R.id.money);
			zujian.address = (TextView) convertView.findViewById(R.id.address);
			zujian.search_date = (TextView) convertView
					.findViewById(R.id.search_date);
			zujian.search_date2 = (TextView) convertView
					.findViewById(R.id.search_date2);
			zujian.search_img2 = (RelativeLayout) convertView
					.findViewById(R.id.search_img2);
			zujian.titlebar = (RelativeLayout) convertView
					.findViewById(R.id.titlebar);
			convertView.setTag(zujian);
		} else {
			zujian = (Zujian) convertView.getTag();
		}
		// 绑定数据
		zujian.image.setBackgroundResource((Integer) data.get(position).get(
				"img"));
		zujian.no.setText((String) data.get(position).get("no"));
		zujian.title.setText((String) data.get(position).get("title"));
		zujian.kind.setText((String) data.get(position).get("kind"));
		zujian.money.setText((String) data.get(position).get("money"));
		if (data.get(position).get("date") != null) {
			zujian.search_img2.setVisibility(View.VISIBLE);
			zujian.search_date
					.setText(((String) data.get(position).get("info"))
							.substring(8, 10));
			zujian.search_date2
					.setText((String) data.get(position).get("date"));
		}

		if (data.get(position).get("kind").toString().equals("[收入]")) {
			zujian.money.setTextColor(Color.parseColor("#ffff0000"));
		} else {
			zujian.money.setTextColor(Color.parseColor("#5ea98d"));
		}
		if (time) {
			zujian.titlebar.setVisibility(View.VISIBLE);
			zujian.info.setText((String) data.get(position).get("info"));
			textaddress = (String) data.get(position).get("address");
			if (textaddress.indexOf("[") > -1) {
				zujian.address.setText(textaddress.substring(0,
						textaddress.indexOf("[")));
			} else {
				zujian.address.setText(textaddress);
			}
		} else {
			zujian.titlebar.setVisibility(View.GONE);
		}
		return convertView;
	}

}
