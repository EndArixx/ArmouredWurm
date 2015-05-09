package game;

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
import java.io.IOException;
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

		//For Testing hitboxes 
	public final static boolean renderHitBox = false;
	
		//THESE ARE VARIALBLES!
	public Soldier badguys[];
	public Spike spikes[];
	public Platform platforms[];
	public Platform ladders[];
	public Door doors[];
	public Explosive bombs[];
	public World theWorld;
	public TileMap gameWorld;
	public Looper weather[];
	public PlayerChar player;
	
	
	public gun hammer[]; 
	public Explosive bullets[] = new Explosive[4];
	public Explosive missiles[] = new Explosive[4];
	
		//A buffer to hold sprites to free up redundancy
	public Map<String,BufferedImage> lvlspriteData;
	public Map<String,BufferedImage> permaSprites;
	
		//this is for the window
	public static Dimension window  = new Dimension(1280,720);
	public int windowHB[] = new int[4];
	public Sprite loading;
	
		//it is important that you have both of these so the game closes
		//	in the correct order.
	protected boolean isRunning = false;
	public boolean gameRun =true;
	
	protected Image screen;
	protected static String windowName = "Armored Wurm";
		//Variables for cardinal directions.
	protected boolean N,S,W,E,F;
		//these are holders to smooth out movement
	private boolean Wh,Eh,Jh;
	
	private String lvl = "res/TEST.txt";
	private boolean isLoading = false;
	private int loadTarget;
	
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
			this.addKeyListener(this);	
		}
	}
	
	protected void loadLevel(String lvlname)
	{
		
		lvlspriteData.clear();
		lvlName = lvlname;
			//this is designed to load in a specifically designed "Map" file 
		String name;
		String[] temp;
		BufferedReader br;
	    try {
	    	FileReader fr = new FileReader(lvlname);
	    	br = new BufferedReader(fr);
	        String line = br.readLine();
	        name = line;
	        
	        	//Load in the TileData
	        line = br.readLine();
	        temp = line.split(",");
	        gameWorld = new TileMap(temp[0],
	        		Integer.parseInt(temp[1]),
	        		Integer.parseInt(temp[2]), 
	        		Integer.parseInt(temp[3]), 
	        		Integer.parseInt(temp[4]),lvlspriteData);
	        
	        	//Load in World Data
	        line = br.readLine();
	        temp = line.split(",");
	        theWorld = new World(Integer.parseInt(temp[0]),
	        		Integer.parseInt(temp[1])); 
	        
	        	//Load in Lvl object
	        line = br.readLine();
	        temp = line.split(",");
	        
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
	        
	        	//first load in looping background stuff
	        for (int i= 0; i < wnum; i++)
	        {
	        	line = br.readLine();
	 	        temp = line.split(",");
	        	weather[i] = new Looper(temp[0],Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),lvlspriteData);
	        }
	        	//second load in ladders
	        for (int i= 0; i < lnum; i++)
	        {
	        	line = br.readLine();
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
	        	//third load in Platforms
	        for (int i= 0; i < pnum; i++)
	        {
	        	line = br.readLine();
	 	        temp = line.split(",");
 	        	platforms[i] = new Platform(temp[0],Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),lvlspriteData);
	 	        if(temp.length >= 4)
	 	        {
	 	        	platforms[i].setHitbox(
	 	        			Integer.parseInt(temp[3]),
	 	        			Integer.parseInt(temp[4]),
	 	        			Integer.parseInt(temp[5]),
	 	        			Integer.parseInt(temp[6]));
	 	        }
	 	        
 	        }
	        for(int i= 0;i< dnum; i++)
	        {
	        	line = br.readLine();
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
	        	line = br.readLine();
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
	        	line = br.readLine();
	 	        temp = line.split(",");
	 	       spikes[i] = new Spike(temp[0],
	 	    		   Integer.parseInt(temp[1]),
	 	    		   Integer.parseInt(temp[2]),
	 	    		  Integer.parseInt(temp[3]),
	 	    		 Integer.parseInt(temp[4]),
	 	    		Integer.parseInt(temp[5]),
	 	    		Integer.parseInt(temp[6]),
	 	    		Integer.parseInt(temp[7]),
	 	    		   lvlspriteData);
	 	        if(temp.length >= 9)
	 	        {
	 	        	spikes[i].setHitbox(
	 	        			Integer.parseInt(temp[8]),
	 	        			Integer.parseInt(temp[9]),
	 	        			Integer.parseInt(temp[10]),
	 	        			Integer.parseInt(temp[11]));
	 	        }
	 	        
 	        }
	        for(int i= 0; i < bombnum; i++)
	        {
	        	line = br.readLine();
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
	        fr.close();
	        br.close();
	        
	    } catch (IOException e) 
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
			
			isLoading = true;
			loadLevel(lvl);
			isLoading = false;
			loadTarget = -1;
			
				//Wh and Eh are used so that when the player is pressing left then presses right, once he lets go of 
				// the right button if he is still holding left he then starts going left again or visa versa
			Wh = false;
			Eh = false;
				//Jh is used so that when the player presses jump he has to let go and press again to jump again
				//	instead if him constantly hopping as the player holds jump
			Jh = true;
				//Menu needs finalization or automation
			pauseMenu = new Sprite("res/Pause.png",0,0,permaSprites );
			
			
			pauseButtons = new Sprite[3];
			pauseButtons[0] = new Sprite("res/pb0.png",135,160,permaSprites);
			pauseButtons[1] = new Sprite("res/pb1.png",156,274,permaSprites);
			pauseButtons[2] = new Sprite("res/pb2.png",156,407,permaSprites );
			
		
			loading = new Sprite("res/loading.png",0,0,permaSprites );
			

			gameover = new Sprite("res/gameover.png",0,0,permaSprites );
			isGameOver = false;
			
					//Player stuff
			player = new PlayerChar("Brodrick","res/50Brodrick2015.png",0,0,280/2,280/2,12,20,permaSprites);
			player.setHitbox(17, 10, 100, 120);	
	}

	public  void update()
	{		
		
		if(!isLoading && !isPaused && !isGameOver)
		{	
			if(player.getDead())
			{
				this.isGameOver = true;
			}
			
			for(int i = 0; i < doors.length ; i++)
			{
				if(Tools.check_collision(doors[i].getHitbox(),player.getHitbox()))
				{
					isLoading = true;
					this.loadTarget = i;
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
			gameWorld.update(theWorld);
			for(i =0; i < weather.length; i++)
			{
				weather[i].update(theWorld);
			}
			for(i =0; i < spikes.length; i++)
			{
				spikes[i].update(theWorld,damageQ);
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
				//PLAYER DAMAGE ADD ENEMY DAMAGE NEXT
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
			
			
			
			
				//this is the first step in the game loop.
			/*for(i = 0; i < badguys.length; i++)
			{
				if(Tools.check_collision(player.getHitbox(),badguys[i].getHitbox()))
				{
					player.damage();
					//gameover();
				}
			}
			
			
			
			for(i = 0; i < bullets.length; i++)
			{
				bullets[i].animateCol();
				bullets[i].update(theWorld);
			}
			for( i = 0; i < bomb.length; i++)
			{
				bomb[i].proxy(player, badguys, bomb, bullets);
				bomb[i].update(theWorld);
			}
			for( i = 0; i < missiles.length; i++)
			{
				missiles[i].proxy(player, badguys, bomb, platforms);
				missiles[i].update(theWorld);
			}
			for( i = 0; i < bullets.length; i++)
			{
				bullets[i].proxy(player, badguys, bomb, platforms);
				bullets[i].update(theWorld);
			}*/
			
			//forground.update();
			
		}
		else if(isLoading && loadTarget != -1)
		{
			int[] tplayer = doors[loadTarget].playerloc;
			int[] tworld = doors[loadTarget].mapstart;
			loadLevel(doors[loadTarget].newMap);
			player.setX(tplayer[0]);
			player.setY(tplayer[1]);
			theWorld.setX(tworld[0]);
			theWorld.setY(tworld[1]);
			isLoading = false;
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
		
		
		if(!isLoading && !isPaused && !isGameOver)
		{
				//this is done before gameWorld because of the way i made the test boat level
			for(i =0; i < weather.length; i++)
			{
				if(Tools.check_collision(windowHB,weather[i].getHitbox())){weather[i].render(g,lvlspriteData);}
			}
			
				//background
			gameWorld.render(g, windowHB,lvlspriteData);
	
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
			/*
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
		else
		{
			loading.render(g,permaSprites);
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
				update();
				
				next_game_tick += SKIP_TICKS;
				loops++;
			}
			render();
			
				//memory stuff
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
			 * Todo
			 * 
			 * 	- add support for moving platforms 
			 * 	
			 */
		if(!isLoading && !isPaused)
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
			for(int i = 0; i < ladders.length ; i++)
			{
				if(Tools.check_collision(ladders[i].getHitbox(), player.getHitbox()))
				{
					onladder = true;
				}
			}
			for(int i = 0; i < platforms.length; i++)
			{
				if(!onladder){if(Tools.check_collision(platforms[i].getHitbox(), player.getheadHitbox()))
					{headbonk = true;}}
				if (Tools.check_collision(platforms[i].getHitbox(), player.getfrontHitbox()))
					{frontbonk = true;}
				if (Tools.check_collision(platforms[i].getHitbox(), player.getbackHitbox()))
					{backbonk = true;}
				if(!onladder){if (Tools.check_collision(platforms[i].getHitbox(), player.getfeetHitbox()))
					{onplatform = true;}}
				
			}
			if(player.getY() < 1)
			{
				headbonk= true;
			}
			
			
				//gravity
			if (!player.getJumping() && !onplatform && !onladder)
			{
				if(player.gravity <0 ) {player.gravity = 0;}
				
				if(player.getY() < 8*window.height/9-player.getHeight())
				{
					player.fall();
	 			}
				else if(-theWorld.getY()+window.getHeight() < theWorld.getHeight())
				{
					
					if(player.gravity < player.gettopGravity()) 
					{
						player.gravity = player.gravity + player.fallrate;  
					}
					
					theWorld.moveYn(player.getGravity());
					
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
				//NORTH
			if (N && !headbonk && !player.getAttacking())
			{
				if(onladder)
				{
					if(player.getY() > window.height/9)
					{
						player.setY((int) (player.getY() - player.gettopRunSpeed()));
					}
					else if (theWorld.getY() < theWorld.height-window.height && theWorld.getY() < player.getGravity())
					{
						theWorld.moveYn(-player.gettopRunSpeed());
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
							theWorld.moveYn(player.getGravity());
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
			
			
				//WEST
			if (W && !backbonk&&!player.getAttacking())
			{ 
				player.setFaceForward(false);
				player.setbackward(true);
				if(player.getX() > 2*window.width/16)
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
						theWorld.moveXp(-player.speedX);
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
				//EAST
			if (E && !frontbonk && !player.getAttacking())
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
						theWorld.moveXp(-player.speedX);
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
				player.setForward(false);
			}
			
			if(!E&& !W)
			{
				player.speedX = 0;
			}
			//SOUTH
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
					
					theWorld.moveYn(player.gettopRunSpeed());
					
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

	public void keyPressed(KeyEvent key) 
	{
		
			//identify which key was pressed
			//set the correct direction to true
			//then change the player model 
		switch (key.getKeyCode())
		{
			//attacklogic
			case KeyEvent.VK_LEFT:
				if(!player.getAttacking())
				{
					player.setFaceForward(false);
					player.startAttack();
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(!player.getAttacking())
				{
					player.setFaceForward(true);
					player.startAttack();
				}
				break;
				
				
			//-------------------------(Y)
			case KeyEvent.VK_W: //UP
				N=true;
				if(isPaused)
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
					
			case KeyEvent.VK_Q: //Quit
				isRunning = false;
					break;
			case KeyEvent.VK_P: //Paused
				if(isPaused)
				{
					isPaused = false;
				}
				else
				{
					isPaused = true;
				}
				break;
					
		}
	}
	public void keyReleased(KeyEvent key) 
	{
			//check the key then turn off that direction
		switch (key.getKeyCode())
		{
			//-------------------------(Y)
			case KeyEvent.VK_W: //UP
				N=false;
				Jh = true;
					break;
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
			case KeyEvent.VK_RIGHT:
				F = false;
					break;
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
