package game;

public class Platform extends Sprite
{
	protected int trueX, trueY;
	public Platform(String spriteloc, int x, int y)
	{
		super(spriteloc,x,y);
		trueX = x;
		trueY = y;
		this.speedX = 0;
		this.speedY = 0;
	}
	public Platform(String inImage,int x,int y,int width ,int height,int rowN,int colN,int timerspeed) 
	{
		super(inImage,x,y,width,height,rowN, colN,timerspeed);
		trueX = x;
		trueY = y;
		this.speedX = 0;
		this.speedY = 0;
		
	}
	public void update(World theWorld)
	{
		this.trueX = (int) (this.trueX + speedX);
		this.trueY = (int) (this.trueY + speedY);
		x = theWorld.getX() + trueX;
		y = theWorld.getY() + trueY;
	}
	public void reset()
	{
		this.speedX = 0;
		this.speedY = 0;
		this.trueX = -width;
		this.trueY = -height;
		this.col = 0;
	}
	public int getTrueX()
		{return this.trueX;}
	public int getTrueY()
		{return this.trueY;}
	public void setTrueX(int x)
		{this.trueX = x;}
	public void setTrueY(int y)
		{this.trueY = y;}

}
