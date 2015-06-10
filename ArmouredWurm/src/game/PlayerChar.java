package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;

import javax.imageio.ImageIO;

/*  
 * Player character
 * 
 * 
 */
public class PlayerChar extends Sprite 
{
	protected double gravity;
	protected int headhitbox[] = new int[4];
	protected int feethitbox[] = new int[4];
	protected int backhitbox[] = new int[4];
	protected int fronthitbox[] = new int[4];
	//protected Sprite legs;
	protected boolean attacking,reverseAttacking, jumping,jumpAttacking, forward, backward,FF, falling,hasR, hurt, dying, player, dead;
	protected gun rifle;
	
	protected double topRunSpeed;
	protected double topGravity;
	protected double topJump;
	protected double fallrate;
	protected double runrate;
	
	protected int checkAni = -1;
	protected double maxHP, HP;
	protected int  hx, hy, HPsW, invol, involtime;
	protected Sprite hpImage;
	protected String charname;
	protected int aidle[],arun[],ajump[],afall[],aattack[],ajumpattack[],aknockback[],adeath[],areverseattack[],acombatstill[];
	
	public PlayerChar(String infile,  Map<String,BufferedImage> spriteData)
	{
		super();
			//booleans
		this.attacking = false;
		this.jumping= false;
		this.jumpAttacking = false;
		this.forward = false;
		this.backward = false;
		this.FF = true;
		this.falling = false;
		this.hurt = false;
		this.dying = false;
		this.dead = false;
		this.invol = 0;
		animate = true;
		this.player = true;
		row = 0;
		col = 0;
		
		String[] temp;
		FileReader fr;
		BufferedReader br;
		 try {
			 	fr = new FileReader(infile);
		    	br = new BufferedReader(fr);
		    	
		        String line = br.readLine();
		        this.charname = line;
		        
		        line = br.readLine();
		        this.name= line;
		        
		        
		        BufferedImage spriteMap = null;
				try 
				{
					spriteMap = ImageIO.read(new File(name));
				}
				catch (IOException e) 
				{
					System.out.println("Error, Bad Sprite:"+ name);
				}
				spriteData.put(name,spriteMap);

		        
		        line = br.readLine();
		        temp = line.split(",");
		        this.x = Integer.parseInt(temp[0]);
		        this.y = Integer.parseInt(temp[1]);
		        
		        line = br.readLine();
		        temp = line.split(",");
		        this.width = Integer.parseInt(temp[0]);
		        this.height = Integer.parseInt(temp[1]);
		        
		        line = br.readLine();
		        temp = line.split(",");
		        this.rowN = Integer.parseInt(temp[0]);
		        this.colN = Integer.parseInt(temp[1]);
		        
		        line = br.readLine();
		        this.maxHP = Integer.parseInt(line);
		        this.HP = maxHP;
		        
		        line = br.readLine();
		        temp = line.split(",");
		        this.topRunSpeed = Integer.parseInt(temp[0]);
		        this.topGravity = Integer.parseInt(temp[1]);
		        this.topJump = Integer.parseInt(temp[2]);
		        this.fallrate = Double.parseDouble(temp[3]);
		        this.runrate = Double.parseDouble(temp[4]);
		        
		        line = br.readLine();
		        this.involtime = Integer.parseInt(line);
		        
		        line = br.readLine();
		        temp = line.split(",");
		        aidle = new int[4];
				aidle[0] = Integer.parseInt(temp[0]);
				aidle[1] = Integer.parseInt(temp[1]);
				aidle[2] = Integer.parseInt(temp[2]);
				aidle[3] = Integer.parseInt(temp[3]);
				
				line = br.readLine();
		        temp = line.split(",");
				arun = new int[4];
				arun[0] = Integer.parseInt(temp[0]);
				arun[1] = Integer.parseInt(temp[1]);
				arun[2] = Integer.parseInt(temp[2]);
				arun[3] = Integer.parseInt(temp[3]);
				
				line = br.readLine();
		        temp = line.split(",");
				ajump = new int[4];
				ajump[0] = Integer.parseInt(temp[0]);
				ajump[1] = Integer.parseInt(temp[1]);
				ajump[2] = Integer.parseInt(temp[2]);
				ajump[3] = Integer.parseInt(temp[3]);
				
				line = br.readLine();
		        temp = line.split(",");
				afall = new int[4];
				afall[0] = Integer.parseInt(temp[0]);
				afall[1] = Integer.parseInt(temp[1]);
				afall[2] = Integer.parseInt(temp[2]);
				afall[3] = Integer.parseInt(temp[3]);
				
				line = br.readLine();
		        temp = line.split(",");
				aattack = new int[6];
				aattack[0] = Integer.parseInt(temp[0]);
				aattack[1] = Integer.parseInt(temp[1]);
				aattack[2] = Integer.parseInt(temp[2]);
				aattack[3] = Integer.parseInt(temp[3]);
				aattack[4] = Integer.parseInt(temp[4]);
				aattack[5] = Integer.parseInt(temp[5]);
				
				line = br.readLine();
		        temp = line.split(",");
				ajumpattack = new int[6];
				ajumpattack[0] = Integer.parseInt(temp[0]);
				ajumpattack[1] = Integer.parseInt(temp[1]);
				ajumpattack[2] = Integer.parseInt(temp[2]);
				ajumpattack[3] = Integer.parseInt(temp[3]);
				ajumpattack[4] = Integer.parseInt(temp[4]);
				ajumpattack[5] = Integer.parseInt(temp[5]);
				
				line = br.readLine();
		        temp = line.split(",");
				aknockback = new int[4];
				aknockback[0] = Integer.parseInt(temp[0]);
				aknockback[1] = Integer.parseInt(temp[1]);
				aknockback[2] = Integer.parseInt(temp[2]);
				aknockback[3] = Integer.parseInt(temp[3]);
				
				line = br.readLine();
		        temp = line.split(",");
				adeath = new int[4];
				adeath[0] = Integer.parseInt(temp[0]);
				adeath[1] = Integer.parseInt(temp[1]);
				adeath[2] = Integer.parseInt(temp[2]);
				adeath[3] = Integer.parseInt(temp[3]);
			
				line = br.readLine();
		        temp = line.split(",");
				areverseattack = new int[6];
				areverseattack[0] = Integer.parseInt(temp[0]);
				areverseattack[1] = Integer.parseInt(temp[1]);
				areverseattack[2] = Integer.parseInt(temp[2]);
				areverseattack[3] = Integer.parseInt(temp[3]);
				areverseattack[4] = Integer.parseInt(temp[4]);
				areverseattack[5] =	Integer.parseInt(temp[5]);
				
				line = br.readLine();
		        temp = line.split(",");
				acombatstill = new int[4];
				acombatstill[0] = Integer.parseInt(temp[0]);
				acombatstill[1] = Integer.parseInt(temp[1]);
				acombatstill[2] = Integer.parseInt(temp[2]);
				acombatstill[3] = Integer.parseInt(temp[3]);
		        
		        line = br.readLine();
		        temp= line.split(",");
		        this.setHitbox(
		        		Integer.parseInt(temp[0]), 
		        		Integer.parseInt(temp[1]), 
		        		Integer.parseInt(temp[2]),
		        		Integer.parseInt(temp[3]));
		        
		        fr.close();
		        br.close();
	        
	    } catch (IOException e) {System.out.println("Im sorry the Player File: "+infile+" could not be loaded!");}
		 
		this.speedX = 0;
		this.speedY = 0;
		this.gravity = 0;
		
		 	//HP STUFF THAT NEEDS WORK
			//John make HP independant of player
		this.hx = 800;
		this.hy = 30;
			//this needs work
		this.hpImage = new Sprite("res/hpbar.png",hx,hy,spriteData);
		this.HPsW = hpImage.width;
		
		this.timerspeed = 3;
			//old rifle stuff, will be removed or converted
		this.hasR = false;
		
		 
	}
	
	public PlayerChar(String name, String spriteloc,int x, int y, int width, int height,int row,int col,  Map<String,BufferedImage> spriteData)
	{
		super(spriteloc,x,y,width,height,row,col,15, spriteData);
		this.maxHP = 100;
		this.HP = 100;
			//John make HP independant of player
		this.hx = 800;
		this.hy = 30;
			//this need swork
		this.hpImage = new Sprite("res/hpbar.png",hx,hy,spriteData);
		this.HPsW = hpImage.width;
		
			//booleans
		this.attacking = false;
		this.jumping= false;
		this.jumpAttacking = false;
		this.forward = false;
		this.backward = false;
		this.FF = true;
		this.falling = false;
		this.hurt = false;
		this.dying = false;
		this.dead = false;
		this.invol = 0;
			//THIS IS STILL TESTING, I might set up something different
		this.involtime = 25;
		
			//useful
		this.player = true;
		
			//movement stuff
		this.speedX = 0;
		this.speedY = 0;
		this.topRunSpeed = 15;
		this.gravity = 0;
		this.topGravity = 15;
		this.topJump = 30;
		this.fallrate = 1.5;
		this.runrate = 3;
		
			//Animations stuff
				//[0] right
				//[1] left
				//[2] frames
				//[3] speed
		aidle = new int[4];
		aidle[0] = 18;
		aidle[1] = 19;
		aidle[2] = 8;
		aidle[3] = 3;
		
		arun = new int[4];
		arun[0] = 2;
		arun[1] = 3;
		arun[2] = 16;
		arun[3] = 3;
		
		ajump = new int[4];
		ajump[0] = 4;
		ajump[1] = 5;
		ajump[2] = 8;
		ajump[3] = 3;
		
		afall = new int[4];
		afall[0] = 6;
		afall[1] = 7;
		afall[2] = 1;
		afall[3] = 3;
		
		aattack = new int[6];
		aattack[0] = 8;
		aattack[1] = 9;
		aattack[2] = 8;
		aattack[3] = 3;
			//attack zones
		aattack[4] = 5;
		aattack[5] = 7;
		
		ajumpattack = new int[6];
		ajumpattack[0] = 10;
		ajumpattack[1] = 11;
		ajumpattack[2] = 10;
		ajumpattack[3] = 3;
		ajumpattack[4] = 6;
		ajumpattack[5] = 9;
		
		
		aknockback = new int[4];
		aknockback[0] = 12;
		aknockback[1] = 13;
		aknockback[2] = 10;
		aknockback[3] = 3;
		
		adeath = new int[4];
		adeath[0] = 14;
		adeath[1] = 15;
		adeath[2] = 8;
		adeath[3] = 3;
		
		areverseattack = new int[6];
		areverseattack[0] = 16;
		areverseattack[1] = 17;
		areverseattack[2] = 8;
		areverseattack[3] = 3;
		areverseattack[4] = 3;
		areverseattack[5] = 5;
		
		acombatstill = new int[4];
		acombatstill[0] = 18;
		acombatstill[1] = 19;
		acombatstill[2] = 8;
		acombatstill[3] = 3; 
		
		
		
		this.timerspeed = 3;
		this.hasR = false;
		//legs = new Sprite(legloc,x,y,width,height,2,0,timerspeed);
		this.setHitbox(0, 0, width, height);
	}
	
	
	public void giveGun(gun rifle)
	{
		this.hasR = true;
		this.rifle = rifle;
	}

	public void damage() 
	{
			//old test
		System.out.println("OUCH! THAT HURT!");
	}
	public void damage(int amount)
	{		
		if(invol == 0)
		{
				//heavy damage?
			invol++;
			this.HP -= amount;
			if(this.HP > 0)
			{
				hpImage.width =(int) (hpImage.width*(HP/maxHP));
			}
			if(!hurt)
			{
				this.startHurt();
			}
		}
	}
	public void startHurt()
	{
		this.hurt = true;
			//This is going to be used for hurt animations and things of that nature
	}
	
	public void startDie()
	{
		if(!dying)
		{
				//this will animate death
			this.firstloop = true;
			this.col = 0;
			this.colN = adeath[2];
			if(this.FF){row = this.adeath[0];}
			else{row = this.adeath[1];}
			this.dying = true;
		}
	}
	public void startAttack()
	{
		if(!attacking)
		{
			if(jumping || falling)
			{
				this.firstloop = true;
				this.attacking= true;
				this.jumpAttacking = true;
				if(FF)
				{
					this.row = ajumpattack[0];
					this.col = 0;
					this.colN = ajumpattack[2];
				}
				else
				{
					this.row =  ajumpattack[1];
					this.col = 0;
					this.colN = ajumpattack[2];
				}
			}
			else
			{
				this.firstloop = true;
				this.attacking= true;
				if(FF)
				{
					this.row = aattack[0];
					this.col = 0;
					this.colN = aattack[2];
				}
				else
				{
					this.row =  aattack[1];
					this.col = 0;
					this.colN = aattack[2];
				}
			}
		}
	}
	public void startReverseAttack()
	{
		if(!attacking)
		{
			if(jumping || falling)
			{
				this.reverseAttacking = true;
				this.firstloop = true;
				this.attacking= true;
				if(FF)
				{
					this.row = ajumpattack[0];
					this.col = 0;
					this.colN = ajumpattack[2];
				}
				else
				{
					this.row =  ajumpattack[1];
					this.col = 0;
					this.colN = ajumpattack[2];
				}
			}
			else
			{
				this.reverseAttacking = true;
				this.firstloop = true;
				this.attacking= true;
				if(FF)
				{
					this.row = areverseattack[0];
					this.col = 0;
					this.colN = areverseattack[2];
				}
				else
				{
					this.row =  areverseattack[1];
					this.col = 0;
					this.colN = areverseattack[2];
				}
			}
		}
	}
	public void attack(Queue<DamageHitbox> damageQ)
	{
			//Damage goes here!
		if(!this.firstloop)
		{	
			this.attacking= false;
			this.reverseAttacking = false;
			this.jumpAttacking = false;
		}
		else
		{
			if(col >= aattack[4] && col <= aattack[4])
			{
				if((this.FF && !reverseAttacking) || (!this.FF && reverseAttacking))
				{
					DamageHitbox out = new DamageHitbox( this.x+ this.width/2 , this.y ,(int)(this.width*0.75), this.height/4, 5 , 3);
					damageQ.add(out);
				}
				else
				{
					DamageHitbox out = new DamageHitbox( this.x- this.width/2 , this.y ,(int)(this.width*0.75), this.height/4, 5 , 3);
					damageQ.add(out);
				}
			}
		}
		
		
	}
	public void fire(World theWorld)
	{
		if(hasR)
		{
			rifle.Fire(theWorld);
		}
	}
	//--------------------------------------------------------------------UPDATE
	public void update(Queue<DamageHitbox> damageQ)
	{		
			//death :(
		if ( !(HP > 0) && !dying)
		{

			startDie();
		}
		else if(dying)
		{
			if(this.firstloop == false)
			{
					//time to die
				this.dead = true;
			}
		}
		else
		{
			
				//this give the player a short amount of time after taking damage to be invinsible
			if(invol > 0)
			{
				if(invol > involtime)
				{
					invol = 0;
				}
				else
				{
					invol++;
				}
			}
			
			
			
				//Test stuff!
			if(jumping){this.timerspeed = 1.5;}
			else if(attacking){this.timerspeed = 3;}
			else{this.timerspeed = 3;}
			
			if(attacking)
			{
				attack(damageQ);
			}
			else if(jumping || forward || backward )
			{
				if(forward)
				{
					if(jumping)
					{
							//Right facing Jump Animation
						this.row = ajump[0];
						this.colN = ajump[2];
					}
					else if(falling)
					{
							//Right facing Falling Animation
						this.row = afall[0];
						this.col = 0;
						this.colN = afall[2];
					}
					else
					{
							//Right facing Running Animation
						this.row = arun[0];
						this.colN = arun[2];
					}
				}
				else if(backward)
				{
					if  (jumping)
					{
							//Left facing Jump Animation
						this.row = ajump[1];
						this.colN = ajump[2];
					}
					else if(falling)
					{
							//Left facing Falling Animation
						this.row = afall[1];
						this.col = 0;
						this.colN = afall[2];
					}
					else
					{
							//Left facing Running Animation
						this.row = arun[1];
						this.colN = arun[2];
					}
				}
				else
				{
					if(FF)
					{
						if(jumping)
						{
								//Right facing Jump Animation
							this.row = ajump[0];
							this.colN = ajump[2];
						}
						
					}
					else
					{
						if  (jumping)
						{
								//Left facing Jump Animation
							this.row = ajump[1];
							this.colN = ajump[2];
						}
					}
				}
			}
			else if(falling)
			{
				if(FF)
				{
					this.row = afall[0];
					this.col = 0;
					this.colN = afall[2];
				}
				else
				{
					this.row = afall[1];
					this.col = 0;
					this.colN = afall[2];
				}
			}
			else
			{
					//test
				this.timerspeed = 5;
				
				if (FF)//FF = facing forward
				{
						//Idle pose facing Right
					this.row = aidle[0];
					this.colN = aidle[2];
				}
				else
				{
						//Idle pose facing Left
					this.row = aidle[1];
					this.colN = aidle[2];
				}
			}
			//legs.setX(x);
			//legs.setY(y);
			if(hasR)
			{
				rifle.update();
			}
				//I put this check in to make sure that the animation always starts at the beginning 
			if(this.checkAni != this.row )
			{
				col = 0;
				this.checkAni= this.row;
			}	
		}
		this.animateCol();
		//legs.animateCol();
	}
	public void render(Graphics g,  Map<String,BufferedImage> spriteData)
	{	
			//TEST render HP
		if(HP > 0 && player)
		{
			g.drawImage( spriteData.get(hpImage.name), hpImage.x, hpImage.y , (int) (HPsW*(HP/maxHP)) , hpImage.height, null);
		}
		
		if(Engine.renderHitBox)
		{
			g.setColor(Color.BLUE);
			g.drawRect(this.headhitbox[0]+this.x, this.headhitbox[1]+this.y,this.headhitbox[2], this.headhitbox[3]);
			g.drawRect(this.feethitbox[0]+this.x, this.feethitbox[1]+this.y,this.feethitbox[2], this.feethitbox[3]);
			g.setColor(Color.YELLOW);
			g.drawRect(this.fronthitbox[0]+this.x, this.fronthitbox[1]+this.y,this.fronthitbox[2], this.fronthitbox[3]);
			g.drawRect(this.backhitbox[0]+this.x, this.backhitbox[1]+this.y,this.backhitbox[2], this.backhitbox[3]);

		}
		if(hasR)
		{
			rifle.render(g, spriteData);
		}
		//legs.render(g);
		super.render(g, spriteData);
	}
	
	public boolean fullHP()
	{
		if(this.HP == this.maxHP)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public void addHP(int inHP)
	{
		if(inHP < 0 )
		{
			return;
		}
		inHP += this.HP;
		if(inHP > maxHP)
		{
			this.HP = maxHP;
		}
		else
		{
			this.HP = inHP;
		}
	}
	
	
	public void fall()
	{			

		if(this.gravity < this.topGravity) 
		{
			this.gravity += this.fallrate;
		}
		
		this.y = (int) (this.y + gravity);
		this.falling = true;
	}
	public void moveYn()
	{
		if(this.gravity < this.topGravity) 
		{	
			this.gravity += this.fallrate;
		}
		this.y = (int) (this.y + gravity);
	}
	
	public void moveXp()
	{
		if(this.speedX < this.topRunSpeed)
		{
			this.speedX += this.runrate;
		}
		this.x = (int) (this.x + speedX);
	}

	public void moveXn()
	{
		if(this.speedX > -this.topRunSpeed)
		{
			this.speedX -= this.runrate;
		}
		this.x = (int) (this.x + speedX);
	}

	public double getGravity()
		{return this.gravity;}	
	public void setGravity(double in)
		{this.gravity = in;}
	public void moveYp()
		{this.y = (int) (this.y + this.topRunSpeed);}
	public void setFaceForward(boolean FF)
		{this.FF = FF;}
	public void setJumping(boolean jumping)
		{this.jumping = jumping;}
	public void setForward(boolean forward)
		{this.forward = forward;}
	public void setbackward(boolean backward)
		{this.backward = backward;}
	public void setfalling(boolean falling)
		{this.falling = falling;}
	public boolean getFacingForward()
		{return this.FF;}
	public boolean getJumping()
		{return this.jumping;}
	public boolean getForward()
		{return this.forward;}
	public boolean getBackward()
		{return this.backward;}
	public double gettopRunSpeed()
	{return this.topRunSpeed;}
	public double gettopGravity()
		{return this.topGravity;}
	public double gettopJump()
		{return this.topJump;}
	public void settopRunSpeed(int in)
		{this.topRunSpeed= in;}
	public void settopGravity(int in)
		{this.topGravity = in;}
	public void settopJump(int in)
		{this.topJump = in;}
	public boolean getDead()
		{return this.dead;}
	public boolean getAttacking()
		{return attacking;}
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
	public boolean getRA()
	{
		return(this.reverseAttacking);
	}
	public boolean getJA()
	{
		return(this.jumpAttacking);
	}

	
	
}
