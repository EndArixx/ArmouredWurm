package game.triggers;

public class State 
{
/*
This is nothing more then a boolean and a name 

I dont know if i have to hardcode it

 
 Ok so there will be a string to State maps.
 so you can get all teh on states..OHHHHH
 I know.
 
 
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
	public void setOn(Boolean in)
	{
		on = in;
	}
	
}
