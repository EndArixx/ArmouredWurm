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
	Spark spark;
	State state;
	PlayerChar player;
	State[] currentStates;
	//John find a way to setup the cause of the trigger
	
	
	/*Things that the trigger needs:
		1) MoveStack
		2) HP
			i)min
			ii)max
		3) current states
			i) is HP a state?
	
	*/
	public Trigger(State in)
	{
		state = in;
	}
	
	public void Pull()
	{
		spark.strike(player);
	}

}







