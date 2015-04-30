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
 * 			change			()
 * 			move			()
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
		 * this loads the available sprites for the player.
		 */
		String name;
		String[] temp;
		BufferedReader br;
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
	public void saveLevel(String lvlname)
	{
		try
		{
			FileWriter fw = new FileWriter("res/" + lvlname);
				
				//Write out Name
			fw.write(lvlname + "\n");
				//Write out background data
			fw.write(gameWorld.tileName + ","
					+ gameWorld.tileRow + ","
					+ gameWorld.tileCol + ","
					+ gameWorld.tileWidth + ","
					+ gameWorld.tileHeight + "\n" );
				//Write world Size
			fw.write(theWorld.width + ","
					+theWorld.height + "\n");
			
			
				//Writeout item data
			fw.write(weather.length + ","
					+ ladders.length + ","
					+ platforms.length + "," 
					+ doors.length + ","
					+ badguys.length +
					",0" + "\n");
										//these zeros are for future additions
			
				//Write out weathers
			for(int i = 0;i < weather.length; i++)
			{
				fw.write(weather[i].name + ","
						+(int) weather[i].speedX + ","
						+(int) weather[i].trueY + "\n");
			}
			
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
			
				//Write out Platforms
			for(int i = 0;i < platforms.length; i++)
			{
				fw.write(platforms[i].name + ","
						+ platforms[i].trueX + ","
						+ platforms[i].trueY + ","
						+ platforms[i].hitbox[0] + ","
						+ platforms[i].hitbox[1] + ","
						+ platforms[i].hitbox[2] + ","
						+ platforms[i].hitbox[3] + "\n");
			}
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
						+badguys[i].col + ","); //HERE
				fw.write(badguys[i].patrolL+","
						+badguys[i].patrolR+","
						+badguys[i].hitbox[0] + ","
						+badguys[i].hitbox[1] + ","
						+badguys[i].hitbox[2] + ","
						+badguys[i].hitbox[3] + "\n");
			}
				
			//fw.write(" \n");
			fw.close();
		
		}catch (IOException e) {e.printStackTrace();}
	}
	
	private void setUp() 
	{	
		this.Error = false;
		
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
		this.modeTotal = 4;
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
		loadSprites(sTypesLoc);
		loadEnemySprites(enemyTypesLoc);
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
						lvlspriteData);
			
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
				lvlspriteData);
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
			
				//background
			gameWorld.render(g, windowHB,lvlspriteData);

			g.setFont(new Font("MonoSpace", Font.PLAIN, 20)); 

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
					g.drawString("["+target+"]",50,50);
				}
			}
			if(modeCounter == 1)
			{
				if(ladders.length != 0)
				{
					g.setColor(Color.GREEN);
					g.drawRect(ladders[target].hitbox[0]+ladders[target].x,
							ladders[target].hitbox[1]+ladders[target].y,
							ladders[target].hitbox[2], 
							ladders[target].hitbox[3]);
					g.drawString("["+target+"]",50,50);
				}
			}
			if(modeCounter == 2)
			{
				if(doors.length != 0)
				{
					g.setColor(Color.BLUE);
					g.drawRect(doors[target].hitbox[0]+doors[target].x,
							doors[target].hitbox[1]+doors[target].y,
							doors[target].hitbox[2], 
							doors[target].hitbox[3]);
					g.drawString("["+target+"]",50,50);
				}
			}
			if(modeCounter == 3)
			{	//Soldiers
				if(badguys.length != 0)
				{
					g.setColor(Color.YELLOW);
					g.drawRect(badguys[target].hitbox[0]+badguys[target].x,
							badguys[target].hitbox[1]+badguys[target].y,
							badguys[target].hitbox[2], 
							badguys[target].hitbox[3]);
					g.drawString("["+target+"]",50,50);
					
					g.drawRect(badguys[target].patrolL + theWorld.getX(),theWorld.getY() + badguys[target].trueY,badguys[target].patrolR- badguys[target].patrolL,badguys[target].height/2);
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
			
			gameWorld.update(theWorld);
			for(i =0; i < weather.length; i++)
			{
				weather[i].update(theWorld);
			}
			
			
			if(saving) //THIS IS WHERE IT SAVES FOR NOW!
			{
				saveLevel("TEST.txt");
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
			if(modeCounter == 1)
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
			if(modeCounter == 2)
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
			if(modeCounter == 3)
			{
				//Adding a new Door
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
			}
		}
			//MOVE WINDOW SOUTH
		if(S)
		{
			if(-theWorld.getY()+window.getHeight()<theWorld.getHeight())
			{
				theWorld.moveYn(scrollSpeed);
			}
		}
			//MOVE WINDOW WEST
		if(W)
		{
			if(theWorld.getX() < 0)
			{
				theWorld.moveXp(scrollSpeed);
			}
		}
			//MOVE WINDOW EAST
		if(E)
		{
			if(-theWorld.getX() < theWorld.getWidth()-window.width)
			{
				theWorld.moveXp(-scrollSpeed);
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
			if(mover[target].getTrueY() > 0)
			{
				mover[target].moveYn(this.moveSpeed);
			}
		}
			//MOVE PLATFORM SOUTH
		if(moveS && mover.length !=0)
		{
			if(mover[target].getTrueY() < (theWorld.getHeight() - mover[target].getHeight()))
			{
				mover[target].moveYp(this.moveSpeed);
			}
		}
			//MOVE PLATFORM EAST
		if(moveE && platforms.length !=0)
		{
			if(mover[target].getTrueX() < (theWorld.getWidth() - mover[target].getWidth()))
			{
				mover[target].moveXp(this.moveSpeed);
			}
		}
			//MOVE PLATFORM WEST
		if(moveW && mover.length !=0)
		{
			if(mover[target].getTrueX() > 0)
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
					if(modeCounter == 1)
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
					if(modeCounter == 2)
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
					if(modeCounter == 3)
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
					if(modeCounter == 1)
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
					if(modeCounter == 2)
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
					if(modeCounter == 3)
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
					if(modeCounter == 1)
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
					if(modeCounter == 2)
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
					if(modeCounter == 3)
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
