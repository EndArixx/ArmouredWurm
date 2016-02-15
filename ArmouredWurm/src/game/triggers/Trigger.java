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
	// 2 = Spark
	int triggerType;
	Spark spark;
	State state;
	//John find a way to setup the cause of the trigger
	
	public Trigger(State in)
	{
		triggerType = 1;
		state = in;
	}
	public Trigger(Spark in)
	{
		triggerType = 2;
		spark = in;
	}
	public Trigger(int type) 
	{
		triggerType = type;
	}
	
	public void Pull()
	{
		if(triggerType == 1)
		{
			state.change();
		}
		else if(triggerType == 2)
		{
			spark.strike();
		}
	}

}







