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
	protected boolean atEnd;
	protected boolean hangOnEnd;
	
	//Value sparks, John look into an inheritance system.
	protected String valueName;
	protected int kindOfChange;
	/*
	  Error     0
	  Max   >   1
	  Min   <   2
	  Plus  +   3
	  Set   =   4
	 */
	
	public Spark(String inData)
	{
		String[] Data = inData.split(",");
		this.type = Data[0];
		if(type.equals("A")) //This is an animation
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
		else if(type.equals("V"))
		{
			this.name = Data[1];
			this.valueName = Data[2];
			switch(Data[3])
			{
				case ">":
					kindOfChange = 1;
					break;
				case "<":
					kindOfChange = 2;
					break;
				case "+":
					kindOfChange = 3;
					break;
				case "=":
					kindOfChange = 4;
					break;
				default:
					kindOfChange = 0;
			}
			this.value = Integer.parseInt(Data[3]);
			
		}
		else //This is a state change 
		{
			this.name = Data[1];	
		} 
	}
	public int getValue()
	{
		return this.value;
	}
	public String getValueName()
	{
		return this.valueName;
	}
	public String getName()
	{
		return this.name;
	}
}
