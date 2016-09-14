package game.triggers;
/*
 * This will be the move stack.
 * This is going to keep track of the last 10 or so moves.
 * 
 * Thats right, this is for COMBOs!!!! AH YEAH!
 * 
 * This is part of the upcoming combat changes.
 * 
 * Idea:
 * 		have a render option to show the moves so the player can practice COMBOS! OHYEAH!
 *
 *	Dont worry about "removing" things this is more about holding the Last 10 items. its a  of a stack.
 *
 *
 *	Guide 
	L = light attack
	H = Hevay attack
	S = Special
	
	jump? moveleft moveright?
	hmmmmmm
	
	
 */
public class MoveStack {

	private int stackLen;
	private char[] stackMain;
	private int stackP;
	public MoveStack() 
	{
		 this(10); 		
	}
	public MoveStack(int len) 
	{
		stackLen = len;
		stackMain = new char[stackLen];
		for(int i = 0; i<stackLen;i++)
		{
			stackMain[i] = 0;
		}
				
	}
	public char[] getStack()
	{
		char[] out = new char[stackLen];
		for(int i= 0;i<stackLen; i++)
		{
				//John think about this.
			if(i+stackP<stackLen)
			{
				out[i]  = stackMain[i+stackP];
			}
			else
			{
				out[i] = stackMain[i+stackP - stackLen];
			}
		}
		return out;
	}
	public void add(char in)
	{
		if(stackP>0)
		{
			stackP--;
			
		}
		else
		{
			stackP= stackLen -1;
		}
		stackMain[stackP] = in;
	}
	public void test()
	{
		for(int i =0;i<stackLen; i++)
		{
			System.out.print("[" + this.getStack()[i] +"]");
			
		}
		System.out.println();
	}
	
}
