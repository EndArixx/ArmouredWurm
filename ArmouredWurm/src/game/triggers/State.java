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
	public boolean[] getTypes()
	{
		boolean[] out = {dead,moving,touchingGround,idle,attacking,spotEnemy};
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

	
}
