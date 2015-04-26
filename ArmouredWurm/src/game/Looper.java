package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Looper extends Platform
{
	private boolean loop;
	public Looper(String spriteloc,int loopXspeed,int Y,  Map<String,BufferedImage> spriteData)
	{
		super(spriteloc,0,Y,spriteData);
		this.loop = true;
		this.speedX = loopXspeed;
		this.speedY = 0;
	}
	public void update(World theWorld)
	{
		x = (int) (x + speedX);
		y = theWorld.getY() + trueY;
	}
	public void render(Graphics g,  Map<String,BufferedImage> spriteData) 	
	{
		if (x < -width + Engine.window.width && loop)
		{	
			setX(0);
		}
		super.render(g,spriteData);
	}
}
