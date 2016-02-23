package game.triggers;

public class State 
{
/*
 What does a state need?
 
  animation data?
  
  movement data?
  
  This might be better called 'state change'
  
  //Do i even need this class???
 
 */
	private char[] name;
	private boolean on;
	public State(char[] inName) 
	{
		this.name = inName;
		this.on = false;
	}
	
	public void change()
	{
		//This will return a new state?
		//So 
	}
}
