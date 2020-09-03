package com.mao.overtime.io;

import java.util.*;
import java.io.*;
import android.os.*;
import com.mao.overtime.*;

public class ObjectIO <T>
{

	private File dir = new File(Environment.getExternalStorageDirectory(), "/overtime/");

	//序列化
	public void outObject(T t, String fname)
	{

		try
		{
			mkDir(dir, fname.substring(0, 4));

			if (null == t)
			{
				File f = new File(dir + File.separator + fname);
				f.delete();
			}
			else
			{
				FileOutputStream fos = new FileOutputStream(dir + File.separator + fname);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(t);
				oos.flush();
				oos.close();
			}
		}
		catch (Exception e)
		{
			MyLog.d("Out IOException");
		}

	}

	//反序列化
	public T inObject(String fname)
	{

		T t = null;

		try
		{
			FileInputStream fis = new FileInputStream(dir + File.separator + fname);
			ObjectInputStream ois = new ObjectInputStream(fis);
			t = (T) ois.readObject();
		}
		catch (ClassNotFoundException e)
		{
			//MyLog.d("Class Not Found");
		}
		catch (IOException e)
		{
			//MyLog.d(" In IOException");
		}

		return t;
	}

	//创建文件
	public void mkDir(File file, String dirname)
	{
		try 
		{
			File f = new File(file, dirname);

			if (!file.exists())
			{   
				file.mkdir();
			}

			if (!f.exists())
			{   
				f.mkdir();
			}
		}
		catch (Exception e)
		{}
	}
}
