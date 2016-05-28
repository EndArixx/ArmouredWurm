package game.triggers;
/*
what is a value?
originally i was thinking a value was the same as a state.
AND I WAS WRONG!
a states is esseutially a boolean that descripts what the character is doing.
A Value is a number that says something about the character.

example
HP
run speed
armor 
damage

notes:
are negative numbers allowed?

what about default values?

*/

public class Value 
{
	public String name;
	
	private int curValue;
	private int maxValue;
	private int minValue;
	
	
	public Value(String inname, int inMax,int inCur, int inMin) 
	{
		name = inname;
		curValue= inCur;
		maxValue= inMax;
		minValue= inMin;
	}
	
	//SETS/GETS------------------------------------------------------
	public void setValue(int in)
		{this.curValue=in;}
	public void setMaxValue(int in)
		{this.maxValue=in;}
	public void setMinValue(int in)
		{this.minValue=in;}
	public int getValue()
		{return this.curValue;}
	public int getMaxValue()
		{return this.maxValue;}
	public int getMinValue()
		{return this.minValue;}
}
