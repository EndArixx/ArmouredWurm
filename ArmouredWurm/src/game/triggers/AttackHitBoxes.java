package game.triggers;

public class AttackHitBoxes 
{
	//This will be used for hitbox generation with the new trigger system
	//This will be used at the same time as a combat Spark.
	//this will prolly  need more then 4 things...
	
	private int[][] interanlHitboxes;
		//a normal hitbox is only for points...but this might need an offset
	private int lenofData = 4;
	public AttackHitBoxes(int[][] in) 
	{
		interanlHitboxes = in;
	}
	public AttackHitBoxes(String in)
	{
		String[] hitboxes = in.split(";");
		String[] hit;
		interanlHitboxes = new int[hitboxes.length / lenofData][lenofData];
		for(int i = 0; i < (hitboxes.length / lenofData); i++)
		{
			hit = hitboxes[i].split(",");
			
			for (int j = 0; j< lenofData; j++)
			{
				interanlHitboxes[i][j] = Integer.parseInt(hit[j]);
			}
		}
		
	}
	public int[] getHitBox(int index)
	{
		
		return interanlHitboxes[index];
	}
}
