package game.triggers;

import game.PlayerChar;

public class Spark 
{
/*
 Things a spark needs?
 animation data
 		X,Y
 		length
 		speed
 		hang on end?
 movement data?
 */
	public int xloc;
	public int yloc;
	public int animationlength;
	public boolean looping;
	public int speed;
	//??
	boolean atend;
	boolean hangonend;

	
	//can this animation be broken?
	String spriteName;
	public Spark(int inx,int iny,int inlen,String inloc,int inspeed,boolean loops ) 
	{
		xloc = inx;
		yloc = iny;
		animationlength = inlen;
		looping = loops;
		speed = inspeed;
	}
	public void strike(PlayerChar player)
	{
		//this will send the correct data to the player to run the animation.
		//should i send data to the player, or should the player access it?
	}
}
