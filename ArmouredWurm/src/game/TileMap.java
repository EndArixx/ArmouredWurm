package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Map;

public class TileMap
{
	protected String tileName; 
	private Platform[] Tiles;
	
	protected int tileRow;
	protected int tileCol;
	protected int tileWidth;
	protected int tileHeight;
	
	public TileMap(String mapdata, int row, int col,int width ,int height,  Map<String,BufferedImage> spriteData)
	{
		tileName = mapdata;
		tileRow = row;
		tileCol = col;
		tileWidth = width;
		tileHeight = height;
		
		
		Tiles = new Platform[row * col]; 
		int x = 0;
		int y;
		String targetdata;
		for(int i = 0; i < row; i++)
		{
			for(int j = 0; j < col ; j++)
			{
				y = x+1;
				targetdata = mapdata + y + ".png";
				Tiles[x] = new Platform(targetdata, j*width, i*height, spriteData);
				x++;
			}
		}
	}
	public Platform[] getTiles()
	{
		return Tiles;
	}
	public void render(Graphics g, int[] windowHB,  Map<String,BufferedImage> spriteData)
	{
		for (int i = 0; i < Tiles.length; i++)
		{			
			if(Tools.check_collision(windowHB, Tiles[i].getHitbox()))
			{
					Tiles[i].render(g, spriteData);
			}
		}
	}
	public void update(World map)
	{
		for (int i = 0; i < Tiles.length; i++)
		{
			Tiles[i].update(map);
		}
	}
}
