package game.actions;

import java.awt.image.BufferedImage;
import java.util.Map;


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
	public Actor(String spriteloc, int x, int y, Map<String, BufferedImage> spriteData) 
	{
		super(spriteloc, x, y, spriteData);
	}
	
}
