package game.triggers;

import game.PlayerChar;


/*
 What kinds of actions can be caused by a trigger?
 1) movements 
 	Run
 	jump
 	fall?
 2) attacks
 	punch 
 	kick
 	etc
 */


/*
Inputs:
Move Stack
	the last X moves (prolly 10)
keyboard
	buttons being pressed

States
	External
		platforms
		can see enemies
	Internal
		HP
		running
		jumping
		crouching 
 */
public class Trigger 
{
	
	/*
	 what does a trigger need?
	 (these will all be in the map side)
	 1) history 
	 	the players previouse inputs
	 2) buttons pressed 
	 	current buttons being pressed
	 3) external
	 	stuff that is happening in the world around him (platforms, enemys)
	 4) internal
	 	player stuff, current state,etc
 	 5) other?
 	 
 	 what does a trigger do?
 	 causes a spark and possibly a state change.
 	 	 
	 */
	
	
	
	
	Spark spark;
	State state;
	PlayerChar player;
	State[] currentStates;
	String name;
	//John find a way to setup the cause of the trigger
	
	
	/*Things that the trigger needs:
		1) MoveStack
		2) HP
			i)Min
			ii)Max
		3) current states
			i) is HP a state?
	*/
	public Trigger(Spark in)
	{
		spark = in;
	}
	public Trigger(String inline)
	{
		//parse through the inline stuff
		String[] Data = inline.split(";");
		name = Data[0];
		System.out.println(inline);
	}
	
	public void Pull()
	{
		
		System.out.println(name);
		//spark.strike(player);
		//state stuff?
	}
	public String getName()
	{
		return this.name;
	}

}







