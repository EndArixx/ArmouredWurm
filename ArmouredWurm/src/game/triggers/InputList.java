package game.triggers;

import game.Tools;

public class InputList 
{
	public boolean N;
	public boolean S;
	public boolean E;
	public boolean W;
		//attack
	public boolean AL; //Light
	public boolean AH; //Medium
	public boolean AS; //Special
	
		//John here!
	public MoveStack moveStack;
	
	public void setBooleans(int input)
	{
		boolean[] list = Tools.IntToBoolean(input);
		N = list[0];
		S = list[0];
		E = list[0];
		W = list[0];
		AL = list[0];
		AH = list[0];
		AS = list[0];
	}
	public InputList() 
	{
		N = false;
		S = false;
		E = false;
		W = false;
		
		AL = false;
		AH = false;
		AS = false;
	}
	public boolean[] getList()
	{
		//John fix this
		boolean[] out = new boolean[7];
		out[0] = N;
		out[1] = S;
		out[2] = E;
		out[3] = W;
		out[4] = AL;
		out[5] = AH;
		out[6] = AS;
		return out;
	}
	public String getOn()
	{
		//John you are going to have to think of a system where order isnt important
		String out = "";
		if(N)
			out += "N";
		if(S)
			out += "S";
		if(E)
			out += "E";
		if(W)
			out += "W";
		if(AL)
			out += "AL";
		if(AH)
			out += "AH";
		if(AS)
			out += "AS";
		return out;
	}
	public String toString()
	{
		String out = "";
		if(N)
			out += "1";
		else
			out += "0";
		if(S)
			out += "1";
		else
			out += "0";
		if(E)
			out += "1";
		else
			out += "0";
		if(W)
			out += "1";
		else
			out += "0";
		if(AL)
			out += "1";
		else
			out += "0";
		if(AH)
			out += "1";
		else
			out += "0";
		if(AS)
			out += "1";
		else
			out += "0";
		
		return out;
	}
}
