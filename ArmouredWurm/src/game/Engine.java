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

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Engine  extends Applet implements Runnable, KeyListener 
{
		//THESE ARE VARIALBLES!
	public BadGuy badguys[]= new BadGuy[10];
	public Platform platforms[];
	public Platform ladders[];
	public Explosive bomb[] = new Explosive[15];
	public World theWorld;
	public TileMap partyboat;
	public Looper weather[];
	public PlayerChar player;
	public Soldier baddy;
	public gun hammer[];
	public Explosive bullets[] = new Explosive[4];
	public Explosive missiles[] = new Explosive[4];
		//this is for the window
	public static Dimension window  = new Dimension(1280,720);
	public int windowHB[] = new int[4];
	public Sprite loading;
	private boolean isRunning = false;
	private Image screen;
	private static String windowName = "Armored Wurm";
		//Variables for cardnal directions.
	private boolean N,S,W,E,F;
		//these are holders to smooth out movement
	private boolean Wh,Eh;
	
	private String lvl = "res/mountain.txt";
	private boolean isLoading = false;
		//For Testing hitboxes 
	public final static boolean renderHitBox = true;
		//Pause Menu Data
	private boolean isPaused = false;
	private int pauseButnum = 0;
	private int pauseMax = 2;
	public Sprite pauseMenu;
	public Sprite pauseButtons[];
//constructor---------------------------------------------------------------------------------------	
	public Engine()
	{
			//this is the constructor for the main engine
		setPreferredSize(window);
		setUp();
		this.addKeyListener(this);	
	}
	
	
	public void loadLevel(String lvlname)
	{
			//this is designed to load in a specifically designed "Map" file 
		String name;
		String[] temp;
		BufferedReader br;
	    try {
	    	br = new BufferedReader(new FileReader(lvlname));
	        String line = br.readLine();
	        name = line;
	        
	        	//Load in the TileData
	        line = br.readLine();
	        temp = line.split(",");
	        partyboat = new TileMap(temp[0],
	        		Integer.parseInt(temp[1]),
	        		Integer.parseInt(temp[2]), 
	        		Integer.parseInt(temp[3]), 
	        		Integer.parseInt(temp[4]));
	        
	        	//Load in World Data
	        line = br.readLine();
	        temp = line.split(",");
	        theWorld = new World(Integer.parseInt(temp[0]),
	        		Integer.parseInt(temp[1])); 
	        
	        	//Load in Lvl object
	        line = br.readLine();
	        temp = line.split(",");
	        
	        weather = new Looper[Integer.parseInt(temp[0])];
	        int wnum = Integer.parseInt(temp[0]);
	        ladders = new Platform[Integer.parseInt(temp[1])];
	        int lnum  = Integer.parseInt(temp[1]);
	        platforms = new Platform[Integer.parseInt(temp[2])];
	        int pnum  = Integer.parseInt(temp[2]);
	        	//first load in looping background stuff
	        for (int i= 0; i < wnum; i++)
	        {
	        	line = br.readLine();
	 	        temp = line.split(",");
	        	weather[i] = new Looper(temp[0],Integer.parseInt(temp[1]),Integer.parseInt(temp[2]));
	        }
	        	//second load in ladders
	        for (int i= 0; i < lnum; i++)
	        {
	        	line = br.readLine();
	 	        temp = line.split(",");
	        	ladders[i] = new Platform(temp[0],Integer.parseInt(temp[1]),Integer.parseInt(temp[2]));
	        }
	        	//third load in Platforms
	        for (int i= 0; i < pnum; i++)
	        {
	        	line = br.readLine();
	 	        temp = line.split(",");
	        	platforms[i] = new Platform(temp[0],Integer.parseInt(temp[1]),Integer.parseInt(temp[2]));
	        }
	        	//MORE STUFF COMMING
	        br.close();
	    } catch (IOException e) {e.printStackTrace();}
	    
	}
	private void setUp() 
	{	
				
				//window hitbox
			windowHB[0]= 0;
			windowHB[1]= 0;
			windowHB[2]= window.width;
			windowHB[3]= window.height;
			isLoading = true;
			loadLevel(lvl);
			isLoading = false;
			
			Wh = false;
			Eh = false;
				//Menu needs finalization or automation
			pauseMenu = new Sprite("res/Pause.png",0,0);
			pauseButtons = new Sprite[3];
			pauseButtons[0] = new Sprite("res/pb0.png",135,160);
			pauseButtons[1] = new Sprite("res/pb1.png",156,274);
			pauseButtons[2] = new Sprite("res/pb2.png",156,407);
			
		
			loading = new Sprite("res/loading.png",0,0);
			
					//Player stuff
			player = new PlayerChar("Brodrick","res/50Brodrick2015.png",0,0,280/2,280/2,12,20);
			player.setHitbox(17, 10, 100, 120);	
	}

	public  void update()
	{	
		if(!isLoading && !isPaused)
		{
			theWorld.update();
			player.update(); 
			int i;
			for(i = 0; i < platforms.length;i++)
			{
				platforms[i].update(theWorld);
			}
			for(i = 0; i < ladders.length;i++)
			{
				ladders[i].update(theWorld);
			}
			partyboat.update(theWorld);
			for(i =0; i < weather.length; i++)
			{
				weather[i].update(theWorld);
			}
				//this is the first step in the game loop.
			/*for(i = 0; i < badguys.length; i++)
			{
				if(Tools.check_collision(player.getHitbox(),badguys[i].getHitbox()))
				{
					player.damage();
					//gameover();
				}
			}
			
			for(i = 0; i < badguys.length; i++)
			{
				badguys[i].update(theWorld);
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
		
			//baddy.update(theWorld,platforms);
			//baddy.sight(player, theWorld);
			//forground.update();
			
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

		if(!isLoading && !isPaused)
		{
				//background
			for(i =0; i < weather.length; i++)
			{
				if(Tools.check_collision(windowHB,weather[i].getHitbox())){weather[i].render(g);}
			}
			partyboat.render(g, windowHB);
			
				//render the player
			player.render(g);
			
				//render the platforms]
			for(i = 0; i < platforms.length;i++)
			{
				if(Tools.check_collision(windowHB,platforms[i].getHitbox())){platforms[i].render(g);}
			}
			for(i = 0; i < ladders.length;i++)
			{
				if(Tools.check_collision(windowHB,ladders[i].getHitbox())){ladders[i].render(g);}
			}
			
				//render the player
			player.render(g);
			/*
			if(Tools.check_collision(windowHB,baddy.getHitbox())){baddy.render(g);}
			for(i = 0; i < badguys.length; i++)
			{
				if(Tools.check_collision(windowHB,badguys[i].getHitbox())){badguys[i].render(g);}
			}
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
		else if (isPaused == true)
		{
			pauseMenu.render(g);
			pauseButtons[pauseButnum].render(g);
		}
		else
		{
			loading.render(g);
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
			loops = 0;
			while(  System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) 
			{
				movement();
				update();
				
				next_game_tick += SKIP_TICKS;
				loops++;
			}
			render();
		}
		
	}
	public void start() 
	{
		isRunning =true;
		new Thread(this).start();
	}
	public void gameover()
	{
		isRunning=false;
	}
	
	

	
	private void movement()
	{
		if(!isLoading && !isPaused)
		{
			
				//redesign movement
					//new movement will be acceleration based, that way getting a running start will matter
					// Terminal velocity will be a thing (aka maxspeed)
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
					if(onplatform || onladder|| player.getY() >= window.height-player.getHeight())
					{
						player.gravity = -player.gettopJump();
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
			if (W && !backbonk)
			{ 
				player.setFaceForward(false);
				player.setbackward(true);
				if(player.getX() > 2*window.width/16)
				{
					player.moveXn();
				}
				else if(theWorld.getX() < 0)
				{
					if(player.speedX > -player.topRunSpeed)
					{
						player.speedX -= player.runrate;
					}
					theWorld.moveXp(-player.speedX);
				}
				else if(player.getX() > 0)
				{
					player.moveXn();
				}
			}
			else
			{
				player.setbackward(false);
			}
				//EAST
			if (E && !frontbonk)
			{
				player.setFaceForward(true);
				player.setForward(true);
				if(player.getX() < 6*window.width/16)
				{
					player.moveXp();
				}
				else if(-theWorld.getX() < theWorld.getWidth()-window.width)
				{

					if(player.speedX < player.topRunSpeed)
					{
						player.speedX += player.runrate;
					}
					theWorld.moveXp(-player.speedX);
				}
				else if (player.getX() < window.width -player.getWidth())
				{
					player.moveXp();
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
					
			case KeyEvent.VK_RIGHT:
				F = true;
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
					
			case KeyEvent.VK_L: //DEV_BUTTON Load debug level           DEV!!!!!!!
				player.setX(0);
				player.setY(0);
				theWorld.setX(0);
				theWorld.setY(0);
				isLoading = true;
				loadLevel("res/testlevel.txt");
				isLoading = false;
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
		Engine primeGame = new Engine();
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
		while(primeGame.isRunning)
		{
			//this will close it after the game has ended...except on ubuntu...
		}
		gameFrame.dispose();
	}
public void keyTyped(KeyEvent arg0) {}
}
