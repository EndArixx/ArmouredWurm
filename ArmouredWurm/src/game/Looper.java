package game;

import java.awt.Graphics;

public class Looper extends Platform
{
	private boolean loop;
	public Looper(String spriteloc,int loopXspeed,int Y)
	{
		super(spriteloc,0,Y);
		this.loop = true;
		this.speedX = loopXspeed;
		this.speedY = 0;
	}
	public void update(World theWorld)
	{
		x = (int) (x + speedX);
		y = theWorld.getY() + trueY;
	}
	public void render(Graphics g) 	
	{
		if (x < -width + Engine.window.width && loop)
		{	
			setX(0);
		}
		super.render(g);
	}
}
