package game.actions;

//This is a basic action 

/*
 
 Base Action. all actions will be an action :P
 
 
 Goals: 
 	 this will be all actions that all AI will use in the game.
 

 How do actions work?
 
 every action has a Cause and an effect. 
 
 []   John look into using the already made trigger engine.
 []   effects should cause things:
 
 	Causes 
 		Values
 		cooldown
 		
 
    example: (sub classes?)
    	Attack
    	movement
    	Value Change
    
    there should also be a priority, certain actions are more favorable, this priority should by dynamic, so if a specific action keeps being used its favor will lower. 
    
 */

public class Action 
{

	private String ActionID;
	private int cooldown;
	private double priority; 
	
	public Action(String inActionID)
	{
		this.ActionID = inActionID;
		
		//Lookup the data? or pass it in with a populator?
		
	}
	
	public void Cause()
	{
		//what can cause an actions?
		
		//honestly this should be very similar to the trigger engine.
		//only instead of player input is should be based on values and recharge
		
		
		/*
		 
		  1) Value
		  		this is the main point
		  2) Recharge timer. 
		  		Actions should have a cooldown
		 */

	}
	
	public void Effect()
	{
		//This is what happens when you 
	}
	
	
	//GETS and SETS------------------------------------------------------
	public String getActionID()
	{
		return this.ActionID;
	}
	
}
