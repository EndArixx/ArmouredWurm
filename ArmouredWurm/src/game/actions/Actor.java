package game.actions;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import game.Tools;
import game.triggers.AttackHitBoxes;
import game.triggers.Spark;
import game.triggers.State;
import game.triggers.Trigger;
import game.triggers.Value;


/*
 
 This is an Actor
 All Enemies and possible Allied NPCs will be Actors
 Things:
 
 Uses an Action system 
 Uses Triggers for animation (see playerCharV2)
 	this should piggy back off Actions
 Uses Nodes for movement
  	actions move from node to node
  	range?
  	is the player a node?
 
 */

public class Actor extends game.Platform
{		
	private String name;
	protected String valueloc;
	protected HashMap<String,Action> actionMap;
	protected HashMap<String,Value> valueMap;
	
	
	public Actor(String indata,String spriteloc, int x, int y, Map<String, BufferedImage> spriteData) 
	{
		super(spriteloc,x,y,spriteData);
		
		this.name = indata.split(",")[0];
		this.animate = true;
		this.firstloop = true;
		String[] temp = indata.split(indata);

		
		BufferedReader br = null;
		int err = 0;
		
		//first you need to get the info parsed out, then you will call actor files to get the action data
		//so what does an actor need?
		// X,y (in super)
		// True X True Y (this is in platform)
		// actions
		// causes/triggers
		// animation data?
		// values
		
		// How will the data work?
		/*
		  	Map data:
		  		ActorName, X, Y, ActorType, 
		  			other? patrol, speficic map related things?
		  	ActorType File
		  		actions
		  		values
	 
		  
		 */

		try 
		{
			//create a new Armoured Wurm reader.
			Tools reader = new Tools();
			
	    	//Favor external files, then internal
	    	err= 1;
	    	//br = reader.getBR(infile);
	        String line = Tools.readlineadv(br);
	    	//reader.closeBR();
	    	
	        
	        
	        //Actions---------------------------------------------------------
	        
	        err= 6;
	    	br = reader.getBR(valueloc);
	        
	        this.actionMap = new HashMap<String,Action>();
	        Action tAction;
	        while((line = Tools.readlineadv(br))!=null)
	        {
	        	tAction = new Action(line);
		        this.actionMap.put(tAction.getName(),tAction);
	        }
	        reader.closeBR();        
	    	
	    	//Values---------------------------------------------------------
	        
	        err= 7;
	    	br = reader.getBR(valueloc);
	        
	        this.valueMap = new HashMap<String,Value>();
	        Value tValue;
	        while((line = Tools.readlineadv(br))!=null)
	        {
	        	tValue = new Value(line);
		        this.valueMap.put(tValue.getName(),tValue);
	        }
	        reader.closeBR();
	        
	        
	        
	    } catch (Exception e) 
		{
	    	System.out.println("Im sorry the Actor: "+name+" or one of its files could not be loaded!\n Error #"+ err + "\n " + e.toString());
	    	//JOptionPane.showMessageDialog(null,  "Player File: "+infile +"Error:"+err+"\n"+ e.toString() , "Render Error", JOptionPane.ERROR_MESSAGE);
	    } 
		
		
	}
}
