package model;

import java.awt.Point;

public class Mario extends Unit {

	public Mario(int hp, int distance, int attack, int attackRange,
			Point p) {
		super("Mario", hp, distance, attack, attackRange, p, 'W');
		
	}

}
