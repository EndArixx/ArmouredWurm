package game;

import java.awt.image.BufferedImage;
import java.util.Map;

public class Door extends Platform {
	protected String newMap;
	protected int[] playerloc;
	protected int[] mapstart;
	
	public Door(String spriteloc, int x, int y, int plocx,int plocy, String mapname, int mapsx,int mapsy,  Map<String,BufferedImage> spriteData)
	{
		super(spriteloc, x, y,spriteData);
		this.newMap= mapname;
		this.playerloc = new int[2];
		this.playerloc[0] = plocx;
		this.playerloc[1] = plocy;
		this.mapstart = new int[2];
		this.mapstart[0] = mapsx;
		this.mapstart[1] = mapsy;
		
		
	}
}
