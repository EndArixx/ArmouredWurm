package game;

import java.io.BufferedReader;
import java.io.IOException;

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
	
	//This is my all new Advanced Readline
	//This ignores any line which starts with a #
	public static String readlineadv(BufferedReader br) throws IOException
	{
		String out = br.readLine();
		while(out.charAt(0) == '#')
		{
			out = br.readLine();
		}
		return out;
	}
}
