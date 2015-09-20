package game;

import java.awt.image.BufferedImage;
import java.util.Map;

public class MovingSpike extends Spike {

	public MovingSpike(String spriteloc, int x, int y,
			Map<String, BufferedImage> spriteData) {
		super(spriteloc, x, y, spriteData);
		// TODO Auto-generated constructor stub
	}

	public MovingSpike(String inImage, int x, int y, int width, int height,
			int rowN, int colN, int timerspeed, int damage,
			Map<String, BufferedImage> spriteData) {
		super(inImage, x, y, width, height, rowN, colN, timerspeed, damage,
				spriteData);
		// TODO Auto-generated constructor stub
	}

}
