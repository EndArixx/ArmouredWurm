package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

public class Platform extends Sprite
{
	protected int trueX, trueY, value;
		//Parallax
	protected double parallaxSpeed;
		//Destruction
	protected boolean destroyable,destroyed;
	protected String destroyedSprite;
		//Motion
	protected int leftPatrol, rightPatrol;
	protected boolean FF,moving;
	
		//this is needed to prevent losing alot of data when moving parallax elements
	protected double trueXdub, trueYdub;
	public Platform(String spriteloc, int x, int y,  Map<String,BufferedImage> spriteData)
	{
		super(spriteloc,x,y, spriteData);
		this.destroyable = false;
		this.destroyed = false;
		trueX = x;
		trueY = y;
		trueXdub = trueX;
		trueYdub = trueY;
		this.speedX = 0;
		this.speedY = 0;
		this.parallaxSpeed = 0;
		this.moving = false;
	}
	public Platform(String inImage,int x,int y,int width ,int height,int rowN,int colN,int timerspeed,  Map<String,BufferedImage> spriteData) 
	{
		super(inImage,x,y,width,height,rowN, colN,timerspeed,  spriteData);
		this.destroyable = false;
		this.destroyed = false;
		trueX = x;
		trueY = y;
		trueXdub = trueX;
		trueYdub = trueY;
		this.speedX = 0;
		this.speedY = 0;
		this.parallaxSpeed = 0;
		this.moving = false;
	}
	public Platform(String inImage,int x,int y,int width ,int height,int ColN,int timerspeed,  Map<String,BufferedImage> spriteData,int value)
	{
		super(inImage,x,y,width,height,0,ColN,timerspeed,spriteData);
		this.destroyable = false;
		this.destroyed = false;
		trueX = x;
		trueY = y;
		trueXdub = trueX;
		trueYdub = trueY;
		this.speedX = 0;
		this.speedY = 0;
		this.value = value;
		this.parallaxSpeed = 0;
		this.moving = false;
	}
	public void update(World theWorld)
	{
		//John put the move and destruction logic here.
		this.trueX = (int) (this.trueX + speedX);
		this.trueY = (int) (this.trueY + speedY);
		x = theWorld.getX() + trueX;
		y = theWorld.getY() + trueY;
		animateCol();
	}
	public void update()
	{
		this.x =  this.trueX;
		this.y =  this.trueY;
	}
	public void reset()
	{
		this.speedX = 0;
		this.speedY = 0;
		this.trueX = -width;
		this.trueY = -height;
		this.col = 0;
	}
	public void make_movable( int leftP, int rightP)
	{	
		if(rightP < leftP)
		{
			System.out.println("ERROR-Please check right and left!");
			return;
		}
		//John motion logic
		leftPatrol = rightP;
		rightPatrol = leftP;
		this.moving = true;
	}
	public void make_Destroyable(String destroyedloc, Map<String,BufferedImage> spriteData )
	{
			//John New code destroyable
		BufferedImage spriteMap = null;
		try 
		{
			if(new File(destroyedloc).isFile())
			{
				spriteMap = ImageIO.read(new File(destroyedloc));

			}
			else
			{
				spriteMap = ImageIO.read(getClass().getResource("/"+destroyedloc));
			}
			this.destroyable = true;
			this.destroyed = false;
			this.destroyedSprite = destroyedloc;
			spriteData.put(destroyedloc,spriteMap);
		}
		catch (IOException e) 
		{
			this.destroyable = false;
			System.out.println("Error, Bad Sprite:"+ destroyedloc);
			//e.printStackTrace();
		}
	}
	
	public int getTrueX()
		{return this.trueX;}
	public int getTrueY()
		{return this.trueY;}
	public void setTrueX(int x)
		{this.trueX = x;}
	public void setTrueY(int y)
		{this.trueY = y;}
	public int getValue()
		{return this.value;}
	public void setValue(int in)
		{this.value = in;}
	public double getParSpeed()
	{
		return(this.parallaxSpeed);
	}
	public void setParSpeed(double inSpeed)
	{
		this.parallaxSpeed = inSpeed;
	}
	public void moveXp(int speed)
	{
		this.trueX = this.trueX + speed;
	}
	public void moveYp(int speed)
	{
		this.trueY = this.trueY + speed;
	}
	public void moveXn(int speed)
	{
		this.trueX = this.trueX - speed;
	}
	public void moveYn(int speed)
	{
		this.trueY = this.trueY - speed;
	}

}
