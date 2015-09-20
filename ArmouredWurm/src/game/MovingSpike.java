package game;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Queue;

public class MovingSpike extends Spike 
{
	protected int leftPatrol, rightPatrol;
	protected boolean FF;

	public MovingSpike(String spriteloc, int x, int y,
			Map<String, BufferedImage> spriteData) 
	{
		super(spriteloc, x, y, spriteData);
		this.leftPatrol = -1;
		this.rightPatrol = -1;
		this.FF = true;
		
	}

	public MovingSpike(String inImage, int x, int y, int width, int height,
			int rowN, int colN, int timerspeed, int damage,
			Map<String, BufferedImage> spriteData) 
	{
		super(inImage, x, y, width, height, rowN, colN, timerspeed, damage,spriteData);
		
		
	}
	
	public void update(World theWorld,Queue<DamageHitbox> damageQ)
	{
		super.update(theWorld);
		
		
		
		animateCol();
	}
	
	public void setPath(int inLeft, int inRight)
	{
		this.leftPatrol = inLeft;
		this.rightPatrol = inRight;
	}
	
	

}
