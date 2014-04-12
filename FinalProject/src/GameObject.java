import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;


public class GameObject {
	private Point location;
	private BufferedImage image;
	private char charRepresentation;
	
	GameObject(BufferedImage i, Point p, Graphics g, char c){
		this.location = p;
		this.image = i;
		this.charRepresentation = c;
	}
	
	
	public Point getLocation(){
		return location;
	}
	
	public void setLocation(){
		
	}
	
	public char getCharRepresentation(){
		return charRepresentation;
	}
	
}
