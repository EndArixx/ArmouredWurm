package game.triggers;

public class State 
{
/*
This is nothing more then a boolean and a name 

I dont know if i have to hardcode it

 
 Ok so there will be a string to State maps.
 so you can get all the on states..OHHHHH
 I know.
 States
 
	1) dead
	2) moving
	3) airborne
		i) Jump
		ii) fall
		iii) fly?
	4) touching ground
		i)platform
		ii)ground
	5) idle
	6) attacking
		i) Light
		ii) Heavy
		iii) Special
	7) spot enemy
	8) aggro?
 
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
