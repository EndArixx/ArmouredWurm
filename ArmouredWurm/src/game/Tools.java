package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Tools 
{
	
	private static int grace = 0;
	public static int deadZoneX()
	{
		return -500;
	}
	public static int deadZoneY()
	{
		return -500;
	}
	public static boolean check_collision(int[] hitboxA, int[] hitboxB)
	{
		return check_collision(hitboxA[0],hitboxA[1],hitboxA[2],hitboxA[3],hitboxB[0],hitboxB[1],hitboxB[2],hitboxB[3]);
	}
	public static boolean check_collision
			(int firstX, 
			int firstY, 
			int firstwid,
			int firsthig,
			int secondX,
			int secondY,
			int secondwid,
			int secondhig)
	{
				//grace is a small amount of over lap the the program allows
				//make each box smaller by the amount of grace set above
			firstX+=grace ; 
			firstY+=grace ;
			firstwid-=grace ;
			firsthig-=grace ;
			secondX+=grace ;
			secondY+=grace ;
			secondwid-=grace ;
			secondhig-=grace ;
			
				//set a bool for X overlap and Y overlap
			boolean x= false ,y = false;
			
				/*
				  the logic here is very simple 
					if one of one of the boxes walls is between BOTH the other boxes walls 
					then is overlaps on that axis
					
					if they overlap on both X and Y then you have a collision
				*/
			if ((firstX+firstwid) >= (secondX) && firstX <= secondX)
			{
				x = true;
			}
			if (firstX <= secondX + secondwid && firstX >= secondX)
			{
				x = true;
			}
			if ((firstY+firsthig) >= (secondY) && firstY <= secondY)
			{
				y = true;
			}
			if (firstY <= secondY + secondhig && firstY >= secondY)
			{
				y = true;
			}
				//DEAD!
			if (x && y)
			{
					return true;
			}
				//SAFE!
			return false;
	}
	
	//John look into this
	public static int BooleansToInt(boolean[] inBool)
	{
		int out = 0;
		for(int i = 0 ; i < inBool.length; i++)
		{
			if(inBool[inBool.length- i-1])
			{
				int nth = 1;
				for (int j = 0; j < i; j++)
				{
					nth = nth * 2;
				}
				out += nth;
			}
		}
		return out;
	}
	
	//John look into this.
	public static boolean[] IntToBoolean(int inInt)
	{
		    boolean[] out = new boolean[8];
		    for (int i = 0; i < 8; i++) {
		        out[8 - 1 - i] = (1 << i & inInt) != 0;
		    }
		    return out;
	}
	
	//This is my all new Advanced Readline
	//This ignores any line which starts with a #
	public static String readlineadv(BufferedReader br)
	{
		try // john look into this
		{
			String out = br.readLine();
			if(out == null)
				return null;
			out = out.trim();
			while(out.charAt(0) == '#')
			{
				out = br.readLine();
			}
			return out;
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	
	int type = 0;
	FileReader fr = null;
	BufferedReader br = null;
	InputStream is = null;
	InputStreamReader isr = null;
	
	//files opening logic
	public BufferedReader getBR(String file)
	{
		try
		{
			if(new File(file).isFile())
	    	{
				type = 1;
	    		fr = new FileReader(file);
	    		br = new BufferedReader(fr);
	    	}
	    	else
	    	{
	    		type = 2;
	    		is = getClass().getResourceAsStream("/"+file);
	    		isr = new InputStreamReader(is);
	    		br = new BufferedReader(isr);
	    	}
		}
		catch (Exception e) 
		{
			type = -1;
			System.out.println("Error opening: "+file);
			return null;
		}
		return br;
	}
	public void closeBR()
	{
		try
		{
		br.close();
        if(type ==1)
        {
        	fr.close();
        }
        else if(type == 2)
        {
        	is.close();
    		isr.close();
        }
		}
		catch (Exception e) 
		{
			type = -1;
			System.out.println("Error");
		}
	}
	
}
