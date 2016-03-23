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
	public String name;
	public String Sprite;
	public int xloc;
	public int yloc;
	public int length;
	public int animationlength;
	public boolean looping;
	public int speed;
	//public AttackHitBoxes hitboxes;
	public String hitboxName;
	public int value;
	//??
	boolean atEnd;
	boolean hangOnEnd;

	
	//can this animation be broken?
	String spriteName;
	public Spark(String inName, int inx,int iny,int inlen,String inloc,int inspeed,boolean loops ) 
	{
		this.name = inName;
		xloc = inx;
		yloc = iny;
		animationlength = inlen;
		looping = loops;
		speed = inspeed;
	}
	public Spark(String inData)
	{
		String[] Data = inData.split(",");
		this.name = Data[0];
		this.Sprite = Data[1]; //JOHN THIS WILL NEED TO HAVE A WILD CARD
		this.xloc = Integer.parseInt(Data[2]);
		this.yloc = Integer.parseInt(Data[3]);
		this.length = Integer.parseInt(Data[4]);
		this.speed = Integer.parseInt(Data[5]); // JOHN WILD CARD OR Default
		this.looping = Boolean.parseBoolean(Data[6]);
		this.hitboxName = "NONE"; //default
		if(Data.length > 7)
		{
			this.value = Integer.parseInt(Data[7]);
			if(Data.length > 8)
			{
				this.hitboxName = Data[8];
			}
		}
		else this.value = -1; //Default
	}
	public String getName()
	{
		return this.name;
	}
	public void strike(PlayerChar player)
	{
		//this will send the correct data to the player to run the animation.
		//should i send data to the player, or should the player access it?
	}
	
	public String testSpark()
	{
		String out ="";
		out += this.name +", ";
		out += this.Sprite+", ";
		out += this.xloc+", ";
		out += this.yloc+", ";
		out += this.length+", ";
		out += this.speed+", ";
		out += this.looping+", ";
		out += this.value+", ";
		out += this.hitboxName+"";
		return out;
	}
}
