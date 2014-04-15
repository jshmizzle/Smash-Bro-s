package model;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class MegaMan extends Unit{

	public MegaMan(String n, int hp, int distance, int attack, int attackRange, BufferedImage i, Point p, char c) {
		super(n, hp, distance, attack, attackRange, i, p, c);
	}
	
	public MegaMan( int hp, int distance, int attack, Point p){
		super("MegaMan", hp, distance, attack, 3, p, 'M');
		
	}

}
