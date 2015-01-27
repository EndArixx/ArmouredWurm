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
	public static Dimension window  = new Dimension(1280,720);
	public int windowHB[] = new int[4];
	private boolean isRunning = false;
	private Image screen;
	private static String windowName = "PROJECT BLALO";
	private boolean N,S,W,E,F;
	
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
	        
	        for (int i= 0; i < wnum; i++)
	        {
	        	line = br.readLine();
	 	        temp = line.split(",");
	        	weather[i] = new Looper(temp[0],Integer.parseInt(temp[1]),Integer.parseInt(temp[2]));
	        }
	        
	        for (int i= 0; i < lnum; i++)
	        {
	        	line = br.readLine();
	 	        temp = line.split(",");
	        	ladders[i] = new Platform(temp[0],Integer.parseInt(temp[1]),Integer.parseInt(temp[2]));
	        }
	        for (int i= 0; i < pnum; i++)
	        {
	        	line = br.readLine();
	 	        temp = line.split(",");
	        	platforms[i] = new Platform(temp[0],Integer.parseInt(temp[1]),Integer.parseInt(temp[2]));
	        }
	        
	        br.close();
	    } catch (IOException e) {e.printStackTrace();}
	    
	}
	private void setUp() 
	{	
		/*Goals 1/26/15
		 * create a preference file []
		 * create a level file []
		 * connect two or more levels together []
		 */
		
				//First load the options 
			
				//window hitbox
			windowHB[0]= 0;
			windowHB[1]= 0;
			windowHB[2]= window.width;
			windowHB[3]= window.height;

			
			
		
			loadLevel("res/testlevel.txt");
			
			player = new PlayerChar("playerOne","res/BroTop.png","res/BroLeg.png",50,150,202,191,2,0);
			player.setHitbox(50, 0, 100, 180);
			
			
			/*String platf = "res/platform0-1.png";
			String wall = "res/wall0-1.png";
			String ladd = "res/ladder0-1.png";
			partyboat = new TileMap("res/map0-", 2, 5, 1280, 720);
			theWorld = new World(6400,1440); 
			weather = new Looper[3];
			weather[2] = new Looper("res/loop0-1.png",-5,450);
			weather[1] = new Looper("res/loop0-2.png",-2,10);
			weather[0] = new Looper("res/loop0-2.png",-2,weather[1].height + 5);
			
			ladders = new Platform[4];
			ladders[0] = new Platform(ladd ,600,260);
			ladders[1] = new Platform(ladd ,900,610);
			ladders[2] = new Platform(ladd ,4000,610);
			ladders[3] = new Platform(ladd ,4500,260);
			platforms = new Platform[24];
				//TOP
			platforms[0] = new Platform(platf,300,350);
			int len = platforms[0].getWidth();
			platforms[1] = new Platform(platf,300+ len,350);
			platforms[2] = new Platform(platf,300+ len*2,350);
			platforms[3] = new Platform(platf,300+ len*3,350);
			platforms[4] = new Platform(platf,300+ len*4,350);
			platforms[5] = new Platform(platf,300+ len*5,350);
			platforms[6] = new Platform(platf,300+ len*6,350);
			platforms[7] = new Platform(platf,len*7,350);
				//MIDDLE
			platforms[8] =  new Platform(platf,300,700);
			platforms[9] =  new Platform(platf,300+ len,700);
			platforms[10] = new Platform(platf,300+ len*2,700);
			platforms[11] = new Platform(platf,300+ len*3,700);
			platforms[12] = new Platform(platf,300+ len*4,700);
			platforms[13] = new Platform(platf,300+ len*5,700);
				//Bottem
			platforms[14] = new Platform(platf,350,1050);
			platforms[15] = new Platform(platf,350+ len,1050);
			platforms[16] = new Platform(platf,350+ len*2,1050);
			platforms[17] = new Platform(platf,350+ len*3,1050);
			platforms[18] = new Platform(platf,350+ len*4,1050);
			platforms[19] = new Platform(platf,350+ len*5,1050);
			
			platforms[20] = new Platform(wall ,300,450);
			platforms[21] = new Platform(wall ,350,700);
			platforms[22] = new Platform(wall ,325+len*6,450);
			platforms[23] = new Platform(wall ,225+len*6,700);*/
			
			
	}

	public  void update()
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
	public void render()
	{
		int i;
			//Construct backbuffer. 
		Graphics g = screen.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, window.width, window.height);
			
		
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
			while(  System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) {
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
			//gravity
		if (!N && !onplatform && !onladder)
		{
			if(player.getY() < 8*window.height/9-player.getHeight())
			{
				player.fall();
			}
			else if(-theWorld.getY()+window.getHeight() < theWorld.getHeight())
			{
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
		}
			//NORTH
		
		if (N && !headbonk)
		{
			player.setJumping(true);
			if(player.getY() > window.height/9)
			{
				player.moveYn();
			}
			else if (theWorld.getY() < theWorld.height-window.height && theWorld.getY() < -player.speedY)
			{
				theWorld.moveYp(player.speedY);
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
				theWorld.moveXp(player.speedX);
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
				theWorld.moveXn(player.speedX);
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
		//SOUTH
		if(S && !onplatform && !falling)
		{
			if(player.getY() < 8*window.height/9-player.getHeight())
			{
				player.moveYp();
			}
			else if(-theWorld.getY()+window.getHeight() < theWorld.getHeight())
			{
				theWorld.moveYn(player.speedX);
			}
			else if(player.getY() < window.height-player.getHeight())
			{
				player.moveYp();
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
					
			case KeyEvent.VK_RIGHT:
				F = true;
					break;
					
			case KeyEvent.VK_Q: //Quit
				isRunning = false;
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
					break;
			case KeyEvent.VK_D: //RIGHT
				E = false;
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
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		//Game Time
		primeGame.start();
		while(primeGame.isRunning)
		{
			//this will close it after the game has ended...except on ubuntu
		}
		gameFrame.dispose();
	}
public void keyTyped(KeyEvent arg0) {}
}
