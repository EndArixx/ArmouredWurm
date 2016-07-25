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
	public String type;
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
		type = Data[0];
		if(type == "A") //This is an animation
		{
			this.name = Data[1];
			this.Sprite = Data[2]; //JOHN THIS WILL NEED TO HAVE A WILD CARD
			this.xloc = Integer.parseInt(Data[3]);
			this.yloc = Integer.parseInt(Data[4]);
			this.length = Integer.parseInt(Data[5]);
			this.speed = Integer.parseInt(Data[6]); // JOHN WILD CARD OR Default
			this.looping = Boolean.parseBoolean(Data[7]);
			this.hitboxName = "NONE"; //default
			if(Data.length > 8)
			{
				this.value = Integer.parseInt(Data[8]);
				if(Data.length > 9)
				{
					this.hitboxName = Data[9];
				}
			}
			else this.value = -1; //Default
		}
		else //This is a state change 
		{
			this.name = Data[1];
		}
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
