package game;

public class Explosive extends Platform
{
	private int type, id, explcolN, rocketW , rocketH, blastW, blastH, rockcolN, rocketspeed, blastspeed;
	private boolean exploding, armed, missile,bullet, bomb, freindly;
	public Explosive(
			String spriteloc,
			int x, int y,
			int width ,int height,
			int type,
			int blastW, int blastH,
			int id,
			int rockcolN,int explcolN,
			int rocketspeed, int blastspeed) 
	{
		//type 0 bomb, type 1 rocket, type 2 bullet 
		super(spriteloc, x, y,width , height , 2 ,rockcolN,rocketspeed);
		if(type == 0)
		{
			this.bomb = true;
			this.missile = false;
			this.freindly = false;
			this.bullet = false;
		}
		else if(type == 1)
		{
			this.bomb = false;
			this.missile = true;
			this.freindly = true;
			this.bullet = false;
		}
		else if(type == 2)
		{
			this.bomb = false;
			this.missile = false;
			this.freindly = true;
			this.bullet = true;
		}
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
	public void proxy(PlayerChar player, BadGuy[] badguys, Explosive[] bombs, Platform[] ledge)
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
							badguys[i].damage();
						}
					}
					else if(col > 2 || this.freindly)
					{
						badguys[i].damage();
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
	}
	public void explode()
	{
		if (!exploding)
		{
			if (!this.bullet )
			{
				this.speedX = 0;
				this.speedY = 0;
				this.row = 1;
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
				spriteImage = spriteMap.getSubimage((col * width), (row * height), width, height);
				}
			else
			{
				reset();	
			}
		}
	}
	public void update(World m)
	{
		
		
		if (this.col == this.colN && exploding)
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
		}
		else
		{
			animateCol();	
		}
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
		{this.type = 1;}
	
}
