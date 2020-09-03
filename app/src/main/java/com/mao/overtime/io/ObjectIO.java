package com.mao.overtime.io;

import java.util.*;
import java.io.*;
import android.os.*;
import com.mao.overtime.*;

public class ObjectIO <T>
{

	private File root = new File(Environment.getExternalStorageDirectory(), "/overtime/");

	//序列化
	public void outObject(T t, String fname)
	{

		try
		{
			mkDir();
			File dir  = new File(root, fname.split("/")[0]);

			if (fname.contains("/"))
			{
				if (null == t)
				{
					File f = new File(root + File.separator + fname);
					if (f.exists())
					{
						f.delete();
					}

					File d = new File(root, fname.split("/")[0]);
					if (d.exists() && d.isDirectory() && d.listFiles().length == 0)
					{
						d.delete();
					}
				}
				else
				{
					if (!dir.exists())
					{   
						dir.mkdir();
					}
					
					FileOutputStream fos = new FileOutputStream(root + File.separator + fname);
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(t);
					oos.flush();
					oos.close();
				}
			}
			else
			{
				FileOutputStream fos = new FileOutputStream(root + File.separator + fname);
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
			FileInputStream fis = new FileInputStream(root + File.separator + fname);
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
	public void mkDir()
	{
		try 
		{
			if (!root.exists())
			{   
				root.mkdir();
			}
		}
		catch (Exception e)
		{}
	}
}
