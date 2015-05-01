package game;

public class DamageHitbox 
{
	/*
	 * this is a DamageHitbox, everytime an attack is made one of these will be added to a queue.
	 * the queue is then compared to all the targets.
	 * 
	 */
	
	int[] hitbox;
	int type;
	int amount;
	public DamageHitbox(int x, int y, int width, int height, int inamount, int intype)
	{	/*
		attack types
			0 = Enemy melee regular
			1 = Enemy melee heavy attack
			2 = Enemy range regular
			3 = Player melee
			4 = Bomb
			5 = spike
		*/
		
		this.type = intype;
		this.hitbox = new int[4];
		this.hitbox[0] = x;
		this.hitbox[1] = y;
		this.hitbox[2] = width;
		this.hitbox[3] = height;
		
		this.amount = inamount;
		
	}
	
	public DamageHitbox(int[] inhitbox, int inamount, int intype)
	{	
		this.type = intype;
		this.hitbox = inhitbox;
		this.amount = inamount;
		
	}
	public int[] getHitbox()
	{
		return hitbox;
	}
}
