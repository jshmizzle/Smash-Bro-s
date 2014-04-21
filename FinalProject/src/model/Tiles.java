package model;

import java.awt.Point;
import java.util.ArrayList;

public class Tiles {
	public int distance;
	public ArrayList <Point> points=new ArrayList<Point>();
	public String status;
	
	public Tiles(){
		distance=900;
		status="unmarked";
	}
	
}
