import java.awt.Point;
import java.util.ArrayList;
public class GameBoard {
	
	private char[][] gameBoard;
	private int boardWidth = 20;
	private int boardHeight = 20;
	private ArrayList<Unit> units;
	private char[][] terrain;
	
	public GameBoard(ArrayList<Unit> units){
		gameBoard = new char[boardWidth][boardHeight];
		terrain = new char[boardWidth][boardHeight];
		this.units = units;
	}
	
	public ArrayList<Point> shortestPath(Point fromHere, Point toThere){
		return null;
	}
	
	public boolean checkAvailable(){
		return false;
	}
	
	public GameObject inspectPosition(Point p){
		return null;
	}
	
	public boolean chooseMove(Unit u, Point p){
		return false;
	}
	
	private void updateObservers(){
		
	}
	
	public char[][] getGameBoard(){
		return gameBoard;
	}
	
	public int getBoardWidth(){
		return boardWidth;
	}
	
	public int getBoardHeight(){
		return boardHeight;
	}
	
	public char[][] getTerrain(){
		return terrain;
	}
	//whatthe
}