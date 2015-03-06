package game;

import java.awt.Color;
import java.awt.Graphics;

public class PlayerChar extends Sprite 
{
	protected int gravity;
	protected int headhitbox[] = new int[4];
	protected int feethitbox[] = new int[4];
	protected int backhitbox[] = new int[4];
	protected int fronthitbox[] = new int[4];
	protected int legA[] = new int[2];
	protected int topA[] = new int[2];
	//protected Sprite legs;
	protected boolean jumping, forward, backward,FF, falling,hasR;
	protected gun rifle;
	
	protected int topRunSpeed;
	protected int topGravity;
	protected int topJump;
	
	protected int checkAni = -1;

	public PlayerChar(String name, String spriteloc,int x, int y, int width, int height,int row,int col)
	{
		super(spriteloc,x,y,width,height,row,col,15);
		
		jumping= false;
		forward = false;
		backward = false;
		FF = true;
		falling = false;
			//movement stuff
		this.speedX = 0;
		this.speedY = 0;
		this.topRunSpeed = 15;
		this.gravity = 0;
		this.topGravity = 10;
		this.topJump = 10;
		
		this.timerspeed = 3;
		//this.legA[0] = 10;
		//this.legA[1] = 10;
		this.topA[0] = 20;
		this.topA[1] = 20;
		this.hasR = false;
		//legs = new Sprite(legloc,x,y,width,height,2,0,timerspeed);
		this.setHitbox(0, 0, width, height);
	}
	public void setlegA(int S)
	{
		this.legA[0]= S;
		this.legA[1] = S;
	}
	public void settopA(int S)
	{
		this.topA[0] = S;
		this.topA[1] = S;
	}
	public void giveGun(gun rifle)
	{
		this.hasR = true;
		this.rifle = rifle;
	}

	public void damage() 
	{
		System.out.println("OUCH! THAT HURT!");
	}
	public void fire(World theWorld)
	{
		if(hasR)
		{
			rifle.Fire(theWorld);
		}
	}
	public void update()
	{
			
			
		
		if (FF)//FF = facing forward
		{
				//Idle pose facing Right
			this.row = 0;
			this.setColN(0);
			//legs.setRow(0);
			//legs.setColN(0);
		}
		else
		{
				//Idle pose facing Left
			this.row = 1;
			this.setColN(0);
			//legs.setRow(2);
			//legs.setColN(0);
		}
		
			//Test stuff!
		if(jumping){this.timerspeed = 1.5;}
		else{this.timerspeed = 3;}
		
		if(jumping || forward || backward)
		{
			if(forward)
			{
				if(jumping)
				{
						//Right facing Jump Animation
					//legs.setRow(4);
					//legs.setColN(0);
					this.row = 4;
					//this.col = 1;
					this.colN = 20;
				}
				else if(falling)
				{
						//Right facing Falling Animation
					//legs.setRow(4);
					//legs.setColN(0);
					this.row = 10;
					this.col = 1;
					this.colN = 0;
				}
				else
				{
						//Right facing Running Animation
					//legs.setRow(1);
					//legs.setColN(legA[0]);
					this.row = 2;
					this.colN = topA[1];
				}
			}
			else if(backward)
			{
				if  (jumping)
				{
						//Left facing Jump Animation
					//legs.setRow(5);
					//legs.setColN(0);
					this.row = 5;
					//this.col = 1;
					this.colN = 20;
				}
				else if(falling)
				{
						//Left facing Falling Animation
					//legs.setRow(5);
					//legs.setColN(0);
					this.row = 11;
					this.col = 1;
					this.colN = 0;
				}
				else
				{
						//Left facing Running Animation
					//legs.setRow(3);
					//legs.setColN(legA[1]);
					this.row = 3;
					this.colN = topA[1];
				}
			}
		}
		else
		{
				//This is other, This might become an error Frame.
			this.col = 0;
			this.colN = 0;
		}
		//legs.setX(x);
		//legs.setY(y);
		if(hasR)
		{
			rifle.update();
		}
			//I put this check in to make sure that the animation always starts at the beginning 
		if(this.checkAni != this.row )
		{
			col = 0;
			this.checkAni= this.row;
		}
			
		
		this.animateCol();
		//legs.animateCol();
	}
	public void render(Graphics g)
	{	
	
		
		if(Engine.renderHitBox)
		{
			g.setColor(Color.RED);
			g.drawRect(this.headhitbox[0]+this.x, this.headhitbox[1]+this.y,this.headhitbox[2], this.headhitbox[3]);
			g.setColor(Color.BLUE);
			g.drawRect(this.fronthitbox[0]+this.x, this.fronthitbox[1]+this.y,this.fronthitbox[2], this.fronthitbox[3]);
			g.setColor(Color.YELLOW);
			g.drawRect(this.backhitbox[0]+this.x, this.backhitbox[1]+this.y,this.backhitbox[2], this.backhitbox[3]);
			g.setColor(Color.WHITE);
			g.drawRect(this.feethitbox[0]+this.x, this.feethitbox[1]+this.y,this.feethitbox[2], this.feethitbox[3]);
		}
		if(hasR)
		{
			rifle.render(g);
		}
		//legs.render(g);
		super.render(g);
	}
	
	public void fall()
		{
				//This is old and no longer used
			this.y = this.y + gravity;
			this.falling = true;
		}
	public int getGravity()
		{return this.gravity;}
	public void moveXp()
		{this.x = this.x + speedX;}
	public void moveYp()
		{this.y = this.y + speedY;}
	public void moveXn()
		{this.x = this.x - speedX;}
	public void moveYn()
		{this.y = this.y - speedY;}
	public void setFaceForward(boolean FF)
		{this.FF = FF;}
	public void setJumping(boolean jumping)
		{this.jumping = jumping;}
	public void setForward(boolean forward)
		{this.forward = forward;}
	public void setbackward(boolean backward)
		{this.backward = backward;}
	public void setfalling(boolean falling)
		{this.falling = falling;}
	public boolean getFacingForward()
		{return this.FF;}
	public boolean getJumping()
		{return this.jumping;}
	public boolean getForward()
		{return this.forward;}
	public boolean getBackward()
		{return this.backward;}
	public int gettopRunSpeed()
	{return this.topRunSpeed;}
	public int gettopGravity()
		{return this.topGravity;}
	public int gettopJump()
		{return this.topJump;}
	public void settopRunSpeed(int in)
		{this.topRunSpeed= in;}
	public void settopGravity(int in)
		{this.topGravity = in;}
	public void settopJump(int in)
		{this.topJump = in;}

	public void setHitbox(int x, int y, int width, int height)
	{
		this.hitbox[0] = x;
		this.hitbox[1] = y;
		this.hitbox[2] = width;
		this.hitbox[3] = height;
		this.headhitbox[0] = x;
		this.headhitbox[1] = y;
		this.headhitbox[2] = width;
		this.headhitbox[3] = height/4;
		this.fronthitbox[0] = x + width/2;
		this.fronthitbox[1] = y + height/8;
		this.fronthitbox[2] = width/2;
		this.fronthitbox[3] = 3*height/4;
		this.backhitbox[0] = x;
		this.backhitbox[1] = y + height/8;
		this.backhitbox[2] = width/2;
		this.backhitbox[3] = 3*height/4;
		this.feethitbox[0] = x;
		this.feethitbox[1] = y + 3* height/4;
		this.feethitbox[2] = width;
		this.feethitbox[3] = height/4;
	}
	public int[] getheadHitbox()
	{
		int outbox[] = new int[4];
		outbox[0] = x + headhitbox[0];
		outbox[1] = y + headhitbox[1];
		outbox[2] = headhitbox[2];
		outbox[3] = headhitbox[3];
		return outbox;
	}
	public int[] getfrontHitbox()
	{
		int outbox[] = new int[4];
		outbox[0] = x + fronthitbox[0];
		outbox[1] = y + fronthitbox[1];
		outbox[2] = fronthitbox[2];
		outbox[3] = fronthitbox[3];
		return outbox;
	}
	public int[] getbackHitbox()
	{
		int outbox[] = new int[4];
		outbox[0] = x + backhitbox[0];
		outbox[1] = y + backhitbox[1];
		outbox[2] = backhitbox[2];
		outbox[3] = backhitbox[3];
		return outbox;
	}
	public int[] getfeetHitbox()
	{
		int outbox[] = new int[4];
		outbox[0] = x + feethitbox[0];
		outbox[1] = y + feethitbox[1];
		outbox[2] = feethitbox[2];
		outbox[3] = feethitbox[3];
		return outbox;
	}
	

	
	
}
