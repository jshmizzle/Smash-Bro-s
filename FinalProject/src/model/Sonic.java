package model;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class Sonic extends Unit {

	private Image sonicImage = null;
	//SonicThe = ImageIO.read(new File( "Sonic.gif"));
	
	
	public Sonic(char c) {
		super("Sonic", 100, 20, 3, 1, new Point(0, 0), c);
	}
}
