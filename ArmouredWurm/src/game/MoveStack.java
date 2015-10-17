package game;
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
 */
public class MoveStack {

	int stackLen;
	int[] stackMain;
	int stackP;
	public MoveStack() 
	{
		stackMain = new int[stackLen];
		
				
	}
	public int getStack()
	{
		//John you need to send out a reordered Queue here...Look into this.
		return 0;
	}
	public void Add()
	{
		//Add logic to add ontop of the Que
	}
	
}
