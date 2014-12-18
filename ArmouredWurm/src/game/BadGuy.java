package game;

public class BadGuy extends Platform
{
	public BadGuy(String spriteloc, int x, int y)
	{
		super(spriteloc,x,y);
	}
	public void damage()
	{
		this.reset();
	}
	
}
