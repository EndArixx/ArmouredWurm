package game;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextField;


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
 */

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
	public boolean targetH;
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
	
		//Movement directions (north,south etc...)
	protected boolean moveN,moveS,moveE,moveW, saving, adding, deleting, addL, delL, changing, chaL; 
	protected boolean hitboxXB,hitboxXS,hitboxYB,hitboxYS;
	
	private boolean promtformapname = true;
	 
		//Constructor
	public LevelEditor(boolean test)
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
					+ platforms.length + "," 
					+ doors.length +
					",0,0" + "\n");
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
			//fw.write(" \n");
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
		this.modeTotal = 3;
		this.modeCounter = 0;
		this.tab = false;
		this.tabL = true;
		
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
		}
		
		loadLevel(lvl);
		loadSprites(sTypesLoc);
		
		genericPlat =  spriteTypes[0];
	}
	public Platform[] platformSwitchSprite(Platform[] inplat)
	{
		if( inplat.length != 0)
		{
			 inplat[target] = new Platform(spriteTypes[stCounter], inplat[target].getTrueX(), inplat[target].getTrueY());
			
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
		
		temp[ inplat.length]= new Platform(genericPlat, -theWorld.getX() + window.width/2, -theWorld.getY() + window.height/2);
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
					 indoor[target].mapstart[1]);
			
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
		
		Door temp[] = new Door[ indoor.length + 1];
		for(int i =0; i < indoor.length;i++)
		{
			temp[i] = indoor[i];
		}
		
		temp[ indoor.length]= new Door(genericPlat,
				-theWorld.getX() + window.width/2,
				-theWorld.getY() + window.height/2
				,0,0,genericMap,0,0);
		 indoor = temp;
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
				if(Tools.check_collision(windowHB,weather[i].getHitbox())){weather[i].render(g);}
			}
			
				//background
			gameWorld.render(g, windowHB);
			
				//render the platforms
			for(i = 0; i < platforms.length;i++)
			{
				if(Tools.check_collision(windowHB,platforms[i].getHitbox())){platforms[i].render(g);}
			}
			for(i = 0; i < ladders.length;i++)
			{
				if(Tools.check_collision(windowHB,ladders[i].getHitbox())){ladders[i].render(g);}
			}
			for(i = 0; i < doors.length;i++)
			{
				if(Tools.check_collision(windowHB,doors[i].getHitbox())){doors[i].render(g);}
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
				}
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
					//Adding a new platform
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
		
		if(!shiftmonitor)
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
		}
		else
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
	public void platformHitBox(Platform[] inplat, int PHmove)
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
		LevelEditor primeGame = new LevelEditor(true);
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
