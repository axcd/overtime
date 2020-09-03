package com.mao.overtime.config;

import java.util.*;
import com.mao.overtime.bean.*;
import com.mao.overtime.view.*;
import com.mao.overtime.enum.*;
import com.mao.overtime.io.*;
import java.text.*;
import com.mao.overtime.*;

public class Config
{
	private static int startDay = 24;
	private static Date today;
	private static Date selectedDate;
	private static Date startDate;
	private static Date endDate;
	private static Month preMonth;
	private static Month nextMonth;
	private static DayView selectedView;
	
	//测试数据
	public static Month getMonth()
	{
		Month month = new Month("2020/10");
		Day day = null;

		day = new Day("2020-10-01", Shift.DAY, Fake.LEAVE, Rate.ONE_AND_HALF, Hour.THREE);month.setDay(1,day);
		day = new Day("2020-10-02", Shift.DAY, Fake.NORMAL, Rate.ONE_AND_HALF, Hour.THREE);month.setDay(2,day);
		day = new Day("2020-10-03", Shift.DAY, Fake.NORMAL, Rate.THREE, Hour.THREE);month.setDay(3,day);
		day = new Day("2020-10-04", Shift.MIDDLE, Fake.LEAVE, Rate.ONE_AND_HALF, Hour.THREE);month.setDay(4,day);
		day = new Day("2020-10-05", Shift.MIDDLE, Fake.TAKEOFF, Rate.ONE_AND_HALF, Hour.ELEVEN_AND_HALF);month.setDay(5,day);
		day = new Day("2020-10-06", Shift.MIDDLE, Fake.NORMAL, Rate.ONE_AND_HALF, Hour.THREE);month.setDay(6,day);
		day = new Day("2020-10-07", Shift.NIGHT, Fake.SICK, Rate.ONE_AND_HALF, Hour.THREE);month.setDay(7,day);

		return month;
	}
	
	//设置Config
	public static void set(Calendar calendar)
	{
		setStartDate(calendar);
		setEndDate(calendar);

		Calendar cal = (Calendar)calendar.clone();
		ObjectIO<Month> io = new ObjectIO<Month>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");

		String nMonth = sdf.format(cal.getTime());
		setNextMonth(io.inObject(nMonth));
		if(null == getNextMonth())
		{
			setNextMonth(new Month(nMonth));
		}

		cal.add(Calendar.MONTH,-1);
		String pMonth = sdf.format(cal.getTime());    
		setPreMonth(io.inObject(pMonth));
		if(null == getPreMonth())
		{
			setPreMonth(new Month(pMonth));
		}
	}
	
	//设置开始日期
	public static void setStartDate(Calendar calendar)
	{
		Calendar start_cal = (Calendar)calendar.clone();
	    if(Config.getStartDay()!=1)start_cal.add(Calendar.MONTH, -1);
		int maxDay = start_cal.getActualMaximum(Calendar.DATE);
		
		if(maxDay<startDay){
			start_cal.set(Calendar.DATE, maxDay);
		}else{
			start_cal.set(Calendar.DATE, startDay-1);
		}
		Config.startDate = start_cal.getTime();
	}

	public static Date getStartDate()
	{
		return startDate;
	}

	//设置结束日期
	public static void setEndDate(Calendar calendar)
	{
		Calendar end_cal = (Calendar)calendar.clone();
	    if(Config.getStartDay()==1)end_cal.add(Calendar.MONTH,1);
		int maxDay = end_cal.getActualMaximum(Calendar.DATE);
		
		if(maxDay<startDay){
			end_cal.set(Calendar.DATE,maxDay+1);
		}else{
			end_cal.set(Calendar.DATE, startDay);
		}
		Config.endDate = end_cal.getTime();
	}

	public static Date getEndDate()
	{
		return endDate;
	}

	public static void setSelectedDate(Date selectedDate)
	{
		Config.selectedDate = selectedDate;
	}

	public static Date getSelectedDate()
	{
		return selectedDate;
	}

	public static void setSelectedView(DayView selectedView)
	{
		Config.selectedView = selectedView;
	}

	public static DayView getSelectedView()
	{
		return selectedView;
	}

	public static void setToday(Date today)
	{
		Config.today = today;
	}

	public static Date getToday()
	{
		return today;
	}

	public static void setStartDay(int startDay)
	{
		Config.startDay = startDay;
	}

	public static int getStartDay()
	{
		return startDay;
	}

	public static void setPreMonth(Month preMonth)
	{
		Config.preMonth = preMonth;
	}

	public static Month getPreMonth()
	{
		return preMonth;
	}

	public static void setNextMonth(Month nextMonth)
	{
		Config.nextMonth = nextMonth;
	}

	public static Month getNextMonth()
	{
		return nextMonth;
	}
	
}
