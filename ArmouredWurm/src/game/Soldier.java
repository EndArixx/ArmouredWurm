package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Queue;

/* 
 * BAD GUYS
 * 
 * this will be the logic for the enemy AI
 *	
 *
 *	Load/Save		 [X]
 *		LOAD (X)
 *		SAVE (X)
 *
 *	enemy movement 	[]
 *		got patroling animated
 *		attack patterns?
 *		melee [X]
 *		range []
 *		charge[?]
 *
 *  enemy Animation []
 *  	attack [X]
 *  	patrolling[x]
 *  	idle	[kinda]
 *  
 *  what want to do is establish traits
 *  	things like Fear and stuff like that.
 *  
 *  
 *  
 */
public class Soldier extends PlayerChar
{
		/*This is a test AI 
		 * 	currently it can
		 * 		Patrol
		 * 		Target and shoot (old)
		 * 		charge 			(old)
		 * 		move
		 * 		animate
		 */
	int patrolL,patrolR,L,R,movingL[], movingR[], idle[], trueX, trueY, chargeS, chargeD,chargeB,chargeM;
	int vision[],meleeRange[], reactionT, reactionC, attackA[], fallA[],attackMoveA[],damageZ[];
	boolean patroling, charging, reacting, attacking, patrols ;
	String spritetop;
	String spriteleg;
	
	
		//test varables
	Sprite alert;
	
	//protected Sprite legs;
	public Soldier
		(String name, String spriteloc,String legloc,int x, int y, int width, int height,int row,int col,  Map<String,BufferedImage> spriteData) 
	{
		super(name, spriteloc, x, y, width, height, row, col, spriteData); //---check
		this.spritetop = spriteloc;
		this.spriteleg = legloc;
		
		
			//this is for testing things-----------------------
		alert = new Sprite("res/icu.png",-50,-50, spriteData);
		this.player = false;
		this.timerspeed = 5;
			//end testing--------------------------------------
		
		this.trueX = x;
		this.trueY = y;
		this.movingL = new int[2];
		this.movingR = new int[2];
		this.idle = new int[4];
		this.fallA = new int[4];
		this.attackMoveA = new int[4];
		this.attackA = new int[4];
			//animations 
		this.movingL[0] = 3;
		this.movingL[1] = 20;
		this.movingR[0] = 2;
		this.movingR[1] = 20;
			//attack move
		this.attackMoveA[0] = 4;
		this.attackMoveA[1] = 20;
		this.attackMoveA[2] = 5;
		this.attackMoveA[3] = 20;
			//idle
		this.idle[0] = 0;
		this.idle[1] = 10;
		this.idle[2] = 1;
		this.idle[3] = 10;
			//Attack animation frames
		this.attackA[0] = 6;
		this.attackA[1] = 10;
		this.attackA[2] = 7;
		this.attackA[3] = 10;
			//fall animation frames
		this.fallA[0] = 10;
		this.fallA[1] = 12;
		this.fallA[2] = 11;
		this.fallA[3] = 12;
			
		this.damageZ = new int[2];
		this.damageZ[0] = 4;
		this.damageZ[1] = 7;
		
		this.col = 0;
		this.colN = 10;
		
		//THIS ISNT A HITBOX!
			//this should be a hitbox
		this.vision = new int[2];
		this.vision[0] = 800;
		this.vision[1] = 400;
			//melee zone
		this.meleeRange = new int[4];
		this.meleeRange[0] = 400;
		this.meleeRange[1] = 200;
		this.meleeRange[2] = 0;
		this.meleeRange[3] = 0;
			//Health
		this.HP = 50;
		
		this.timerspeed = 3;
		
		this.chargeS = 1;
		this.chargeB = 5;
		this.chargeM = 400;
		this.chargeD = 0;
		this.charging = false;
		this.reactionT = 50;
		this.reactionC = 0;
		this.reacting = false;
		this.attacking = false;
		this.patrols = false;
			//acceleration movement
		this.speedX = 5;
		this.gravity = 5;
	}
	@SuppressWarnings("unused")
	public void aim(PlayerChar target)
	{
			//this is old and currently not in use.
			//what this does is basically find the player and draw a line to him.
		float xs,ys,xt,yt;
		int projs = rifle.getprojSpeed(); 
		boolean right, under;
		if(target.getX() < this.x)
		{
			right = true;
			xt = Math.abs((this.x + this.width/2) - (target.getX() + target.getWidth()/2));
		}
		else
		{
			right = false;
			xt = Math.abs((target.getX()/2 + target.getWidth()) -( this.x + this.width/2));
		}
		if(target.getY() < this.y)
		{
			under = true;
			yt = Math.abs((this.y + this.height/2) - (target.getY() + target.getHeight()/2));
		}
		else 
		{
			under = false;
			yt =Math.abs((target.getY()/2 + target.getHeight()) -( this.y + this.height/2));
		}
		if(xt == yt)
		{
			xs= -rifle.getprojSpeed()/2;
			if(under)
			{
				ys = -rifle.getprojSpeed()/2;
			}
			else
			{
				ys = rifle.getprojSpeed()/2;
			}
		}
		else
		{
			xs = (xt/(xt+yt)) *  projs;
			if(under)
			{
				ys = (-yt/(xt+yt)) *  projs;
			}
			else
			{
				ys = (yt/(xt+yt)) *  projs;
			}
		}
		rifle.target(Math.round(xs) ,Math.round(ys));
	}
	//--------------------------------------------------------------------------------------------UPDATE
	public void update(World theWorld, Platform[] p, Queue<DamageHitbox> damageQ)
	{
		boolean f = true;
		for(int i = 0; i< p.length ;i++)
		{
			if(Tools.check_collision(this.getfeetHitbox(), p[i].getHitbox()))
			{
				f = false;
				this.falling = false;
			}
		}
		if((this.trueY + this.height) > theWorld.height)
		{
			f = false;
			this.falling = false;
		}
		if(f)
		{
			fall();
			this.patroling = false;
		}
		
		x = theWorld.getX() + trueX;
		y = theWorld.getY() + trueY;
			//should this be an elseif chain?
		if(!f && attacking)
		{
			if(col >= colN)
			{
				attacking = false;
					//Pause after attacks or build it in the animations?
			}
		}
		if(!f && attacking)
		{
			meleeAttack(damageQ);
		}
		if(!f && patroling)
		{
			patrol();
		}
		if(!f && charging)
		{
			charge();
		}
		else if(!f && reacting)
		{
			react(theWorld);
		}

		
		//legs.setX(x);
		//legs.setY(y);
		if(hasR)
		{
			rifle.update();
		}
		this.animateCol();
		//legs.animateCol();
	}
	public void update(World theWorld)
	{
		this.trueX = (int) (this.trueX + speedX);
		this.trueY = (int) (this.trueY + speedY);
		x = theWorld.getX() + trueX;
		y = theWorld.getY() + trueY;
	}
	public void setSightbox(int vwidth, int vheight)
	{
		vision[0] = vwidth;
		vision[1] = vheight;
	}
	public void fall()
	{
		this.trueY = (int) (this.trueY + this.gravity);
		if(!this.falling)
		{
			if(this.FF)
			{
				this.row = this.fallA[0];
				this.colN = this.fallA[1];
				this.col = 0;
			}
			else
			{
				this.row = this.fallA[2];
				this.colN = this.fallA[3];
				this.col = 0;
			}
		}
		this.falling = true;
	}
	public void setPatrol(int patrolL,int patrolR)
	{
		this.patrols = true;
		this.patroling = true;
		this.patrolR = patrolR;
		this.patrolL = patrolL;
		this.row = movingR[0];
		this.colN = movingR[1];
		//legs.row = movingR[0];
		//legs.colN = movingR[1];
		//legs.col = 0;
	}
	public void sight(PlayerChar target,World theWorld)
	{
		if(this.falling)
		{
			return;
		}
		int sightHitbox[] = new int[4];
		if(this.FF)
		{
			sightHitbox[0] = theWorld.getX() + trueX;
		}
		else
		{
			sightHitbox[0] =  theWorld.getX() + trueX - vision[0]  + width;
		}
		sightHitbox[1] =  theWorld.getY() + trueY - vision[1] + this.getHeight();
		sightHitbox[2] = vision[0];
		sightHitbox[3] = vision[1];
		
		if(Tools.check_collision(target.getHitbox(), sightHitbox))
		{
			if(this.FF)
			{
				sightHitbox[0] = x;
			}
			else
			{
				sightHitbox[0] =  x - meleeRange[0] + width;
			}
			sightHitbox[1] = y - meleeRange[1] + this.getHeight();
			sightHitbox[2] = meleeRange[0];
			sightHitbox[3] = meleeRange[1];
			if(Tools.check_collision(target.getHitbox(), sightHitbox))
			{
				if(!attacking)
				{
					//TEST!
					startMelee(); //TEST!
				}
			}
			else if(!reacting)
			{	
					//this is where he moves in close to attack
				//move();
				startReact();
			}

		}
		else
		{	
			if(!reacting && !charging && !attacking)
			{
				if(!patroling)
				{
					startPatrol();
				}
			}
		}
	}
	public void startPatrol()
	{
		this.timerspeed = 5;///TEMP1111111111111111
		if(this.FF)
		{
			this.row = movingR[0];
			this.colN = movingR[1];
			this.col = 0;
		}
		else
		{
			this.row = movingL[0];
			this.colN = movingL[1];
			this.col = 0;
		}
		
		patroling = true;
	}
	public void startReact()
	{
		this.timerspeed = 5; ///TEMP1111111111111111
		if(this.FF)
		{
			this.col =0;
			this.row = this.idle[0];
			this.colN = this.idle[1];
		}
		else
		{
			this.col =0;
			this.row = this.idle[2];
			this.colN = this.idle[3];
		}
		this.reacting = true;
		this.patroling = false;
	}
	private void react(World theWorld) 
	{
		patroling = false;
		if(reactionC < reactionT)
		{
			reactionC++;
		}
		else
		{
			//this.charging = true;	
			this.reacting = false;
			this.reactionC = 0;
		}
			
	}
	public void startMelee()
	{
		this.timerspeed = 3;///TEMP1111111111111111
		
		
		this.patroling = false;
		this.firstloop = true;
		if(this.FF)
		{
			this.row = attackA[0];
			this.colN = attackA[1];
			this.col = 0;
			this.attacking = true;
		}
		else
		{
			this.row = attackA[2];
			this.colN = attackA[3];
			this.col = 0;
			this.attacking = true;
		}
	}
	public void meleeAttack(Queue<DamageHitbox> damageQ)
	{
		if(!this.firstloop)
		{	
				//hit animations only run through once
			this.attacking= false;
		}
		if(this.damageZ[0] <= this.col && this.col <=  this.damageZ[1])
		{	
				//this is all test data and shouldnt be hardcoded!
			if(this.FF)
			{
				DamageHitbox out = new DamageHitbox( this.x+ this.width/2 , this.y ,(int)(this.width*0.75), this.height, 5 , 1);
				damageQ.add(out);
			}
			else
			{
				DamageHitbox out = new DamageHitbox( this.x- this.width/2 , this.y ,(int)(this.width*0.75), this.height, 5 , 1);
				damageQ.add(out);
			}

		}
	}
	public void patrol()
	{
		//PREVENT WALKING THROUGH PLATFORMS
		//GIVE ABILITY TO JUMP ONTOP OR OVER
		
		
			//This should make sure the badguy starts walking again if he stops
		if(this.FF)
		{
			if(this.row != this.movingR[0])
			{
				this.row = movingR[0];
				this.colN = movingR[1];
				this.col = 0;
			}
		}
		else
		{
			if(this.row != this.movingL[0])
			{
				this.row = movingL[0];
				this.colN = movingL[1];
				this.col = 0;
			}
		}
		
			//now check to make sure its still within bounds
		if(this.FF)
		{
			if(trueX > patrolR)
			{
				this.FF = false;
				this.row = movingL[0];
				this.colN = movingL[1];
				//legs.row = movingL[0];
				//legs.colN = movingL[1];
				//legs.col = 0;
			}
			else
			{
				trueX = (int) (trueX + speedX);
			}
		}
		else
		{
			if(trueX < patrolL)
			{
				this.FF = true;
				this.row = movingR[0];
				this.colN = movingR[1];
				//legs.row = movingR[0];
				//legs.colN = movingR[1];
				//legs.col = 0;
			}
			else
			{
				trueX = (int) (trueX - speedX);
			}
		}
	}
	public void charge()
	{
			//this works!
		if (chargeD >= chargeM)
		{
			charging = false;
			chargeS = 1;
			chargeD = 0;
		}
		else if(FF)
		{
			trueX = trueX + chargeS;
			
 		}
		else
		{
			trueX = trueX - chargeS;
		}
		chargeD = chargeD + chargeS;
		chargeS = chargeS + chargeB;
		
	}
	public void render(Graphics g,  Map<String,BufferedImage> spriteData)
	{
		if(Engine.renderHitBox)
		{	
				//SIGHT RANGE
			int[] sightHitbox = new int[4];
			g.setColor(Color.BLUE);
			if(this.FF)
			{
				sightHitbox[0] = x;
			}
			else
			{
				sightHitbox[0] =  x - vision[0] + width;
			}
			sightHitbox[1] = y - vision[1] + this.getHeight();
			sightHitbox[2] = vision[0];
			sightHitbox[3] = vision[1];
			g.drawRect(sightHitbox[0], sightHitbox[1],sightHitbox[2], sightHitbox[3]);
			
				//ATTACK RANGE
			g.setColor(Color.RED);
			if(this.FF)
			{
				sightHitbox[0] = x;
			}
			else
			{
				sightHitbox[0] =  x - meleeRange[0] + width;
			}
			sightHitbox[1] = y - meleeRange[1] + this.getHeight();
			sightHitbox[2] = meleeRange[0];
			sightHitbox[3] = meleeRange[1];
			g.drawRect(sightHitbox[0], sightHitbox[1],sightHitbox[2], sightHitbox[3]);
			
			
		}
		if(this.reacting)
		{
			alert.x = this.x + (this.width/2) - (alert.width/2);
			alert.y = this.y - alert.height;
			alert.render(g,spriteData);
		}
		super.render(g, spriteData);
	}
}
 