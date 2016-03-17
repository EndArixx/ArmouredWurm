package game.triggers;

public class State 
{
/*
This is nothing more then a boolean and a name 

I don't know if i have to hardcode it

 
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
	8) has aggro
 
 states should have features, things that can apply to multiple states

the trigger system will worry about the acutal names of 'allowed states' which is more then one
but when you actually go to pick a state it should be super simpler
AKA
NAME types and possibe suptimes.

do i allow 'idential states?'
sure why not?
the trigger system is the one that assignis states.

 
 */
	private String name;
	private boolean on;
	//types
	private boolean dead;
	private boolean moving;
	private boolean touchingGround;
	private boolean idle;
	private boolean attacking;
	private boolean spotEnemy;
	private boolean agroo;
	private String[] subType;
	
	
	public State(String inName) 
	{
		this.name = inName;
		this.on = false;
	}
	public String getName()
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
