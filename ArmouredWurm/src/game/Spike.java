package game;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Queue;

public class Spike extends Platform
{
	public int HP;
	public int damageNum;
	public boolean invol;
	public int type; 
	public Spike(String spriteloc, int x, int y,  Map<String,BufferedImage> spriteData)
	{
		super(spriteloc,x,y,spriteData);
	}
	public Spike(String inImage,int x,int y,int width ,int height,int rowN,int colN,int timerspeed,int damage, Map<String,BufferedImage> spriteData)
	{
		super(inImage,x,y,width ,height, rowN,colN,timerspeed, spriteData);
		this.HP = 100;
		this.damageNum = damage;
		this.invol = true;
		this.type = 5;
	}
	public void update(World theWorld,Queue<DamageHitbox> damageQ)
	{
		super.update(theWorld);
		if(this.HP < 0)
		{
			this.reset();
		}
		DamageHitbox out = new DamageHitbox( this.x + this.hitbox[0], this.y + this.hitbox[1] ,+ this.hitbox[2],+ this.hitbox[3], damageNum , type);
		damageQ.add(out);
		
		
	}
	
	public void damage(int amount)
	{
		this.HP -= amount;
	}
	
	
}
