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

}
