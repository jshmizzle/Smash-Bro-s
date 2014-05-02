package model;

import java.awt.Point;
import java.util.ArrayList;

//can test if you want
public class TestShortestPath {
	public static void main(String[] args) {
		GameBoard game = new GameBoard(null, null, Map.First, Scenario.Princess);
		ArrayList<Point> a = game.findAttackRange(new Point(7, 7), 4);
		if (a == null) {
			System.out.println("hi");
		} else {
			for (int i = 0; i < a.size(); i++) {
				System.out.println(a.get(i).toString());
			}
		}
	}
}
