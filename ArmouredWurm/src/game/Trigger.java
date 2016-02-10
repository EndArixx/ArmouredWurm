package game;
/*
 *This is a trigger, The idea of a trigger its it has a specific input, and causes either a state change or a spark 
 *
Input: input control and a MovehistoryQueue
output: either a state change or a Spark
 */

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
