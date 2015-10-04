package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;

import javax.imageio.ImageIO;

public class Explosive extends Platform
{
	public int type, id, explcolN, rocketW , rocketH, blastW, blastH, rockcolN, rocketspeed, blastspeed;
	public String blast, bombimage;
	private boolean exploding, armed, missile,bullet, bomb, freindly;
	public Explosive(
			String spriteloc,
			String blastspriteloc,
			int x, int y,
			int width ,int height,
			int type,
			int blastW, int blastH,
			int id,
			int rockcolN,int explcolN,
			int rocketspeed, int blastspeed,  Map<String,BufferedImage> spriteData) 
		{
		super(spriteloc, x, y,width , height , 0 ,rockcolN,rocketspeed, spriteData);
		
		this.bombimage = spriteloc;
		this.blast = blastspriteloc;
		//this.blast = new Sprite(blastspriteloc,x,y,blastW,blastH,0,explcolN,blastspeed,spriteData);
		BufferedImage spriteMap = null;
		try 
		{
			if(new File(blastspriteloc).isFile())
			{
				spriteMap = ImageIO.read(new File(blastspriteloc));

			}
			else
			{
				spriteMap = ImageIO.read(getClass().getResource("/"+blastspriteloc));
			}
			spriteData.put(blastspriteloc,spriteMap);
		}
		catch (IOException e) 
		{
			System.out.println("Error, Bad Sprite:"+ blastspriteloc);
			//e.printStackTrace();
		}

			/*
			 * type 0 bomb hurts player
			 * type 1 bomb hurts enemies
			 */
		
		this.rocketW = width;
		this.rocketH = height;
		this.blastW = blastW;
		this.blastH = blastH;
		this.type = type;
		this.id = id;
		this.rockcolN = rockcolN;
		this.explcolN = explcolN;
		this.armed = false;
		this.rocketspeed = rocketspeed;
		this.blastspeed = blastspeed;
		

	}
		/*
		 * this is old proximity will now be handled by the Main engine!
		 */
	public void proxy(PlayerChar player, Spike[] badguys, Explosive[] bombs, Platform[] ledge)
	{
		if (armed || bullet)
		{
			if(!freindly)
			{
				if (Tools.check_collision(player.getHitbox(), this.getHitbox()))
				{
					if (!exploding)
					{
						this.explode();
					}
					else if(col > 2)
					{
						player.damage();
					}
				}
			}
			for (int i = 0; i< badguys.length; i++)
			{
				if (Tools.check_collision(badguys[i].getHitbox(), this.getHitbox()))
				{
					if (!exploding)
					{
						this.explode();
						if (type == 2)
						{
							//badguys[i].damage();
						}
					}
					else if(col > 2 || this.freindly)
					{
						//badguys[i].damage();
					}
				}
			}
			for (int i = 0; i< bombs.length; i++)
			{
				if (Tools.check_collision(bombs[i].getHitbox(), this.getHitbox()) && (this.id != bombs[i].getID()))
				{
					if(!exploding && !bomb)
					{
						this.explode();
					}
					if((col > 2 && exploding))// || bombs[i].GetTBo())
					{
						bombs[i].explode();
					}
				}
			}
			for (int i = 0; i< ledge.length; i++)
			{
				if (Tools.check_collision(ledge[i].getHitbox(), this.getHitbox()))
				{
					if (!exploding)
					{
						this.explode();
					}
				}	
			}
		}
	}
	
	public void reset()
	{
		
		this.speedX = 0;
		this.speedY = 0;
		this.trueX = -rocketW;
		this.trueY = -rocketH;
		this.col = 0;
		this.name = bombimage;
	}
	public void explode()
	{
		if (!exploding)
		{
			if (!this.bullet )
			{				
				this.firstloop = true;
				this.name = this.blast;
				this.speedX = 0;
				this.speedY = 0;
				this.row = 0;
				this.col = 0;
				this.exploding = true;
				this.colN = this.explcolN;
				this.width = this.blastW;
				this.height = this.blastH;
				this.trueX = (trueX -(blastW/2)+(rocketW/2));
				this.trueY = (trueY -(blastH/2)+(rocketH/2));
				this.timer = 10;
				this.timerspeed = blastspeed;
				this.setHitbox(0, 0, width, height);
				//spriteImage = spriteMap.getSubimage((col * width), (row * height), width, height);
				}
			else
			{
				reset();	
			}
		}
	}
	public void Exploding(Queue<DamageHitbox> damageQ)
	{
		DamageHitbox out = new DamageHitbox( this.x , this.y ,this.blastW,this.blastH, 5 , 4);
		damageQ.add(out);
	}
	public void update(World m,Queue<DamageHitbox> damageQ)
	{
		if (!this.firstloop && exploding)
		{
			trueX = Tools.deadZoneX();
			trueY = Tools.deadZoneY();
			this.armed = false;
			exploding = false;
			this.width = this.rocketW;
			this.height = this.rocketH;		
			this.colN = this.rockcolN;
			this.setHitbox(0, 0, width, height);
			this.row = 0;
			this.col = 0;
			this.timerspeed = rocketspeed;
			this.name = this.bombimage;
		}
		else if(exploding)
		{ 
			if(col > 0)
			{
				Exploding(damageQ);
			}
		}
		else
		{
			//Hmmmm
		}
		animateCol();
		super.update(m);
	}
	public boolean GetTBo()
		{return this.bomb;}
	public boolean GetTM()
		{return this.missile;}
	public boolean GetTBu()
		{return this.bullet;}
	public boolean getTF()
		{return this.freindly;}
	public int getID()
		{return this.id;}
	public void setID(int id)
		{this.id = id;}
	public void setArmed(boolean armed)
		{this.armed = armed;}
	public boolean getArmed()
		{return this.armed;}
	public boolean getExploding()
		{return this.exploding;}
	public void setType(int i) 
		{this.type = i;}
	public int getType()
		{return this.type;}
	
}
