package com.mao.overtime;

import android.app.*;
import android.os.*;
import android.widget.*;
import java.util.*;
import android.view.*;
import android.content.Context;
import com.mao.overtime.view.*;
import java.text.*;
import android.graphics.*;
import com.mao.overtime.config.*;
import android.content.*;
import com.mao.overtime.bean.*;
import android.content.pm.*;
import android.support.v4.app.*;
import android.*;
import com.mao.overtime.activity.*;
import com.mao.overtime.util.*;

public class MainActivity extends Activity 
{
	private GridView gv;
	private Calendar cal;

	static TextView mtv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		
		PermisssionUtil.requestPermission(this);
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		mtv = (TextView)findViewById(R.id.mainTextView1);
		gv = (GridView)findViewById(R.id.mainGridView);

		cal = Calendar.getInstance();
		Config.setToday(cal.getTime());

		//如果大于开始日期显示在下一月
		if (cal.get(Calendar.DATE) > Config.getStartDay() && Config.getStartDay() != 1)
		{
			cal.add(Calendar.MONTH, 1);
		}

		//获取View
		getCalendarView(cal);
    }

	//上一月
	public void preMonth(View view)
	{
		cal.add(Calendar.MONTH, -1);
		getCalendarView(cal);
	}

	//下一月
	public void nextMonth(View view)
	{
		cal.add(Calendar.MONTH, 1);
		getCalendarView(cal);
	}

	public void getCalendarView(Calendar cal)
	{

		//设置显示年月		
		Calendar calendar = (Calendar)cal.clone();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		TextView tv = (TextView)findViewById(R.id.yyyyMM);
		tv.setText(sdf.format(calendar.getTime()));

		//添加canlendar配置
		Config.set(cal);

		//设置开始日期
		if (Config.getStartDay() != 1)
		{
			calendar.add(Calendar.MONTH, -1);
		}
		calendar.set(Calendar.DATE, Config.getStartDay());

		//获取List<Date>
		List<Date> dates = new ArrayList<Date>();
		int w = calendar.get(Calendar.DAY_OF_WEEK);
		int n = calendar.getActualMaximum(Calendar.DATE);
		int m = (int)Math.ceil(1.0 * (n + w - 1) / 7) * 7;
		calendar.add(Calendar.DATE, 1 - w);	
		while (dates.size() < m)
		{ 
			dates.add(calendar.getTime());
			calendar.add(Calendar.DATE, 1);
		}

		//设置适配器
        ListAdapter adapter = new DayAdapter(this, dates);
		gv.setAdapter(adapter);
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu)
//	{
//		// TODO: Implement this method
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.main_menu, menu);
//
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onMenuItemSelected(int featureId, MenuItem item)
//	{
//		Intent intent = new Intent();
//		// TODO: Implement this method
//		switch (item.getItemId())
//		{
//
//			case R.id.count:
//
//				return true;
//
//			case R.id.set:
//
//				return true;
//		}
//		return super.onMenuItemSelected(featureId, item);
//	}
	
}

class DayAdapter extends ArrayAdapter<Date>
{
	public DayAdapter(Context context, List<Date> values) 
	{
		super(context, R.layout.day_view, values);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.day_view, parent, false);

		final Date date = getItem(position);

//		MainActivity. mtv.setText("s-> " + Config.getStartDate()  + "\ne-> " + Config.getEndDate());
//		MainActivity.mtv.setVisibility(0);

		//设置View的文本
		final DayView textView = (DayView) view.findViewById(R.id.entryTextView1);
		textView.setText(date.getDate() + "");

		//在范围之内日期设置
		if (date.after(Config.getStartDate()) && date.before(Config.getEndDate()))
		{
			textView.setBackgroundColor(Color.parseColor("#FFFDDFEE"));
			
			//是否绘制
			textView.setDraw(true);

			//标记今天
			if (date.equals(Config.getToday()))
			{
				textView.setToday(true);
			}

			//标记选中
			if (date.equals(Config.getSelectedDate()))
			{
				textView.setSelected(true);
				Config.setSelectedView(textView);
			}

			//绑定数组数据
			SimpleDateFormat sdfm = new SimpleDateFormat("yyyy/MM");
			SimpleDateFormat sdfd = new SimpleDateFormat("dd");
			int i = Integer.parseInt(sdfd.format(date));

			if (Config.getPreMonth().getIndex().equals(sdfm.format(date)))
			{
				if (null != Config.getPreMonth().getDay(i))
				{
					textView.setDay((Config.getPreMonth().getDay(i)));
				}
			}

			if (Config.getNextMonth().getIndex().equals(sdfm.format(date)))
			{
				if (null != Config.getNextMonth().getDay(i))
				{
					textView.setDay((Config.getNextMonth().getDay(i)));
				}
			}

			//设置选中单击事件
			textView.setOnClickListener(new View.OnClickListener(){
					public void onClick(View view)
					{
						if (null != Config.getSelectedView())
						{
							Config.getSelectedView().setSelected(false);
						}

						Config.setSelectedDate(date);
						Config.setSelectedView(textView);
						textView.setSelected(true);
					}
				});

			//添加长按view事件
			textView.setOnLongClickListener(new View.OnLongClickListener(){
					public boolean onLongClick(View view)
					{

						if (null != Config.getSelectedView())
						{
							Config.getSelectedView().setSelected(false);
						}

						Config.setSelectedDate(date);
						Config.setSelectedView(textView);
						textView.setSelected(true);
						

						//设置周末
						if(position%7==0 || position%7==6)
						{
							Config.setWeekend(true);
						}else
						{
							Config.setWeekend(false);
						}
						
						//这儿写长按逻辑
						Intent intent = new Intent();
						intent.setClass(getContext(), UpdateActivity.class);
						getContext().startActivity(intent);
						return true;
					}
				});
		}
		else
		{
			//不在范围之内的View，取消父类点击效果
			textView.setOnClickListener(new View.OnClickListener(){ public void onClick(View view){} });
		}

		return view;
	}
}
