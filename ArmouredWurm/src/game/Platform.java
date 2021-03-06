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
	protected boolean destroyable,destroyed,hasDestSkin;
	protected String destroyedSprite;
		//Motion
	protected int leftPatrol, rightPatrol, patrolSpeed,patrolTimer, cpatrolTimer,destColN,destrowN, HP;
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
		
		if(destroyed)
		{
			//BLARG
		}
			//Movement logic
		if(moving)
		{
			if(cpatrolTimer >= patrolTimer)
			{
				if(FF)
				{
					this.trueX = trueX + this.patrolSpeed;
					if(trueX > rightPatrol)
					{
						FF = false;
					}
				}
				else
				{
					this.trueX = trueX - this.patrolSpeed;
					if(trueX < leftPatrol)
					{
						FF = true;
					}
				}
				cpatrolTimer = 0;
			}
			
			
			cpatrolTimer++;
		}
			//John Check this
		if(this.rowN >1)
		{
			if(FF)
			{
				this.row = 0;
			}
			else
			{
				this.row = 1;
			}
		}
		//Location
			//I don't thing i use SpeedX or Y at all
		this.trueX = (int) (this.trueX + speedX);
		this.trueY = (int) (this.trueY + speedY);
			//This makes sure it gets rendered at the correct location.
		x = theWorld.getX() + trueX;
		y = theWorld.getY() + trueY;
		
			//Animate :D
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
	public void make_movable( int leftP, int rightP,int inspeed,int intimer)
	{	
			//This is important The way the speed works is based on a timer, 
			//the higher the timer the longer it takes to update
		if(rightP < leftP)
		{
			System.out.println("ERROR-Please check right and left!");
			return;
		}
			//John motion logic
		this.leftPatrol = leftP;
		this.rightPatrol = rightP;
		this.patrolSpeed = inspeed;
		this.patrolTimer = intimer;
		this.moving = true;
		this.FF = true;
			//This is the current time (its relative 0 = start)
		this.cpatrolTimer = 0;
	}
	public void  make_Destroyable(String destroyedloc,int indestColN,int indestrowN,int inHP, Map<String,BufferedImage> spriteData )
	{
		make_Destroyable( destroyedloc, indestColN, indestrowN, spriteData );
		this.HP = inHP;
	}
	public void make_Destroyable(String destroyedloc,int indestColN,int indestrowN, Map<String,BufferedImage> spriteData )
	{
			//John New code destroyable
		BufferedImage spriteMap = null;
		try 
		{
			this.HP = 1;
			if(!destroyedloc.equals("none"))
			{
				this.hasDestSkin = true;
				if(new File(destroyedloc).isFile())
				{
					spriteMap = ImageIO.read(new File(destroyedloc));
				}
				else
				{
					spriteMap = ImageIO.read(getClass().getResource("/"+destroyedloc));
				}
			}
			else
			{
				this.hasDestSkin = false;
			}
			this.destColN = indestColN;
			this.destrowN = indestrowN;
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
	public int movingSpeed()
	{
		if(moving)
		{
			if(FF)
			{
				return this.patrolSpeed;
				
			}
			else
			{
				return -this.patrolSpeed;
			}
		} 
			//This shouldnt happen, but just in case 
		else
		{
			return 0;
		}
	}
	public void destroy()
		{
			this.destroyed = true;
			this.colN = this.destColN; 
			this.rowN = this.destrowN;
			this.row = 0;
			this.col = 0;
			if(this.hasDestSkin) 
			{
				this.name = destroyedSprite;
			}
			else
			{
					//This moves it to the deadZone.
				this.trueX = -this.width;
				this.trueY = 0;
			}
				//can destroyed things move?
			this.moving = false;
		}
	public void damage(int amount)
	{
			//This might need work
		this.HP -= amount;
		if(this.HP <= 0)
		{
			destroy();
		}
	}
	public int getLeftPatrol()
		{return this.leftPatrol;}
	public int getRightPatrol()
		{return this.rightPatrol;}
	public int getDestColN()
		{return this.destColN;}
	public int getDestRowN()
		{return this.destrowN;}
	public String getDestroyedSprite()
		{return this.destroyedSprite;}
	public boolean isDestroyable()
		{return destroyable;}
	public boolean isDestroyed()
		{return destroyed;}
	public boolean getMoving()
		{return this.moving;}
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
