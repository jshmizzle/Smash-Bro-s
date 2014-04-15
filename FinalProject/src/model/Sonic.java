package model;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class Sonic extends Unit {

	private Image sonicImage = null;
	//SonicThe = ImageIO.read(new File( "Sonic.gif"));
	
	
	public Sonic(String n, int hp, int attack, Point p) {
		super("Sonic", hp, 20, attack, 1, p, 'S');
	}
}
