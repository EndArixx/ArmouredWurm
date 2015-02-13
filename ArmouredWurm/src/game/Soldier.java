package game;

public class Soldier extends PlayerChar
{
		/*This is a test AI 
		 * 	currently it can
		 * 		Patrol
		 * 		Target and shoot
		 * 		move
		 * 		animate
		 */
	int patrolL,patrolR,L,R,movingL[], movingR[], idle[], trueX, trueY, chargeS, chargeD,chargeB,chargeM;
	int vision[], reactionT, reactionC;
	boolean patroling, charging, reacting;
	protected Sprite legs;
	public Soldier
		(String name, String spriteloc,String legloc,int x, int y, int width, int height,int row,int col) 
	{
		super(name, spriteloc, x, y, width, height, row, col); //JOHN THIS MAY BE BROKEN LOOK INTO WHEN RE-DESIGNING AI
		this.trueX = x;
		this.trueY = y;
		this.movingL = new int[2];
		this.movingR = new int[2];
		this.idle = new int[4];
		this.movingL[0] = 3;
		this.movingL[1] = 10;
		this.movingR[0] = 1;
		this.movingR[1] = 10;
		this.idle[0] = 0;
		this.idle[1] = 1;
		this.idle[2] = 0;
		this.idle[3] = 1;
		this.col = 0;
		this.colN = 1;
		this.vision = new int[2];
		this.vision[0] = 800;
		this.vision[1] = 400;
		this.chargeS = 1;
		this.chargeB = 5;
		this.chargeM = 400;
		this.chargeD = 0;
		this.charging = false;
		this.reactionT = 50;
		this.reactionC = 0;
		this.reacting = false;
	}
	public void aim(PlayerChar target)
	{
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
	public void update(World theWorld, Platform[] p)
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
		}
		
		x = theWorld.getX() + trueX;
		y = theWorld.getY() + trueY;
		if(patroling && !falling)
		{
			patrol();
		}
		if(charging)
		{
			charge();
		}
		if(reacting)
		{
			react(theWorld);
		}
		legs.setX(x);
		legs.setY(y);
		if(hasR)
		{
			rifle.update();
		}
		this.animateCol();
		legs.animateCol();
	}
	public void setSightbox(int vwidth, int vheight)
	{
		vision[0] = vwidth;
		vision[1] = vheight;
	}
	public void fall()
	{
		this.trueY = this.trueY + this.gravity;
		this.falling = true;
	}
	public void setPatrol(int patrolL, int patrolR )
	{
		patroling = true;
		this.patrolR = patrolR;
		this.patrolL = patrolL;
		legs.row = movingR[0];
		legs.colN = movingR[1];
		legs.col = 0;
	}
	public void sight(PlayerChar target,World theWorld)
	{
		int sightHitbox[] = new int[4];
		if(this.FF)
		{
			sightHitbox[0] = theWorld.getX() + trueX;
		}
		else
		{
			sightHitbox[0] =  theWorld.getX() + trueX - vision[0];
		}
		sightHitbox[1] =  theWorld.getY() + trueY - vision[1] + this.getHeight();
		sightHitbox[2] = vision[0];
		sightHitbox[3] = vision[1];
		if(Tools.check_collision(target.getbackHitbox(), sightHitbox) && hasR)
		{
			this.aim(target);
			this.fire(theWorld);
			//reacting = true;
		}
		else
		{	
			if(!reacting && !charging)
			{
				patroling = true;
			}
		}
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
			this.charging = true;	
			this.reacting = false;
			this.reactionC = 0;
		}
			
	}
	public void patrol()
	{
		//PREVENT WALKING THROUGH PLATFORMS
		//GIVE ABILITY TO JUMP ONTOP OR OVER
		if(this.FF)
		{
			if(trueX > patrolR)
			{
				this.FF = false;
				legs.row = movingL[0];
				legs.colN = movingL[1];
				legs.col = 0;
				this.row = 1;
			}
			else
			{
				trueX = trueX + speedX;
			}
		}
		else
		{
			if(trueX < patrolL)
			{
				this.FF = true;
				legs.row = movingR[0];
				legs.colN = movingR[1];
				legs.col = 0;
				this.row = 0;
			}
			else
			{
				trueX = trueX - speedX;
			}
		}
	}
	public void charge()
	{
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
}
 