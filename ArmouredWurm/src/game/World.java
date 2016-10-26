package game;

public class World
{
	protected int  x , y, width , height, speedX, speedY;
	protected int hitbox[] = new int[4];
	public World(int i, int j) 
	{
			this.width = i;
			this.height= j;
			speedX= 0;
			speedY= 0;
			x = 0;
			y = 0;
			this.hitbox[0] = 0;
			this.hitbox[1] = 0;
			this.hitbox[2] = width;
			this.hitbox[3] = height;
	}
	
	public void update()
	{
		this.x = this.x + speedX;
		this.y = this.y + speedY;
	}
	public void setHitbox(int x, int y, int width, int height)
	{
		this.hitbox[0] = x;
		this.hitbox[1] = y;
		this.hitbox[2] = width;
		this.hitbox[3] = height;
	}
	public int[] getHitbox()
	{
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
	public void moveX(double speed)
	{
		this.x = (int) (this.x + speed);
	}
	public void moveY(double speed)
	{
		this.y = (int) (this.y + speed);
	}
	//LEGACY JOHN REMOVE THESE!
	public void moveXp(double speed)
	{
		this.x = (int) (this.x + speed);
	}
	public void moveYp(double speed)
	{
		this.y = (int) (this.y + speed);
	}
	public void moveXn(double speed)
	{
		this.x = (int) (this.x - speed);
	}
	public void moveYn(double speed)
	{
		this.y = (int) (this.y - speed);
	}

}
