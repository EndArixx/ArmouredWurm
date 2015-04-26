package game;

import java.awt.image.BufferedImage;
import java.util.Map;

public class BadGuy extends Platform
{
	public BadGuy(String spriteloc, int x, int y,  Map<String,BufferedImage> spriteData)
	{
		super(spriteloc,x,y ,spriteData);
	}
	public void damage()
	{
		this.reset();
	}
	
}
