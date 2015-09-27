package game;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Queue;
//JOHN RETIRE THIS CLASS!!!!! IT SHOULD BE REPLACED WITH MOVING PLATFORMS>SPIKES!
public class MovingSpike extends Spike 
{
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
