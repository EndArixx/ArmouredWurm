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
 * Step one		[]
 * 		Render a background and allow movement using WASD OR Arrow Keys
 * 
 * Step Two		[] 
 * 		add ability to load a level
 * 
 * Step Three	[]
 * 		add ability to add objects and move around objects
 * 
 * Step yon 		[]
 * 		add ability to Save
 * 
 * Stem V 		[]
 * 		add ability to change hitbox
 * 		
 * 
 */
public class LevelEditor extends Applet implements Runnable, KeyListener
{
	public BadGuy badguys[];
	public Platform platforms[];
	public Platform ladders[];
	public Explosive bomb[];
	public World theWorld;
	public TileMap gameWorld;
	public Looper weather[];
	public PlayerChar player;
	public Soldier baddy;
	public gun hammer[];
	public Explosive bullets[];
	public Explosive missiles[]
			;
		//this is for the window
	public static Dimension window  = new Dimension(1280,720);
	public int windowHB[] = new int[4];
	public Sprite loading;
	
		//it is important that you have both of these so the game closes
		//	in the correct order.
	private boolean isRunning = false;
	public boolean gameRun =true;
	
	private Image screen;
	private static String windowName = "Armored Wurm";
		//Variables for cardnal directions.
	private boolean N,S,W,E,F;
		//these are holders to smooth out movement
	private boolean Wh,Eh,Jh;
	
	private String lvl = "res/mountain.txt";
	private boolean isLoading = false;
		//For Testing hitboxes 
	public final static boolean renderHitBox = true
			;
	
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
		
		
	}
	public void start() 
	{
		isRunning =true;
		new Thread(this).start();
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
				update();
				
				next_game_tick += SKIP_TICKS;
				loops++;
			}
			render();
			
				//working on fixing memory leak
			System.gc();
		}
			//this is after game
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
		for(i =0; i < weather.length; i++)
		{
			if(Tools.check_collision(windowHB,weather[i].getHitbox())){weather[i].render(g);}
		}
		gameWorld.render(g, windowHB);
		
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
		gameWorld.update(theWorld);
		for(i =0; i < weather.length; i++)
		{
			weather[i].update(theWorld);
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
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
