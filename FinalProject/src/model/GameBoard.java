package model;
import java.awt.Point;
import java.util.ArrayList;



public class GameBoard {
	
	
	/************************************************************************************/
	
	private char[][] gameBoard;
	private int boardWidth = 20;
	private int boardHeight = 20;
	private ArrayList<Unit> userUnits;
	private ArrayList<Unit> compUnits;
	private char[][] terrain;
	private int currentMap;
	private int currentScenario;
	
	/************************************************************************************/
	
	/**
	 * GameBoard constructor, takes a list of users units, list of comp units, map level, and a scenario.
	 * Based on the arguments given, the board will create a 2D char array representing the locations of
	 * game objects on the board.  Scenario 1 is Kill the Section Leader, scenario 2 is Death Match, scenario 3 is (dunno yet)
	 */
	

	public GameBoard(ArrayList<Unit> userUnits,ArrayList<Unit> compUnits, int map, int scenario){

		gameBoard = new char[boardWidth][boardHeight];
		terrain = new char[boardWidth][boardHeight];
		this.userUnits = userUnits;
		this.compUnits = compUnits;
		currentMap = map;
		currentScenario = scenario;
		
		
		for(int i = 0; i<gameBoard.length; i++)
			for(int j = 0; j<gameBoard[0].length; j++){
				gameBoard[i][j] = ' ';
			}
		
		
		int i=0;
		for(Unit u: userUnits){
			gameBoard[0][boardHeight/2-1+i] = u.getCharRepresentation();
			Point p = new Point(0, boardHeight/2-1+i);
			u.setLocation(p);
			i++;
		}
		
		int j=0;
		for(Unit c : compUnits){
			gameBoard[boardHeight-1][boardWidth/2-1+j] = c.getCharRepresentation();
			Point p = new Point(boardHeight-1, boardWidth/2-1+j);
			c.setLocation(p);
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
			
			gameBoard[boardHeight/2-1][0]='#';
			gameBoard[boardHeight/2-1][1]='#';
			gameBoard[boardHeight/2+1][0]='#';
			gameBoard[boardHeight/2+1][1]='#';
			
			gameBoard[boardHeight/2-1][boardWidth-1]='#';
			gameBoard[boardHeight/2-1][boardWidth-2]='#';
			gameBoard[boardHeight/2+1][boardWidth-1]='#';
			gameBoard[boardHeight/2+1][boardWidth-2]='#';


		}
		
	}

	/************************************************************************************/

	public boolean checkAvailable(Point point){
		
		int x = (int)point.getX();
		int y = (int)point.getY();
		
		if(x >= boardWidth || x < 0)
			return false;
		
		if(y >= boardHeight || y < 0)
			return false;
		
		if(gameBoard[(int) point.getX()][(int) point.getY()]==' ')
			return true;

		return false;
	}
	
	/************************************************************************************/

	public char inspectPosition(Point p){
		return gameBoard[(int) p.getX()][(int) p.getY()];
	}
	
	/************************************************************************************/
	
	public boolean moveUp(Unit u){
		Point uSpot = u.getLocation();
		int row =(int) uSpot.getX();
		int column =(int) uSpot.getY();
		Point nextSpot = new Point (row-1, column);
		
		if(checkAvailable(nextSpot)){
			u.setLocation(nextSpot);
			gameBoard[row][column] = ' ';
			gameBoard[row-1][column] = u.getCharRepresentation();
			return true;
		}
		
		return false;
	}
	
	/************************************************************************************/
	
	public boolean moveDown(Unit u){
		Point uSpot = u.getLocation();
		int row =(int) uSpot.getX();
		int column =(int) uSpot.getY();
		Point nextSpot = new Point (row +1, column);
		
		if(checkAvailable(nextSpot) ){
			u.setLocation(nextSpot);
			gameBoard[row][column] = ' ';
			gameBoard[row+1][column] = u.getCharRepresentation();
			return true;
		}
		
		return false;
	}
	
	/************************************************************************************/

	public boolean moveRight(Unit u){
			Point uSpot = u.getLocation();
			int row =(int) uSpot.getX();
			int column =(int) uSpot.getY();
			Point nextSpot = new Point (row, column+1);
			
			if(checkAvailable(nextSpot))
			{		
				u.setLocation(nextSpot);
				gameBoard[row][column] = ' ';
				gameBoard[row][column+1] = u.getCharRepresentation();
				return true;
			}
			
			return false;
			
		}
		
	/************************************************************************************/
	
	public boolean moveLeft(Unit u){
		Point uSpot = u.getLocation();
		int row =(int) uSpot.getX();
		int column =(int) uSpot.getY();
		Point nextSpot = new Point (row, column-1);
		
		if(checkAvailable(nextSpot))
		{		
			u.setLocation(nextSpot);
			gameBoard[row][column] = ' ';
			gameBoard[row][column-1] = u.getCharRepresentation();
			return true;
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
	
	public String gameBoardToString(){
		String str = "";
		for(int i=0; i < boardWidth;i++){
			for(int j=0; j < boardHeight;j++){
				str += gameBoard[boardWidth][boardHeight];
			}
		}
		return str;
	}
	
	/************************************************************************************/
	
	public String toString(){
		String result="";
		for(int row=0; row<gameBoard.length; row++){
			for(int col=0; col<gameBoard[0].length; col++){
				if(gameBoard[row][col]!=' '){
					result+=""+gameBoard[row][col];
				}
				else{
					result+=" ";
				}
			}
			result+="\n";
		}
		return result;
	}
	
	/************************************************************************************/
	
	/**
	 * 
	 * @param currentUnit This is the unit who is currently making an attack. Will be compared to the Unit at the given point if applicable to determine whether or not they are on the same team.
	 * @param p The position in question.
	 * @return A boolean stating whether or not it is an enemy at that position.
	 */
	
	public boolean checkIfEnemy(Unit currentUnit, Point p){
		int x=(int)p.getX();
		int y=(int)p.getY();
		char charRep=currentUnit.getCharRepresentation();
		
		if (gameBoard[x][y]==' ' || gameBoard[x][y]=='#')
			return false;
		else if(charRep<='z' && charRep>='a'){
			//the character is on the team represented by lowercase chars
			if(gameBoard[x][y]>='A' || gameBoard[x][y]<='Z')
				return true;
			else 
				return false;
		}
		else{
			//the character is on the team represented by lowercase chars
			if(gameBoard[x][y]>='a' || gameBoard[x][y]<='z')
				return true;
			else 
				return false;
		}
	}
	
	/************************************************************************************/
	
	
}