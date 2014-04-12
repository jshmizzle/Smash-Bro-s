import java.awt.Point;
import java.util.ArrayList;



public class GameBoard {
	
	
	
	private char[][] gameBoard;
	private int boardWidth = 20;
	private int boardHeight = 20;
	private ArrayList<Character> userUnits;
	private ArrayList<Character> compUnits;
	private char[][] terrain;
	private int currentMap;
	private int currentScenario;
	
	/**
	 * GameBoard constructor, takes a list of users units, list of comp units, map level, and a scenario.
	 * Based on the arguments given, the board will create a 2D char array representing the locations of
	 * game objects on the board.  Scenario 1 is Kill the Section Leader, scenario 2 is Death Match, scenario 3 is (dunno yet)
	 */
	
	public GameBoard(ArrayList<Character> userUnits,ArrayList<Character> compUnits, int map, int scenario){
		
		gameBoard = new char[boardWidth][boardHeight];
		terrain = new char[boardWidth][boardHeight];
		this.userUnits = userUnits;
		this.compUnits = compUnits;
		currentMap = map;
		currentScenario = scenario;
		
		
		int i=0;
		for(char c : userUnits){
			gameBoard[boardWidth/2-1+i][boardHeight] = c;
			i++;
		}
		
		int j=0;
		for(char c : compUnits){
			gameBoard[boardWidth/2-1+j][0] = c;
			j++;
		}
		
		if(currentMap==1){
			gameBoard[boardHeight/2-5][boardWidth/2-2] = '#';
			gameBoard[boardHeight/2-4][boardWidth/2-2] = '#';
			gameBoard[boardHeight/2-3][boardWidth/2-2] = '#';
			gameBoard[boardHeight/2+5][boardWidth/2-2] = '#';
			gameBoard[boardHeight/2+4][boardWidth/2-2] = '#';
			gameBoard[boardHeight/2+3][boardWidth/2-2] = '#';
			
			gameBoard[boardHeight/2-5][boardWidth/2+2] = '#';
			gameBoard[boardHeight/2-4][boardWidth/2+2] = '#';
			gameBoard[boardHeight/2-3][boardWidth/2+2] = '#';
			gameBoard[boardHeight/2+5][boardWidth/2+2] = '#';
			gameBoard[boardHeight/2+4][boardWidth/2+2] = '#';
			gameBoard[boardHeight/2+3][boardWidth/2+2] = '#';
			
			gameBoard[boardHeight/2-2][boardWidth/2-5] = '#';
			gameBoard[boardHeight/2-2][boardWidth/2-4] = '#';
			gameBoard[boardHeight/2-2][boardWidth/2-3] = '#';
			gameBoard[boardHeight/2-2][boardWidth/2+5] = '#';
			gameBoard[boardHeight/2-2][boardWidth/2+4] = '#';
			gameBoard[boardHeight/2-2][boardWidth/2+3] = '#';
			
			gameBoard[boardHeight/2+2][boardWidth/2-5] = '#';
			gameBoard[boardHeight/2+2][boardWidth/2-4] = '#';
			gameBoard[boardHeight/2+2][boardWidth/2-3] = '#';
			gameBoard[boardHeight/2+2][boardWidth/2+5] = '#';
			gameBoard[boardHeight/2+2][boardWidth/2+4] = '#';
			gameBoard[boardHeight/2+2][boardWidth/2+3] = '#';

		}
		
	}
	
	public ArrayList<Point> shortestPath(Point a, Point b){
		
		ArrayList<Point> moves = new ArrayList<>();
	
		return null;
	}
	
	public boolean checkAvailable(Point point){
		
		if(gameBoard[(int) point.getY()][(int) point.getX()]==' ')
			return true;
		
		return false;
	}
	
	public char inspectPosition(Point p){
		
		return gameBoard[(int) p.getX()][(int) p.getY()];
	}
	
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
	
	public String gameBoardToString(){
		String str = "";
		for(int i=0; i < boardWidth;i++){
			for(int j=0; j < boardHeight;j++){
				str += gameBoard[boardWidth][boardHeight];
			}
		}
		return str;
	}
	
}