package model;

import java.awt.Point;

@SuppressWarnings("serial")
public class Link extends Unit{

	public Link(char c) {
		super("Link", 100, 4, 25, 8, new Point(0, 0), c);
	}

}
