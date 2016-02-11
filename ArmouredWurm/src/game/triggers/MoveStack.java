package game.triggers;
/*
 * This will be the move que.
 * This is going to keep track of the last 10 or so moves.
 * 
 * Thats right, this is for COMBOs!!!! AH YEAH!
 * 
 * This is part of the upcoming combat changes.
 * 
 * Idea:
 * 		have a render option to show the moves so the player can practice COMBOS! OHYEAH!
 *
 *	Dont worry about "removing" things this is more about holding the Last 10 items. i guess its not really a queue its more of a stack.
 *
 *
 *	Guide 
 *	0 - Blank?
 *	1 - jump
 *	2 = right
 *	3 = left
 *	4 = down?
 *	5 = up?
 */
public class MoveStack {

	private int stackLen;
	private int[] stackMain;
	private int stackP;
	public MoveStack() 
	{
		stackLen = 10;
		stackMain = new int[stackLen];
		for(int i = 0; i<stackLen;i++)
		{
			stackMain[i] = 0;
		}
				
	}
	public MoveStack(int len) 
	{
		stackLen = len;
		stackMain = new int[stackLen];
		for(int i = 0; i<stackLen;i++)
		{
			stackMain[i] = 0;
		}
				
	}
	public int[] getStack()
	{
		int[] out = new int[stackLen];
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
	public void add(int in)
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

		int[] test;
		for(int i =0;i<stackLen; i++)
		{
			test = this.getStack();
			System.out.println("["
								+ test[0]+"] ["
								+ test[1]+"] ["
								+ test[2]+"] ["
								+ test[3]+"] ["
								+ test[4]+"] ["
								+ test[5]+"] ["
								+ test[6]+"] ["
								+ test[7]+"] ["
								+ test[8]+"] ["
								+ test[9]+"] ");
		}
		
	}
	
}
