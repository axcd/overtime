package com.mao.overtime.activity;

import android.app.*;
import android.os.*;
import com.mao.overtime.*;
import android.view.*;
import android.view.WindowManager.*;
import java.text.*;
import com.mao.overtime.config.*;
import com.mao.overtime.bean.*;
import com.mao.overtime.io.*;
import com.mao.overtime.enum.*;
import android.widget.*;
import android.widget.CompoundButton.*;
import android.icu.util.*;

public class UpdateActivity extends Activity
{
	private Shift shift = Shift.DAY;
	private Rate rate = Rate.ONE_AND_HALF;
	private Fake fake = Fake.NORMAL;
	private Hour hour = Hour.THREE;

	private ObjectIO<Month> io = new ObjectIO<Month>();

	private int d;
	private String m;
	private Month month;
	private String date;
	private int y =80;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);

		//从下面插入效果
		Display display = getWindowManager().getDefaultDisplay(); 
		Window window = getWindow();	
		LayoutParams windowLayoutParams = window.getAttributes(); 
		windowLayoutParams.width = (int) (display.getWidth() * 1.0); 
		windowLayoutParams.height = (int) (display.getHeight() * 0.65); 	
		window.setGravity(Gravity.BOTTOM);	
		window.setWindowAnimations(R.style.MyDialogAnimation);

		//获取选中的月份
		SimpleDateFormat msdf = new SimpleDateFormat("yyyy/MM/dd");
		date = msdf.format(Config.getSelectedDate());
		d =  Integer.parseInt(date.substring(8));
		m = date.substring(0, 7);
		if (m.equals(Config.getPreMonth().getIndex()))
		{
			month = Config.getPreMonth();
		}
		else
		{
			month = Config.getNextMonth();
		}

		RadioGroup shiftRadioGroup = (RadioGroup)findViewById(R.id.shiftRadioGroup);
		RadioGroup rateRadioGroup = (RadioGroup)findViewById(R.id.rateRadioGroup);
		RadioGroup fakeRadioGroup = (RadioGroup)findViewById(R.id.fakeRadioGroup);
		RadioGroup hourRadioGroup = (RadioGroup)findViewById(R.id.hourRadioGroup);
		
		final ScrollView hourScrollView = (ScrollView)findViewById(R.id.hourScrollView);
		hourScrollView.post(new Runnable(){
				public void run()
				{
					hourScrollView.scrollTo(0, getY());
				}
			});
			
		//增加监听
		shiftRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
				public void onCheckedChanged(RadioGroup p1, int p2)
				{
					RadioButton rb = (RadioButton) findViewById(p2);
					String str = rb.getText().toString();
					shift = Shift.get(str);
				}
			});

		rateRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
				public void onCheckedChanged(RadioGroup p1, int p2)
				{
					RadioButton rb = (RadioButton) findViewById(p2);
					String str = rb.getText().toString();
					rate = Rate.get(str);
				}
			});

		fakeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
				public void onCheckedChanged(RadioGroup p1, int p2)
				{
					RadioButton rb = (RadioButton) findViewById(p2);
					String str = rb.getText().toString();
					fake = Fake.get(str);
				}
			});

		hourRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
				public void onCheckedChanged(RadioGroup p1, int p2)
				{
					RadioButton rb = (RadioButton) findViewById(p2);
					String str = rb.getText().toString();
					hour = Hour.get(str);
				}
			});

		((RadioButton)shiftRadioGroup.getChildAt(0)).setChecked(true);
		((RadioButton)rateRadioGroup.getChildAt(0)).setChecked(true);
		((RadioButton)fakeRadioGroup.getChildAt(0)).setChecked(true);
		((RadioButton)hourRadioGroup.getChildAt(6)).setChecked(true);

		//周末设置双倍倍数
		if (Config.isWeekend())
		{
			((RadioButton)rateRadioGroup.getChildAt(1)).setChecked(true);
			((RadioButton)hourRadioGroup.getChildAt(22)).setChecked(true);
		}
		//回显加班信息
		if (null != month.getDay(d))
		{
			int n = shiftRadioGroup.getChildCount();
			for (int i=0;i < n;i++)
			{
				RadioButton rb = (RadioButton)shiftRadioGroup.getChildAt(i);
				if (rb.getText().toString().equals(month.getDay(d).getShift().getShiftName()))
				{
					rb.setChecked(true);
					break;
				}
			}

			n = rateRadioGroup.getChildCount();
			for (int i=0;i < n;i++)
			{
				RadioButton rb = (RadioButton)rateRadioGroup.getChildAt(i);
				if (rb.getText().toString().equals(month.getDay(d).getRate().getRateName()))
				{
					rb.setChecked(true);
					break;
				}
			}

			n = fakeRadioGroup.getChildCount();
			for (int i=0;i < n;i++)
			{
				RadioButton rb = (RadioButton)fakeRadioGroup.getChildAt(i);
				if (rb.getText().toString().equals(month.getDay(d).getFake().getFakeName()))
				{
					rb.setChecked(true);
					break;
				}
			}

			n = hourRadioGroup.getChildCount();
			for (int i=0;i < n;i++)
			{
				RadioButton rb = (RadioButton)hourRadioGroup.getChildAt(i);
				if (rb.getText().toString().equals(month.getDay(d).getHour().getHourName()))
				{
					rb.setChecked(true);
					setY((i/6)*60);
					break;
				}
			}
		}
		
		hourScrollView.post(new Runnable(){
				public void run()
				{
					hourScrollView.scrollTo(0, getY());
				}
			});
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public int getY()
	{
		return y;
	}

	public void onDelete(View view)
	{
		//View和记录置空
		Config.getSelectedView().setDay(null);
		month.setDay(d, null);

		//判断month是否为空
		int i;
		for (i = 0;i < month.getDays().length;i++)
		{
			if (month.getDay(i) != null)
			{
				break;
			}
		}

		//如果该月为空
		if (i == month.getDays().length)
		{
			month = null;
		}

		//保存
		io.outObject(month, m);

		finish();
	}

	public void onInsert(View view)
	{
		//添加加班
		Day day = new Day(date, shift, fake, rate, hour);
		Config.getSelectedView().setDay(day);
		month.setDay(d, day);
		io.outObject(month, m);
		finish();
	}

	@Override
	public void finish()
	{
		super.finish();
		overridePendingTransition(R.anim.dialog_exit, 0); 
	}

}
