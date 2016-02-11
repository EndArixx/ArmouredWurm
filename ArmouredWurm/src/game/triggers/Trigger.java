package game.triggers;


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
keyboard
external
internal?
 */
public class Trigger 
{
	// 1 = State
	// 2 = trigger
	int triggerType;
	Spark spark;
	State state;
	
	public Trigger(int type) 
	{
		triggerType = type;
	}
	
	//John think of a better name other then run
	public void run()
	{
		if(triggerType == 1)
		{
			spark.run();
		}
		else if(triggerType == 2)
		{
			state.run();
		}
	}

}







