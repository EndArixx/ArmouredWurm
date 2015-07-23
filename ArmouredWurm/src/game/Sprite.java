package game;
//This is the Parent to all rendered objects in the game. 
//A sprite is simple an image
//This sprites all have X/Y quards, animation and rendering.
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

public class Sprite{
	
	protected String fileName;	
	//protected BufferedImage spriteMap;
	//protected BufferedImage spriteImage;
	protected boolean animate, firstloop;
	protected int  x , y, width , height;
	protected double speedX, speedY;
	protected int rowN, colN, row, col;
	double timerspeed;
	protected float timer;
	protected int hitbox[] = new int[4];
	
	protected String name;
	
//Constructor---------------------------------------------------------------------------------------
	public Sprite(String inImage,int x,int y,  Map<String,BufferedImage> spriteData)
	{
		name = inImage;
			//input is the image of the sprite itself, and its location.
		BufferedImage spriteMap = null;
		try 
		{
			if(new File(inImage).isFile())
			{
				spriteMap = ImageIO.read(new File(inImage));

			}
			else
			{
				spriteMap = ImageIO.read(getClass().getResource("/"+inImage));
			}
		}
		catch (IOException e) 
		{
			System.out.println("Error, Bad Sprite:"+ inImage);
			//e.printStackTrace();
		}
		
		spriteData.put(inImage,spriteMap);

		this.fileName = inImage;
		this.x = x;
		this.y = y;
		this.width = spriteData.get(name).getWidth();
		this.height = spriteData.get(name).getHeight();
			//this just sets up a standard hitbox of the image, this can be changed later.
		this.hitbox[0] = 0;
		this.hitbox[1] = 0;
		this.hitbox[2] = width;
		this.hitbox[3] = height;
		//spriteImage= spriteMap;
		this.animate = false;
		this.firstloop = false;
	}
	public Sprite(String inImage,int x,int y,int width ,int height,int rowN,int colN,int timerspeed, Map<String,BufferedImage> spriteData)
	{
		name = inImage;
			//This input is for sprites with animation and stuff like that.
		BufferedImage spriteMap = null;
		try 
		{
			if(new File(inImage).isFile())
			{
				spriteMap = ImageIO.read(new File(inImage));

			}
			else
			{
				spriteMap = ImageIO.read(getClass().getResource("/"+inImage));
			}
		}
		catch (IOException e) 
		{
			System.out.println("Error, Bad Sprite:"+ inImage);
			//e.printStackTrace();
		}
		
		spriteData.put(inImage,spriteMap);
		
		this.x = x;
		this.y = y;
		this.rowN = rowN;
		this.colN = colN;
		this.width = width;
		this.height = height;
		this.hitbox[0] = 0;
		this.hitbox[1] = 0;
		this.hitbox[2] = width;
		this.hitbox[3] = height;
		this.timerspeed = timerspeed;
		this.animate = true;
		this.row = 0;
		this.col = 0;
			//A subimage is the image that will show on the screen
		//spriteImage= spriteMap.getSubimage((col * width), (row * height), width, height);
	}
	public Sprite()
	{
		//empty for a reason 
	}
	public void animateCol()
	{
			//This is what makes the sprite move or animate.
			//		Sprite are animated in a left to right fasion.
		
		
			//this first part is a check so that no timer gets too large and causes an error
		if (timer > 100000){timer = 0;}
			//essentualy a time increment
		timer++;
		
			//this sees if it is time to switch to the new animation.
		if (col < colN)
		{ 
			if( timerspeed != 0)
			{
				if(timer%timerspeed == 0)
				{
						//new image is created and will be rendered.
					timer = 0;
					//spriteImage= spriteMap.getSubimage((col * width), (row * height), width, height);
					col++;
					if(col == colN)
					{
						col = 0;
						timer = 0;
						this.firstloop = false;
					}
				}	
			}
			else 
			{
				
			}
		}
		else
		{
			col = 0;
			this.firstloop = false;
			timer = 0;
			
		}
		
		
	}
	public void animateRow()
	{
		//this will probably not be used in most cases because sprites are usually setup 
		//to be rendered left to right not up and down.
		
		row++;
			//a check to make sure that it doesn't render past the final image.
		if (row > rowN)
		{ 
			row = 0;
		}
		//spriteImage= spriteMap.getSubimage((col * width) - width, (row * height)-height, width, height);
	}
//public GETS AND SETS------------------------------------------------------------------------------	
			//GET AND SET ROW  & COL
	public void setRow(int row)
		{this.row = row;}
	public void setCol(int col)
		{this.col = col;}
	public int getRow()
		{return this.row; }
	public int getCol()
		{return this.col;}
			//GET AND SET ROW NUMBER AND COL NUMBER
	public void setRowN(int rowN)
		{this.rowN = rowN;}
	public void setColN(int colN)
		{this.colN = colN;}
	public int getRowN()
		{return this.rowN; }
	public int getColN()
		{return this.colN;}
			//GET AND SET X AND Y
			//GET ANDS SET X and Y SPEEDS
	public void setXspeed(int speedX)
		{this.speedX = speedX;}
	public void setYspeed(int speedY)
		{this.speedY = speedY;}
	public double getXspeed()
		{return speedX;}
	public double getYspeed()
		{return speedY;}
	public void setTimerSpeed(int speed)
	{
		this.timerspeed = speed;
	}
	
	public void render(Graphics g,Map<String,BufferedImage> spriteData)
	{
		try
		{
		
			if(animate)
			{
				BufferedImage spriteImage= spriteData.get(name).getSubimage((col * width), (row * height), width, height);
				g.drawImage(spriteImage, x,y ,  width , height, null);
				spriteImage.flush();
			}
			else
			{
				g.drawImage(spriteData.get(name), x,y ,  width , height, null);
			}
			if(Engine.renderHitBox)
			{
				g.setColor(Color.GREEN);
				g.drawRect(this.hitbox[0]+this.x, this.hitbox[1]+this.y,this.hitbox[2], this.hitbox[3]);
			}
		}
		catch (Exception e)
		{
			System.out.println("x" + this.x +" y" +this.y +" name:"+ this.name +" w" + this.width +" h"+ this.height +" C" + this.col +" R" +this.row );
		}
	}
	///--------------------------------------------------------------------------------------------------
	public void update()
	{
			//this updates the position of the sprite based on speed
		this.x = (int) (this.x + speedX);
		this.y = (int) (this.y + speedY);
	}
	public void setHitbox(int x, int y, int width, int height)
	{
			//creates a new hitbox
		this.hitbox[0] = x;
		this.hitbox[1] = y;
		this.hitbox[2] = width;
		this.hitbox[3] = height;
	}
	public int[] getHitbox()
	{
			//returns the hitbox
		int outbox[] = new int[4];
		outbox[0] = x + hitbox[0];
		outbox[1] = y + hitbox[1];
		outbox[2] = hitbox[2];
		outbox[3] = hitbox[3];
		return outbox;
	}
	public void setHitbox(int hitbox[])
		{this.hitbox = hitbox;}
	public int getWidth()
	{return width;}
	public int getHeight()
	{return height;}
	public void setWidth(int width)
	{this.width = width;}
	public void setHeight(int height)
	{this.height = height;}
	public void setX(int x)
	{this.x = x;}
	public void setY(int y)
	{this.y = y;}
	public int getX()
	{return x;}
	public int getY()
	{return y;}
	public void moveXp(int speed)
	{
		this.x = this.x + speed;
	}
	public void moveYp(int speed)
	{
		this.y = this.y + speed;
	}
	public void moveXn(int speed)
	{
		this.x = this.x - speed;
	}
	public void moveYn(int speed)
	{
		this.y = this.y - speed;
	}

}
