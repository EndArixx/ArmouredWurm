package game.triggers;

public class AttackHitBoxes 
{
	//This will be used for hitbox generation with the new trigger system
	//This will be used at the same time as a combat Spark.
	//this will prolly  need more then 4 things...
	//This system will be Index[x,y,width,height]
	//the X and Y are offsets
	
	private int[][] internalHitboxes;
		//a normal hitbox is only for points...but this might need an offset
	private int lenofData = 4;
	public AttackHitBoxes(int[][] in) 
	{
		internalHitboxes = in;
	}
	public AttackHitBoxes(String in)
	{
		String[] hitboxes = in.split(";");
		String[] hit;
		internalHitboxes = new int[hitboxes.length][lenofData];
		for(int i = 0; i < (hitboxes.length); i++)
		{
			hit = hitboxes[i].split(",");
			
			for (int j = 0; j< lenofData; j++)
			{
				internalHitboxes[i][j] = Integer.parseInt(hit[j]);
			}
		}
		
	}
	public int[] getHitBox(int index)
	{	
		return internalHitboxes[index];
	}
	public int[] getHitBox(int index, int x, int y)
	{
		int[] out =  {internalHitboxes[index][0]+x,internalHitboxes[index][1]+y,internalHitboxes[index][2],internalHitboxes[index][3]};
		return out;
	}
	
	//this is for testing and can be removed afterword.
	public String getString()
	{
		String out = "";
		for(int i = 0;i < internalHitboxes.length; i++)
		{
			for (int j = 0; j < lenofData; j++)
			{
				out += internalHitboxes[i][j];
			}
			out += " ";
		}
		return out; 
	}
}
