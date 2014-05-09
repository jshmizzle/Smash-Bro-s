package model;

import java.awt.Point;

@SuppressWarnings("serial")
public class Mario extends Unit {

	public Mario(char c) {
		super("Mario", 100, 6, 5, 3, new Point(0,0), c);
		//		name, hp, move distance, dmg, range, point, character
	}

}
