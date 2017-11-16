package game.actions;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.Tools;
import game.World;
import game.triggers.Trigger;

//This is the actor controller
public class ExecutiveDirector 
{
	private List<Actor> CurrentCast;
	
	//This will control all of the Actors when a map is loaded.
	//It tells them what to do :)
	//Im going to simplify this at first, it work be able to place troops yet, this just holds onto the actors
	
	//1st goal: Populate map when provided with txt file.
	
	
	//CONSTRUCTORS---------------------------------------
	public ExecutiveDirector()
	{
		//setup
		this.CurrentCast = new ArrayList<Actor>(); 
	}
	
	
	
	//METHODS--------------------------------------------
	public void PrePareNewMap(String inputData, Map<String,BufferedImage> spriteData)
	{
		//PARSE LINE HERE
		
		
		String line;
		String[] temp;
		BufferedReader br = null;
		Tools reader = new Tools();
		
		if(CurrentCast != null)
		{
			CurrentCast.clear();
		}
		
		br = reader.getBR(inputData);
		
        Actor Act;
        while((line = Tools.readlineadv(br))!=null)
        {
        	temp = line.split(",");
        	Act = new Actor(temp[0], temp[1], Integer.parseInt(temp[2]), Integer.parseInt(temp[3]), spriteData);
        	
        	this.CurrentCast.add(Act);
        }
        reader.closeBR();
		
		
	}
	
	public void update(World theWorld)
	{
		//Loop through all actors and update them.
		for(Actor A : this.CurrentCast)
		{
			//This will have to be overloaded, the Actors will do more than just simply update their Position like the platforms do.
			A.update(theWorld);
		}
	}
	
	public void Render(Graphics g, Map<String,BufferedImage> spriteData)
	{
		//Loop through all the Actors then render them.
		for(Actor A : this.CurrentCast)
		{
			A.render(g, spriteData);
		}
	}
	
}
