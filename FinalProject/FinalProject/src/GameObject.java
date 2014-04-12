import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

	/************************************************************************************/

public class GameObject {
	
	// The instance variables
	
	private Point location;
	private BufferedImage image;
	private char charRepresentation;
	
	/************************************************************************************/
	
	/*
	* 
	* This is the constructor for a new GameObject. Every GameObject has it's own image, as well as 
	* a starting location, and a representation on the board.
	*
	*/
	
	GameObject(BufferedImage i, Point p, Graphics g, char c){
		this.location = p;
		this.image = i;
		this.charRepresentation = c;
	}
	
	/************************************************************************************/
	//gets the current location on the board.
	
	public Point getLocation(){
		return location;
	}
	
	/************************************************************************************/
	//sets the location of the unit to the point that is passed in
	public void setLocation(Point p){
		this.location = p;
	}
	
	/************************************************************************************/
	
	public char getCharRepresentation(){
		return charRepresentation;
	}
	
	/************************************************************************************/
}
