package model;

import java.awt.Point;

public class Mario extends Unit {

	public Mario() {
		super("Mario", 100, 6, 5, 3, new Point(0,0), 'W');
		//		name, hp, move distance, dmg, range, point, character
	}

}
