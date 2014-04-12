import java.awt.Point;
import java.util.ArrayList;
public class GameBoard {
	
	/************************************************************************************/
	
	private char[][] gameBoard;
	private int boardWidth = 20;
	private int boardHeight = 20;
	private ArrayList<Character> units;
	private char[][] terrain;
	
<<<<<<< HEAD
	/************************************************************************************/
	
	public GameBoard(ArrayList<Unit> units){
=======
	public GameBoard(ArrayList<Character> units){
>>>>>>> 0bd9f46af15be6490be90bf4843a96009fa0f807
		gameBoard = new char[boardWidth][boardHeight];
		terrain = new char[boardWidth][boardHeight];
		this.units = units;
	}
	
	/************************************************************************************/
	
	public ArrayList<Point> shortestPath(Point fromHere, Point toThere){

		return null;
	}
	
<<<<<<< HEAD
	/************************************************************************************/
	
	public boolean checkAvailable(){
=======
	public boolean checkAvailable(Point point){
		
		if(gameBoard[(int) point.getY()][(int) point.getX()]==' ')
			return true;
		
>>>>>>> 0bd9f46af15be6490be90bf4843a96009fa0f807
		return false;
	}
	
	/************************************************************************************/
	
	public GameObject inspectPosition(Point p){
		return null;
	}
	
	/************************************************************************************/
	
	public boolean chooseMove(Unit u, Point p){
		
		
		if(u.isAlive() && checkAvailable(p)){
			
			int x = (int) Math.abs(u.getLocation().x-p.getX());
			int y = (int) Math.abs(u.getLocation().y-p.getY());
			
			int distance = x + y;
			
			if(u.getDistance()>=distance){
				gameBoard[(int) p.getY()][(int) p.getX()]=u.getCharRepresentation();
				u.setLocation(p);
			}
		}
		
		return false;
	}
	
	/************************************************************************************/
	
	private void updateObservers(){
		
	}
	
	/************************************************************************************/
	
	public char[][] getGameBoard(){
		return gameBoard;
	}
	
	/************************************************************************************/
	
	public int getBoardWidth(){
		return boardWidth;
	}
	
	/************************************************************************************/
	
	public int getBoardHeight(){
		return boardHeight;
	}
	
	/************************************************************************************/
	
	public char[][] getTerrain(){
		return terrain;
	}
	
}