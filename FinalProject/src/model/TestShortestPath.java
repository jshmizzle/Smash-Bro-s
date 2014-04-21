package model;

import java.awt.Point;
import java.util.ArrayList;

//can test if you want
public class TestShortestPath {
	public static void main(String[] args) {
		GameBoard game = new GameBoard(null, null, 1, 1);
		ArrayList<Point> a = game.findShortestPath(new Point(7, 7), new Point(8, 8));
		if (a == null) {
			System.out.println("hi");
		} else {
			for (int i = 0; i < a.size(); i++) {
				System.out.println(a.get(i).toString());
			}
		}
	}
}
