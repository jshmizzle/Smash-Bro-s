import java.awt.Point;
import java.util.ArrayList;
public class GameBoard {
	
	/************************************************************************************/
	
	private char[][] gameBoard;
	private int boardWidth = 20;
	private int boardHeight = 20;
	private ArrayList<Character> userUnits;
	private ArrayList<Character> compUnits;
	private char[][] terrain;
	
<<<<<<< HEAD
<<<<<<< HEAD
	/************************************************************************************/
	
	public GameBoard(ArrayList<Unit> units){
=======
	public GameBoard(ArrayList<Character> units){
>>>>>>> 0bd9f46af15be6490be90bf4843a96009fa0f807
=======
	public GameBoard(ArrayList<Character> userUnits,ArrayList<Character> compUnits){
		
>>>>>>> 525b58cff99e2613cdff058a4373f331693f2442
		gameBoard = new char[boardWidth][boardHeight];
		terrain = new char[boardWidth][boardHeight];
		this.userUnits = userUnits;
		this.compUnits = compUnits;
		
		
		int i=0;
		for(char c : userUnits){
			gameBoard[boardWidth/2-1+i][boardHeight] = c;
			i++;
		}
		
		int j=0;
		for(char c : compUnits){
			gameBoard[boardWidth/2-1+j][boardHeight] = c;
			j++;
		}
		
		
		
	}
	
<<<<<<< HEAD
	/************************************************************************************/
	
	public ArrayList<Point> shortestPath(Point fromHere, Point toThere){

=======
	public ArrayList<Point> shortestPath(Point a, Point b){
		
		ArrayList<Point> moves = new ArrayList<>();
		
		
		
>>>>>>> 525b58cff99e2613cdff058a4373f331693f2442
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
	
<<<<<<< HEAD
	/************************************************************************************/
	
	public GameObject inspectPosition(Point p){
		return null;
=======
	public char inspectPosition(Point p){
		
		return gameBoard[(int) p.getX()][(int) p.getY()];
>>>>>>> 525b58cff99e2613cdff058a4373f331693f2442
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