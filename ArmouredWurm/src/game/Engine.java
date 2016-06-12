/*
 * 
 * Armoured Wurm
 * created by: John Stanley
 * 
 * 
 * 
 */



package game;

import game.triggers.InputList;
import game.triggers.MoveStack;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings({ "serial", "unused" })
public class Engine  extends Applet implements Runnable, KeyListener 
{

	public String version = "Version 1.0.214";
		//For Testing hitboxes 
	public final static boolean renderHitBox = false;
	public boolean isEngine;
		//THESE ARE VARIABLES!
	public Soldier badguys[];
	public Spike spikes[];
	public Platform platforms[];
	public Platform ladders[];
	public Platform parallax[];
	protected int parallaxStart[][]; 
	public Door doors[];
	public Explosive bombs[];
	public World theWorld;
	public TileMap gameWorld;
	public Looper weather[];
	public PlayerChar player;
	public PlayerChar player2; //JOHN REMOVE!!!!!!!!!!!!!!!!!!!!
	public Sprite playerHP;
	public Sprite playerHPpips;
	public int hx, hy,hxP,hyP, hpPipLen; //HPsW,
	public Platform healthpicks[];	; Platform saveZone[];
		//Stack
	public MoveStack mStack;
		//One way Platforms
	public Platform onewayUnderPlatform[];
	public Platform onewayOverPlatform[];
	public Platform onewayLeftPlatform[];
	public Platform onewayRightPlatform[];
	
	public gun hammer[]; 
	public Explosive bullets[] = new Explosive[4];
	public Explosive missiles[] = new Explosive[4];
	
		//A buffer to hold sprites to free up redundancy
	public Map<String,BufferedImage> lvlspriteData;
	public Map<String,BufferedImage> permaSprites;
	
		//this is for the window
	public static Dimension window  = new Dimension(1280,720);
	public int windowHB[] = new int[4];

	
		//it is important that you have both of these so the game closes
		//	in the correct order.
	protected boolean isRunning = false;
	public boolean gameRun =true;
	
	protected Image screen;
	protected static String windowName = "Armored Wurm";
		//control schemes
			//0 = left  handed
			//1 = right handed
	int ControlS = 1;
	
		//Variables for cardinal directions.
	protected boolean N,S,W,E,F;
		// combat variables
	protected int attackL,attackH,attackS;
		//these are holders to smooth out movement
	private boolean Wh,Eh,Jh;
	
		//save File 
	private String saveName,saveFileName;
	
		//Loading screen variables
	private String lvl = "res/TEST.txt";
	private boolean isLoading;
	private boolean isLoadingF;
	private boolean loadCont;
	private int loadTarget;
	public Sprite loading,loadingF;
	
		//to fix a popping bug
	boolean oneupdate;
	
		//Sound Zone
	protected SoundEngine soundHandler;
	
		//DamageHitboxs Zone
	private Queue<DamageHitbox> damageQ = new LinkedList<DamageHitbox>();
		//John do you need this?
	private Queue<DamageHitbox> damageQhitbox = new LinkedList<DamageHitbox>();
	
		//Pause Menu Data
	private boolean isPaused = false;
	private int pauseButnum = 0;
	private int pauseMax = 2;
	public Sprite pauseMenu;
	public Sprite pauseButtons[];
	private int restartdata[];
		//pause selector and helper;
	private boolean ps, psh;
	
	
		//main menu stuff
	private boolean inMainMenu = false;
	private int mainMenuButnum = 0;
	private int mainMenuMax;
	public Sprite mainMenu;
	public Sprite mainMenuButtons[];
	private boolean terminator = false;
	private boolean start = false;
		//main menu Enter &  mainmenuEnterHelper
	private boolean mme,mmeh;
	
	public String lvlName;
		//game over screen
	public Sprite gameover;
	public boolean  isGameOver;
	public boolean Error;
//constructor---------------------------------------------------------------------------------------	
	public Engine(boolean b)
	{
			//this is the constructor for the main engine
		if(b)
		{
			setPreferredSize(window);
			setUp();
			isEngine = true;
			this.addKeyListener(this);	
		}
		else
		{
			isEngine = false;
		}
	}
	
	protected void loadLevel(String lvlname)
	{	
			//this makes sure the load screen always shows up
		if(isEngine) this.render();
		
			//memory stuff 
		if(screen != null)
		{
			screen.flush();
		}
		screen = createVolatileImage(window.width,window.height); 
		isLoadingF = false;
		//isPaused = true;
		lvlspriteData.clear();
		badguys = null;
		spikes = null;
		platforms= null;
		ladders = null;
		parallax = null;
		doors = null;
		bombs = null;
		theWorld = null;
		gameWorld = null;
		weather = null;
		healthpicks = null;
		onewayUnderPlatform = null;
		onewayOverPlatform = null;
		onewayLeftPlatform = null;
		onewayRightPlatform = null;
		System.gc();
		
		int t = 0;
		
		lvlName = lvlname;
			//this is designed to load in a specifically designed "Map" file 
		String name;
		String[] temp;
			//This is for special, platforms and spikes
		String[] temp2;
		BufferedReader br;
	    try {
	    	FileReader fr = null;
	    	InputStream is = null;
	    	InputStreamReader isr = null;
	    		//Favor external files, then internal
	    	if(new File(lvlname).isFile())
	    	{
	    		t = 1;
	    		fr = new FileReader(lvlname);
	    		br = new BufferedReader(fr);
	    	}
	    	else //John add a new case for if neither exists
	    	{
	    		t = 2;
	    		is = getClass().getResourceAsStream("/"+lvlname);
	    		isr = new InputStreamReader(is);
	    		br = new BufferedReader(isr);
	    	}
	    	
	        String line = Tools.readlineadv(br);
	        name = line;
	        
	        	//Load in the TileData
	        line = Tools.readlineadv(br);
	        	//John comment logic, ALso look into removing the redundancy 
	        if(!line.equals("null"))
	        {
	        temp = line.split(",");
	        gameWorld = new TileMap(temp[0],
	        		Integer.parseInt(temp[1]),
	        		Integer.parseInt(temp[2]), 
	        		Integer.parseInt(temp[3]), 
	        		Integer.parseInt(temp[4]),lvlspriteData);
	        }
	        	//Load in World Data
	        line = Tools.readlineadv(br);
	        temp = line.split(",");
	        theWorld = new World(Integer.parseInt(temp[0]),
	        		Integer.parseInt(temp[1])); 
	        
	        	//Load in Lvl object
	        line = Tools.readlineadv(br);
	        temp = line.split(",");
	        	//John switch these, you dont need the ints!
	        int wnum = Integer.parseInt(temp[0]);
	        weather = new Looper[wnum];
	        
	        int lnum  = Integer.parseInt(temp[1]);
	        ladders = new Platform[lnum];

	        
	        int pnum  = Integer.parseInt(temp[2]);
	        platforms = new Platform[pnum];

	        int dnum =  Integer.parseInt(temp[3]);
	        doors = new Door[dnum];
	        
	        int bnum = Integer.parseInt(temp[4]);
	        badguys = new Soldier[bnum];
	        
	        int snum = Integer.parseInt(temp[5]);
	        spikes = new Spike[snum];
	        		
	        int bombnum = Integer.parseInt(temp[6]);
	        bombs = new Explosive[bombnum];
	        
	        int hnum = Integer.parseInt(temp[7]);
	        healthpicks = new Platform[hnum];
	        
	        int paranum = Integer.parseInt(temp[8]);
	        parallax = new Platform[paranum];
	        parallaxStart = new int[2][paranum]; 

	        int savenum = Integer.parseInt(temp[9]);
	        saveZone = new Platform[savenum];
	        
	        int onePlatUndernum = Integer.parseInt(temp[10]);
	        onewayUnderPlatform = new Platform[onePlatUndernum];
	        
	        int onePlatOvernum = Integer.parseInt(temp[11]);
	    	onewayOverPlatform = new Platform[onePlatOvernum];
	    	
	    	int onePlatLeftnum = Integer.parseInt(temp[12]);
	    	onewayLeftPlatform = new Platform[onePlatLeftnum];
	    	
	    	int onePlatRightnum = Integer.parseInt(temp[13]);
	    	onewayRightPlatform = new Platform[onePlatRightnum];
	        
	        	//first load in looping background stuff
	        for (int i= 0; i < wnum; i++)
	        {
	        	line = Tools.readlineadv(br);
        		temp = line.split(",");
        		weather[i] = new Looper(temp[0],Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),lvlspriteData);
        	}
	        	//second load in ladders
	        for (int i= 0; i < lnum; i++)
	        {
	        	line = Tools.readlineadv(br);
	        	if(line.charAt(0) == '#')
	        	{
	        			//This ignores comments
	        		i--;
	        	}
	        	else
	        	{
		 	        temp = line.split(",");
		        	ladders[i] = new Platform(temp[0],Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),lvlspriteData);
		        	if(temp.length >= 4)
		 	        {
		 	        	ladders[i].setHitbox(
		 	        			Integer.parseInt(temp[3]),
		 	        			Integer.parseInt(temp[4]),
		 	        			Integer.parseInt(temp[5]),
		 	        			Integer.parseInt(temp[6]));
		 	        }
	        	}
	        }
	        	//third load in Platforms
	        for (int i= 0; i < pnum; i++)
	        {
	        	line = Tools.readlineadv(br);
		 	        temp = line.split(",");
	 	        	if(temp.length <= 8)
		 	        {
	 	        		platforms[i] = new Platform(temp[0],
	 	        				Integer.parseInt(temp[1]),
	 	        				Integer.parseInt(temp[2]),
	 	        				lvlspriteData);
		 	        	if(temp.length >= 4)
		 	        	{
		 	        		platforms[i].setHitbox(
		 	        			Integer.parseInt(temp[3]),
		 	        			Integer.parseInt(temp[4]),
		 	        			Integer.parseInt(temp[5]),
		 	        			Integer.parseInt(temp[6]));
		 	        	}
		 	        }
		 	        //JOHN PUT LOGIC FOR ANIMATION,DESTRUCTION,AND MOTION
	 	        	/*
	 	        	 *	13 = Destruction
	 	        	 *	14 = Motion
	 	        	 * 
	 	        	 * Ok the idea is to no break up destruction and Motion with a '<' instead of ,
	 	        	 *  this will require multiple splits. but i think it will make things alot simpler in the long run.
	 	        	 */
	 	        	else
	 	        	{
	 	        		platforms[i] = new Platform(temp[0],
	 	 	 	    		   Integer.parseInt(temp[1]),
	 	 	 	    		   Integer.parseInt(temp[2]),
	 	 	 	    		   Integer.parseInt(temp[3]),
	 	 	 	    		   Integer.parseInt(temp[4]),
	 	 	 	    		   Integer.parseInt(temp[5]),
	 	 	 	    		   Integer.parseInt(temp[6]),
	 	 	 	    		   Integer.parseInt(temp[7]),
	 	 	 	    		   //Integer.parseInt(temp[8]),
	 	 	 	    		   lvlspriteData);
	 	 	        	platforms[i].setHitbox(
	 	 	        			Integer.parseInt(temp[9]),
	 	 	        			Integer.parseInt(temp[10]),
	 	 	        			Integer.parseInt(temp[11]),
	 	 	        			Integer.parseInt(temp[12]));
	 	 	        		//This is Destruction
	 	 	        	temp2 = temp[13].split(">");
	 	 	        	if(temp2.length == 4)//Boolean.parseBoolean(temp2[0]))
	 	 	        	{
	 	 	        		platforms[i].make_Destroyable(temp2[1],Integer.parseInt(temp2[2]),Integer.parseInt(temp2[3]),lvlspriteData);
	 	 	        	}
	 	 	        	else if(temp2.length == 5)//Boolean.parseBoolean(temp2[0]))
	 	 	        	{
	 	 	        		platforms[i].make_Destroyable(temp2[1],Integer.parseInt(temp2[2]),Integer.parseInt(temp2[3]),Integer.parseInt(temp2[4]),lvlspriteData);
	 	 	        	}
	 	 	        		//This make is Movable
	 	 	        	temp2 = temp[14].split(">");
	 	 	        	if(temp2.length == 5)//Boolean.parseBoolean(temp2[0]))
	 	 	        	{
	 	 	        		platforms[i].make_movable(Integer.parseInt(temp2[1]),Integer.parseInt(temp2[2]),Integer.parseInt(temp2[3]),Integer.parseInt(temp2[4]));
	 	 	        	}
	 	 	        	
		 	        }
 	        }
	        for(int i= 0;i< dnum; i++)
	        {
	        	line = Tools.readlineadv(br);
	 	        temp = line.split(",");
	 	        doors[i] = new Door(temp[0],
	 	        		Integer.parseInt(temp[1]),
	 	        		Integer.parseInt(temp[2]),
	 	        		Integer.parseInt(temp[3]),
	 	        		Integer.parseInt(temp[4]),
	 	        		temp[5],
	 	        		Integer.parseInt(temp[6]),
	 	        		Integer.parseInt(temp[7]),lvlspriteData);
	 	       if(temp.length >= 9)
	 	        {
	 	    	   doors[i].setHitbox(
	        			Integer.parseInt(temp[8]),
	        			Integer.parseInt(temp[9]),
	        			Integer.parseInt(temp[10]),
	        			Integer.parseInt(temp[11]));
	        	}
	 	    }
	        
	        for(int i = 0; i < bnum; i++)
	        {
	        	line = Tools.readlineadv(br);
	        	temp = line.split(",");
	        	badguys[i] = new Soldier(
	        			temp[0],temp[1],temp[2],
	        			Integer.parseInt(temp[3]),
	        			Integer.parseInt(temp[4]),
	        			Integer.parseInt(temp[5]),
	        			Integer.parseInt(temp[6]),
	        			Integer.parseInt(temp[7]),
	        			Integer.parseInt(temp[8]),lvlspriteData,soundHandler);
	        	if(temp.length >= 10)
	        	{
	        		badguys[i].setPatrol(Integer.parseInt(temp[9]),Integer.parseInt(temp[10]));
	        		badguys[i].setHitbox(Integer.parseInt(temp[11]), 
	        				Integer.parseInt(temp[12]),
	        				Integer.parseInt(temp[13]), 
	        				Integer.parseInt(temp[14]));
	        	}
	        	
	        }
	        for (int i= 0; i < snum; i++)
	        {
	        	line = Tools.readlineadv(br);
	 	        temp = line.split(",");
	 	        spikes[i] = new Spike(temp[0],
	 	    		   Integer.parseInt(temp[1]),
	 	    		   Integer.parseInt(temp[2]),
	 	    		  Integer.parseInt(temp[3]),
	 	    		 Integer.parseInt(temp[4]),
	 	    		Integer.parseInt(temp[5]),
	 	    		Integer.parseInt(temp[6]),
	 	    		Integer.parseInt(temp[7]),
	 	    		Integer.parseInt(temp[8]),
	 	    		   lvlspriteData);
	 	        if(temp.length >= 9)
	 	        {
	 	        	spikes[i].setHitbox(
	 	        			Integer.parseInt(temp[9]),
	 	        			Integer.parseInt(temp[10]),
	 	        			Integer.parseInt(temp[11]),
	 	        			Integer.parseInt(temp[12]));
	 	        }
	 	        if(temp.length > 13)
	 	        {
		 	        temp2 = temp[13].split(">");
	 	        	if(temp2.length == 4)//Boolean.parseBoolean(temp2[0]))
	 	        	{
	 	        		spikes[i].make_Destroyable(temp2[1],Integer.parseInt(temp2[2]),Integer.parseInt(temp2[3]),lvlspriteData);
	 	        	}
	 	        	else if(temp2.length == 5)//Boolean.parseBoolean(temp2[0]))
 	 	        	{
	 	        		spikes[i].make_Destroyable(temp2[1],Integer.parseInt(temp2[2]),Integer.parseInt(temp2[3]),Integer.parseInt(temp2[4]),lvlspriteData);
 	 	        	}
	 	        		//This make is Movable
	 	        	temp2 = temp[14].split(">");
	 	        	if(temp2.length == 5)//Boolean.parseBoolean(temp2[0]))
	 	        	{
	 	        		spikes[i].make_movable(Integer.parseInt(temp2[1]),Integer.parseInt(temp2[2]),Integer.parseInt(temp2[3]),Integer.parseInt(temp2[4]));
	 	        	}
	 	        }
 	        
	 	        
 	        }
	        for(int i= 0; i < bombnum; i++)
	        {
	        	line = Tools.readlineadv(br);
	        	temp = line.split(",");
	        	bombs[i] = new Explosive(
	        			temp[0],
	        			temp[1],
	        			Integer.parseInt(temp[2]),
	        			Integer.parseInt(temp[3]),
	        			Integer.parseInt(temp[4]),
	        			Integer.parseInt(temp[5]),
	        			Integer.parseInt(temp[6]),
	        			Integer.parseInt(temp[7]),
	        			Integer.parseInt(temp[8]),
	        			Integer.parseInt(temp[9]),
	        			Integer.parseInt(temp[10]),
	        			Integer.parseInt(temp[11]),
	        			Integer.parseInt(temp[12]),
	        			Integer.parseInt(temp[13]),
	        			lvlspriteData
	        			);
        	}
	        for(int i = 0; i< hnum; i++)
	        {
	        	line = Tools.readlineadv(br);
        		temp = line.split(",");
        		healthpicks[i] = new Platform(
        			temp[0],
        			Integer.parseInt(temp[1]),
        			Integer.parseInt(temp[2]),
        			Integer.parseInt(temp[3]),
        			Integer.parseInt(temp[4]),
        			Integer.parseInt(temp[5]),
        			Integer.parseInt(temp[6]),
        			lvlspriteData,
        			Integer.parseInt(temp[7])
        			);
	        }
	        for(int i = 0; i < paranum; i++)
	        {
	        	line = Tools.readlineadv(br);
	        	temp = line.split(",");
	        	parallax[i] = new Platform(temp[0],
	        			Integer.parseInt(temp[1]),
	        			Integer.parseInt(temp[2]),
	        			lvlspriteData
	        			);
	        	parallax[i].setParSpeed(Double.parseDouble(temp[3]));
	        	
	        	parallaxStart[0][i] = Integer.parseInt(temp[1]); 
	        	parallaxStart[1][i] = Integer.parseInt(temp[2]);
        	}
	        for(int i = 0; i< savenum; i++)
	        {
	        	line = Tools.readlineadv(br);
	        	temp = line.split(",");
	        	saveZone[i] = new Platform(temp[0],
	        			Integer.parseInt(temp[1]),
	        			Integer.parseInt(temp[2]),
	        			Integer.parseInt(temp[3]),
	        			Integer.parseInt(temp[4]),
	        			Integer.parseInt(temp[5]),
	        			Integer.parseInt(temp[6]),
	        			Integer.parseInt(temp[7]),
	        			lvlspriteData
	        			);
	        	saveZone[i].setValue(1);
	        }
	        	//John work on one way platforms
	        for (int i= 0; i < onewayUnderPlatform.length; i++)
	        {
	        	line = Tools.readlineadv(br);
	 	        temp = line.split(",");
	 	        		//John Change this to animated
	 	      	onewayUnderPlatform[i] = new Platform(temp[0],Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),lvlspriteData);
	 	        if(temp.length >= 4)
	 	        {
	 	        	platforms[i].setHitbox(
	 	        			Integer.parseInt(temp[3]),
	 	        			Integer.parseInt(temp[4]),
	 	        			Integer.parseInt(temp[5]),
	 	        			Integer.parseInt(temp[6]));
	 	        } 
 	        }
	        for (int i= 0; i < onewayOverPlatform.length; i++)
	        {
	        	line = Tools.readlineadv(br);
	 	        temp = line.split(",");
	 	        		//John Change this to animated
	 	      	onewayOverPlatform[i] = new Platform(temp[0],Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),lvlspriteData);
	 	        if(temp.length >= 4)
	 	        {
	 	        	platforms[i].setHitbox(
	 	        			Integer.parseInt(temp[3]),
	 	        			Integer.parseInt(temp[4]),
	 	        			Integer.parseInt(temp[5]),
	 	        			Integer.parseInt(temp[6]));
	        	}
 	        }
	        for (int i= 0; i < onewayLeftPlatform.length; i++)
	        {
	        	line = Tools.readlineadv(br);
	 	        temp = line.split(",");
	 	        		//John Change this to animated
	 	      	onewayLeftPlatform[i] = new Platform(temp[0],Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),lvlspriteData);
	 	        if(temp.length >= 4)
	 	        {
	 	        	platforms[i].setHitbox(
	 	        			Integer.parseInt(temp[3]),
	 	        			Integer.parseInt(temp[4]),
	 	        			Integer.parseInt(temp[5]),
	 	        			Integer.parseInt(temp[6]));
	 	        }
 	        }
	        for (int i= 0; i < onewayRightPlatform.length; i++)
	        {
	        	line = Tools.readlineadv(br);
	 	        temp = line.split(",");
	 	        		//John Change this to animated
	 	      	onewayRightPlatform[i] = new Platform(temp[0],Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),lvlspriteData);
	 	        if(temp.length >= 4)
	 	        {
	 	        	platforms[i].setHitbox(
	 	        			Integer.parseInt(temp[3]),
	 	        			Integer.parseInt(temp[4]),
	 	        			Integer.parseInt(temp[5]),
	 	        			Integer.parseInt(temp[6]));
	 	        }
 	        }
	        
	        
	        
	        
	        	//Close the files 
	        br.close();
	        if(t ==1)
	        {
	        	fr.close();
	        }
	        else if(t == 2)
	        {
	        	is.close();
	    		isr.close();
	        }
	        
	    } catch (Exception e) 
	    {
	    	System.out.println("Im sorry the Map File: "+lvlname+" could not be loaded!");
	    	this.Error = true;
	    }
	    
	}
	private void setUp() 
	{	
			this.Error = false;
			soundHandler = new SoundEngine();
			
				//window hitbox
			windowHB[0]= 0;
			windowHB[1]= 0;
			windowHB[2]= window.width;
			windowHB[3]= window.height;
			
			lvlspriteData = new HashMap<String,BufferedImage>();
			permaSprites = new HashMap<String,BufferedImage>();
			
				//This is the move History
			mStack = new MoveStack(10);
			
				//SET up menu here 
			this.inMainMenu = true;
			this.mme = false;
			this.mmeh= true;
			this.mainMenu = new Sprite("res/menu/menuback.png",0,0,permaSprites);
			this.mainMenuMax = 3;
			mainMenuButtons = new Sprite[mainMenuMax+1];
			for(int i = 0;i < this.mainMenuMax + 1; i++)
			{
				this.mainMenuButtons[i] = new Sprite("res/menu/menu"+i+".png",0,0,permaSprites);
			}
			
			isLoading = false;
			isLoadingF = false;
			loadCont = false;
			loading = new Sprite("res/loading.png",0,0,permaSprites );
			loadingF = new Sprite("res/menu/space2cont.png",400,550,permaSprites);
			
			//Wh and Eh are used so that when the player is pressing left then presses right, once he lets go of 
				// the right button if he is still holding left he then starts going left again or visa versa
			Wh = false;
			Eh = false;
				//Jh is used so that when the player presses jump he has to let go and press again to jump again
				//	instead if him constantly hopping as the player holds jump
			Jh = true;
			
				//Menu needs finalization or automation
			pauseMenu = new Sprite("res/Pause.png",0,0,permaSprites );
				//combat
			attackL = 0;
			attackH = 0;
			attackS = 0;
			
			pauseButtons = new Sprite[3];
			pauseButtons[0] = new Sprite("res/pb0.png",135,160,permaSprites);
			pauseButtons[1] = new Sprite("res/pb1.png",156,274,permaSprites);
			pauseButtons[2] = new Sprite("res/pb2.png",156,407,permaSprites );
			restartdata = new int[5];
			restartdata[0] =0;
			restartdata[1] =0;
			restartdata[2] =0;
			restartdata[3] =0;
			ps = false;
			psh = true;
		

			

			gameover = new Sprite("res/gameover.png",0,0,permaSprites );
			isGameOver = false;
			
					//Player stuff (john set this up in the save file)
			player = new PlayerChar("res/player/brov4.txt",permaSprites);
			
					//testing the new Trigger stuff!
					//So this crashes eclipse......Joy
					//Memory issssssssuesssssssssssssssssss :(
			player2 = new PlayerChar("res/player/brov6-2.txt",permaSprites,false);
			
					//Health bar stuff
			restartdata[4] = (int) player.HP;
			this.hx = 50;
			this.hy = 50;
			this.hyP = 22;
			this.hxP = 83;
			hpPipLen = 20;
			this.playerHP = new Sprite("res/player/barPlus.png",hx,hy,permaSprites);
			this.playerHPpips = new Sprite("res/player/barP.png",0,hy + hyP,permaSprites);
			//this.HPsW = playerHP.width;
			
			
			//player = new PlayerChar("Brodrick","res/50 Brodrick V4 Spritemap.png",0,0,180,180,12,20,permaSprites);
			//player.setHitbox(30, 15, 100, 140);	
	}
	public void saveFile()
	{
		/*
		 * This will save all the players progress.
		 * This should use specific save spots that give full health,
		 */
		
		/*
		 * SAVE FILE
		 * 
		 * 1)	name 
		 * 2)	date 
		 * 3)	playerfile
		 * 4) 	maxHP
		 * 5) 	mapfile
		 * 6)	zonedata: playerX, playerY, mapX, mapY
		 */
		player.addHP(player.maxHP);
		try
		{
			File theDir = new File("savegames");
			if (!theDir.exists()) 
			{
			    try
			    {
			        theDir.mkdir();
			    } 
			    catch(SecurityException se)
			    {
			        System.out.println("ERROR: Permisson denied");
			    }        
			}
			
			FileWriter fw = new FileWriter(saveFileName);
			
			fw.write(saveName +"\n");
				//date stuff
			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			Date dateobj = new Date();
			fw.write(df.format(dateobj) + "\n");
			fw.write(player.file + "\n");
			fw.write((int) player.maxHP  +"\n");
			fw.write(lvlName + "\n");
			fw.write(restartdata[0] +","+
				(restartdata[1] )+","+
				(restartdata[2])+","+
				(restartdata[3])+"\n"
				);
			//fw.write(" \n");
			fw.close();
		
		}catch (IOException e) {e.printStackTrace();}
		
		
	}
	public void loadFile(String inSaveFile)
	{
			// FREE UP SOME MEMORY TO PREVENT CRASH!]
			//John clean this up.
		screen.flush();
		screen = createVolatileImage(window.width,window.height); 
		isLoadingF = false;
		//isPaused = true;
		lvlspriteData.clear();
		badguys = null;
		spikes = null;
		platforms= null;
		ladders = null;
		parallax = null;
		doors = null;
		bombs = null;
		theWorld = null;
		gameWorld = null;
		weather = null;
		healthpicks = null;
		onewayUnderPlatform = null;
		onewayOverPlatform = null;
		onewayLeftPlatform = null;
		onewayRightPlatform = null;
		System.gc();
		
		String name;
		BufferedReader br;
		int t = 1;
	    try {
	    	FileReader fr = null;
	    	InputStream is = null;
	    	InputStreamReader isr = null;
	    		//Favor external files, then internal
	    	if(new File(inSaveFile).isFile())
	    	{
	    		t = 1;
	    		fr = new FileReader(inSaveFile);
	    		br = new BufferedReader(fr);
	    	}
	    	else //John add a new case for if neither exists
	    	{
	    		t = 2;
	    		is = getClass().getResourceAsStream("/"+inSaveFile);
	    		isr = new InputStreamReader(is);
	    		br = new BufferedReader(isr);
	    	}
	    	
	    		//stuff
	    	loadTarget = -1;
	    	isLoading = true;
	    	this.isGameOver = false;
	    	
	    		//Name
	    	String line  = Tools.readlineadv(br);
	    	saveName = line;
	    	saveFileName = inSaveFile;
	    	
	    		//DATESTUFF
	    	line  = Tools.readlineadv(br);
	    	//System.out.println("Loading \"" + line + "\" File" );
	    		//Player Stuff
	    	line  = Tools.readlineadv(br);
	    	player = new PlayerChar(line,permaSprites);
	    	line = Tools.readlineadv(br);
	    	player.maxHP = Integer.parseInt(line);
	    		// level stuff
	    	line = Tools.readlineadv(br);
	    	loadLevel(line);
	    	line = Tools.readlineadv(br);
	    	String[] temp = line.split(",");
	    	player.setX(Integer.parseInt(temp[0]));
			restartdata[0] =Integer.parseInt(temp[0]);
			player.setY(Integer.parseInt(temp[1]));
			restartdata[1] = Integer.parseInt(temp[1]);
			theWorld.setX(Integer.parseInt(temp[2]));
			restartdata[2] = Integer.parseInt(temp[2]);
			theWorld.setY(Integer.parseInt(temp[3]));
			restartdata[3] = Integer.parseInt(temp[3]);
			isLoadingF = true;
			
				//Close the files 
	        br.close();
	        if(t ==1)
	        {
	        	fr.close();
	        }
	        else if(t == 2)
	        {
	        	is.close();
	    		isr.close();
	        }
	        
	    } catch (IOException e) 
	    {
	    	System.out.println("Im sorry the Save File: "+inSaveFile+" could not be loaded!");
	    	this.Error = true;
	    }
	}
	public  void update()
	{	
			//This makes the loading screen work.
		if(start)
		{
			start = false; 
			if(mainMenuButnum == 1)
			{
				loadFile("savegames/newgame.txt");
					//this needs work for multi save support
				saveName = "New Game";
				saveFileName = "savegames/save1.txt";
			}
			else if(mainMenuButnum == 2)
			{
				loadFile("savegames/save1.txt");
			}
			oneupdate=true;
		}
		
		if((!isLoading && !isPaused && !isGameOver && !inMainMenu )||oneupdate)
		{	
			oneupdate=false;
			if(player.getDead())
			{
				this.isGameOver = true;
			}
			/*if(player.HP > 0)
			{
					//Old Health bar.
				//playerHP.width =(int) (playerHP.width*(player.HP/player.maxHP));
			}*/
			for(int i = 0; i < doors.length ; i++)
			{
				if(Tools.check_collision(doors[i].getHitbox(),player.getHitbox()))
				{
					isLoading = true;
					this.loadTarget = i;
					isLoadingF = false;
					//isLoading = false;
				}
			}
			theWorld.update();
			
			if(soundHandler.getAudible())
			{
				soundHandler.update();
			}
			
			player.update(damageQ); 
			int i;
			
			for(i = 0; i < parallax.length;i++)
			{

				parallax[i].setTrueX((int)( parallaxStart[0][i]+(theWorld.x*parallax[i].getParSpeed())));
				parallax[i].setTrueY((int)( parallaxStart[1][i]+(theWorld.y*parallax[i].getParSpeed())));
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
			if(gameWorld != null) gameWorld.update(theWorld);
			for(i =0; i < weather.length; i++)
			{
				weather[i].update(theWorld);
			}
			for(i =0; i < spikes.length; i++)
			{
				spikes[i].update(theWorld,damageQ);
			}
			for(i = 0; i < saveZone.length; i++)
			{

				saveZone[i].update(theWorld);
				if(Tools.check_collision(player.getHitbox(),saveZone[i].getHitbox()))
				{
					saveZone[i].setValue(0);
					saveFile();
				}
				if(!Tools.check_collision(theWorld.getHitbox(),saveZone[i].getHitbox()))
				{
					saveZone[i].setValue(1);
				}
			}
			for(i = 0; i < healthpicks.length; i++)
			{
				healthpicks[i].update(theWorld);
				
				if(Tools.check_collision(player.getHitbox(),healthpicks[i].getHitbox()))
				{
					if(!player.isfullHP())
					{
						player.addHP(healthpicks[i].getValue());
						healthpicks[i].setTrueX(-healthpicks[i].getWidth()*2);
					}
				}
			}
			for(i = 0; i < badguys.length; i++)
			{
				badguys[i].update(theWorld, platforms, damageQ);
				badguys[i].sight(player, theWorld);
			}
			for(i = 0; i< bombs.length; i++)
			{
				bombs[i].update(theWorld,damageQ);
				if(bombs[i].getType() == 0)
				{
					if(Tools.check_collision(player.getfrontHitbox(),bombs[i].getHitbox())){bombs[i].explode();}
					if(Tools.check_collision(player.getbackHitbox(),bombs[i].getHitbox())){bombs[i].explode();}
						//John add checks for good bombs and things
				}
			}
				//PLAYER DAMAGE ADD ENEMY DAMAGE NEXT  - JOHN
			for( DamageHitbox x : damageQ)
			{
				if(x.getType() == 0 || x.getType() == 4 || x.getType() == 5)
				{
					if(Tools.check_collision(player.getfrontHitbox(),x.getHitbox())){player.damage(x.amount);}
					if(Tools.check_collision(player.getbackHitbox(),x.getHitbox())){player.damage(x.amount);}
				}
				if(x.getType() == 3)
				{
					for(i = 0; i< badguys.length; i++)
					{
						if(Tools.check_collision(badguys[i].getfrontHitbox(),x.getHitbox())){badguys[i].damage(x.amount);}
						if(Tools.check_collision(badguys[i].getbackHitbox(),x.getHitbox())){badguys[i].damage(x.amount);}
					}
					for(i = 0; i< platforms.length; i++)
					{
						if(platforms[i].isDestroyable())
						{		//Destroyable Platforms
							if(Tools.check_collision(platforms[i].getHitbox(),x.getHitbox()))
									{platforms[i].damage(x.amount);}
						}
					}
					for(i = 0; i< spikes.length; i++)
					{
						if(spikes[i].isDestroyable())
						{		//Destroyable Platforms
							if(Tools.check_collision(spikes[i].getHitbox(),x.getHitbox()))
									{spikes[i].damage(x.amount);}
						}
					}
				}
					//John change this so that some bombs don't set off others
				for(i = 0; i < bombs.length; i++)
				{
					if(Tools.check_collision(bombs[i].getHitbox(),x.getHitbox())){if(!bombs[i].getExploding()){bombs[i].explode();}}
				}
				
					//For Debugin
				if(renderHitBox )
				{
					damageQhitbox.add(x);
				}
			}
				//there might be a better way of doing this
			damageQ.clear();
			
		}
		else if(inMainMenu)
		{
			if(mme)
			{
				mmeh= false;
				mme = false;
				/*if(mainMenuButnum == 0)
				{
						//NOTHING
				}
				else*/
				if(mainMenuButnum == 1)
				{
					loadTarget = -1;
					this.start = true;
						//NEW GAME
					inMainMenu = false;
					isLoading = true;
					isLoadingF = false;
					
					
						//John give options for 3 save files?
					/*saveName = "New Game";
					saveFileName = "savegames/save1.txt";
						
					loadFile("savegames/newgame.txt");*/
					
				}
				else if(mainMenuButnum == 2)
				{
					loadTarget = -1;
					this.start = true;
					
					inMainMenu = false;
					isLoading = true;
					isLoadingF = false;
						//LOAD a choosen save file? 
						//future updates
						//		 Multiple save files.
					//loadFile("savegames/save1.txt");
					
				}
				else if(mainMenuButnum == 3)
				{		
						//QUIT
					inMainMenu = false;
					isGameOver = true;
					terminator = true;
				}
			}
		}
		else if(isLoading)
		{
			if(loadTarget >= 0 && !isLoadingF)
			{
				int[] tplayer = doors[loadTarget].playerloc;
				int[] tworld = doors[loadTarget].mapstart;
				loadLevel(doors[loadTarget].newMap);
				player.setX(tplayer[0]);
				restartdata[0] = tplayer[0];
				player.setY(tplayer[1]);
				restartdata[1] = tplayer[1];
				theWorld.setX(tworld[0]);
				restartdata[2] = tworld[0];
				theWorld.setY(tworld[1]);
				restartdata[3] = tworld[1];
				
				restartdata[4] =(int) player.HP;
				
					//John look into preventing world popping!
				/*player.update(damageQ);
				theWorld.update();
				if(gameWorld != null)
				{
					gameWorld.update(theWorld);
				}*/
				oneupdate=true;
			}
			if (loadTarget == -2)
			{
				loadTarget = 0;
				loadLevel(lvlName);
				player.setX(restartdata[0]);
				player.setY(restartdata[1] );
				theWorld.setX(restartdata[2]);
				theWorld.setY(restartdata[3]);
				player.HP = restartdata[4];
			}	
			isLoadingF = true;
			if(loadCont)
			{
				isLoading = false;
			}
		}
		else if (isPaused)
		{
			if(ps)
			{
				ps = false;
				psh = false;
				if(pauseButnum == 0)
				{
					isPaused = false;
				}
				else if(pauseButnum == 1)
				{
					loadTarget = -2;
					isPaused = false;
					isLoading = true;
					isLoadingF= false;
				}
				else if(pauseButnum == 2)
				{		
						//QUIT
					isPaused = false;
					isGameOver = true;
					terminator = true;
				}
			}
		}
		
		
	}

	public void render()
	{
		int i;		
			//Construct backbuffer. 
				//this prevents the tearing you get as things are rendered.
		Graphics g = screen.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, window.width, window.height);
		
		
		if(!isLoading && !isPaused && !isGameOver && !inMainMenu && isRunning )
		{
				//JOHN - this is done before gameWorld because of the way i made the test boat level
			for(i =0; i < weather.length; i++)
			{
				if(Tools.check_collision(windowHB,weather[i].getHitbox())){weather[i].render(g,lvlspriteData);}
			}
			
				//background stuff 
			if(gameWorld != null) gameWorld.render(g, windowHB,lvlspriteData);
				//more backgrounds
			for(i = 0; i < parallax.length;i++)
			{
				if(Tools.check_collision(windowHB,parallax[i].getHitbox())){parallax[i].render(g,lvlspriteData );}
			}
				//render the platforms]
			for(i = 0; i < platforms.length;i++)
			{
				if(Tools.check_collision(windowHB,platforms[i].getHitbox())){platforms[i].render(g,lvlspriteData );}
			}
			for(i = 0; i < ladders.length;i++)
			{
				if(Tools.check_collision(windowHB,ladders[i].getHitbox())){ladders[i].render(g,lvlspriteData );}
			}
			for(i = 0; i < doors.length;i++)
			{
				if(Tools.check_collision(windowHB,doors[i].getHitbox())){doors[i].render(g,lvlspriteData );}
			}
			for(i = 0; i < spikes.length; i++)
			{
				if(Tools.check_collision(windowHB,spikes[i].getHitbox())){spikes[i].render(g,lvlspriteData );}
			}
			for(i = 0; i < saveZone.length; i++)
			{
				if(Tools.check_collision(windowHB,saveZone[i].getHitbox()))
				{	
					if(saveZone[i].getValue() == 1)
					{
						saveZone[i].render(g,lvlspriteData);
					}
				}
			}
			for(i = 0; i < healthpicks.length;i++)
			{
				if(Tools.check_collision(windowHB,healthpicks[i].getHitbox())){healthpicks[i].render(g,lvlspriteData );}
			}
			for(i = 0; i < bombs.length; i++)
			{
				if(Tools.check_collision(windowHB,bombs[i].getHitbox())){bombs[i].render(g,lvlspriteData );}
			}
				//render the player
			player.render(g,permaSprites);
			for(i = 0; i < badguys.length; i++)
			{
				if(Tools.check_collision(windowHB,badguys[i].getHitbox())){badguys[i].render(g,lvlspriteData );}
			}
			if(renderHitBox )
			{
				for( DamageHitbox x : damageQhitbox)
				{
					if(Tools.check_collision(windowHB,x.getHitbox()))
					{
						g.setColor(Color.RED);
						g.drawRect(x.hitbox[0], x.hitbox[1],x.hitbox[2], x.hitbox[3]);
					}
				}
				damageQhitbox.clear();
			}
			if(player.HP > 0)
			{
				//g.drawImage(permaSprites.get(playerHP.name),playerHP.x, playerHP.y , (int) (HPsW*(player.HP/player.maxHP)) , playerHP.height, null);
				playerHP.render(g,permaSprites);
				for(i = 0; i < player.HP/10; i++)
				{
					playerHPpips.setX(playerHP.getX() + this.hxP + (i*this.hpPipLen));
					playerHPpips.render(g,permaSprites);
				}
			}
			/*//These are from a really old version of the game.
			for(i = 0; i < bomb.length; i++)
			{
				if(Tools.check_collision(windowHB,bomb[i].getHitbox())){bomb[i].render(g);}
			}
			for(i = 0; i < bullets.length; i++)
			{
				if(Tools.check_collision(windowHB,bullets[i].getHitbox())){bullets[i].render(g);}
			}
			for(i = 0; i < missiles.length; i++)
			{
				if(Tools.check_collision(windowHB,missiles[i].getHitbox())){missiles[i].render(g);}
			}
			*/
		}
		else if (isPaused)
		{
			pauseMenu.render(g,permaSprites );
			pauseButtons[pauseButnum].render(g,permaSprites );
		}
		else if(isGameOver)
		{
			gameover.render(g,permaSprites );
		}
		else if(inMainMenu)
		{
			mainMenu.render(g,permaSprites);
			mainMenuButtons[mainMenuButnum].render(g,permaSprites);
			g.setColor(Color.GREEN);
			g.drawString(version,window.width -100,window.height - 20);
		}
		else if(isLoading)
		{
			loading.render(g,permaSprites);
			if(isLoadingF)
			{
				loadingF.render(g,permaSprites);
			}
		}
		else
		{
			//ERROR!!!
			System.out.println("UNKNOWN STATE: render()");
		}
			// Get window's graphics object. 
		g = getGraphics();
			// Draw backbuffer to window.
		g.drawImage(screen,0,0,window.width,window.height,0,0,window.width, window.height,null);
			//NO CODEBELOW!
		g.dispose();	
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
			/*
			 * This running loop logic is a modified from
			 * Eli Delventhal's guide "Game loops!"
			 *
			 * Link: http://www.java-gaming.org/index.php?topic=24220.0		 
			 */
			loops = 0;
			while(  System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) 
			{	
				movement();
				combat();
				update();
				
				next_game_tick += SKIP_TICKS;
				loops++;
			}
			render();
				//This is to prevent the game from freaking out when a level is loaded	
			if(this.isLoading) next_game_tick =  System.currentTimeMillis(); 
			if(this.terminator) isRunning = false;
			System.gc();
		}
			//this is after game
		this.gameRun=false;
		System.exit(0);
	}
	public void start() 
	{
		if(!Error)
		{
			isRunning =true;
			new Thread(this).start();
		}
		else
		{
			System.out.println("Error - Quitting");
			gameRun = false;
		}
	}
	public void gameover()
	{
		isRunning=false;
	}
	
	private void movement()
	{
			/*
			~new combat update!~
			This will now only handle passing the inputs and all the movement should be handled by the Update method.
			Hopefully i will be able to retire this method.
			
			OR 
			use this as the input passer.
			
			 */
		if(!isLoading && !isPaused && !inMainMenu)
		{

			if(F)
			{
				player.fire(theWorld);
			}
			boolean onplatform = false;
			boolean headbonk = false;
			boolean frontbonk = false;
			boolean backbonk = false;
			boolean onladder = false;
			boolean falling = true;
				//John this might need to change
			int movingPlatspeed = 0;
			for(int i = 0; i < ladders.length ; i++)
			{
				if(Tools.check_collision(ladders[i].getHitbox(), player.getHitbox()))
				{
					onladder = true;
				}
			}
			for(int i = 0; i < platforms.length; i++)
			{
				if(!platforms[i].isDestroyable()||!platforms[i].isDestroyed())
				{
					if(!onladder && !headbonk){if(Tools.check_collision(platforms[i].getHitbox(), player.getheadHitbox()))
						{headbonk = true;}}
					
					if (!frontbonk && Tools.check_collision(platforms[i].getHitbox(), player.getfrontHitbox()))
						{
							frontbonk = true;
							if(platforms[i].getMoving())
								{movingPlatspeed = platforms[i].movingSpeed();}
						}
					if (!backbonk && Tools.check_collision(platforms[i].getHitbox(), player.getbackHitbox()))
						{
							backbonk = true;
							if(platforms[i].getMoving())
								{movingPlatspeed = platforms[i].movingSpeed();}
						}
					if(!onladder && !onplatform){if (Tools.check_collision(platforms[i].getHitbox(), player.getfeetHitbox()))
					{
						onplatform = true;
							
							//MOVING PLATFORM Logic
						if(platforms[i].getMoving())
							{movingPlatspeed = platforms[i].movingSpeed();}
					}}
				}
			}
				//this will move the player if they are on a moving platform
			if((!backbonk || movingPlatspeed > 0) && (!frontbonk|| movingPlatspeed < 0))
			{
				
				if(movingPlatspeed < 0 )
				{
					if(player.getX() >  6*window.width/16)
						{player.setX(player.getX() + movingPlatspeed);}
					else if(theWorld.getX() < 0)
						{theWorld.moveXp(-movingPlatspeed);}
					else if(player.getX() > 0)
						{player.setX(player.getX() + movingPlatspeed);}
				}
				else if(movingPlatspeed > 0 )
				{
					if(player.getX() <  6*window.width/16)
						{player.setX(player.getX() + movingPlatspeed);}
					else if(-theWorld.getX() < theWorld.getWidth()-window.width)
						{theWorld.moveXp(-movingPlatspeed);}
					else if(player.getX() < window.width -player.getWidth())
						{player.setX(player.getX() + movingPlatspeed);}
				}
			}
			if(player.getY() < 1)
			{
				headbonk= true;
			}
			
			
				
			
			
				//gravity~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			if (!player.getJumping() && !onplatform && !onladder)
			{
				if(player.gravity <0 ) {player.gravity = 0;}
				
				if(player.getY() < 6*window.height/9-player.getHeight())
				{
					player.fall();
	 			}
				else if(-theWorld.getY()+window.getHeight() < theWorld.getHeight())
				{
					
					if(player.gravity < player.gettopGravity()) 
					{
						player.gravity = player.gravity + player.fallrate;  
					}
					
					theWorld.moveYn((int)player.getGravity());					
					player.setfalling(true);				
				}
				else if(player.getY() < window.height-player.getHeight())
				{
					player.fall();
				}
				else
				{
					player.setfalling(false);
				}
	
			}
			else
			{
				player.setfalling(false);
				falling = false;
				if(!player.getJumping())
				{
					player.gravity = 0;
				}
			}
				//NORTH~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			if (N && !headbonk)
			{
				if(onladder)
				{
					if(player.getY() > window.height/9)
					{
						player.setY((int) (player.getY() - player.gettopRunSpeed()));
					}
					else if (theWorld.getY() < theWorld.height-window.height && theWorld.getY() < player.getGravity())
					{
						theWorld.moveYn((int) -player.gettopRunSpeed());	
					}
					else if(player.getY() > 0)
					{
						player.setY((int) (player.getY() - player.gettopRunSpeed()));
					}
				}
				else
				{
					player.setJumping(true);
					if((onplatform || onladder|| player.getY() >= window.height-player.getHeight()) && Jh)
					{
						player.gravity = -player.gettopJump();
						Jh = false;
					}
					
					
					
					if(player.gravity < 0)
					{
						if(player.getY() > window.height/9)
						{
							player.moveYn();
						}
						else if (theWorld.getY() < theWorld.height-window.height && theWorld.getY() < player.getGravity())
						{
							//theWorld.moveYp(player.speedY);
							
							if(player.gravity < player.gettopGravity()) 
							{
								player.gravity = player.gravity + player.fallrate;
							}
							theWorld.moveYn((int) player.getGravity());
						}
						else if(player.getY() > 0)
						{
							player.moveYn();
						}
					}
					else
					{
						player.setJumping(false);
					}
				}	
			}
			else
			{
				player.setJumping(false);
			}
			
			
				//WEST~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			if (W && !backbonk && ((!player.getAttacking()) || player.getJA()))
			{ 
				player.setFaceForward(false);
				player.setbackward(true);
				if(player.getX() >  6*window.width/16)
				{
						//this if statement is to prevent the player from getting stuck in a wall;
					if(!frontbonk ||player.speedX <= 0) 
					{
						player.moveXn();
					}
					else if(player.speedX > -player.topRunSpeed)
					{
						player.speedX -= player.runrate;
					}
				}
				else if(theWorld.getX() < 0)
				{
					if(player.speedX > -player.topRunSpeed)
					{
						player.speedX -= player.runrate;
					}
					if(!frontbonk || player.speedX <= 0)
					{
						theWorld.moveXp((int) -player.speedX);
					}
				}
				else if(player.getX() > 0)
				{
					if(!frontbonk || player.speedX <= 0)
					{
						player.moveXn();
					}
					else if(player.speedX > -player.topRunSpeed)
					{
						player.speedX -= player.runrate;
					}
				}
			}
			else
			{
				player.setbackward(false);
			}
				//EAST~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			if (E && !frontbonk && ((!player.getAttacking()) || player.getJA()))
			{
				player.setFaceForward(true);
				player.setForward(true);
				if(player.getX() < 6*window.width/16)
				{
					if (!backbonk || player.speedX >= 0)
					{
						player.moveXp();
					}
					else if(player.speedX < player.topRunSpeed)
					{
						player.speedX += player.runrate;
					}
				}
				else if(-theWorld.getX() < theWorld.getWidth()-window.width)
				{

					if(player.speedX < player.topRunSpeed)
					{
						player.speedX += player.runrate;
					}
					if (!backbonk || player.speedX >= 0)
					{
						theWorld.moveXp((int) -player.speedX);
					}
				}
				else if (player.getX() < window.width -player.getWidth())
				{
					if(!backbonk || player.speedX >= 0)
					{
						player.moveXp();
					}
					else if(player.speedX < player.topRunSpeed)
					{
						player.speedX += player.runrate;
					}
						 
				}
			}
			else
			{	
					//JOHN RETHING KNOCKBACK!
				player.setForward(false);
			}
			
			if(!E&& !W)
			{
				player.speedX = 0;
			}
			//SOUTH~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			if(S && onladder)
			{
					//this is so that the ladder movements aren't based on gravity.
				if(player.gravity <0 ) {player.gravity = 0;}
				//player.setOnladder(true)
				
				
				if(player.getY() < 8*window.height/9-player.getHeight())
				{
					player.moveYp();
	 			}
				else if(-theWorld.getY()+window.getHeight() < theWorld.getHeight())
				{
					
					theWorld.moveYn((int) player.gettopRunSpeed());					
				}
				else if(player.getY() < window.height-player.getHeight())
				{
					player.moveYp();
				}
				
			}
			else
			{
				//player.setOnladder(true)
			}
		}
		
	}

	private void combat()
	{
		//this is for the new combat update.
		
		//create the input object right now this is a string ill prolly change it later
		//what about a map???
		InputList userInput = new InputList();
		//Get input from keys.
			//directions
		userInput.N = N;
		userInput.S = S;
		userInput.E = E;
		userInput.W = W;
			//attack
		if(attackL > 0) userInput.AL = true;
		if(attackH > 0) userInput.AH = true;
		if(attackS > 0) userInput.AS = true;
			//External inputs?
		userInput.moveStack = mStack;
		
		//game.triggers.State jason = new game.triggers.State(new char[]{'t','e','s','t'}); //for testing
		
		//sent input object to player class.
		//System.out.println(input); //This is for testing
		//userInput.moveStack.test();
		
		//john this is a good way to get to string.
		//System.out.println(String.valueOf(mStack.getStack()));
		//player2.triggerEngine(userInput, mStack);
	}
	
	public void keyPressed(KeyEvent key) 
	{
		
			//identify which key was pressed
			//set the correct direction to true
			//then change the player model 
		if(ControlS ==0)
		{
			switch (key.getKeyCode())
			{
				//attacklogic
				case KeyEvent.VK_SPACE:
					N=true;
					if(inMainMenu)
					{
						if(mmeh)
						{
							mme= true;
						}
					}
					else if(isPaused)
					{
						if(psh)
						{
							ps= true;
						}
					}
					else if(isLoadingF)
					{
						loadCont = true;
					}
					if(isGameOver)
					{
						inMainMenu = true;		
						isGameOver = false;
					}
	
					break;
				case KeyEvent.VK_ENTER:
					if(inMainMenu)
					{
						if(mmeh)
						{
							mme= true;
						}
					}
					else if(isPaused)
					{
						if(psh)
						{
							ps= true;
						}
					}
					else if(isLoadingF)
					{
						loadCont = true;
					}
					if(isGameOver)
					{
						inMainMenu = true;		
						isGameOver = false;
					}
	
					break;
	 			case KeyEvent.VK_LEFT:
					if(!player.getAttacking())
					{
						if(player.getFacingForward())
						{
							player.startReverseAttack();
						}
						else
						{
							//player.setFaceForward(false);
							player.startAttack();
						}
					}
					break;
				case KeyEvent.VK_RIGHT:
					if(!player.getAttacking())
					{
						if(!player.getFacingForward())
						{
							player.startReverseAttack();
						}
						else
						{
						//player.setFaceForward(true);
						player.startAttack();
						}
					}
					break;
					
					
				//-------------------------(Y)
				case KeyEvent.VK_W: //UP
					//N=true;
					if(inMainMenu)
					{
						if(mainMenuButnum != 0)
						{
							mainMenuButnum--;
						}
						else
						{
							mainMenuButnum = pauseMax;
						}
					}
					else if(isPaused)
					{
						if(pauseButnum != 0)
						{
							pauseButnum--;
						}
						else
						{
							pauseButnum = pauseMax;
						}
					}
						break;
				case KeyEvent.VK_S: //DOWN
					S=true;
					if(isPaused)
					{
						if(pauseButnum != pauseMax)
						{
							pauseButnum++;
						}
						else
						{
							pauseButnum = 0;
						}
					}
					else if(inMainMenu)
					{
						if(mainMenuButnum != mainMenuMax)
						{
							mainMenuButnum++;
						}
						else
						{
							mainMenuButnum = 0;
						}
					}
						break;
				//-------------------------(X)
				case KeyEvent.VK_A: //LEFT
					W = true;
					if(E == true)
					{
						Eh = true;
						E = false;
					}
					break;
						
				case KeyEvent.VK_D: //RIGHT
					E = true;
					if(W == true)
					{
						Wh = true;
						W = false;
					}
					break;
						
				case KeyEvent.VK_O: //switch controls
					ControlS = 1;
						break;
						
				case KeyEvent.VK_ESCAPE: //Paused
					if(!inMainMenu && !isLoading)
					{
						if(isPaused)
						{
							isPaused = false;
						}
						else
						{
							pauseButnum = 0;
							isPaused = true;
						}
					}
					break;
			}
		}
		else if(ControlS == 1)
		{
			switch (key.getKeyCode())
			{
				//attacklogic
				case KeyEvent.VK_SPACE:
					N=true;
					if(inMainMenu)
					{
						if(mmeh)
						{
							mme= true;
						}
					}
					else if(isPaused)
					{
						if(psh)
						{
							ps= true;
						}
					}
					else if(isLoadingF)
					{
						loadCont = true;
					}
					if(isGameOver)
					{
						inMainMenu = true;		
						isGameOver = false;
					}
	
					break;
				case KeyEvent.VK_ENTER:
					if(inMainMenu)
					{
						if(mmeh)
						{
							mme= true;
						}
					}
					else if(isPaused)
					{
						if(psh)
						{
							ps= true;
						}
					}
					else if(isLoadingF)
					{
						loadCont = true;
					}
					if(isGameOver)
					{
						inMainMenu = true;		
						isGameOver = false;
					}
	
					break;
	 			case KeyEvent.VK_A:
					if(!player.getAttacking())
					{
						if(player.getFacingForward())
						{
							player.startReverseAttack();
						}
						else
						{
							//player.setFaceForward(false);
							player.startAttack();
						}
					}
					break;
				case KeyEvent.VK_D:
					if(!player.getAttacking())
					{
						if(!player.getFacingForward())
						{
							player.startReverseAttack();
						}
						else
						{
						//player.setFaceForward(true);
						player.startAttack();
						}
					}
					break;
					
					
				//-------------------------(Y)
				case KeyEvent.VK_UP: //UP
					//N=true;
					if(inMainMenu)
					{
						if(mainMenuButnum != 0)
						{
							mainMenuButnum--;
						}
						else
						{
							mainMenuButnum = pauseMax;
						}
					}
					else if(isPaused)
					{
						if(pauseButnum != 0)
						{
							pauseButnum--;
						}
						else
						{
							pauseButnum = pauseMax;
						}
					}
						break;
				case KeyEvent.VK_DOWN: //DOWN
					S=true;
					if(isPaused)
					{
						if(pauseButnum != pauseMax)
						{
							pauseButnum++;
						}
						else
						{
							pauseButnum = 0;
						}
					}
					else if(inMainMenu)
					{
						if(mainMenuButnum != mainMenuMax)
						{
							mainMenuButnum++;
						}
						else
						{
							mainMenuButnum = 0;
						}
					}
						break;
				//-------------------------(X)
				case KeyEvent.VK_LEFT: //LEFT
					W = true;
					if(E == true)
					{
						Eh = true;
						E = false;
					}
					break;
						
				case KeyEvent.VK_RIGHT: //RIGHT
					E = true;
					if(W == true)
					{
						Wh = true;
						W = false;
					}
					break;
						
				case KeyEvent.VK_I: //switch controls
					ControlS = 0;
						break;
						
				case KeyEvent.VK_ESCAPE: //Paused
					if(!inMainMenu && !isLoading)
					{
						if(isPaused)
						{
							isPaused = false;
						}
						else
						{
							pauseButnum = 0;
							isPaused = true;
						}
					}
					break;
			}
		}
		//NEW CONTROL SCHEME-------------------------------------------------------------
		if(ControlS == 2 || true)
		{
			switch (key.getKeyCode())
			{
				case KeyEvent.VK_SPACE:
					/*N=true;
					if(inMainMenu)
					{
						if(mmeh)
						{
							mme= true;
						}
					}
					else if(isPaused)
					{
						if(psh)
						{
							ps= true;
						}
					}
					else if(isLoadingF)
					{
						loadCont = true;
					}
					if(isGameOver)
					{
						inMainMenu = true;		
						isGameOver = false;
					}
	
					break;
				case KeyEvent.VK_ENTER:
					if(inMainMenu)
					{
						if(mmeh)
						{
							mme= true;
						}
					}
					else if(isPaused)
					{
						if(psh)
						{
							ps= true;
						}
					}
					else if(isLoadingF)
					{
						loadCont = true;
					}
					if(isGameOver)
					{
						inMainMenu = true;		
						isGameOver = false;
					}
	
					break;*/
					//Light
	 			case KeyEvent.VK_J:
	 				if(attackL == 0)
	 				{ 
	 					attackL = 1;
	 					mStack.add('L');
	 				}
	 				else attackL = 2;
	 				break;
					//heavy
				case KeyEvent.VK_K:
					if(attackH == 0)
					{ 
						attackH = 1;
						mStack.add('H');
					}
					else attackH = 2;
					break;
					//Special
				case KeyEvent.VK_L:
					if(attackS == 0)
					{ 
						attackS = 1;
						mStack.add('S');
					}
					else attackS = 2;
					break;
					
					
				/*//-------------------------(Y)
				case KeyEvent.VK_UP: //UP
					//N=true;
					if(inMainMenu)
					{
						if(mainMenuButnum != 0)
						{
							mainMenuButnum--;
						}
						else
						{
							mainMenuButnum = pauseMax;
						}
					}
					else if(isPaused)
					{
						if(pauseButnum != 0)
						{
							pauseButnum--;
						}
						else
						{
							pauseButnum = pauseMax;
						}
					}
						break;
				case KeyEvent.VK_DOWN: //DOWN
					S=true;
					if(isPaused)
					{
						if(pauseButnum != pauseMax)
						{
							pauseButnum++;
						}
						else
						{
							pauseButnum = 0;
						}
					}
					else if(inMainMenu)
					{
						if(mainMenuButnum != mainMenuMax)
						{
							mainMenuButnum++;
						}
						else
						{
							mainMenuButnum = 0;
						}
					}
						break;
				//-------------------------(X)
				case KeyEvent.VK_LEFT: //LEFT
					W = true;
					if(E == true)
					{
						Eh = true;
						E = false;
					}
					break;
						
				case KeyEvent.VK_RIGHT: //RIGHT
					E = true;
					if(W == true)
					{
						Wh = true;
						W = false;
					}
					break;
						
				case KeyEvent.VK_ESCAPE: //Paused
					if(!inMainMenu && !isLoading)
					{
						if(isPaused)
						{
							isPaused = false;
						}
						else
						{
							pauseButnum = 0;
							isPaused = true;
						}
					}
					break;*/
			}
		}
	}
	public void keyReleased(KeyEvent key) 
	{
			//check the key then turn off that direction
		if(ControlS ==0)
		{
			switch (key.getKeyCode())
			{
				case KeyEvent.VK_SPACE:
					N=false;
					Jh = true;
					if(inMainMenu)
					{
						mmeh = true;
					}
					
					psh= true;
					loadCont = false;
				break;
				//-------------------------(Y)
				case KeyEvent.VK_W: //UP
					/*N=false;
					Jh = true;
						break;*/
				case KeyEvent.VK_S: //DOWN
					S=false;
						break;
				//-------------------------(X)
				case KeyEvent.VK_A: //LEFT
					W = false;
					Wh = false;
					if(Eh)
					{
						Eh = false;
						E = true;
					}
					break;
					
				case KeyEvent.VK_D: //RIGHT
					E = false;
					Eh = false;
					if(Wh)
					{
						Wh = false;
						W = true;
					}
					break;	
					
									//FIRE WEAPON
										//old!
				case KeyEvent.VK_RIGHT:
					F = false;
						break;
			}
		}
		else if(ControlS == 1)
		{
			switch (key.getKeyCode())
			{
				case KeyEvent.VK_SPACE:
					N=false;
					Jh = true;
					if(inMainMenu)
					{
						mmeh = true;
					}
					
					psh= true;
					loadCont = false;
				break;
				//-------------------------(Y)
				case KeyEvent.VK_UP: //UP
					/*N=false;
					Jh = true;
						break;*/
				case KeyEvent.VK_DOWN: //DOWN
					S=false;
						break;
				//-------------------------(X)
				case KeyEvent.VK_LEFT: //LEFT
					W = false;
					Wh = false;
					if(Eh)
					{
						Eh = false;
						E = true;
					}
					break;
					
				case KeyEvent.VK_RIGHT: //RIGHT
					E = false;
					Eh = false;
					if(Wh)
					{
						Wh = false;
						W = true;
					}
					break;
			}
		}
		if(ControlS == 2 ||true)
		{
			switch (key.getKeyCode())
			{
				/*
				case KeyEvent.VK_SPACE:
					N=false;
					Jh = true;
					if(inMainMenu)
					{
						mmeh = true;
					}
					
					psh= true;
					loadCont = false;
				break;
				//-------------------------(Y)
				case KeyEvent.VK_UP: //UP
					//N=false;
					//Jh = true;
					//	break;
				case KeyEvent.VK_DOWN: //DOWN
					S=false;
						break;
				//-------------------------(X)
				case KeyEvent.VK_LEFT: //LEFT
					W = false;
					Wh = false;
					if(Eh)
					{
						Eh = false;
						E = true;
					}
					break;
					
				case KeyEvent.VK_RIGHT: //RIGHT
					E = false;
					Eh = false;
					if(Wh)
					{
						Wh = false;
						W = true;
					}
					break;
					*/
					//Light
	 			case KeyEvent.VK_J:
	 				attackL = 0;
	 				break;
					//heavy
				case KeyEvent.VK_K:
					attackH = 0;
					break;
					//Special
				case KeyEvent.VK_L:
					attackS = 0;
					break;
			}
		}
	}
	//MAIN<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>	
	public static void main(String[] args) 
	{
		//Set up
		Engine primeGame = new Engine(true);
		JFrame gameFrame = new JFrame();
		gameFrame.add(primeGame);
		gameFrame.pack();
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setTitle(windowName);
		gameFrame.setVisible(true);
			//yeah close it when it is done.
		gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		
		//Game Time
		primeGame.start();
		while(primeGame.gameRun)
		{
			//Game is currently running
		}
			//Close on exit
		gameFrame.dispose();
		
	}
public void keyTyped(KeyEvent arg0) {}
}
