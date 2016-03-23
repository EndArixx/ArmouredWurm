package game.triggers;

public class State 
{
/*
This is nothing more then a boolean and a name 

I don't know if i have to hard code it

 
 Ok so there will be a string to State maps.
 so you can get all the on states..OHHHHH
 I know.
 States
 
	1) dead
	2) moving
	3) airborne
		i) Jumping
		ii) falling
		iii) flying
	4) touching ground
		i)on platform
		ii)on ground
	5) idle
	6) attacking
		i) Light
		ii) Heavy
		iii) Special
	7) spot enemy
	
	--MORE FOR LATER--
	8) has aggro 
	9) guard
	10) croutch
	11) swim
 
 states should have features, things that can apply to multiple states

the trigger system will worry about the actual names of 'allowed states' which is more then one
but when you actually go to pick a state it should be super simpler
AKA
NAME types and possible sub-types.

do i allow 'identical states?'
sure why not?
the trigger system is the one that assigns states.

 
 */
	private String name;
	//private boolean on;
	//types
	private boolean dead;
	private boolean moving;
	private boolean touchingGround;
	private boolean idle;
	private boolean attacking;
	private boolean spotEnemy;
	//private boolean agro;
	private String[] subType;
	//IF YOU ADD SOMETHING HERE ADD IT TO THE RETURN STATEMENT!
	
	public State(String inName,boolean inDead,boolean inMoving,boolean inTouchingGround,boolean inIdle,boolean inAttacking,boolean inSpotEnemy) 
	{
		this.name = inName;
		this.dead = inDead;
		this.moving = inMoving;
		this.touchingGround = inTouchingGround;
		this.idle = inIdle;
		this.attacking = inAttacking;
		this.spotEnemy = inSpotEnemy;
		
	}
	public State(String inName,boolean inDead,boolean inMoving,boolean inTouchingGround,boolean inIdle,boolean inAttacking,boolean inSpotEnemy,String[] inSubType) 
	{
		this( inName, inDead, inMoving, inTouchingGround, inIdle, inAttacking, inSpotEnemy);
		this.subType = inSubType;
	}
	public State(String inName, String inputString) 
	{
		this.name = inName;
		String[] splitString = inputString.split(",");
		this.dead = Boolean.parseBoolean(splitString[0]);
		this.moving = Boolean.parseBoolean(splitString[1]);
		this.touchingGround = Boolean.parseBoolean(splitString[2]);
		this.idle = Boolean.parseBoolean(splitString[3]);
		this.attacking = Boolean.parseBoolean(splitString[4]);
		this.spotEnemy = Boolean.parseBoolean(splitString[5]);
	}
	public boolean[] getTypes()
	{
		boolean[] out = {dead,moving,touchingGround,idle,attacking,spotEnemy};
		return out;
	}
	public String getTypeString()
	{
		String out ="";
		if(dead) out += "1,";
		else out += "0,";
		if(moving)  out += "1,";
		else out += "0,";
		if(touchingGround)  out += "1,";
		else out += "0,";
		if(touchingGround) out += "1,";
		else out += "0,";
		if(attacking)  out += "1,";
		else out += "0,";
		if(spotEnemy) out += "1";
		else out += "0";
		return out;
	}
	public String[] getSubTypes()
	{
		return subType;
	}
	
	public String getName()
	{
		return name;
	}
	public void testStates()
	{
		System.out.print(name);
		if(dead) System.out.print("DEAD ");
		else System.out.print("Not DEAD ");
		if(moving) System.out.print("Moving ");
		else System.out.print("Not Moving ");
		if(touchingGround) System.out.print("on ground ");
		else System.out.print("Not on ground ");
		if(touchingGround) System.out.print("Idle ");
		else System.out.print("Not Idle ");
		if(attacking) System.out.print("Attacking ");
		else System.out.print("Not Attacking ");
		if(spotEnemy) System.out.print("Sees enemy ");
		else System.out.print("doesnt see enemy ");
		System.out.println();
	}

	
}
