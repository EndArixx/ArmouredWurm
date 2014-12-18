package game;

import java.awt.Graphics;

public class TileMap
{
	private Platform[] Tiles;
	public TileMap(String mapdata, int row, int col,int width ,int height)
	{
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
				Tiles[x] = new Platform(targetdata, j*width, i*height);
				x++;
			}
		}
	}
	public Platform[] getTiles()
	{
		return Tiles;
	}
	public void render(Graphics g, int[] windowHB)
	{
		for (int i = 0; i < Tiles.length; i++)
		{			
			if(Tools.check_collision(windowHB, Tiles[i].getHitbox()))
			{
					Tiles[i].render(g);
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
