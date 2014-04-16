package model;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class MegaMan extends Unit{

	public MegaMan(String n, int hp, int distance, int attack, int attackRange, BufferedImage i, Point p, char c) {
		super(n, hp, distance, attack, attackRange, i, p, c);
	}
	
	public MegaMan(){
		super("MegaMan", 100, 6, 12, 2, new Point(0, 0), 'M');
		//    name, hp, movedistance, dmg, range, starting point, character representation
	}

}
