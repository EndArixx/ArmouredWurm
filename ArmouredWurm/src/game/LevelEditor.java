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
import javax.swing.JFrame;


//this is a new idea i had to help make lvls

/*Level Editor!
 * Goals: to creates a fast and easy way to generate the level txt file that can easily be read in by the main game engine, 
 * this is to help prevent types when creating the level and will also allow the people to see how the level will look visually.
 */



/*
 * Step one		[X]
 * 		Render a background and allow movement using WASD OR Arrow Keys
 * 			Render back 	(X)
 * 			WASD move		(X)
 * Step Two		[] 
 * 		add ability to load a level
 * 			-load file 		(X)
 * 			-promt user		() 
 * Step Three	[]
 * 		add ability to add objects and move around objects
 * 			-platforms		()
 * 				+add			<>
 * 				+move			<X>
 * 			-weathers		()
 * 			-ladders		()
 * 			-Other?			()
 * 			-map"Doors"  	()
 * Step yon 		[]
 * 		add ability to Save
 * 
 * Stem V 		[]
 * 		add ability to change hitbox
 * 		
 * 
 */
public class LevelEditor extends Engine implements Runnable, KeyListener
{
	/**
	 * Level Editor
	 */

		//lvl name
	private String lvl = "res/mountain.txt";
	protected int scrollSpeed = 10;
	protected int moveSpeed = 10;
	
		//Object targeting
	public int target;
	public boolean targetH;
	private boolean RtargetH;
	
		//Movement directions (north,south etc...)
	boolean MN,MS,ME,MW;
		//Constructor
	public LevelEditor()
	{
			//this is the constructor for the main engine
		setPreferredSize(window);
		setUp();
		this.addKeyListener(this);	
	}
	
	private void setUp() 
	{	
		windowHB[0]= 0;
		windowHB[1]= 0;
		windowHB[2]= window.width;
		windowHB[3]= window.height;
		
		MN = false;
		MS = false;
		ME = false;
		MW = false;
		
		this.targetH= true;
		this.RtargetH = true;
		this.target=0;
			//Promt uses to load a level here
		loadLevel(lvl);
	}
	
	public void addPlaform()
	{
		/*create temp thats 1 bigger then plat[] 
		 * add each piece then make a new one for 
		 * the last one;
		 * then finally set plat[] = temp
		 */
	}
	public void deletePlatform(int target)
	{
		/*
		 * create a tombstone at the target deletion, 
		 * create temp new array that is 1 smaller then plat[]
		 * then add everything but tomb 
		 */
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

			//background
		gameWorld.render(g, windowHB);
		
		for(i =0; i < weather.length; i++)
		{
			if(Tools.check_collision(windowHB,weather[i].getHitbox())){weather[i].render(g);}
		}
			//render the platforms]
		for(i = 0; i < platforms.length;i++)
		{
			if(Tools.check_collision(windowHB,platforms[i].getHitbox())){platforms[i].render(g);}
		}
		for(i = 0; i < ladders.length;i++)
		{
			if(Tools.check_collision(windowHB,ladders[i].getHitbox())){ladders[i].render(g);}
		}
		g.setColor(Color.RED);
		g.drawRect(platforms[target].hitbox[0]+platforms[target].x,
				platforms[target].hitbox[1]+platforms[target].y,
				platforms[target].hitbox[2], 
				platforms[target].hitbox[3]);
		
		
			// Get window's graphics object. 
		g = getGraphics();
			// Draw backbuffer to window. 
		g.drawImage(screen,0,0,window.width,window.height,0,0,window.width, window.height,null);
			//NO CODEBELOW!
		g.dispose();				
	}
	public  void update()
	{	
			//Selected item will have Red hitbox?
			//	visible hitbox?
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
		gameWorld.update(theWorld);
		for(i =0; i < weather.length; i++)
		{
			weather[i].update(theWorld);
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
		
		//PLATFORM MOVEMENT-------------------------------------
			//MOVE PLATFORM NORTH
		if(MN)
		{
			if(platforms[target].getTrueY() > 0)
			{
				platforms[target].moveYn(this.moveSpeed);
			}
		}
			//MOVE PLATFORM SOUTH
		if(MS)
		{
			if(platforms[target].getTrueY() < (theWorld.getHeight() - platforms[target].getHeight()))
			{
				platforms[target].moveYp(this.moveSpeed);
			}
		}
			//MOVE PLATFORM EAST
		if(ME)
		{
			if(platforms[target].getTrueX() < (theWorld.getWidth() - platforms[target].getWidth()))
			{
				platforms[target].moveXp(this.moveSpeed);
			}
		}
			//MOVE PLATFORM WEST
		if(MW)
		{
			if(platforms[target].getTrueX() > 0)
			{
				platforms[target].moveXn(this.moveSpeed);
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
					
			//------------------------------OTHER
			case KeyEvent.VK_Q: //Quit
				isRunning = false;
					break;
					
			//---------------------------TARGETING
			case KeyEvent.VK_T: //Target
				if(targetH)
				{
					if(target < platforms.length-1)
					{
						this.target++;
						this.targetH=false;
					}
					else
					{
						this.target = 0;
						this.targetH=false;
					}
				}
					break;
			case KeyEvent.VK_R: //reverse Target
				if(RtargetH)
				{
					if(target > 0)
					{
						this.target--;
						this.RtargetH=false;
					}
					else
					{
						this.target = platforms.length-1;
						this.RtargetH=false;
					}
				}
					break;
			//------------------------------------MOVEMENT
					//-------------------------(Y)
			case KeyEvent.VK_UP: //UP
				MN=true;
					break;
			case KeyEvent.VK_DOWN: //DOWN
				MS=true;
					break;
					//-------------------------(X)
			case KeyEvent.VK_LEFT: //LEFT
				MW = true;
					break;
					
			case KeyEvent.VK_RIGHT: //RIGHT
				ME = true;
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
					
			//---------------------------TARGETING
			case KeyEvent.VK_T: //Target
				this.targetH = true;
					break;
			case KeyEvent.VK_R: //Reverse Target
				this.RtargetH = true;
					break;
					
			//------------------------------------MOVEMENT
					//-------------------------(Y)
			case KeyEvent.VK_UP: //UP
				MN=false;
					break;
			case KeyEvent.VK_DOWN: //DOWN
				MS=false;
					break;
					//-------------------------(X)
			case KeyEvent.VK_LEFT: //LEFT
				MW = false;
					break;
					
			case KeyEvent.VK_RIGHT: //RIGHT
				ME = false;
					break;
		}
		
	}
	
	
	public static void main(String[] args) 
	{
		//Set up
		LevelEditor primeGame = new LevelEditor();
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
}
