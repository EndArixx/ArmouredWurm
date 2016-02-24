package game.triggers;

public class State 
{
/*
This is nothing more then a boolean and a name 

 */
	private char[] name;
	private boolean on;
	
	public State(char[] inName) 
	{
		this.name = inName;
		this.on = false;
	}
	public char[] getName()
	{
		return name;
	}
	public boolean isOn()
	{
		return on;
	}
}
