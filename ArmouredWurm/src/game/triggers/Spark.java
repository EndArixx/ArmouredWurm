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
	int xloc;
	int yloc;
	int animationlength;
	boolean looping;
	int speed;
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
	}
}
