package game;

public class gun extends Sprite
{
	protected PlayerChar master;
	protected int ammoCount,projspeed,projspeedX,projspeedY, hands[], barrel[];
	protected Explosive[] ammo;
	protected boolean cooldown;
	public gun(String weaponImage, PlayerChar master, Explosive[] ammo,int projspeed) 
	{
		super(weaponImage, master.getX(), master.getY());
		this.master = master;
		this.ammo = ammo;
		this.ammoCount = 0;
		this.speedX = 0;
		this.speedY = 0;
		this.timer = 0;
		this.timerspeed = 50; 
		this.cooldown = true;
		this.projspeed = projspeed;
		this.projspeedX = projspeed;
		this.projspeedY = 0;
		this.hands = new int[2];
		this.hands[0] = 0;
		this.hands[1] = 56;
		this.barrel = new int[2];
		this.barrel[0] = 0;
		this.barrel[1] = 56;
		
	}	
	public int getprojSpeed()
	{
		return this.projspeed;
	}
	public void Fire(World theWorld)
	{
		if(cooldown)
		{
			ammo[ammoCount].setArmed(true);
			ammo[ammoCount].setTrueX(barrel[0] + master.getX() - theWorld.getX());
			ammo[ammoCount].setTrueY(barrel[1] + master.getY() - theWorld.getY());
			ammo[ammoCount].setYspeed(0);
			if (master.getFacingForward())
			{
				ammo[ammoCount].setXspeed(projspeedX);
				ammo[ammoCount].setYspeed(projspeedY);
				ammo[ammoCount].setRow(0);
			}
			else
			{
				ammo[ammoCount].setXspeed(-projspeedX);
				ammo[ammoCount].setYspeed(projspeedY);
				ammo[ammoCount].setRow(1);
			}
			ammoCount++;
			timer = 1;
			cooldown = false;
		}
		
	}
	public void update()
	{
		if(ammoCount == ammo.length)
		{
			ammoCount = 0;
		}
		ammo[ammoCount].setTrueX(-ammo[ammoCount].getWidth());
		ammo[ammoCount].setTrueY(0);
		ammo[ammoCount].setXspeed(0);
		ammo[ammoCount].setYspeed(0);
		this.x = master.getX()+ this.hands[0];
		this.y = master.getY()+ this.hands[1];
		if(timer%timerspeed == 0)
		{
			timer =0;
			cooldown = true;
		}
		timer++;
	}
	public void target(int xspeed, int yspeed)
	{
		projspeedX = xspeed;
		projspeedY = yspeed;
	}
	
	
}
