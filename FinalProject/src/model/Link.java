package model;

import java.awt.Point;

@SuppressWarnings("serial")
public class Link extends Unit{

	public Link(char c) {
		super("Link", 60, 4, 5, 7, new Point(0, 0), c);
	}

}
