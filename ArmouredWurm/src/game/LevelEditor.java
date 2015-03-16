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
import java.io.FileWriter;
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
 * 		add ability to add/delete objects and move around objects
 * 			-platforms		(X)
 * 				+add			<X>
 * 				+move			<X>
 * 				+delete			<X>
 * 			-weathers		()
 * 			-ladders		()
 * 			-Other?			()
 * 			-map"Doors"  	()
 * 
 * Step yon 	[X]
 * 		add ability to Save
 * 			this will have to keep getting updated
 * 			as new functionality is added.
 * 
 * Step V 		[]
 * 		add ability to change hitbox
 * 	
 * Step 6		[]
 * 		add ability to change which image an object is using 
 */

public class LevelEditor extends Engine implements Runnable, KeyListener
{
	/**
	 * Level Editor
	 */

		//lvl name
	private String lvl = "res/testlevel.txt";
	protected int scrollSpeed = 10;
	protected int moveSpeed = 10;
	
		//Object targeting
	public int target;
	public boolean targetH;
	private boolean RtargetH;
	private boolean isLoading = false;
	protected String genericPlat; 
	
	
		//this will hold all the different types of sprites
	protected String[] spriteTypes;
	protected String sTypesLoc = "res/SpriteTypes.txt";
	protected int stCounter;
	
		//Movement directions (north,south etc...)
	boolean MN,MS,ME,MW, saving, adding, deleting, addH, delH, changing, chaH; 
		
		//Constructor
	public LevelEditor()
	{
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
					+ladders.length + ","
					+ platforms.length + ",0,0,0" + "\n");
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
						+ ladders[i].trueY + "\n");
			}
			
				//Write out Platforms
			for(int i = 0;i < platforms.length; i++)
			{
				fw.write(platforms[i].name + ","
						+ platforms[i].trueX + ","
						+ platforms[i].trueY + "\n");
			}
			fw.write(" \n");
			fw.close();
		
		}catch (IOException e) {e.printStackTrace();}
	}
	
	
	
	private void setUp() 
	{	
		this.windowHB[0]= 0;
		this.windowHB[1]= 0;
		this.windowHB[2]= window.width;
		this.windowHB[3]= window.height;
		
		this.saving = false;
		this.adding = false;
		this.deleting = false;
		this.changing = false;
		this.MN = false;
		this.MS = false;
		this.ME = false;
		this.MW = false;
		
		this.addH = true;
		this.delH = true;
		this.chaH = true;
		this.stCounter = 0;
		
		this.targetH= true;
		this.RtargetH = true;
		this.target=0;
		
			//Promt should ask user for level name here #JOHN DO THIS
		loadLevel(lvl);
		loadSprites(sTypesLoc);
		
		genericPlat = "res/platform0-1.png";
	}
	
	public void addPlaform()
	{
		/*create temp thats 1 bigger then plat[] 
		 * add each piece then make a new one for 
		 * the last one;
		 * then finally set plat[] = temp
		 */
		
		Platform temp[] = new Platform[platforms.length + 1];
		for(int i =0; i < platforms.length;i++)
		{
			temp[i] = platforms[i];
		}
		
		temp[platforms.length]= new Platform(genericPlat, -theWorld.getX() + window.width/2, -theWorld.getY() + window.height/2);
		platforms = temp;
		target = platforms.length-1;
	}
	public void deletePlatform()
	{
		
		/*
		 * create a tombstone at the target deletion, 
		 * create temp new array that is 1 smaller then plat[]
		 * then add everything but tomb 
		 */
		if(platforms.length == 0)
		{
			return;
		}
		int tracker = 0;
		Platform temp[] = new Platform[platforms.length - 1];
		for(int i =0; i < platforms.length - 1;i++)
		{
			if(i == target && tracker == 0)
			{
				tracker++;
				i--;
			}
			else
			{
				temp[i] = platforms[i + tracker];
			}
		}
		if(this.target != 0)
		{
			this.target--;
		}
		platforms = temp;
		
		
	}
	public void platformSwitchSprite()
	{
		if(platforms.length != 0)
		{
			platforms[target] = new Platform(spriteTypes[stCounter],platforms[target].getTrueX(),platforms[target].getTrueY());
			
			if(stCounter < spriteTypes.length - 1)
			{
				stCounter++;
			}
			else
			{
				stCounter = 0;
			}
		}
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


		if(!isLoading)
		{
			for(i =0; i < weather.length; i++)
			{
				if(Tools.check_collision(windowHB,weather[i].getHitbox())){weather[i].render(g);}
			}
			
				//background
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
			
			
			if(platforms.length != 0)
			{
				g.setColor(Color.RED);
				g.drawRect(platforms[target].hitbox[0]+platforms[target].x,
					platforms[target].hitbox[1]+platforms[target].y,
					platforms[target].hitbox[2], 
					platforms[target].hitbox[3]);
			}
		}
		if(isLoading)
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
	public  void update()
	{	
		if(!isLoading)
		{
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
			gameWorld.update(theWorld);
			for(i =0; i < weather.length; i++)
			{
				weather[i].update(theWorld);
			}
			
			if(saving) //THIS IS WHERE IT SAVES FOR NOW!
			{
				saveLevel("TEST.txt");
			}
				//Adding a new platform
			if(adding && addH)
			{
				addH = false;
				adding = false;
				
				isLoading = true;
				addPlaform();
				isLoading = false;
			}
				//Deleting
			if(deleting && delH)
			{
				delH = false;
				deleting = false;
				 
				isLoading = true;
				deletePlatform();
				isLoading = false;
			}
			
				//changing the Sprite
			if(changing && chaH)
			{
				chaH = false;
				changing = false;
				
				isLoading = true;
				platformSwitchSprite();
				isLoading = false;
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
		
		//PLATFORM MOVEMENT-------------------------------------
			//MOVE PLATFORM NORTH
		if(MN && platforms.length !=0)
		{
			if(platforms[target].getTrueY() > 0)
			{
				platforms[target].moveYn(this.moveSpeed);
			}
		}
			//MOVE PLATFORM SOUTH
		if(MS && platforms.length !=0)
		{
			if(platforms[target].getTrueY() < (theWorld.getHeight() - platforms[target].getHeight()))
			{
				platforms[target].moveYp(this.moveSpeed);
			}
		}
			//MOVE PLATFORM EAST
		if(ME && platforms.length !=0)
		{
			if(platforms[target].getTrueX() < (theWorld.getWidth() - platforms[target].getWidth()))
			{
				platforms[target].moveXp(this.moveSpeed);
			}
		}
			//MOVE PLATFORM WEST
		if(MW && platforms.length !=0)
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
					
			case KeyEvent.VK_L: //Load new sprite image
				if(chaH)
				{
					this.changing = true;
				}
					break;
					
			//---------------------------------ADDING/DELETING
			case KeyEvent.VK_N:	//Adding
				if(addH)
				{
					this.adding = true;
				}
				break;
			case KeyEvent.VK_M:	//Deleting
				if(delH)
				{
					this.deleting = true;
				}
				break;
			//---------------------------TARGETING
			case KeyEvent.VK_T: //Target
				if(targetH)
				{
					if(target < platforms.length-1)
					{
						this.target++;
					}
					else
					{
						this.target = 0;
					}
					this.targetH=false;
				}
					break;
			case KeyEvent.VK_R: //reverse Target
				if(RtargetH)
				{
					if(target > 0)
					{
						this.target--;
					}
					else
					{
						this.target = platforms.length-1;
					}
					this.RtargetH=false;
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
			//---------------------------------ADDING/DELETING
			case KeyEvent.VK_N:	//Adding
				this.addH = true;
					break;
			case KeyEvent.VK_M:	//Deleting
				this.delH = true;
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
					
			//----------------------------------FILE IO
			case KeyEvent.VK_P:	 //SAVE
				saving = false;
					break;
			//------------------------------------OTHER
			case KeyEvent.VK_L: //Load new sprite image
				this.chaH = true;
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
