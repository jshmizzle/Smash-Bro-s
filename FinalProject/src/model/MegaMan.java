package model;

import java.awt.Point;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class MegaMan extends Unit{

	public MegaMan(String n, int hp, int distance, int attack, int attackRange, BufferedImage i, Point p, char c) {
		super(n, hp, distance, attack, attackRange, i, p, c);
	}
	
	public MegaMan(char c){
		super("MegaMan", 100, 6, 35, 3, new Point(0, 0), c);
		//    name, hp, movedistance, dmg, range, starting point, character representation
	}

}
