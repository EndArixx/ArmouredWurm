package game.triggers;

import java.util.Map;

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

one trigger can cause multiple sparks.
	animations
	StateChanges
	other?
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
	
	protected PlayerChar player; //do I need this?
	protected State[] currentStates;
	protected String name;
	
	//John find a way to setup the cause of the trigger
	//#Name;history;input;allowed states;type?;Spark;state;
	protected String[] causes;
	protected String history; //This should be a varchar[]!
	protected String playerinput;
	protected String allowedStates;
	protected String type;
	protected String[] sparkNames;
	protected String stateName;
	

	public Trigger(String inline)
	{
		String[] Data = inline.split(";");
		name = Data[0];
		history = Data[1];
		playerinput = Data[2];
		allowedStates = Data[3];
		type = Data[4];
		sparkNames = Data[5].split(",");
		stateName = Data[6];
		
		causes = buildCauses(playerinput,history,allowedStates);
		
		//System.out.println(inline);
	}
	
	//Build the Causes
	public static String buildCause(String ininputs, String inhistory, String instate)
	{
		String cause = ininputs+";"+inhistory+";"+instate;
		
		return cause;
	}
	public static String[] buildCauses(String ininputs, String inhistory, String inallowedStates)
	{
		String[] causes = inallowedStates.split(";");//= inputs+";"+history+";"+state;
		for(int i =0 ; i< causes.length ; i++ )
		{
			causes[i] = buildCause(ininputs,inhistory,inallowedStates);
		}
		return causes;
	}
	
	public void Pull()
	{
		
		System.out.println(name);
		//spark.strike(player);
		//state stuff?
	}
	public String[] getCauses()
	{
		return causes;
	}
	
	public String getName()
	{
		return this.name;
	}

}







