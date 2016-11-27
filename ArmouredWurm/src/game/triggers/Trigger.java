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
	protected State[] currentStates;
	protected String name;

	
	protected String history; //This should be a varchar[]?
	protected String[] playerinput;
	protected String allowedStates;
	protected String type;
	protected String[] sparkNames;
	protected String stateName;
	protected String[] ValueMarkers;
	protected boolean Interuptable;

	public Trigger(String inline)
	{
		String[] Data = inline.split(";");
		name = Data[0];
		history = Data[1];
		playerinput = Data[2].split(",");
		allowedStates = Data[3];
		type = Data[4];
		sparkNames = Data[5].split(",");
		stateName = Data[6];
		Interuptable = Boolean.parseBoolean(Data[7]);
		if( Data.length > 8) 
		{
			ValueMarkers = Data[8].split(",");	
		}
		else
		{
			ValueMarkers = null;
		}
	}
	
	public String getName()
	{
		return this.name;
	}
	public String[] getSparks()
	{
		return this.sparkNames;
	}
	public String getState()
	{
		return this.stateName;
	}
	public boolean isInteruptable()
	{
		return this.Interuptable;
	}
	public String[] getInputControl()
	{
		return this.playerinput;
	}
	public String getAllowedStates()
	{
		return this.allowedStates;
	}
	
	public String getHistory()
	{
		return this.history;
	}
	public String[] getValueMarkers()
	{
		return this.ValueMarkers;
	}
}







