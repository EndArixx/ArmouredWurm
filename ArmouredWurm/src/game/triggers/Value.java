package game.triggers;

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
	public Value(String indata)
	{
		String[] data = indata.split(",");
		name = data[0];
		curValue = Integer.parseInt(data[1]);
		maxValue = Integer.parseInt(data[2]);
		minValue = Integer.parseInt(data[3]);
	}
	
	//SETS/GETS------------------------------------------------------
	public void setValue(int in)
		{
			if (in > maxValue)
			{
				this.curValue = maxValue;
			}
			else if (in < minValue)
			{
				this.curValue = maxValue;
			}
			else
			{
				this.curValue=in;
			}
		}
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
	public String getName() 
		{return this.name;}
	
	public void add(int in)
	{
		this.curValue += in;
		if(curValue < minValue)
		{
			this.curValue = minValue;
		}
		if(curValue > maxValue)
		{
			this.curValue = maxValue;
		}
	}
	public boolean getBool()
	{
		if(curValue == 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public void setValue(boolean value) 
	{
		if(value)
		{
			this.curValue = 1;
		}
		else
		{
			this.curValue = 0;
		}
	}
}
