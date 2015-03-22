package game;

public class Door extends Platform {
	protected String newMap;
	protected int[] playerloc;
	protected int[] mapstart;
	
	public Door(String spriteloc, int x, int y, int plocx,int plocy, String mapname, int mapsx,int mapsy)
	{
		super(spriteloc, x, y);
		this.newMap= mapname;
		this.playerloc = new int[2];
		this.playerloc[0] = plocx;
		this.playerloc[1] = plocy;
		this.mapstart = new int[2];
		this.mapstart[0] = mapsx;
		this.mapstart[1] = mapsy;
		
		
	}
}
