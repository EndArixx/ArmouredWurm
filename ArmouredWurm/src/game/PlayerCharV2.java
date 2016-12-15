package game;

import game.triggers.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	protected String currentTrigger;
	protected Trigger[] triggers;
	protected String state;
	protected Spark[] sparks;
	protected State currstate;
	protected AttackHitBoxes currHitbox;
	protected boolean hasDamageHitbox;
	protected Map<String,AttackHitBoxes> HitboxMap;
	protected String[] masterState;
	protected Map<String,State> statesMap;
	protected Map<String,Spark> sparksMap;
	protected Map<String,Value> valueMap;
	protected Map<String,String> playerSprites;
	
	protected Map<String,Trigger> triggerMap;
	protected List<String> triggerNames;
	protected String HPName = "HP";
	protected int invol; //invulnerable timer
	protected boolean isInteruptable;
	
	protected String triggerloc;
	protected String statesloc;
	protected String sparkloc;
	protected String attackloc;
	protected String valueloc;
	
	protected double XVel; //This is the CUrrent speed
	protected double YVel;
	protected double Xaccel; //This is the acceleration of said values.(how much it increases by)
	protected double YaccelUp;
	protected double YaccelDn;
	
	protected double MaxXVel; //Max veloceties. 
	protected double MaxYVelUp;  //This needs to be negative
	protected double MaxYVelDn; //terminal velocity (+y)
	//SpeedX = current X speed 
	//SpeedY = current Y Speed
	
	protected boolean isInAnimation; //Use this to force the animation to fully playout
	
	//JOHN REMOVE!
	protected String[] testAni;
	protected int testN;
	
	//Constructors 
	public PlayerCharV2(String infile, Map<String,BufferedImage> spriteData)
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
	        temp = Tools.readlineadv(br).split(",");
	        
	        String startState = temp[0];
	        String startSpark = temp[1];
	        
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
	    	br = reader.getBR(triggerloc);
	        this.triggerMap = new HashMap<String,Trigger>();
	        Trigger tri;
	        this.triggerNames = new ArrayList<String>();
	        while((line = Tools.readlineadv(br))!=null)
	        {
	        	 tri = new Trigger(line);
	        	 //String[] causes = tri.getCauses();
	        	 //for (int i = 0; i < causes.length; i++)
	        	 //{
		        	 //this.triggerMap.put( causes[i], tri);
	        		 //john think about this
	        	 //}
        		 this.triggerMap.put(tri.getName(), tri);
        		 this.triggerNames.add(tri.getName());

	        }
	        reader.closeBR();
	     
	        //STATES---------------------------------------------------------
	        err= 4;
	    	br = reader.getBR(statesloc);
	        this.statesMap = new HashMap<String,State>();
	        State tempstate;
	        masterState = Tools.readlineadv(br).split(">");
	        while((line = Tools.readlineadv(br))!=null)
	        {
	        	temp= line.split(">");
	        	tempstate = new State(temp[0],masterState,temp[1].split(","));
		        this.statesMap.put(temp[0],tempstate);
	        }
	        reader.closeBR();
	        
	        
	        //Sparks---------------------------------------------------------
	        err= 5;
	        if(new File(sparkloc).isFile())
	    	br = reader.getBR(sparkloc);
	        this.sparksMap = new HashMap<String,Spark>();
	        Spark tspark;
	        while((line = Tools.readlineadv(br))!=null)
	        {
	        	tspark = new Spark(line);
	        	this.sparksMap.put(tspark.getName(),tspark);
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
			
			
			//OTHER-----------------------------------------------------------
	        
	        //Load up moveSpeeds
	        this.XVel = this.getValue("XVel");
	        this.YVel = this.getValue("YVel");
	        this.Xaccel = this.getValue("Xaccel");
	        this.YaccelUp = this.getValue("YaccelUp"); 
	        this.YaccelDn = this.getValue("YaccelDn");
	        this.MaxXVel = this.getValue("MaxXVel");
	        this.MaxYVelUp = this.getValue("MaxYVelUp");
	        this.MaxYVelDn = this.getValue("MaxYVelDn");
	        
	        this.currentTrigger = this.triggerNames.get(0);
	        this.currstate = this.statesMap.get(startState);
			firstloop = true;
			hasDamageHitbox = false;
			
			err= 8;
			Spark x = sparksMap.get(startSpark);
			col = x.xloc;
			colS = x.xloc;
			row = x.yloc;
			this.timerspeed = x.speed;
			colN = x.length + x.xloc;
			this.name = playerSprites.get(x.Sprite);
			this.isInteruptable = true;
	        reader.closeBR();
	        
	    } catch (Exception e) 
		{
	    	System.out.println("Im sorry the Player File: "+infile+" or one of its Sub files could not be loaded!\n Error #"+ err + "\n " + e.toString());
	    } 

        
	}
	
	//Update
	public void update( Queue<DamageHitbox> damageQ)
	{
		//grab the current speeds for movement
		this.setValue("XVel",(int) this.XVel);
        this.setValue("YVel",(int) this.YVel);       
        
        //hitbox stuff
        if(this.hasDamageHitbox)
        {
        	damageQ.add(this.currHitbox.getDamageHitBox(this.col - this.colS, this.x, this.y));
        }
        
		if(invol > 0)
		{
			if(invol >= this.getValue("InvolTime"))
			{
				invol = 0;
			}
			else
			{
				invol++;
			}
		}
        //Death stuff
		if(this.getValue(this.HPName) >=0 && this.col + this.colS == this.colN)
		{
			this.setValue("dead", true);
		}
		else //John look into this!
		{
			this.setValue("dead", false);
		}
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
	
	public void animateCol()
	{
		super.animateCol();
		
		//JOHN PUT SPECIFIC LOGIC IN HERE!
	}
	
	public void triggerEngine(InputList inputs, MoveStack moveHistory)//john add external stuff
	{
		/*
		 1) pass in the following
		 	a) external inputs
		 		i) 		User controls	
		 		ii) 	Enemy 
		 		iii) 	environment
		 	b) move history (this is a history of the keys pressed)
		 
		 2) load up the triggers/states
		
		 3) take the inputs/state/and history to select the correct Trigger.
		 	the correct trigger will be choose by a combination of a Map and a wild card.
		 		 	move
		 		 	
					H = ABC
					1st 'ABC'
					2nd 'BC'
					3rd 'C'
					4th ''
					
		 4) then run the correct trigger
		 
		 5)Another new idea! remove the MAP instead lets just loop through and reduce the file only has 40 lines right now.
		 	
		 */
		
		
		char[] history = moveHistory.getStack();
		boolean testmode = false;
        //if (testmode) System.out.println("X:"+this.getValue("XVel") + " - Y:" +this.getValue("YVel") + " FF" + this.getValue("FF") );
		//this.x = this.valueMap.get("X").getValue();
		//this.y = this.valueMap.get("Y").getValue();
		
		if(this.firstloop && !this.isInteruptable)
		{
			return;
		}
		
		Trigger tri = null;
		List<String> TriggerList = new ArrayList<String>();
		List<String> StupidClone =  new ArrayList<String>();
		this.state = this.currstate.getName();
		//System.out.println("NEW LOOP " + this.state + " : " + this.currstate.getName());
		
		/*for(Iterator<String> x = this.triggerNames.iterator(); x.hasNext();)
		{
			String current = x.next();
			tri  = this.triggerMap.get(current);
			System.out.println(tri.getAllowedStates()+" "+this.state);
			if(tri.getAllowedStates().contains(this.state))
			{
				TriggerList.add(current);
			}
		}*/
		
		
		//CHECK STATES
		TriggerList.addAll(triggerNames);
		StupidClone.addAll(TriggerList);
		if(TriggerList.size() > 1)
		{
			for(Iterator<String> x = StupidClone.iterator(); x.hasNext();)
			{
				String current = x.next();
				tri  = this.triggerMap.get(current);
				if(!tri.getAllowedStates().contains(this.state))
				{
					TriggerList.remove(current);
					if(testmode) System.out.println("State OFF " + current +"  "+tri.getAllowedStates() +" -"+  this.state);
					//x.remove();
				}
				else
				{
					if(testmode)System.out.println("State ON " + current +"  "+tri.getAllowedStates() +" -"+  this.state);
				}
			}
		}
		
		//Check Control Input
		StupidClone.clear();
		StupidClone.addAll(TriggerList);
		if(TriggerList.size() > 1)
		{
			for(Iterator<String> x = StupidClone.iterator(); x.hasNext();)
			{
				String current = x.next();
				
				tri  = this.triggerMap.get(current);
				boolean inputOn = false;
				String[] triInputs = tri.getInputControl();
				for(int i = 0; i < triInputs.length;i++)
				{
					if (triInputs[i].equals(inputs.getOn()))
						{inputOn = true;}
				}
				if(!inputOn)
				{
					TriggerList.remove(current);
					if(testmode)System.out.println("Inputs OFF " + current +"  "+tri.getInputControl() + "-"+inputs.getOn());
				}
				else
				{
					if(testmode)System.out.println("Inputs ON "  + current +"  "+tri.getInputControl() + "-"+inputs.getOn());
				}
			}
		}
		
		//Check history
		StupidClone.clear();
		StupidClone.addAll(TriggerList);
		if(TriggerList.size() > 1)
		{
			for(Iterator<String> x = StupidClone.iterator(); x.hasNext();)
			{
				String current = x.next();
				boolean goodhistory = false;
				String currHistory = String.valueOf(history);
				tri = this.triggerMap.get(current);
				if(testmode)System.out.println("History " + current + " " + currHistory);
				
				for(int i = 0 ; i <= history.length; i++)
				{
					if (tri.getHistory().equals(currHistory))
					{
						 //i = Integer.MAX_VALUE;
						 goodhistory = true;
						 break;
					}
					else if(i != history.length)
					{
						currHistory = currHistory.substring(0,currHistory.length()-1);
					}
					if(testmode)System.out.println("END "+i);
				}
				if(!goodhistory)
				{
					if(testmode)System.out.println("History"); //JOHN ADDRESS THIS
					TriggerList.remove(current);
					//x.remove();
				}
			}
		}

		//CHECK VALUES
		StupidClone.clear();
		StupidClone.addAll(TriggerList);
		if(TriggerList.size() > 1)
		{
			for(Iterator<String> x = StupidClone.iterator(); x.hasNext();)
			{
				String current = x.next();
				tri  = this.triggerMap.get(current);
				if(testmode)System.out.println("VAlues " + current);
				boolean valid = true;
				String[] temp;
				
				String[] values = tri.getValueMarkers();
				for(int i = 0; i < values.length && valid;i++)
				{
					//Supported types !=,<=,>=,==,>,<
					
					//Not equal
					if(values[i].contains("!="))
					{
						temp = values[i].split("!=");
						if(Integer.parseInt(temp[0]) != Integer.parseInt(temp[1]))
							{valid= true;}
						else{valid = false;}
					}
					
					//Less Then OR Equal
					else if(values[i].contains("<="))
					{
						temp = values[i].split("<=");
						if(this.valueMap.get(temp[0]).getValue() <= Integer.parseInt(temp[1]))
							{valid= true;}
						else{valid = false;}
					}
					
					//Greater Then OR Equal
					else if(values[i].contains(">="))
					{
						temp = values[i].split(">=");
						if(this.valueMap.get(temp[0]).getValue() >= Integer.parseInt(temp[1]))
							{valid= true;}
						else{valid = false;}
					}
					
					//Equal
					else if(values[i].contains("=="))
					{
						temp = values[i].split("==");
						if(this.valueMap.get(temp[0]).getValue() == Integer.parseInt(temp[1]))
							{valid= true;}
						else{valid = false;}
					}
					
					//Less Then
					else if(values[i].contains("<"))
					{
						temp = values[i].split("<");
						if(this.valueMap.get(temp[0]).getValue() <= Integer.parseInt(temp[1]))
							{valid= true;}
						else{valid = false;}
					}
					
					//Greater Then
					else if(values[i].contains(">"))
					{
						temp = values[i].split(">");
						if(this.valueMap.get(temp[0]).getValue() > Integer.parseInt(temp[1]))
							{valid= true;}
						else{valid = false;}
					}
					
					else
					{
						if(testmode) System.out.println("Unknown ValueEvaluation: "+values[i]);
						valid = false;
					}
					
				}
				if(!valid)
				{	
					TriggerList.remove(current);
				}
			}
			
		}
		if(testmode)
		{
			for (Entry<String, Value> entry : this.valueMap.entrySet())
			{
				System.out.println(entry.getKey() + "/" + entry.getValue().getValue());
			}
		}
		if(!TriggerList.isEmpty()) 
		{
			if(testmode)System.out.println("BOOOOOOOOM "+TriggerList.get(0) +" "+inputs.getOn() +" FF:"+this.valueMap.get("FF").getValue() );
			tri = this.triggerMap.get(TriggerList.get(0));
			if(this.currentTrigger == tri.getName()){return;} //This was causing issues
			
			
			this.currentTrigger = tri.getName();
			firstloop = true;
			this.isInteruptable = tri.isInteruptable();
			//using 0 for testing John fix this
			Spark sx = sparksMap.get(tri.getSparks()[0]);
			col = sx.xloc;
			colS = sx.xloc;
			row = sx.yloc;
			this.timerspeed = sx.speed;
			colN = sx.length + sx.xloc;
			this.name = playerSprites.get(sx.Sprite);
			if(sx.hasHitBox())
			{
				this.hasDamageHitbox = true;
				this.currHitbox = this.HitboxMap.get(sx.getHitBox());
			}
			else
			{
				this.hasDamageHitbox= false;
			}
			this.currstate = this.statesMap.get(tri.getState());
			if(testmode)System.out.println("-----------"+currstate.getName());
			for(int i = 0;i < this.masterState.length; i++)
			{
				this.valueMap.get(masterState[i]).setValue(this.currstate.getValue(masterState[i]));
				if(testmode)System.out.println(masterState[i]+ "  " + this.currstate.getValue(masterState[i]));
			}
			this.isInteruptable = tri.isInteruptable();
		}
	}
	
	//Movement Zone---------------------------------------------------------
	//this will be the method that engine.Movement will call
	protected void MoveRight(World world,Dimension window)
	{
		this.AccelerateX(this.Xaccel);
	}
	protected void MoveLeft(World world,Dimension window)
	{
		this.AccelerateX(- this.Xaccel);
	}
	protected void jump(World world,Dimension window)
	{
		this.AccelerateY(-this.YaccelUp);
	}
	
	protected void moveX(World theWorld,Dimension window, double amount)
	{
		if(amount < 0) //WEST
		{
			if(this.x >  6*window.width/16)
			{
				x += amount;
			}
			else if(theWorld.getX() < 0)
			{
				theWorld.moveX(-amount);
			}
			else if(x > 0)
			{
				x += amount;
			}
			else
			{
				//you hit the end of the world
			}
		}
		if(amount > 0) //EAST
		{
			if(this.x <  6*window.width/16)
			{
				x += amount;
			}
			else if(-theWorld.getX() < theWorld.getWidth()-window.width)
			{
				theWorld.moveX(-amount);
			}
			else if (this.x < window.width -this.width)
			{
				x += amount;
			}
			else
			{
				//you hit the end of the world
			}
		}
	}
	protected void moveY(World theWorld,Dimension window, double amount)
	{
		if(amount < 0)//UP
		{
			if(this.y >  6*window.height/9-this.height) 
			{
				y += amount;
			}
			else if(theWorld.getY() < 0)
			{
				theWorld.moveY(-amount);
			}
			else if(y > 0)
			{
				y += amount;
			}
			else
			{
				//you hit the end of the world!
			}
		}
		if(amount > 0)//DOWN
		{
			if(this.y < 6*window.height/9-this.height) 
			{
				y += amount;
			}
			else if(-theWorld.getY()+window.getHeight() < theWorld.getHeight())
			{
				theWorld.moveY(-amount);
			}
			else if(this.y < window.height-this.height)
			{
				y += amount;
			}
			else
			{
				//you hit the end of the world!
			}
		}
	}
	public void move(World theWorld,Dimension window)
	{
		if(this.XVel != 0)
		{
			this.moveX(theWorld, window, XVel);
		}
		if(this.YVel != 0)
		{
			this.moveY(theWorld, window, YVel);
		}
	}
	public void moveWithForce(int startforce,World theWorld,Dimension window)
	{
		if(this.XVel + startforce != 0 )
		{
			if(this.XVel > 0) this.setValue("FF", 1);
			else if(this.XVel < 0)this.setValue("FF", 0);
			
			this.moveX(theWorld, window,startforce + XVel);
		}
		if(this.YVel != 0)
		{
			this.moveY(theWorld, window, YVel);
		}
	}
	
	//Acceleration ---------------------------------------------------------
	protected void Accelerate(double xRate,double yRate)
	{
		this.AccelerateX(xRate);
		this.AccelerateY(yRate);
	}
	protected void AccelerateX(double xRate)
	{
		this.XVel += xRate;
		//check against max
		if(this.XVel > this.MaxXVel)
		{
			this.XVel = this.MaxXVel;
		}
		if(this.XVel < -this.MaxXVel)
		{
			this.XVel = -this.MaxXVel;
		}
	}
	protected void AccelerateY(double yRate)
	{
		if(yRate < 0)
		{
			this.YVel = yRate;
		}
		//Removed down, that is handled by DecelerateY.
	}
	
	protected void DecelerateX()
	{
		if(XVel >= Xaccel)
		{
			this.XVel -= this.Xaccel;
		}
		else if(XVel <= -Xaccel)
		{
			this.XVel += this.Xaccel;
		}
		else
		{
			this.XVel = 0;
		}
	}
	protected void DecelerateY()
	{

		this.YVel += this.YaccelDn;
		if(YVel > this.MaxYVelDn)
		{
			this.YVel = this.MaxYVelDn;
		}
	}
	
	// MORE STUFFFFFFFFFFFF----------------------------------------------------------
	public void damage(int amount)
	{		
		if(invol == 0)
		{
			invol++;
			this.addValue(this.HPName,-amount);
			//this.setValue(this.HPName,this.getValue(this.HPName) + amount);
		}
	}
	
	public void render(Graphics g,  Map<String,BufferedImage> spriteData)
	{	
		g.setColor(Color.GREEN);
		
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
	
	//Hitbox Stuff---------------------------------------------------
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
	public int[] getheadHitbox()
	{
		int outbox[] = new int[4];
		outbox[0] = x + headhitbox[0];
		outbox[1] = y + headhitbox[1];
		outbox[2] = headhitbox[2];
		outbox[3] = headhitbox[3];
		return outbox;
	}
	public int[] getfrontHitbox()
	{
		int outbox[] = new int[4];
		outbox[0] = x + fronthitbox[0];
		outbox[1] = y + fronthitbox[1];
		outbox[2] = fronthitbox[2];
		outbox[3] = fronthitbox[3];
		return outbox;
	}
	public int[] getbackHitbox()
	{
		int outbox[] = new int[4];
		outbox[0] = x + backhitbox[0];
		outbox[1] = y + backhitbox[1];
		outbox[2] = backhitbox[2];
		outbox[3] = backhitbox[3];
		return outbox;
	}
	public int[] getfeetHitbox()
	{
		int outbox[] = new int[4];
		outbox[0] = x + feethitbox[0];
		outbox[1] = y + feethitbox[1];
		outbox[2] = feethitbox[2];
		outbox[3] = feethitbox[3];
		return outbox;
	}
	
	//Value stuff------------------------------------------------------
	public int getValue(String valueName)
	{
		try
		{
			return valueMap.get(valueName).getValue();
		}
		catch (Exception e) 
		{
			System.out.println("The following values could not be found: " + valueName);
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
			System.out.println("The following set values could not be found: " + valueName);
		}
	}
	public void setValue(String valueName, boolean value)
	{
		try
		{
			valueMap.get(valueName).setValue(value);
		}
		catch (Exception e) 
		{
			System.out.println("The following set values could not be found: " + valueName);
		}
	}
	public int getMaxValue(String valueName)
	{
		try
		{
			return valueMap.get(valueName).getMaxValue();
		}
		catch (Exception e) 
		{
			System.out.println("The following values could not be found: " + valueName);
			return 0;
		}
	}
	public void setMaxValue(String valueName, int value)
	{
		try
		{
			valueMap.get(valueName).setMaxValue(value);
		}
		catch (Exception e) 
		{
			System.out.println("The following max values could not be found: " + valueName);
		}
	}
	public void addValue(String valueName, int value)
	{
		try
		{
			valueMap.get(valueName).addValue(value);
		}
		catch (Exception e) 
		{
			System.out.println("The following add values could not be found: " + valueName);
		}
	}

	public void setValueToMax(String valueName)
	{
		try
		{
			Value temp = valueMap.get(valueName);
			temp.setValue(temp.getMaxValue());
		}
		catch (Exception e) 
		{
			System.out.println("The following max values could not be found: " + valueName);
		}
	}
	public boolean isValueMax(String valueName)
	{
		try
		{
			Value temp = valueMap.get(valueName);
			if(temp.getMaxValue() == temp.getValue())
			{
				return true;
			}
		}
		catch (Exception e) 
		{
			System.out.println("The following isMax values could not be found: " + valueName);
		}
		return false;
	}
	public boolean isValue(String valueName)
	{
		try
		{
			Value temp = valueMap.get(valueName);
			return temp.getBool();
		}
		catch (Exception e) 
		{
			System.out.println("The following isMax values could not be found: " + valueName);
		}
		return false;
	}
	
}
