package game;

public class Door extends Platform {
	protected String newMap;
	protected int[] playerloc;
	protected int[] mapstart;
	
	public Door(String spriteloc, int x, int y, int[] ploc, String mapname, int[] maps)
	{
		super(spriteloc, x, y);
		this.newMap= mapname;
		this.playerloc = ploc;
		this.mapstart = maps;
		
	}
}
