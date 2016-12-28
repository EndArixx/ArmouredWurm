package game;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextField;


//this is a new idea i had to help make lvls

/*Level Editor!
 * Goals: to creates a fast and easy way to generate the level txt file that can easily be read in by the main game engine, 
 * this is to help prevent types when creating the level and will also allow the people to see how the level will look visually.
 * 
 *
 *
 *	CONTROLS
 *		WASD 	- Movement
 *	
 *		T 		- Target Next
 *		R 		- Target Previous 
 *	
 *		Arrow	- Move Object
 *
 *		P 		- Save
 *		
 *		N 		- Add Object
 *		M 		- Delete Object
 *
 *		L 		- Change sprite
 *
 *		Z		- Hitbox Y bigger
 *		X 		- Hitbox Y smaller
 *		C 		- Hitbox X bigger
 *		V		- hitbox X smaller		
 *
 *		Shift 	- Hitbox Mode
 *						(shift plus arrow moves hitbox etc.)
 *	
 *		Enter 	- change selected type of object
 *						(cycle between platforms, doors etc.)
 *		Y 		- Target Box
 *						(press Y to get a prompt to select a specific object)
 */






/*
 * Step one		[X]
 * 		Render a background and allow movement using WASD OR Arrow Keys
 * 			Render back 	(X)
 * 			WASD move		(X)
 * Step Two		[X] 
 * 		add ability to load a level
 * 			-load file 		(X)
 * 			-promt user		(X) 
 * Step Three	[]
 * 		add ability to add/delete objects and move around objects
 * 			-platforms		(X)
 * 				+add			<X>
 * 				+move			<X>
 * 				+delete			<X>
 * 			-weathers		()
 * 			-ladders		(X)
 * 			-Other?			()
 * 			-map"Doors"  	(X)
 * 
 * Step yon 	[X]
 * 		add ability to Save
 * 			this will have to keep getting updated
 * 			as new functionality is added.
 * 
 * Step V 		[X]
 * 		add ability to change hitbox
 * 			-Move 			(X)
 * 			-resize			(X)
 * 			-SAVE hitbox	(X)
 * 			-LOAD hitbox	(X)
 * 
 * 			SHIFT 			(X)
 * 				changes so that the controls for the hit box are done pressing shift.
 * 	
 * Step 6		[X]
 * 		add ability to change which image an object is using 
 *			-platforms 		(X)
 *
 * Step 7		[]
 * 		add ability to test the Level on the fly 
 * 		press button, test level, close test goes back to editors
 * Step 8 		[]
 * 		bad guys! 
 * 			load/save  		(X)
 * 			change			(X)
 * 			move			(X)
 * 
 * TODO
 * 		1)Animated platform/badguys support
 */

@SuppressWarnings({ "unused", "serial" })
public class LevelEditor extends Engine implements Runnable, KeyListener
{
	/**
	 * Level Editor
	 */

		//lvl name
	private String lvl = "res/TEST.txt";
	protected int scrollSpeed = 10;
	protected int moveSpeed = 10;

		//Idea for Comments
	//John think about this
	
		//Object targeting
	public int target;
	public boolean targetH, Tprompt;
	private boolean RtargetH;
	private boolean isLoading = false;
	protected String genericPlat; 
	protected String genericMap;
	protected boolean shiftmonitor;
	protected boolean controlmonitor;
	protected boolean tab, tabL;
	
	
	protected int modeCounter,modeTotal;
	
		//this will hold all the different types of sprites
	protected String[] spriteTypes;
	protected String sTypesLoc = "res/SpriteTypes.txt";
	protected int stCounter;
	
	protected String[] enemyspriteTypes;
	protected String enemyTypesLoc = "res/EnemyTypes.txt";
	protected int enemyCounter;
	protected int[] enemySX;
	protected int[] enemySY;
	
	protected String[] bombspriteTypes;
	protected String[] bombblastspriteTypes;
	protected String bombTypesLoc = "res/bombTypes.txt";
	protected int bombCounter;
	protected int[] bombW;
	protected int[] bombH;
	protected int[] bombblastW;
	protected int[] bombblastH;
	protected int[] bombCol;
	protected int[] bombblastCol;
	
		//Movement directions (north,south etc...)
	protected boolean moveN,moveS,moveE,moveW, saving, adding, deleting, addL, delL, changing, chaL; 
	protected boolean hitboxXB,hitboxXS,hitboxYB,hitboxYS;
	
	private boolean promtformapname = true;
	 
		//Constructor
	public LevelEditor(boolean b)
	{
		super(b);
			//this is the constructor for the main engine
		setPreferredSize(window);
		setUp();
		this.addKeyListener(this);	
	}
	
	public void loadSprites(String SpriteDataFile)
	{
			/*
			 * Sprite loader:
			 * This is meant to load all the different kinds of sprites
			 *  into the Editor so that they can be changed on the fly.
			 */
		String name;
		String[] temp;
		BufferedReader br;
		try {
	    	FileReader fr = new FileReader(SpriteDataFile);
	    	br = new BufferedReader(fr);
	        String line = br.readLine();
	        name = line;
	        
	        line = br.readLine();
	        spriteTypes = new String[Integer.parseInt(line)];
	        for(int i = 0;i < spriteTypes.length; i++)
	        {
	        	line = br.readLine();
	        	spriteTypes[i] = line;
	        }
	        fr.close();
	        br.close();
        } catch (IOException e) {e.printStackTrace();}
	}
	
	public void loadEnemySprites(String fileloc)
	{
		/* 
		 * this loads the available sprites for the Enemy Soldiers
		 */
		String name;
		String[] temp;
		BufferedReader br;
		this.enemyCounter = 0;
		try {
	    	FileReader fr = new FileReader(fileloc);
	    	br = new BufferedReader(fr);
	        String line = br.readLine();
	        name = line;
	        
	        line = br.readLine();
	        enemyspriteTypes = new String[Integer.parseInt(line)];
	        enemySX = new int[Integer.parseInt(line)];
	        enemySY = new int[Integer.parseInt(line)];
	        
	        for(int i = 0;i < enemyspriteTypes.length; i++)
	        {
	        	line = br.readLine();
	        	temp = line.split(",");
	        	enemyspriteTypes[i] = temp[0];
	        	enemySX[i] =Integer.parseInt( temp[1]);
	        	enemySY[i] =Integer.parseInt( temp[2]);
	        }
	        fr.close();
	        br.close();
        } catch (IOException e) {e.printStackTrace();}	
	}
	public void loadBombSprites(String fileloc)
	{
		/* 
		 * this loads the available sprites for the Bombs
		 */
		this.bombCounter = 0;
		String name;
		String[] temp;
		BufferedReader br;
		try {
	    	FileReader fr = new FileReader(fileloc);
	    	br = new BufferedReader(fr);
	        String line = br.readLine(); //this gets the first comment
	        name = line;
	        
	        line = br.readLine();
	        int x = Integer.parseInt(line);
	        bombspriteTypes = new String[x];
	        bombblastspriteTypes = new String[x];
	        bombW = new int[x];
	        bombH = new int[x];
	        bombblastW = new int[x];
	        bombblastH = new int[x];
	        bombCol = new int[x];
	        bombblastCol   = new int[x];
	        
	        
	        for(int i = 0;i < enemyspriteTypes.length; i++)
	        {
	        	line = br.readLine();
	        	temp = line.split(",");
	        	bombspriteTypes[i] = temp[0];
	        	bombblastspriteTypes[i] = temp[1];
	        	bombW[i] =Integer.parseInt( temp[2]);
	        	bombH[i] =Integer.parseInt( temp[3]);
	        	bombblastW[i] =Integer.parseInt( temp[4]);
	        	bombblastH[i] =Integer.parseInt( temp[5]);
	        	bombCol[i] = Integer.parseInt( temp[6]);
	        	bombblastCol[i] = Integer.parseInt( temp[7]);
	        }
	        fr.close();
	        br.close();
        } catch (IOException e) {e.printStackTrace();}	
	}
	public void saveLevel(String lvlname)
	{
		
		if(this.SavePause >= System.currentTimeMillis() - 5000)
		{
			return; //This only allows you to save every five seconds
		}
		SavePause = System.currentTimeMillis();
		try
		{
			FileWriter fw = new FileWriter(lvlname);
				
				//Write out Name
			fw.write(lvlname + "\n");
				//Write out background data
			if(gameWorld != null) fw.write(gameWorld.tileName + ","
					+ gameWorld.tileRow + ","
					+ gameWorld.tileCol + ","
					+ gameWorld.tileWidth + ","
					+ gameWorld.tileHeight + "\n" );
			else fw.write("null\n");
				
				//Write world Size
			fw.write(theWorld.width + ","
					+theWorld.height + "\n");
			
			
				//Writeout item data
			fw.write(weather.length + ","
					+ ladders.length + ","
					+ platforms.length + "," 
					+ doors.length + ","
					+ badguys.length + ","
					+ spikes.length +  ","
					+ bombs.length+ ","
					+ healthpicks.length + ","
					+ parallax.length + ","
					+ saveZone.length + ","
					+ "0,0,0,0,0\n");
				
			
				//Write out weathers
			fw.write("# Weather-------------------------------------------\n");
			for(int i = 0;i < weather.length; i++)
			{
				fw.write(weather[i].name + ","
						+(int) weather[i].speedX + ","
						+(int) weather[i].trueY + "\n");
			}
			fw.write("# Ladders-------------------------------------------\n");
				//Write out ladders
			for(int i = 0;i < ladders.length; i++)
			{
				fw.write(ladders[i].name + ","
						+ ladders[i].trueX + ","
						+ ladders[i].trueY + ","
						+ ladders[i].hitbox[0] + ","
						+ ladders[i].hitbox[1] + ","
						+ ladders[i].hitbox[2] + ","
						+ ladders[i].hitbox[3] + "\n");
			}
			fw.write("# Platforms-----------------------------------------\n");
				//Write out Platforms
			for(int i = 0;i < platforms.length; i++)
			{		//This is special logic for platforms
				if(!platforms[i].isAnimated())
					{
						fw.write(platforms[i].name + ","
						+ platforms[i].trueX + ","
						+ platforms[i].trueY + ","
						+ platforms[i].hitbox[0] + ","
						+ platforms[i].hitbox[1] + ","
						+ platforms[i].hitbox[2] + ","
						+ platforms[i].hitbox[3] + "\n");
					}
				else
				{
					//Animation 
					fw.write(platforms[i].name + ","
							+ platforms[i].trueX + ","
							+ platforms[i].trueY + ","
							+ platforms[i].getWidth() + ","
							+ platforms[i].getHeight() + ","
							+ platforms[i].getRowN() + ","
							+ platforms[i].getColN() + ","
							+(int) platforms[i].timerspeed + ","
							+	"Blarg" + "," //This is unknown O_O
							+ platforms[i].hitbox[0] + ","
							+ platforms[i].hitbox[1] + ","
							+ platforms[i].hitbox[2] + ","
							+ platforms[i].hitbox[3] + ","
							);
					//Destruction
						if(platforms[i].isDestroyable())
						{
							fw.write("destroy>" +platforms[i].getDestroyedSprite()+
									">"+platforms[i].getDestColN() +
									">"+platforms[i].getDestRowN() +
									",");
						}
						else
						{
							fw.write("ND,");
						}
					//Movement
						if(platforms[i].getMoving())
						{
							fw.write("Move>" 
									+platforms[i].getLeftPatrol()  + ">"
									+platforms[i].getRightPatrol() + ">"
									+platforms[i].patrolSpeed + ">"
									+platforms[i].patrolTimer + ","
									);
						}
						fw.write("\n");
				}
			}
			fw.write("# Doors---------------------------------------------\n");
				//write out doors
			for(int i = 0; i < doors.length; i++)
			{
				fw.write(doors[i].name + ","
						+ doors[i].trueX + ","
						+ doors[i].trueY + ","
						+ doors[i].playerloc[0] + ","
						+ doors[i].playerloc[1] + ","
						+ doors[i].newMap+","
						+ doors[i].mapstart[0] +","
						+ doors[i].mapstart[1] +","
						+ doors[i].hitbox[0] + ","
						+ doors[i].hitbox[1] + ","
						+ doors[i].hitbox[2] + ","
						+ doors[i].hitbox[3] + "\n");
			}
			fw.write("# Soldiers------------------------------------------\n");
				//write out Soldiers
			for(int i = 0; i < badguys.length; i++)
			{
				fw.write(badguys[i].name + ","
						+badguys[i].spritetop + ","
						+badguys[i].spriteleg + ","
						+badguys[i].trueX + ","
						+badguys[i].trueY + ","
						+badguys[i].width + ","
						+badguys[i].height + ","
						+badguys[i].row + ","
						+badguys[i].col + ",");
				fw.write(badguys[i].patrolL+","
						+badguys[i].patrolR+","
						+badguys[i].hitbox[0] + ","
						+badguys[i].hitbox[1] + ","
						+badguys[i].hitbox[2] + ","
						+badguys[i].hitbox[3] + "\n");
			}
			fw.write("# Spikes--------------------------------------------\n");
				//write out spikes
			for(int i = 0; i < spikes.length; i++)
			{
				fw.write(spikes[i].name +","
						+spikes[i].trueX + ","
						+spikes[i].trueY +","
						+spikes[i].width +","
						+spikes[i].height +","
						+spikes[i].rowN +","
						+spikes[i].colN +","
						+(int) spikes[i].timerspeed +","         //JOHN HERE NEEDS WORK
						+spikes[i].damageNum + ","
						+spikes[i].hitbox[0] +","
						+spikes[i].hitbox[1] +","
						+spikes[i].hitbox[2] +","
						+spikes[i].hitbox[3] +",");
					//Destruction
				if(spikes[i].isDestroyable())
				{
					fw.write("destroy>" +spikes[i].getDestroyedSprite()+
							">"+spikes[i].getDestColN() +
							">"+spikes[i].getDestRowN() +
							",");
				}
				else
				{
					fw.write("ND,");
				}
					//Movement
				if(spikes[i].getMoving())
				{
					fw.write("Move>" 
							+spikes[i].getLeftPatrol()  + ">"
							+spikes[i].getRightPatrol() + ">"
							+spikes[i].patrolSpeed + ">"
							+spikes[i].patrolTimer + ","
							);
				}
				else
				{
					fw.write("NM");
				}
				fw.write("\n");
			}
			fw.write("# Bombs---------------------------------------------\n");
				//write out bombs
			for(int i = 0; i< bombs.length; i++)
			{
				fw.write(bombs[i].bombimage + ","
						+bombs[i].blast + ","
						+bombs[i].getTrueX() + ","
						+bombs[i].getTrueY() + ","
						+bombs[i].getWidth() + ","
						+bombs[i].getHeight() + ","
						+bombs[i].getType() +","
						+bombs[i].blastW + ","
						+bombs[i].blastH + ","
						+bombs[i].getID() + ","
						+bombs[i].rockcolN + ","
						+bombs[i].explcolN + ","
						+bombs[i].rocketspeed + ","
						+bombs[i].blastspeed + ","
						+"\n");
			}
			fw.write("# HealthPickups-------------------------------------\n");
				//write out health pickups
			for(int i = 0; i < healthpicks.length; i++)
			{
				fw.write(healthpicks[i].name + ","
						+healthpicks[i].getTrueX() + ","
						+healthpicks[i].getTrueY() + ","
						+healthpicks[i].getWidth() + ","
						+healthpicks[i].getHeight() + "," 
						+healthpicks[i].getColN() + ","
						+(int) healthpicks[i].timerspeed + ","
						+healthpicks[i].getValue() + 
						"\n");
			}
			fw.write("# Parallax------------------------------------------\n");
				//write out parallax
			for(int i = 0; i < parallax.length; i++)
			{
				fw.write(parallax[i].name +","
						//+parallax[i].getTrueX() + ","
						//+parallax[i].getTrueY() + ","
						+parallaxStart[0][i] + ","
						+parallaxStart[1][i] + ","
						+parallax[i].getParSpeed()
						+"\n");
			}
			fw.write("# SaveZone------------------------------------------\n");
			for(int i = 0; i < saveZone.length; i++)
			{
				fw.write(saveZone[i].name + ","
						+saveZone[i].getTrueX() +","
						+saveZone[i].getTrueY() +","
						+saveZone[i].getWidth() +","
						+saveZone[i].getHeight() +","
						+saveZone[i].getRowN()+ ","
						+saveZone[i].getColN()+","
						+(int) saveZone[i].timerspeed+
						"\n"
						);
				
			}
			fw.write("# NewPlatforms--------------------------------------\n");
				//Write out The single direction platforms
					//John look into animating these
			for(int i = 0;i < onewayUnderPlatform.length; i++)
			{
				fw.write(onewayUnderPlatform[i].name + ","
						+ onewayUnderPlatform[i].trueX + ","
						+ onewayUnderPlatform[i].trueY + ","
						+ onewayUnderPlatform[i].hitbox[0] + ","
						+ onewayUnderPlatform[i].hitbox[1] + ","
						+ onewayUnderPlatform[i].hitbox[2] + ","
						+ onewayUnderPlatform[i].hitbox[3] + "\n");
			}
			for(int i = 0;i < onewayUnderPlatform.length; i++)
			{
				fw.write(onewayOverPlatform[i].name + ","
						+ onewayOverPlatform[i].trueX + ","
						+ onewayOverPlatform[i].trueY + ","
						+ onewayOverPlatform[i].hitbox[0] + ","
						+ onewayOverPlatform[i].hitbox[1] + ","
						+ onewayOverPlatform[i].hitbox[2] + ","
						+ onewayOverPlatform[i].hitbox[3] + "\n");
			}
			for(int i = 0;i < onewayUnderPlatform.length; i++)
			{
				fw.write(onewayLeftPlatform[i].name + ","
						+ onewayLeftPlatform[i].trueX + ","
						+ onewayLeftPlatform[i].trueY + ","
						+ onewayLeftPlatform[i].hitbox[0] + ","
						+ onewayLeftPlatform[i].hitbox[1] + ","
						+ onewayLeftPlatform[i].hitbox[2] + ","
						+ onewayLeftPlatform[i].hitbox[3] + "\n");
			}
			for(int i = 0;i < onewayRightPlatform.length; i++)
			{
				fw.write(onewayRightPlatform[i].name + ","
						+ onewayRightPlatform[i].trueX + ","
						+ onewayRightPlatform[i].trueY + ","
						+ onewayRightPlatform[i].hitbox[0] + ","
						+ onewayRightPlatform[i].hitbox[1] + ","
						+ onewayRightPlatform[i].hitbox[2] + ","
						+ onewayRightPlatform[i].hitbox[3] + "\n");
			}
			
			
			
			//fw.write(" \n");
			fw.close();
		
		}catch (IOException e) {e.printStackTrace();}
	}
	
	private void setUp() 
	{	
		this.Error = false;
		soundHandler = new SoundEngine();
		this.SavePause = System.currentTimeMillis();
		
		this.windowHB[0]= 0;
		this.windowHB[1]= 0;
		this.windowHB[2]= window.width;
		this.windowHB[3]= window.height;
			
		lvlspriteData = new HashMap<String,BufferedImage>();
		permaSprites = new HashMap<String,BufferedImage>();
		
		this.saving = false;
		this.adding = false;
		this.deleting = false;
		this.changing = false;
		this.moveN = false;
		this.moveS = false;
		this.moveE = false;
		this.moveW = false;
		
		this.hitboxXB = false;
		this.hitboxXS = false;
		this.hitboxYB = false;
		this.hitboxYS = false;

			//the L stands for limiter, this is makes it so it only happens once per key press.
		this.addL = true;
		this.delL = true;
		this.chaL = true;
			//sprite type counter
		this.stCounter = 0;
		
		
			//Modes
		this.modeTotal = 8;
		this.modeCounter = 0;
		this.tab = false;
		this.tabL = true;
		
		
		Tprompt = true;
		this.targetH= true;
		this.RtargetH = true;
		this.target=0;
		
		shiftmonitor = false;
		controlmonitor = false;
		
		genericMap = "res/testlevel.txt";
		
		if(promtformapname)
		{
			promtformapname = false;
			Component frame = null;
			String s = (String)JOptionPane.showInputDialog(
			                    frame,
			                    "Please enter a map name.",
			                    "File Selector",
			                    JOptionPane.PLAIN_MESSAGE,
			                    null, null,
			                    "map.txt");

			if ((s != null) && (s.length() > 0)) 
			{
				lvl = "res/" +  s;
			}
			else
			{
				//New Map
			}
		}
		
		loadLevel(lvl);
		
			//this is important
		parallaxStart = new int [2][parallax.length]; 
		for(int i = 0; i < parallax.length; i++)
		{
			parallaxStart[0][i] = parallax[i].trueX;
			parallaxStart[1][i] = parallax[i].trueY;
		}
		loadSprites(sTypesLoc);
		loadEnemySprites(enemyTypesLoc);
		loadBombSprites(bombTypesLoc);
		genericPlat =  spriteTypes[0];
	}
	
	public Platform[] platformSwitchSprite(Platform[] inplat)
	{
		if( inplat.length != 0)
		{
			 inplat[target] = new Platform(spriteTypes[stCounter], inplat[target].getTrueX(), inplat[target].getTrueY(),lvlspriteData);
			
			if(stCounter < spriteTypes.length - 1)
			{
				stCounter++;
			}
			else
			{
				stCounter = 0;
			}
		}
		return inplat;
	}
	public Platform[] addPlaform(Platform[] inplat)
	{
		/*create temp thats 1 bigger then plat[] 
		 * add each piece then make a new one for 
		 * the last one;
		 * then finally set plat[] = temp
		 */
		
		Platform temp[] = new Platform[ inplat.length + 1];
		for(int i =0; i < inplat.length;i++)
		{
			temp[i] = inplat[i];
		}
		
		temp[ inplat.length]= new Platform(genericPlat, -theWorld.getX() + window.width/2, -theWorld.getY() + window.height/2,lvlspriteData);
		 inplat = temp;
		target =  inplat.length-1;
		return inplat;
	}
	public Platform[] deletePlatform(Platform[] inplat)
	{
		
		/*
		 * create a tombstone at the target deletion, 
		 * create temp new array that is 1 smaller then plat[]
		 * then add everything but tomb 
		 */
		if(inplat.length == 0)
		{
			return inplat;
		}
		int tracker = 0;
		Platform temp[] = new Platform[inplat.length - 1];
		for(int i =0; i < inplat.length - 1;i++)
		{
			if(i == target && tracker == 0)
			{
				tracker++;
				i--;
			}
			else
			{
				temp[i] = inplat[i + tracker];
			}
		}
		if(this.target != 0)
		{
			this.target--;
		}
		inplat = temp;
		
		return inplat;
		
	}
	public Spike[] platformSwitchSprite( Spike[] inplat)
	{
		if( inplat.length != 0)
		{
			 inplat[target] = new Spike(spriteTypes[stCounter], inplat[target].getTrueX(), inplat[target].getTrueY(),lvlspriteData);
			
			if(stCounter < spriteTypes.length - 1)
			{
				stCounter++;
			}
			else
			{
				stCounter = 0;
			}
		}
		return inplat;
	}
	public Spike[] addPlaform( Spike[] inplat)
	{
		 Spike temp[] = new  Spike[ inplat.length + 1];
		for(int i =0; i < inplat.length;i++)
		{
			temp[i] = inplat[i];
		}
		
		temp[ inplat.length]= new  Spike(genericPlat, -theWorld.getX() + window.width/2, -theWorld.getY() + window.height/2,lvlspriteData);
		temp[inplat.length].damageNum = 5; //JOHN HERE
		inplat = temp; 
		target =  inplat.length-1;
		return inplat;
	}
	public Spike[] deletePlatform(Spike[] inplat)
	{	
		if(inplat.length == 0)
		{
			return inplat;
		}
		int tracker = 0;
		Spike temp[] = new Spike[inplat.length - 1];
		for(int i =0; i < inplat.length - 1;i++)
		{
			if(i == target && tracker == 0)
			{
				tracker++;
				i--;
			}
			else
			{
				temp[i] = inplat[i + tracker];
			}
		}
		if(this.target != 0)
		{
			this.target--;
		}
		inplat = temp;
		return inplat;
	}
	public Explosive[] platformSwitchSprite( Explosive[] inplat)
	{
		if( inplat.length != 0 && spriteTypes.length > 0)
		{
			 inplat[target] = new Explosive(bombspriteTypes[stCounter ],
						bombblastspriteTypes[stCounter ],
						-theWorld.getX() + window.width/2,
						-theWorld.getY() + window.height/2,
						bombW[stCounter ],bombH[stCounter ],0,
						bombblastW[stCounter ],bombblastH[stCounter ],
						0,bombCol[stCounter ],bombblastCol[stCounter ],
						30,10,lvlspriteData);
			
			if(stCounter < spriteTypes.length - 1)
			{
				stCounter++;
			}
			else
			{
				stCounter = 0;
			}
		}
		return inplat;
	}
	public Explosive[] addPlaform( Explosive[] inplat)
	{
		Explosive temp[] = new  Explosive[ inplat.length + 1];
		for(int i =0; i < inplat.length;i++)
		{
			temp[i] = inplat[i];
		}
		
		temp[ inplat.length]= new  Explosive(bombspriteTypes[0],
				bombblastspriteTypes[0],
				-theWorld.getX() + window.width/2,
				-theWorld.getY() + window.height/2,
				bombW[0],bombH[0],0,
				bombblastW[0],bombblastH[0],
				0,bombCol[0],bombblastCol[0],
				30,10,lvlspriteData);
		 inplat = temp;
		target =  inplat.length-1;
		return inplat;
	}
	public Explosive[] deletePlatform(Explosive[] inplat)
	{	
		if(inplat.length == 0)
		{
			return inplat;
		}
		int tracker = 0;
		Explosive temp[] = new Explosive[inplat.length - 1];
		for(int i =0; i < inplat.length - 1;i++)
		{
			if(i == target && tracker == 0)
			{
				tracker++;
				i--;
			}
			else
			{
				temp[i] = inplat[i + tracker];
			}
		}
		if(this.target != 0)
		{
			this.target--;
		}
		inplat = temp;
		return inplat;
	}
	public Door[] doorSwitchSprite(Door[] indoor)
	{
		if( indoor.length != 0)
		{
			 indoor[target] = new Door(
					 spriteTypes[stCounter],
					 indoor[target].getTrueX(),
					 indoor[target].getTrueY(),
					 indoor[target].playerloc[0],
					 indoor[target].playerloc[1],
					 indoor[target].newMap,
					 indoor[target].mapstart[0],
					 indoor[target].mapstart[1],lvlspriteData);
			
			if(stCounter < spriteTypes.length - 1)
			{
				stCounter++;
			}
			else
			{
				stCounter = 0;
			}
		}
		return indoor;
	}
	public Door[] addDoor(Door[] indoor)
	{
		/*create temp thats 1 bigger then plat[] 
		 * add each piece then make a new one for 
		 * the last one;
		 * then finally set plat[] = temp
		 */
		isLoading = true;
		promtformapname = false;
		Component frame = null;
		String s = (String)JOptionPane.showInputDialog(
		                    frame,
		                    "Please enter the Mapdata.",
		                    "Door Creator",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null, null,
		                    "map.txt,0,0,0,0");
		String mapData[] = s.split(",");
		
		if ((s != null) && (s.length() > 0)) 
		{
			lvl = "res/" +  mapData[0];
		}
		else
		{
			return indoor;
		}
		
		Door temp[] = new Door[ indoor.length + 1];
		for(int i =0; i < indoor.length;i++)
		{
			temp[i] = indoor[i];
		}
		
		/*temp[ indoor.length]= new Door(genericPlat,
				-theWorld.getX() + window.width/2,
				-theWorld.getY() + window.height/2
				,0,0,lvl,0,0);*/
		temp[ indoor.length]= new Door(genericPlat,
					-theWorld.getX() + window.width/2,
					-theWorld.getY() + window.height/2
					,Integer.parseInt(mapData[1]),
					Integer.parseInt(mapData[2]),
					lvl,Integer.parseInt(mapData[3]),
					Integer.parseInt(mapData[4]),lvlspriteData);
								
	
		indoor = temp;
	 	isLoading = false;
		target =  indoor.length-1;
		return indoor;
	}
	public Door[] deleteDoor(Door[] inplat)
	{
		
		/*
		 * create a tombstone at the target deletion, 
		 * create temp new array that is 1 smaller then plat[]
		 * then add everything but tomb 
		 */
		if(inplat.length == 0)
		{
			return inplat;
		}
		int tracker = 0;
		Door temp[] = new Door[inplat.length - 1];
		for(int i =0; i < inplat.length - 1;i++)
		{
			if(i == target && tracker == 0)
			{
				tracker++;
				i--;
			}
			else
			{
				temp[i] = inplat[i + tracker];
			}
		}
		if(this.target != 0)
		{
			this.target--;
		}
		inplat = temp;
		
		return inplat;
		
	}
	private Soldier[] soldierSwitchSprite(Soldier[]  inSold) 
	{
		if( inSold.length != 0)
		{
			 inSold[target] = new Soldier(
						enemyspriteTypes[enemyCounter ],
						enemyspriteTypes[enemyCounter],
						"null", 
						-theWorld.getX() + window.width/2,
						-theWorld.getY() + window.height/2,
						enemySX[enemyCounter],
						enemySY[enemyCounter],
						0,
						0,
						lvlspriteData,soundHandler);
			
			if(enemyCounter < spriteTypes.length - 1)
			{
				enemyCounter++;
			}
			else
			{
				enemyCounter = 0;
			}
		}
		return inSold;	
	}
	private Soldier[] deleteSoldier(Soldier[] inSold) 
	{
		/*
		 * create a tombstone at the target deletion, 
		 * create temp new array that is 1 smaller then plat[]
		 * then add everything but tomb 
		 */
		if(inSold.length == 0)
		{
			return inSold;
		}
		int tracker = 0;
		Soldier temp[] = new Soldier[inSold.length - 1];
		for(int i =0; i < inSold.length - 1;i++)
		{
			if(i == target && tracker == 0)
			{
				tracker++;
				i--;
			}
			else
			{
				temp[i] = inSold[i + tracker];
			}
		}
		if(this.target != 0)
		{
			this.target--;
		}
		inSold = temp;
		
		return inSold;
	}
	private Soldier[] addSoldier(Soldier[] inSold) {
		Soldier temp[] = new Soldier[  inSold.length + 1];
		for(int i =0; i <  inSold.length;i++)
		{
			temp[i] =  inSold[i];
		}
		
		temp[  inSold.length]= new Soldier(
				enemyspriteTypes[0],
				enemyspriteTypes[0],
				"null", 
				-theWorld.getX() + window.width/2,
				-theWorld.getY() + window.height/2,
				enemySX[0],
				enemySY[0],
				0,
				0,
				lvlspriteData,soundHandler);
			//Just a quick fix so that the patrol makes sense in the level editor.
		temp[inSold.length].setPatrol(temp[inSold.length].getX()-10,temp[inSold.length].getX()+10);
		inSold = temp;
		target =   inSold.length-1;
		return  inSold;
	}
	public void run()
	{
		screen = createVolatileImage(window.width,window.height); 
		final int TICKS_PER_SECOND = 50;
	    final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
	    final int MAX_FRAMESKIP = 10;
	    long  next_game_tick =  System.currentTimeMillis();
	    int loops;
		while (isRunning)
		{
			loops = 0;
			while(  System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) 
			{
				this.movement();
				this.update();	
				next_game_tick += SKIP_TICKS;
				loops++;
			}
			this.render();
			System.gc();
		}
		this.gameRun=false;
		System.exit(0);
	}	
	public void render()
	{
		int i;		
			//Construct backbuffer. 
				//this prevents the tearing you get as things are rendered.
		Graphics g = screen.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, window.width, window.height);
		

		
		if(!isLoading)
		{
			for(i =0; i < weather.length; i++)
			{
				if(Tools.check_collision(windowHB,weather[i].getHitbox())){weather[i].render(g,lvlspriteData);}
			}
			g.setFont(new Font("MonoSpace", Font.PLAIN, 20)); 

				//backgrounds
			if(gameWorld != null) gameWorld.render(g, windowHB,lvlspriteData);
			
			for(i = 0; i < parallax.length;i++)
			{
				if(Tools.check_collision(windowHB,parallax[i].getHitbox()))
				{
					parallax[i].render(g,lvlspriteData );
					g.setColor(Color.GREEN);
					g.drawString("["+i+"]",parallax[i].getX(),parallax[i].getY());
				}
			}
			
				//render the platforms
			for(i = 0; i < platforms.length;i++)
			{
				if(Tools.check_collision(windowHB,platforms[i].getHitbox()))
				{
					platforms[i].render(g,lvlspriteData);
					g.setColor(Color.RED);
					g.drawString("["+i+"]",platforms[i].getX(),platforms[i].getY());
				}
			}
			for(i = 0; i < ladders.length;i++)
			{
				if(Tools.check_collision(windowHB,ladders[i].getHitbox()))
				{
					ladders[i].render(g,lvlspriteData);
					g.setColor(Color.GREEN);
					g.drawString("["+i+"]",ladders[i].getX(),ladders[i].getY());
				}
			}
			for(i = 0; i < doors.length;i++)
			{
				if(Tools.check_collision(windowHB,doors[i].getHitbox()))
				{
					doors[i].render(g,lvlspriteData);
					g.setColor(Color.BLUE);
					g.drawString("["+i+"]",doors[i].getX(),doors[i].getY());
				}
			}
			for(i = 0; i < spikes.length; i++)
			{
				if(Tools.check_collision(windowHB,spikes[i].getHitbox()))
				{
					spikes[i].render(g,lvlspriteData );
					g.setColor(Color.ORANGE);
					g.drawString("["+i+"]",spikes[i].getX(),spikes[i].getY());
				}
			}
			for(i = 0; i < saveZone.length; i++)
			{
				if(Tools.check_collision(windowHB,saveZone[i].getHitbox()))
				{
					saveZone[i].render(g,lvlspriteData );
					g.setColor(Color.BLUE);
					g.drawString("["+i+"]",saveZone[i].getX(),saveZone[i].getY());
				}
			}
			for(i = 0; i < healthpicks.length; i++)
			{
				if(Tools.check_collision(windowHB,healthpicks[i].getHitbox()))
				{
					healthpicks[i].render(g,lvlspriteData );
					g.setColor(Color.RED);
					g.drawString("["+i+"]",healthpicks[i].getX(),healthpicks[i].getY());
				}
			}
			for(i = 0; i  < bombs.length; i++)
			{
				if(Tools.check_collision(windowHB,bombs[i].getHitbox()))
				{
					bombs[i].render(g,lvlspriteData);
					g.setColor(Color.PINK);
					g.drawString("["+i+"]",bombs[i].getX(),bombs[i].getY());
					
				}
			}
			for(i = 0; i < badguys.length; i++)
			{
				if(Tools.check_collision(windowHB,badguys[i].getHitbox()))
				{
					badguys[i].render(g,lvlspriteData);
					g.setColor(Color.YELLOW);
					g.drawString("["+i+"]",badguys[i].getX(),badguys[i].getY());
				}
			}
			

			if(modeCounter == 0)
			{
				if(platforms.length != 0)
				{
					g.setColor(Color.RED);
					g.drawRect(platforms[target].hitbox[0]+platforms[target].x,
						platforms[target].hitbox[1]+platforms[target].y,
						platforms[target].hitbox[2], 
						platforms[target].hitbox[3]);
					g.drawString("Platform ["+target+"]",50,50);
				}
			}
			else if(modeCounter == 1)
			{
				if(ladders.length != 0)
				{
					g.setColor(Color.GREEN);
					g.drawRect(ladders[target].hitbox[0]+ladders[target].x,
							ladders[target].hitbox[1]+ladders[target].y,
							ladders[target].hitbox[2], 
							ladders[target].hitbox[3]);
					g.drawString("Ladder ["+target+"]",50,50);
				}
			}
			else if(modeCounter == 2)
			{
				if(doors.length != 0)
				{
					g.setColor(Color.BLUE);
					g.drawRect(doors[target].hitbox[0]+doors[target].x,
							doors[target].hitbox[1]+doors[target].y,
							doors[target].hitbox[2], 
							doors[target].hitbox[3]);
					g.drawString("Door ["+target+"]",50,50);
				}
			}
			else if(modeCounter == 3)
			{	//Soldiers
				if(badguys.length != 0)
				{
					g.setColor(Color.YELLOW);
					g.drawRect(badguys[target].hitbox[0]+badguys[target].x,
							badguys[target].hitbox[1]+badguys[target].y,
							badguys[target].hitbox[2], 
							badguys[target].hitbox[3]);
					g.drawString("Bad Guy ["+target+"]",50,50);
					
					g.drawRect(badguys[target].patrolL + theWorld.getX(),theWorld.getY() + badguys[target].trueY,badguys[target].patrolR- badguys[target].patrolL,badguys[target].height/2);
				}
			}
			else if(modeCounter == 4)
			{
				if(spikes.length != 0)
				{
					g.setColor(Color.ORANGE);
					g.drawRect(spikes[target].hitbox[0]+spikes[target].x,
							spikes[target].hitbox[1]+spikes[target].y,
							spikes[target].hitbox[2], 
							spikes[target].hitbox[3]);
					g.drawString("Spike ["+target+"]",50,50);
				}
			}
			else if(modeCounter == 5)
			{
				if(bombs.length != 0)
				{
					g.setColor(Color.PINK);
					g.drawRect(bombs[target].hitbox[0]+bombs[target].x,
							bombs[target].hitbox[1]+bombs[target].y,
							bombs[target].hitbox[2], 
							bombs[target].hitbox[3]);
					g.drawString("Bomb ["+target+"]",50,50);
				}
			}
			else if(modeCounter == 6)
			{
				if(healthpicks.length != 0)
				{
					g.setColor(Color.RED);
					g.drawRect(healthpicks[target].hitbox[0]+healthpicks[target].x,
							healthpicks[target].hitbox[1]+healthpicks[target].y,
							healthpicks[target].hitbox[2], 
							healthpicks[target].hitbox[3]);
					g.drawString("Heath Pickup ["+target+"]",50,50);
				}
			}
			else if(modeCounter == 7)
			{
				if(parallax.length != 0)
				{
					g.setColor(Color.GREEN);
					g.drawRect(parallax[target].hitbox[0]+parallax[target].x,
							parallax[target].hitbox[1]+parallax[target].y,
							parallax[target].hitbox[2], 
							parallax[target].hitbox[3]);
					g.drawString("Parallax ["+target+"]",50,50);
				}
			}
			
		}
		if(isLoading)
		{
			loading.render(g,lvlspriteData);
		}
		
			// Get window's graphics object. 
		g = getGraphics();
			// Draw backbuffer to window. 
		g.drawImage(screen,0,0,window.width,window.height,0,0,window.width, window.height,null);
			//NO CODEBELOW!
		g.dispose();				
	}
	public  void update()
	{	
		if(!isLoading)
		{
			
			if(tab && tabL)
			{				
				tab = false;
				tabL= false;
				
				target = 0;
				
				if(modeCounter < modeTotal -1)
				{
					modeCounter++;
				}
				else
				{
					modeCounter= 0;
				}
			}

				//Selected item will have Red hitbox?
				//visible hitbox?
			theWorld.update();
			int i;
			for(i = 0; i < parallax.length;i++)
			{
				if(i != 0)//John rid this
				{
					parallax[i].setTrueX( (int)( parallaxStart[0][i]+(theWorld.x*parallax[i].getParSpeed())));
					parallax[i].setTrueY( (int)( parallaxStart[1][i]+(theWorld.y*parallax[i].getParSpeed())));
				}
				parallax[i].update();
				
			}
			for(i = 0; i < platforms.length;i++)
			{
				platforms[i].update(theWorld);
			}
			for(i = 0; i < ladders.length;i++)
			{
				ladders[i].update(theWorld);
			}
			for(i = 0; i < doors.length;i++)
			{
				doors[i].update(theWorld);
			}
			for(i = 0; i < badguys.length; i++)
			{
				badguys[i].update(theWorld);
			}
			for(i =0; i < spikes.length; i++)
			{
				spikes[i].update(theWorld);
			}
			for(i = 0; i < healthpicks.length; i++)
			{
				healthpicks[i].update(theWorld);
			}
			for(i = 0; i < bombs.length; i++)
			{
				bombs[i].update(theWorld);
			}
			if(gameWorld != null)gameWorld.update(theWorld);
			for(i =0; i < weather.length; i++)
			{
				weather[i].update(theWorld);
			}
			
			
			if(saving)
			{
				saveLevel(lvlName);
			}
			
			//------------------------------------PLATFORM MODE!
			if(modeCounter == 0)
			{
					//Adding a new platform
				if(adding && addL)
				{
					addL = false;
					adding = false;
					
					isLoading = true;
					platforms = addPlaform(platforms);
					isLoading = false;
				}
					//Deleting
				if(deleting && delL)
				{
					delL = false;
					deleting = false;
					 
					isLoading = true;
					platforms = deletePlatform(platforms);
					isLoading = false;
				}
				
					//changing the Sprite
				if(changing && chaL)
				{
					chaL = false;
					changing = false;
				
					isLoading = true;
					platforms = platformSwitchSprite(platforms);
					isLoading = false;
				}
			}
			else if(modeCounter == 1)
			{
					//Adding a new platform
				if(adding && addL)
				{
					addL = false;
					adding = false;
					
					isLoading = true;
					ladders = addPlaform(ladders);
					isLoading = false;
				}
					//Deleting
				if(deleting && delL)
				{
					delL = false;
					deleting = false;
					 
					isLoading = true;
					ladders = deletePlatform(ladders);
					isLoading = false;
				}
				
					//changing the Sprite
				if(changing && chaL)
				{
					chaL = false;
					changing = false;
				
					isLoading = true;
					ladders = platformSwitchSprite(ladders);
					isLoading = false;
				}
			}
			else if(modeCounter == 2)
			{
					//Adding a new Door
				if(adding && addL)
				{
					addL = false;
					adding = false;
					
					isLoading = true;
					doors = addDoor(doors);
					isLoading = false;
				}
					//Deleting
				if(deleting && delL)
				{
					delL = false;
					deleting = false;
					 
					isLoading = true;
					doors = deleteDoor(doors);
					isLoading = false;
				}
				
					//changing the Sprite
				if(changing && chaL)
				{
					chaL = false;
					changing = false;
				
					isLoading = true;
					doors = doorSwitchSprite(doors);
					isLoading = false;
				}
			}
			else if(modeCounter == 3)
			{
				//Adding a new Soldier 
				if(adding && addL)
				{
					addL = false;
					adding = false;
					
					isLoading = true;
					badguys = addSoldier(badguys);
					isLoading = false;
				}
					//Deleting
				if(deleting && delL)
				{
					delL = false;
					deleting = false;
					 
					isLoading = true;
					badguys = deleteSoldier(badguys);
					isLoading = false;
				}
				
					//changing the Sprite
				if(changing && chaL &&(enemyCounter > 1)) //needs work
				{
					chaL = false;
					changing = false;
				
					isLoading = true;
					badguys = soldierSwitchSprite(badguys);
					isLoading = false;
				}
			}
			else if(modeCounter == 4)
			{
					//Adding a new platform
				if(adding && addL)
				{
					addL = false;
					adding = false;
					
					isLoading = true;
					spikes = addPlaform(spikes);
					isLoading = false;
				}
					//Deleting
				if(deleting && delL)
				{
					delL = false;
					deleting = false;
					 
					isLoading = true;
					spikes = deletePlatform(spikes);
					isLoading = false;
				}
				
					//changing the Sprite
				if(changing && chaL)
				{
					chaL = false;
					changing = false;
				
					isLoading = true;
					spikes = platformSwitchSprite(spikes);
					isLoading = false;
				}
			}
			else if(modeCounter == 5)
			{
				//Adding a new Explosive
				if(adding && addL)
				{
					addL = false;
					adding = false;
					
					isLoading = true;
					bombs = addPlaform(bombs);
					isLoading = false;
				}
					//Deleting
				if(deleting && delL)
				{
					delL = false;
					deleting = false;
					 
					isLoading = true;
					bombs = deletePlatform(bombs);
					isLoading = false;
				}
				
					//changing the Sprite
				if(changing && chaL)
				{
					chaL = false;
					changing = false;
				
					isLoading = true;
					bombs = platformSwitchSprite(bombs);
					isLoading = false;
				}
			}
			else if(modeCounter == 6)
			{
					//Adding a new Medkit
				if(adding && addL)
				{
					addL = false;
					adding = false;
					
					isLoading = true;
					healthpicks = addPlaform(healthpicks);
					isLoading = false;
				}
					//Deleting
				if(deleting && delL)
				{
					delL = false;
					deleting = false;
					 
					isLoading = true;
					healthpicks  = deletePlatform(healthpicks);
					isLoading = false;
				}
				
					//changing the Sprite
				if(changing && chaL)
				{
					chaL = false;
					changing = false;
				
					isLoading = true;
					healthpicks = platformSwitchSprite(healthpicks);
					isLoading = false;
				}
			}
		}
			
	}

	private void movement()
	{
		//WINDOW MOVEMENT-------------------------
			//MOVE WINDOW NORTH
		if(N)
		{
			if (theWorld.getY() < 0)
			{
				theWorld.moveYn(-scrollSpeed);
				/*for(int i = 1; i< parallax.length;i++)
				{
					parallax[i].moveYn(-(int) (scrollSpeed / parallax[i].getParSpeed()));
					
				}*/
			}
		}
			//MOVE WINDOW SOUTH
		if(S)
		{
			if(-theWorld.getY()+window.getHeight()<theWorld.getHeight())
			{
				theWorld.moveYn(scrollSpeed);
				/*for(int i = 1; i< parallax.length;i++)
				{
					parallax[i].moveYn((int) (scrollSpeed / parallax[i].getParSpeed()));
				}*/
			}
		}
			//MOVE WINDOW WEST
		if(W)
		{
			if(theWorld.getX() < 0)
			{
				theWorld.moveXp(scrollSpeed);
				/*for(int i = 1; i< parallax.length;i++)
				{
					parallax[i].moveXp((int) (scrollSpeed / parallax[i].getParSpeed()));
				}*/
			}
		}
			//MOVE WINDOW EAST
		if(E)
		{
			if(-theWorld.getX() < theWorld.getWidth()-window.width)
			{
				theWorld.moveXp(-scrollSpeed);
				/*for(int i = 1; i< parallax.length;i++)
				{
					parallax[i].moveXp(-(int) (scrollSpeed / parallax[i].getParSpeed()));
				}*/
			}
		}
		if(!shiftmonitor && !controlmonitor)
		{
			if(modeCounter == 0)
			{
				//PLATFORM MOVEMENT-------------------------------------
					//MOVE PLATFORM NORTH
				platformMove(platforms);
			}
			else if(modeCounter == 1)
			{
				//LADDER MOVEMENT-------------------------------------
					//MOVE LADDER NORTH
				platformMove(ladders);
			}
			else if(modeCounter == 2)
			{
				platformMove(doors);
			}
			else if(modeCounter == 3)
			{
				platformMove(badguys);		
			}
			else if(modeCounter == 4)
			{
				platformMove(spikes);
			}
			else if(modeCounter == 5)
			{
				platformMove(bombs);
			}
			else if(modeCounter == 6)
			{
				platformMove(healthpicks);
			}
			else if (modeCounter == 7)
			{
				platformMove(parallax);
				
					//this is to fix a saving bug
				if(moveN && parallax.length !=0)
				{
					if(parallax[target].getTrueY() > (0 - theWorld.getHeight()))
					{
						//parallax[target].moveYn(this.moveSpeed);
						parallaxStart[1][target] -= this.moveSpeed;
					}
				}
					//MOVE PLATFORM SOUTH
				if(moveS && parallax.length !=0)
				{
					if(parallax[target].getTrueY() < (theWorld.getHeight() ))// - mover[target].getHeight()))
					{
						//parallax[target].moveYp(this.moveSpeed);
						parallaxStart[1][target] += this.moveSpeed;
					}
				}
					//MOVE PLATFORM EAST
				if(moveE && parallax.length !=0)
				{
					if(parallax[target].getTrueX() < (theWorld.getWidth() ))//- mover[target].getWidth()))
					{
						//parallax[target].moveXp(this.moveSpeed);
						parallaxStart[0][target] += this.moveSpeed;
					}
				}
					//MOVE PLATFORM WEST
				if(moveW && parallax.length !=0)
				{
					if(parallax[target].getTrueX() > (0-parallax[target].getWidth())) 
					{
						//parallax[target].moveXn(this.moveSpeed);
						parallaxStart[0][target] -= this.moveSpeed;
					}
				}
			}
		}
		else if(shiftmonitor && !controlmonitor)
		{
			/*
			 * Hitbox stuff:
			 * 		First Movement
			 * 			change location of hitbox
			 * 		Second Resize
			 * 			change size of hitbox
			 */
			
			int PHmove = 10;
				//Hitbox resizing/moving
			if(modeCounter == 0)
			{
			//---------------------------------Moving
				platformHitBox(platforms, PHmove);
				
			}
			else if(modeCounter == 1)
			{
			//---------------------------------Moving
				platformHitBox(ladders, PHmove);
			}
			else if(modeCounter == 2)
			{
				platformHitBox(doors,PHmove);
			}
			else if(modeCounter == 3)
			{
				platformHitBox(badguys,PHmove);
			}
			else if(modeCounter == 4)
			{
				platformHitBox(spikes, PHmove);
			}
			else if(modeCounter == 5)
			{
				platformHitBox(bombs,PHmove);
			}
			else if(modeCounter == 6)
			{
				platformHitBox(healthpicks,PHmove);
			}
			else if(modeCounter == 7)
			{
				platformHitBox(parallax,PHmove);
			}
		}
		else if(controlmonitor)
		{
			if(modeCounter == 3)
			{
				if(badguys.length !=0)
				{
					if(moveE)
					{
						badguys[target].patrolL += this.moveSpeed; 
					}
					if(moveW)
					{
						badguys[target].patrolL -= this.moveSpeed; 
					}
					
					if(moveN)
					{
						badguys[target].patrolR -= this.moveSpeed; 
					}
					if(moveS)
					{
						badguys[target].patrolR += this.moveSpeed; 
					}
				}
			}
		}
	}
	private void platformMove(Soldier[] mover) 
	{
		if(mover.length != 0)
		{
			if(moveN)
			{
				if(mover[target].getTrueY() > - mover[target].getWidth())
				{
					mover[target].trueY -= this.moveSpeed;
				}
			}
				//MOVE PLATFORM SOUTH
			if(moveS)
			{
				if(mover[target].getTrueY() < (theWorld.getHeight() - mover[target].getHeight()))
				{
					mover[target].trueY += this.moveSpeed;
				}
			}
				//MOVE PLATFORM EAST
			if(moveE)
			{
				if(mover[target].getTrueX() < (theWorld.getWidth() - mover[target].getWidth()))
				{
					mover[target].trueX += this.moveSpeed;
				}
			}
				//MOVE PLATFORM WEST
			if(moveW)
			{
				if(mover[target].getTrueX() > 0)
				{
					mover[target].trueX -= this.moveSpeed;
				}
			}
		}
	}
	public void platformMove(Platform[] mover)
	{
		if(moveN && mover.length !=0)
		{
			if(mover[target].getTrueY() > (0 - theWorld.getHeight()))
			{
				mover[target].moveYn(this.moveSpeed);
			}
		}
			//MOVE PLATFORM SOUTH
		if(moveS && mover.length !=0)
		{
			if(mover[target].getTrueY() < (theWorld.getHeight() ))// - mover[target].getHeight()))
			{
				mover[target].moveYp(this.moveSpeed);
			}
		}
			//MOVE PLATFORM EAST
		if(moveE && mover.length !=0)
		{
			if(mover[target].getTrueX() < (theWorld.getWidth() ))//- mover[target].getWidth()))
			{
				mover[target].moveXp(this.moveSpeed);
			}
		}
			//MOVE PLATFORM WEST
		if(moveW && mover.length !=0)
		{
			if(mover[target].getTrueX() > (0-mover[target].getWidth())) 
			{
				mover[target].moveXn(this.moveSpeed);
			}
		}
	}
	public void platformHitBox(Sprite[] inplat, int PHmove)
	{
			if(moveN && inplat.length !=0)
			{
				inplat[target].setHitbox(
						inplat[target].hitbox[0],
						inplat[target].hitbox[1] - PHmove,
						inplat[target].hitbox[2],
						inplat[target].hitbox[3]);
			}
			if(moveS && inplat.length !=0)
			{
				inplat[target].setHitbox(
						inplat[target].hitbox[0],
						inplat[target].hitbox[1] + PHmove,
						inplat[target].hitbox[2],
						inplat[target].hitbox[3]);
			}
			if(moveE && inplat.length !=0)
			{
				inplat[target].setHitbox(
						inplat[target].hitbox[0]+ PHmove,
						inplat[target].hitbox[1],
						inplat[target].hitbox[2],
						inplat[target].hitbox[3]);
			}
			if(moveW && inplat.length !=0)
			{
				inplat[target].setHitbox(
						inplat[target].hitbox[0]- PHmove,
						inplat[target].hitbox[1],
						inplat[target].hitbox[2],
						inplat[target].hitbox[3]);
			}
			
			//---------------------------------Resizing
			if(hitboxXB && inplat.length !=0)
			{
				inplat[target].setHitbox(
						inplat[target].hitbox[0],
						inplat[target].hitbox[1],
						inplat[target].hitbox[2] + PHmove,
						inplat[target].hitbox[3]);
			}
			if(hitboxXS && inplat.length !=0)
			{
				inplat[target].setHitbox(
						inplat[target].hitbox[0],
						inplat[target].hitbox[1],
						inplat[target].hitbox[2] - PHmove,
						inplat[target].hitbox[3]);
			}
			if(hitboxYB && inplat.length !=0)
			{
				inplat[target].setHitbox(
						inplat[target].hitbox[0],
						inplat[target].hitbox[1],
						inplat[target].hitbox[2],
						inplat[target].hitbox[3] + PHmove);
			}
			if(hitboxYS && inplat.length !=0)
			{
				inplat[target].setHitbox(
						inplat[target].hitbox[0],
						inplat[target].hitbox[1],
						inplat[target].hitbox[2],
						inplat[target].hitbox[3] - PHmove);
			}
			
		}
	public void keyPressed(KeyEvent key) 
	{
		
		switch (key.getKeyCode())
		{
			//------------------------Shift switches to hitbox mode
			case KeyEvent.VK_SHIFT:
				shiftmonitor = true;
					break;
					
			case KeyEvent.VK_CONTROL:
				controlmonitor = true;
					break;
					
			//----------------------------------------TAB mode
			case KeyEvent.VK_ENTER:
				if(tabL)
				{
					tab = true;
				}
				break;
					
			//-------------------------(Y)
			case KeyEvent.VK_W: //UP
				N=true;
					break;
			case KeyEvent.VK_S: //DOWN
				S=true;
					break;
			//-------------------------(X)
			case KeyEvent.VK_A: //LEFT
				W = true;
					break;
					
			case KeyEvent.VK_D: //RIGHT
				E = true;
					break;
					
			//------------------------------OTHER
			case KeyEvent.VK_Q: //Quit
				isRunning = false;
					break;
					
			case KeyEvent.VK_L: //Load new sprite image
				if(chaL)
				{
					this.changing = true;
				}
					break;
			//---------------------------------ADDING/DELETING
			case KeyEvent.VK_N:	//Adding
				if(addL)
				{
					this.adding = true;
				}
				break;
			case KeyEvent.VK_M:	//Deleting
				if(delL)
				{
					this.deleting = true;
				}
				break;
			//----------------------------------TargetPrompt	
			case KeyEvent.VK_Y:
				if(Tprompt)
				{
					Tprompt = false;
					Component frame = null;
					if(modeCounter == 0)
					{
						String[] numbs = new String[platforms.length];
						for(int i =0;i< platforms.length; i++)
						{
							numbs[i] = i + "";
						}
						String s = (String)JOptionPane.showInputDialog(
					                    frame,
					                    "Please enter a number between 0 and "+ platforms.length,
					                    "TARGET",
					                    JOptionPane.PLAIN_MESSAGE,
					                    null,
					                    numbs,
					                    "0");

						if ((s != null) && (s.length() > 0)) 
						{
							if(Integer.parseInt(s) > -1 && Integer.parseInt(s) < platforms.length)
							{
								target = Integer.parseInt(s);
							}
						}
						else
						{
							//hmmm
						}
					}
					else if(modeCounter == 1)
					{
						String[] numbs = new String[ladders.length];
						for(int i =0;i< ladders.length; i++)
						{
							numbs[i] = i + "";
						}
						String s = (String)JOptionPane.showInputDialog(
					                    frame,
					                    "Please enter a number between 0 and "+ ladders.length,
					                    "TARGET",
					                    JOptionPane.PLAIN_MESSAGE,
					                    null,
					                    numbs,
					                    "0");

						if ((s != null) && (s.length() > 0)) 
						{
							if(Integer.parseInt(s) > -1 && Integer.parseInt(s) < ladders.length)
							{
								target = Integer.parseInt(s);
							}
						}
						else
						{
							//hmmm
						}
					}
					else if(modeCounter == 2)
					{
						String[] numbs = new String[doors.length];
						for(int i =0;i< doors.length; i++)
						{
							numbs[i] = i + "";
						}
						String s = (String)JOptionPane.showInputDialog(
					                    frame,
					                    "Please enter a number between 0 and "+ doors.length,
					                    "TARGET",
					                    JOptionPane.PLAIN_MESSAGE,
					                    null,
					                    numbs,
					                    "0");

						if ((s != null) && (s.length() > 0)) 
						{
							if(Integer.parseInt(s) > -1 && Integer.parseInt(s) < doors.length)
							{
								target = Integer.parseInt(s);
							}
						}
						else
						{
							//hmmm
						}
					}
					else if(modeCounter == 3)
					{
						String[] numbs = new String[badguys.length];
						for(int i =0;i< badguys.length; i++)
						{
							numbs[i] = i + "";
						}
						String s = (String)JOptionPane.showInputDialog(
					                    frame,
					                    "Please enter a number between 0 and "+ badguys.length,
					                    "TARGET",
					                    JOptionPane.PLAIN_MESSAGE,
					                    null,
					                    numbs,
					                    "0");

						if ((s != null) && (s.length() > 0)) 
						{
							if(Integer.parseInt(s) > -1 && Integer.parseInt(s) < badguys.length)
							{
								target = Integer.parseInt(s);
							}
						}
						else
						{
							//hmmm
						}
					}
					
					else if(modeCounter == 4)
					{
						String[] numbs = new String[spikes.length];
						for(int i =0;i< spikes.length; i++)
						{
							numbs[i] = i + "";
						}
						String s = (String)JOptionPane.showInputDialog(
					                    frame,
					                    "Please enter a number between 0 and "+ spikes.length,
					                    "TARGET",
					                    JOptionPane.PLAIN_MESSAGE,
					                    null,
					                    numbs,
					                    "0");

						if ((s != null) && (s.length() > 0)) 
						{
							if(Integer.parseInt(s) > -1 && Integer.parseInt(s) < spikes.length)
							{
								target = Integer.parseInt(s);
							}
						}
						else
						{
							//hmmm
						}
					}
					else if(modeCounter == 5)
					{
						String[] numbs = new String[bombs.length];
						for(int i =0;i< bombs.length; i++)
						{
							numbs[i] = i + "";
						}
						String s = (String)JOptionPane.showInputDialog(
					                    frame,
					                    "Please enter a number between 0 and "+ bombs.length,
					                    "TARGET",
					                    JOptionPane.PLAIN_MESSAGE,
					                    null,
					                    numbs,
					                    "0");

						if ((s != null) && (s.length() > 0)) 
						{
							if(Integer.parseInt(s) > -1 && Integer.parseInt(s) < bombs.length)
							{
								target = Integer.parseInt(s);
							}
						}
						else
						{
							//hmmm
						}
					}
					else if(modeCounter == 6)
					{
						String[] numbs = new String[healthpicks.length];
						for(int i =0;i< healthpicks.length; i++)
						{
							numbs[i] = i + "";
						}
						String s = (String)JOptionPane.showInputDialog(
					                    frame,
					                    "Please enter a number between 0 and "+ healthpicks.length,
					                    "TARGET",
					                    JOptionPane.PLAIN_MESSAGE,
					                    null,
					                    numbs,
					                    "0");

						if ((s != null) && (s.length() > 0)) 
						{
							if(Integer.parseInt(s) > -1 && Integer.parseInt(s) < healthpicks.length)
							{
								target = Integer.parseInt(s);
							}
						}
						else
						{
							//hmmm
						}
					}
					else if(modeCounter == 7)
					{
						String[] numbs = new String[parallax.length];
						for(int i =0;i< parallax.length; i++)
						{
							numbs[i] = i + "";
						}
						String s = (String)JOptionPane.showInputDialog(
					                    frame,
					                    "Please enter a number between 0 and "+ parallax.length,
					                    "TARGET",
					                    JOptionPane.PLAIN_MESSAGE,
					                    null,
					                    numbs,
					                    "0");

						if ((s != null) && (s.length() > 0)) 
						{
							if(Integer.parseInt(s) > -1 && Integer.parseInt(s) < parallax.length)
							{
								target = Integer.parseInt(s);
							}
						}
						else
						{
							//hmmm
						}
					}
				}
				break;
			//---------------------------TARGETING
			case KeyEvent.VK_T: //Target
				if(targetH)
				{
					if(modeCounter == 0)
					{
						if(target < platforms.length-1)
						{
							this.target++;
						}
						else
						{
							this.target = 0;
						}
					}
					else if(modeCounter == 1)
					{
						if(target < ladders.length-1)
						{
							this.target++;
						}
						else
						{
							this.target = 0;
						}
					}
					else if(modeCounter == 2)
					{
						if(target < doors.length-1)
						{
							this.target++;
						}
						else
						{
							this.target = 0;
						}
					}
					else if(modeCounter == 3)
					{
						if(target < badguys.length-1)
						{
							this.target++;
						}
						else
						{
							this.target = 0;
						}
					}
					else if(modeCounter == 4)
					{
						if(target < spikes.length-1)
						{
							this.target++;
						}
						else
						{
							this.target = 0;
						}
					}
					else if(modeCounter == 5)
					{
						if(target < bombs.length-1)
						{
							this.target++;
						}
						else
						{
							this.target = 0;
						}
					}
					else if(modeCounter == 6)
					{
						if(target < healthpicks.length-1)
						{
							this.target++;
						}
						else
						{
							this.target = 0;
						}
					}
					else if(modeCounter == 7)
					{
						if(target < parallax.length-1)
						{
							this.target++;
						}
						else
						{
							this.target = 0;
						}
					}
					this.targetH=false;
				}
					break;
			case KeyEvent.VK_R: //reverse Target
				if(RtargetH)
				{
					
					if(modeCounter == 0)
					{
						if(target > 0)
						{
							this.target--;
						}
						else
						{
							this.target = platforms.length-1;
						}
					}
					else if(modeCounter == 1)
					{
						if(target > 0)
						{
							this.target--;
						}
						else
						{
							this.target = ladders.length-1;
						}
					}
					else if(modeCounter == 2)
					{
						if(target > 0)
						{
							this.target--;
						}
						else
						{
							this.target = doors.length-1;
						}
					}
					else if(modeCounter == 3)
					{
						if(target > 0)
						{
							this.target--;
						}
						else
						{
							this.target = badguys.length-1;
						}
					}
					else if(modeCounter == 4)
					{
						if(target > 0)
						{
							this.target--;
						}
						else
						{
							this.target = spikes.length-1;
						}
					}
					else if(modeCounter == 5)
					{
						if(target > 0)
						{
							this.target--;
						}
						else
						{
							this.target = bombs.length-1;
						}
					}
					else if(modeCounter == 6)
					{
						if(target > 0)
						{
							this.target--;
						}
						else
						{
							this.target = healthpicks.length-1;
						}
					}
					else if(modeCounter == 7)
					{
						if(target > 0)
						{
							this.target--;
						}
						else
						{
							this.target = parallax.length-1;
						}
					}
					this.RtargetH=false;
				}
					break;
			//------------------------------------MOVEMENT
					//-------------------------(Y)
			case KeyEvent.VK_UP: //UP
				moveN=true;
					break;
			case KeyEvent.VK_DOWN: //DOWN
				moveS=true;
					break;
					//-------------------------(X)
			case KeyEvent.VK_LEFT: //LEFT
				moveW = true;
					break;
					
			case KeyEvent.VK_RIGHT: //RIGHT
				moveE = true;
					break;
					
			//---------------------------------HIT BOX STuff
					//-------------------------(Y)
			case KeyEvent.VK_C: //bigger
				hitboxXB = true;
					break;	
			case KeyEvent.VK_V: //smaller
				hitboxXS = true;
					break;	
					//-------------------------(X)
			case KeyEvent.VK_X: //bigger
				hitboxYB=true;
					break;
			case KeyEvent.VK_Z: //smaller
				hitboxYS=true;

				break;
				
			//----------------------------------FILE IO
					//SAVE
			case KeyEvent.VK_P:
				if(!saving)
				{
					saving = true;
				}
					break;
					
		}
	}
	public void keyReleased(KeyEvent key) 
	{
			//check the key then turn off that direction
		switch (key.getKeyCode())
		{
			//------------------------Shift switches to hitbox mode
			case KeyEvent.VK_SHIFT:
				shiftmonitor = false;
					break;
					
			case KeyEvent.VK_CONTROL:
				controlmonitor = false;
					break;
					
			//----------------------------------------TAB mode
			case KeyEvent.VK_ENTER:
				tabL = true;
				break;

					
			//-------------------------(Y)
			case KeyEvent.VK_W: //UP
				N=false;
					break;
			case KeyEvent.VK_S: //DOWN
				S=false;
					break;
			//-------------------------(X)
			case KeyEvent.VK_A: //LEFT
				W = false;
					break;
				
			case KeyEvent.VK_D: //RIGHT
				E = false;
					break;	

			//---------------------------TARGETING
			case KeyEvent.VK_T: //Target
				this.targetH = true;
					break;
			case KeyEvent.VK_R: //Reverse Target
				this.RtargetH = true;
					break;
			case KeyEvent.VK_Y:
				Tprompt = true;
				break;
			//---------------------------------ADDING/DELETING
			case KeyEvent.VK_N:	//Adding
				this.addL = true;
					break;
			case KeyEvent.VK_M:	//Deleting
				this.delL = true;
					break;
			//------------------------------------MOVEMENT
					//-------------------------(Y)
			case KeyEvent.VK_UP: //UP
				moveN=false;
					break;
			case KeyEvent.VK_DOWN: //DOWN
				moveS=false;
					break;
					//-------------------------(X)
			case KeyEvent.VK_LEFT: //LEFT
				moveW = false;
					break;
					
			case KeyEvent.VK_RIGHT: //RIGHT
				moveE = false;
					break;
					
			//---------------------------------HIT BOX RESIZE
					//-------------------------(X)
			case KeyEvent.VK_C: //bigger
				hitboxXB = false;
					break;	
			case KeyEvent.VK_V: //smaller
				hitboxXS = false;
					break;	
					//-------------------------(Y)
			case KeyEvent.VK_X: //bigger
				hitboxYB = false;
					break;
			case KeyEvent.VK_Z: //smaller
				hitboxYS = false;
					break;
					
			//----------------------------------FILE IO
			case KeyEvent.VK_P:	 //SAVE
				saving = false;
					break;
			//------------------------------------OTHER
			case KeyEvent.VK_L: //Load new sprite image
				this.chaL = true;
				break;
		}
		
	}
	
	
	public static void main(String[] args) 
	{
			//Set up
		LevelEditor primeGame = new LevelEditor(false);
		JFrame gameFrame = new JFrame();
		gameFrame.add(primeGame);
		gameFrame.pack();
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setTitle(windowName);
		gameFrame.setVisible(true);
			//yeah close it when it is done.
		gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		
			/*
			 * new Idea:
			 * 		add ability to test game without saving or closing
			 * 
			 *		close level editor and run an engine, 
			 *		then when the engine closes go back to the editor?
			 *	
			 *		use a button to switch from editor. 
			 *		closing engine will reopen editor.
			 */
		
			//Game Time
		primeGame.start();
		while(primeGame.gameRun)
		{
			//Game is currently running
		}
		
			//Close on exit
		gameFrame.dispose();
		
	}
}
