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
	int patrolL,patrolR,L,R,moving[], idle[], trueX, trueY, chargeS, chargeD,chargeB,chargeM;
	int vision[],meleeRange[], reactionT, reactionC, attackA[], fallA[],attackMoveA[],damageZ[];
	boolean patroling, charging, reacting, attacking, patrols,attackMoving ;
	String spritetop;
	String spriteleg;
	
	
		//test varables
	Sprite alert;
	
	//protected Sprite legs;
	public Soldier
		(String name, String spriteloc,
				String legloc,int x,
				int y, int width, 
				int height,int row,
				int col, 
				Map<String,BufferedImage> spriteData) 
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
		this.moving = new int[4];
		this.idle = new int[4];
		this.fallA = new int[4];
		this.attackMoveA = new int[4];
		this.attackA = new int[4];
			//animations 
		this.moving[0] = 2;
		this.moving[1] = 20;
		this.moving[2] = 3;
		this.moving[3] = 20;
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
		this.vision = new int[4];
		this.vision[0] = 50;
		this.vision[1] = -25;
		this.vision[2] = 800;
		this.vision[3] = 400;
			//melee zone
		this.meleeRange = new int[4];
		this.meleeRange[0] = 50;
		this.meleeRange[1] = 25;
		this.meleeRange[2] = 200;
		this.meleeRange[3] = 150;
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
		this.attackMoving = false;
 			//acceleration movement
		this.speedX = 2;
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
		if(!f && attackMoving)
		{
			attackmove();
		}
		if(!f && charging)
		{
			charge();
		}
		else if(!f && reacting)
		{
			react(theWorld);
		}

		if(hasR)
		{
			rifle.update();
		}
		this.animateCol();
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
		this.row = moving[0];
		this.colN = moving[1];
	}
		//----------------------------------------------------------------------------------------SIGHT
	public void sight(PlayerChar target,World theWorld)
	{
		if(this.falling)
		{
			return;
		}
		int sightHitbox[] = new int[4];
		if(this.FF)
		{
			sightHitbox[0] = x + vision[0];
		}
		else
		{
			sightHitbox[0] = x - vision[2]  - vision[0] + width;
		}
		sightHitbox[1] =  y - vision[3] + vision[1] + height;
		sightHitbox[2] = vision[2];
		sightHitbox[3] = vision[3];
		
			//this needs work!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		if(Tools.check_collision(target.getHitbox(), sightHitbox))
		{
			
			if(this.FF)
			{
				sightHitbox[0] = x + meleeRange[0];
			}
			else
			{
				sightHitbox[0] =  x - meleeRange[0];
			}
			sightHitbox[1] = y + meleeRange[1];
			sightHitbox[2] = meleeRange[2];
			sightHitbox[3] = meleeRange[3];
			if(Tools.check_collision(target.getHitbox(), sightHitbox))
			{
				if(!attacking)
				{
					//TEST!
					startMelee(); //TEST!
				}
			}
			else if(!attackMoving)
			{	
					//this is where he moves in close to attack
				//move();
				startAttackMove();
			}
			
		}
		else
		{	
			this.attackMoving = false;
			if(!reacting && !charging && !attacking)
			{
				if(!patroling)
				{
					startPatrol();
				}
			}
		}
	}
	public void startAttackMove()
	{
		setFalse();
		
		this.speedX = 3;
		this.timerspeed = 2;///TEMP1111111111111111
		if(this.FF)
		{
			this.row = attackMoveA[0];
			this.colN = attackMoveA[1];
			this.col = 0;
		}
		else
		{
			this.row =attackMoveA[2];
			this.colN = attackMoveA[3];
			this.col = 0;
		}
		
		this.attackMoving = true;
	}
	public void startPatrol()
	{
		setFalse();
		this.speedX = 2;
		this.timerspeed = 4;///TEMP1111111111111111
		if(this.FF)
		{
			this.row = moving[0];
			this.colN = moving[1];
			this.col = 0;
		}
		else
		{
			this.row = moving[2];
			this.colN = moving[3];
			this.col = 0;
		}
		
		patroling = true;
	}
	public void startReact()
	{
		setFalse();
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
	}
	private void react(World theWorld) 
	{
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
		setFalse();
		
		this.firstloop = true;
		if(this.FF)
		{
			this.row = attackA[0];
			this.colN = attackA[1];
			this.col = 0;
		}
		else
		{
			this.row = attackA[2];
			this.colN = attackA[3];
			this.col = 0;
		}
		this.attacking = true;
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
	public void setFalse()
	{
		this.attacking = false;
		this.patroling = false;
		this.charging = false;
		this.attackMoving = false;
		this.reacting = false;
	}
	public void patrol()
	{
		//PREVENT WALKING THROUGH PLATFORMS
		//GIVE ABILITY TO JUMP ONTOP OR OVER
		
		
			//This should make sure the badguy starts walking again if he stops
		if(this.FF)
		{
			if(this.row != this.moving[0])
			{
				this.row = moving[0];
				this.colN = moving[1];
				this.col = 0;
			}
		}
		else
		{
			if(this.row != this.moving[2])
			{
				this.row = moving[2];
				this.colN = moving[3];
				this.col = 0;
			}
		}
		
			//now check to make sure its still within bounds
		if(this.FF)
		{
			if(trueX > patrolR)
			{
				this.FF = false;
				this.row = moving[2];
				this.colN = moving[3];
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
				this.row = moving[0];
				this.colN = moving[1];
			}
			else
			{
				trueX = (int) (trueX - speedX);
			}
		}
	}
	public void attackmove()
	{
		if(this.FF)
		{
			if(trueX > patrolR)
			{
				this.FF = false;
				this.row = attackMoveA[2];
				this.colN = attackMoveA[3];
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
				this.row = attackMoveA[0];
				this.colN = attackMoveA[1];
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
		//--------------------------------------------------------------------------------RENDER
	public void render(Graphics g,  Map<String,BufferedImage> spriteData)
	{
		if(Engine.renderHitBox)
		{	
				//SIGHT RANGE
			g.setColor(Color.BLUE);
			if(this.FF)
			{
				g.drawRect( x + vision[0], y - vision[3] + vision[1] + height, vision[2], vision[3]);
			}
			else
			{
				g.drawRect( x - vision[2]  - vision[0] + width, y - vision[3] + vision[1] + height, vision[2], vision[3]);
			}		
				//ATTACK RANGE
			g.setColor(Color.RED);
			if(this.FF)
			{
				g.drawRect(x + meleeRange[0], y + meleeRange[1],meleeRange[2],meleeRange[3]);
			}
			else
			{
				g.drawRect(x - meleeRange[0], y + meleeRange[1],meleeRange[2],meleeRange[3]);
			}
			
			
			
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
 