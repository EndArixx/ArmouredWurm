package game.triggers;

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
