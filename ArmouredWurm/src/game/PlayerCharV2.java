package game;

import game.triggers.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import javax.imageio.ImageIO;

public class PlayerCharV2 extends Sprite 
{
	
	protected String file;
	protected String charname;
	
	protected int headhitbox[] = new int[4];
	protected int feethitbox[] = new int[4];
	protected int backhitbox[] = new int[4];
	protected int fronthitbox[] = new int[4];
	
	//COMBAT UPDATE 2016
	protected Trigger[] triggers;
	protected String state;
	protected Spark[] sparks;
	protected State currstate;
	protected Map<String,AttackHitBoxes> HitboxMap;
	protected Map<String,State> statesMap;
	protected Map<String,Spark> sparksMap;
	protected Map<String,Value> valueMap;
	protected Map<String,String> playerSprites;
	
	protected String triggerloc;
	protected String statesloc;
	protected String sparkloc;
	protected String attackloc;
	protected String valueloc;
	
	//JOHN REMOVEl
	protected String[] testAni;
	protected int testN;
	
	//should this be a String?
	protected Map<String,Trigger> triggerMap;
	
	//Constructors 
	public PlayerCharV2(String infile, Map<String,BufferedImage> spriteData, boolean derp)
	{
		/*
		 NEW IDEA! 
		---------------------------------------------
		 1)Triggers 
		 2)Sparks
		 	a) attack hitboxes
		 3)States 
		---------------------------------------------
		  the goal is to load the three files listed above then use them to dynamically figure out which animation to run.
		This will have to change the player file.
		
		1) Open player file
		2) Open Sprite file
		3) Open Triggers/states/sparks
		4) load up the TSSs
		5) ...
		 */	
		
		//JOHN THROW SOME TRY CATCHES ON THIS!
		super();
		this.file = infile;
		this.animate = true;
		this.firstloop = true;
		
		String[] temp;
		BufferedReader br = null;
		
		int err = 0;
		try 
		{
			
			//PLAYER FILE---------------------------------
			
			//create a new Armoured Wurm reader.
			Tools reader = new Tools();
			
			
	    		//Favor external files, then internal
	    	err= 1;
	    	br = reader.getBR(infile);
	    	
	        String line = Tools.readlineadv(br);
	        this.charname = line;
	        
	        line = Tools.readlineadv(br);
	        this.name= line;
	        
	        line = Tools.readlineadv(br);
	        temp= line.split(",");
	        
	        //Set hitbox shtuff
	        this.width =Integer.parseInt(temp[0]);
	        this.height = Integer.parseInt(temp[1]);
	        this.setHitbox(
	        		Integer.parseInt(temp[2]), 
	        		Integer.parseInt(temp[3]), 
	        		Integer.parseInt(temp[4]),
	        		Integer.parseInt(temp[5]));

	        
	        //get locations for Trigger files
	        triggerloc = Tools.readlineadv(br);
	    	statesloc = Tools.readlineadv(br);
	    	sparkloc = Tools.readlineadv(br);
	    	attackloc = Tools.readlineadv(br);
	    	valueloc = Tools.readlineadv(br);
	    	String[] spritefiles = Tools.readlineadv(br).split(";");
	        	//Close the files 
	    	reader.closeBR();
	    	
	        err= 2;
        	String spriteHead;
        	String spriteTail;
        	BufferedImage spriteMap = null;
        	 this.playerSprites = new HashMap<String,String>();
	        for(int i = 0; i < spritefiles.length; i++)
	        {
	        	temp = spritefiles[i].split(",");
				try 
				{
					spriteHead = temp[0];
					spriteTail = temp[1];
					if(new File(spriteTail).isFile())
					{
						spriteMap = ImageIO.read(new File(spriteTail));
					}
					else
					{
						spriteMap = ImageIO.read(getClass().getResource("/"+spriteTail));
					}
					this.playerSprites.put(spriteHead,spriteTail);
					spriteData.put(spriteTail,spriteMap);
				}
				catch (Exception e) 
				{
					System.out.println("Error, Bad Sprite:"+ spritefiles[i]);
				}
	        }
	        //TRIGGERS-------------------------------------------------------
	        err= 3;
	    	reader.getBR(triggerloc);
	        this.triggerMap = new HashMap<String,Trigger>();
	        Trigger tri;
	        
	        while((line = Tools.readlineadv(br))!=null)
	        {
	        	 tri = new Trigger(line);
	        	 //John change this from mapping the current name and instead map a cause to it.
	        	 this.triggerMap.put( tri.getName(), tri); 
	        }
	        reader.closeBR();
	        
	     
	        //STATES---------------------------------------------------------
	        err= 4;
	    	br = reader.getBR(statesloc);
	        this.statesMap = new HashMap<String,State>();
	        State tempstate;
	        
	        while((line = Tools.readlineadv(br))!=null)
	        {
	        	temp= line.split(">");
	        	tempstate = new State(temp[0],temp[1]);
		        this.statesMap.put(temp[0],tempstate);
	        }
	        
	        reader.closeBR();
	        
	        
	        //Sparks---------------------------------------------------------
	        err= 5;
	        if(new File(sparkloc).isFile())
	    	br = reader.getBR(sparkloc);
	        this.sparksMap = new HashMap<String,Spark>();
	        Spark tspark;
	        testAni = new String[30]; //TEST
	        int testI = 0; //TEST
	        while((line = Tools.readlineadv(br))!=null)
	        {
	        	tspark = new Spark(line);
	        	this.sparksMap.put(tspark.getName(),tspark);
		        if(tspark.type.equals("A")) //TEST
		        {
		        	testAni[testI] = tspark.name;
		        	testI++;
		        }
	        }
	        reader.closeBR();
	        

	      //Attack hitboxes---------------------------------------------------------
	        err= 6;
	        if(new File(attackloc).isFile())
	    	br = reader.getBR(attackloc);
	        
	        this.HitboxMap = new HashMap<String,AttackHitBoxes>();
	        AttackHitBoxes attackHB;
	        while((line = Tools.readlineadv(br))!=null)
	        {
	        	temp= line.split(">");
	        	attackHB = new AttackHitBoxes(temp[1]);
		        this.HitboxMap.put(temp[0],attackHB);
	        }
	        reader.closeBR();
	        
	        
	        
	      //Values---------------------------------------------------------
	        err= 7;
	    	br = reader.getBR(valueloc);
	        
	        this.valueMap = new HashMap<String,Value>();
	        Value tValue;
	        while((line = Tools.readlineadv(br))!=null)
	        {
	        	tValue = new Value(line);
		        this.valueMap.put(tValue.getName(),tValue);
	        }
	        
	        reader.closeBR();
	        
	    } catch (Exception e) {System.out.println("Im sorry the Player File: "+infile+" or one of its Sub files could not be loaded!\n Error #"+ err);} //JOHN FIX THIS!
		 
		/*//These will be values;
		this.speedX = 0;
		this.speedY = 0;
		this.gravity = 0;
		*/
		this.x = 200;
		this.y = 200;
	}
	
	//Update
	public void update()
	{
		this.animateCol();
	}
	
	public void testAni()
	{
		//Testing for values, its fully operational. :)
		/* 
		int dead = this.valueMap.get("dead").getValue();
		
		if(dead == 1)
		{
			this.valueMap.get("HP").add(-1);
		}
		else 
		{
			this.valueMap.get("HP").add(1);
		}
		if(this.valueMap.get("HP").getValue() == this.valueMap.get("HP").getMaxValue())
		{
			this.valueMap.get("dead").setValue(1);
		}
		if(this.valueMap.get("HP").getValue() == this.valueMap.get("HP").getMinValue())
		{
			this.valueMap.get("dead").setValue(0);
		}
				
		System.out.println(this.valueMap.get("HP").getValue());
		*/

		
		if (!firstloop)
		{
			testN++;
			if (testN >= testAni.length)
			{
				testN = 0;
			}
			firstloop = true;
			Spark x = sparksMap.get(testAni[testN]);
			col = x.xloc;
			colS = x.xloc;
			row = x.yloc;
			this.timerspeed = x.speed;
			colN = x.length + x.xloc;
			this.name = playerSprites.get(x.Sprite);

		}
	}
	
	public void triggerEngine(InputList inputs, MoveStack moveHistory)//john add external stuff
	{
		//So this is how its gonna work.
		/*
		 1) pass in the following
		 	a) external inputs
		 		i) 		User controls	
		 		ii) 	Enemy 
		 		iii) 	environment
		 	b) move history (this is a history of the keys pressed)
		 
		 2) load up the triggers/states
		 
		 2.5) states will be an internal 'Array'
		 
		
		 3) take the inputs/state/and history to select the correct Trigger.
		 	the correct trigger will be choose by a combination of a Map and a wild card.
		 		Example idea (% is wildcard)
		 		 	move
		 		 	History = ABC 
		 			first try ABC
		 			second try %BC
					third try %%C
					finally just run the default.
					NEW IDEA
					
					H = ABC
					1st ABC
					2nd BC
					3rd C
		
		 4) then run the correct trigger
		 	
		 */
		
		
		//this should slowly reduce until either the trigger pulls or the default happens '%''%''%'etc
		char[] history = moveHistory.getStack();
		
		String currentThings;
		boolean success = false;
		Trigger tri;
		//String history = String.valueOf(mStack.getStack());
		
		//TESTING
		currentThings= inputs.toString() +';'+ String.valueOf(history) +';'+ state ; //John add HP and stuff
		
		System.out.println(currentThings + " " + Tools.BooleansToInt(inputs.getList()));
		
		for(int i = 0 ; i < history.length && i >= 0; i++)//DEFAULT ALL % ?
		{
			//John figure out a good separator
				//also figure the state stuff out.
			currentThings= inputs.toString() +';'+ String.valueOf(history) +';'+ state ; //John add HP and stuff
			
			tri = this.triggerMap.get(currentThings);
			
			if(tri != null)
			{
				tri.Pull();
				i = -1; 
				success = true;
			}
			else
			{
				history[ (history.length-1) - i] = '%';
			}
		}	
		if(!success)
		{
			//System.out.println("DEFAULT TRIGGER!");
		}
		
	}
	
	public void render(Graphics g,  Map<String,BufferedImage> spriteData)
	{	
		g.setColor(Color.GREEN);
		g.drawString(sparksMap.get(testAni[testN]).getName(),200,200); //TEST
		
		if(Engine.renderHitBox)
		{
			g.setColor(Color.BLUE);
			g.drawRect(this.headhitbox[0]+this.x, this.headhitbox[1]+this.y,this.headhitbox[2], this.headhitbox[3]);
			g.drawRect(this.feethitbox[0]+this.x, this.feethitbox[1]+this.y,this.feethitbox[2], this.feethitbox[3]);
			g.setColor(Color.YELLOW);
			g.drawRect(this.fronthitbox[0]+this.x, this.fronthitbox[1]+this.y,this.fronthitbox[2], this.fronthitbox[3]);
			g.drawRect(this.backhitbox[0]+this.x, this.backhitbox[1]+this.y,this.backhitbox[2], this.backhitbox[3]);

		}
		super.render(g, spriteData);
	}
	
	public void setHitbox(int x, int y, int width, int height)
	{
		this.hitbox[0] = x;
		this.hitbox[1] = y;
		this.hitbox[2] = width;
		this.hitbox[3] = height;
		this.headhitbox[0] = x+(width/8);
		this.headhitbox[1] = y;
		this.headhitbox[2] = 3*(width/4);
		this.headhitbox[3] = height/4;
		this.fronthitbox[0] = x + width/2;
		this.fronthitbox[1] = y + height/8;
		this.fronthitbox[2] = width/2;
		this.fronthitbox[3] = 3*height/4;
		this.backhitbox[0] = x;
		this.backhitbox[1] = y + height/8;
		this.backhitbox[2] = width/2;
		this.backhitbox[3] = 3*height/4;
		this.feethitbox[0] = x+(width/8);
		this.feethitbox[1] = y + 3* height/4;
		this.feethitbox[2] = 3*(width/4);
		this.feethitbox[3] = height/4;
	}
	
	public int getValue(String valueName)
	{
		try
		{
			return valueMap.get(valueName).getValue();
		}
		catch (Exception e) 
		{
			System.out.println("The following valuse could not be found: " + valueName);
			return 0;
		}
	}
	
	public void setValue(String valueName, int value)
	{
		try
		{
			valueMap.get(valueName).setValue(value);
		}
		catch (Exception e) 
		{
			System.out.println("The following valuse could not be found: " + valueName);
		}
	}
}
