package game.actions;

import java.util.List;

//This is the actor controller
public class ExecutiveDirector 
{
	private List<Actor> MainCast;
	
	//This will control all of the Actors when a map is loaded.
	//It tells them what to do :)
	
	//CONSTRUCTORS---------------------------------------
	public ExecutiveDirector()
	{
		//setup
	}
	
	
	
	//METHODS--------------------------------------------
	public void PrePareNewMap(List<Actor> newCast)
	{
		this.MainCast = newCast;
		//What happens when a new map gets loaded?
		
		
	}
	
	
}
