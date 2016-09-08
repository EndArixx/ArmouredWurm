package game.triggers;

import java.util.HashMap;
import java.util.Map;

import game.Tools;

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
	private String stateNames[];
	private boolean stateValues[];
	private int count;
	
	public State(String inName,String[] inStateNames,String[] inStateValues)
	{
		this.name = inName;
		this.count = inStateNames.length;
		stateNames = new String[count];
		stateValues = new boolean[count];
		for(int i = 0; i < count; i++)
		{
			if(i < inStateValues.length)
			{
				stateNames[i] = inStateNames[i];
				stateValues[i] = Boolean.parseBoolean(inStateValues[i]);
			}
		}
		
	}
	public boolean getValue(String inName)
	{
		for(int i = 0;i < count;i++)
		{
			if(stateNames.equals(inName))
			{
				return stateValues[i];
			}
		}
		
		//Default to false.
		return false; 
	}
	
	public String[] getStateNames()
	{
		return this.stateNames;
	}
	
	public boolean[] getStateValues()
	{
		return this.stateValues;
	}
	
	public int getStateNumber()
	{
		return Tools.BooleansToInt(stateValues);
	}
	
	
	public String getName()
	{
		return name;
	}
}
