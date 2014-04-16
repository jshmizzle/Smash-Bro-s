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
	private ArrayList<String> playerList;
	
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
		playerList = new ArrayList<>();
		
		//playerList.add(player1);
		//playerList.add(player2);

		
		for(int i = 0; i<gameBoard.length; i++)
			for(int j = 0; j<gameBoard[0].length; j++){
				gameBoard[i][j] = ' ';
			}
		
		gameBoard[0][boardHeight/2] = 'P';
		
		int i=0;
		for(Unit u: userUnits){
			if(u.getCharRepresentation() == 'P' || u.getCharRepresentation() == 'p'){
	
			}
			else{
				gameBoard[1][boardHeight/2-2+i] = u.getCharRepresentation();
				Point p = new Point(1, boardHeight/2-2+i);
				u.setLocation(p);
				i++;
			}
		}
		
		int j=0;
		
		gameBoard[boardHeight -1][boardHeight/2] = 'p';
		for(Unit c : compUnits){
			if(c.getCharRepresentation() == 'P' || c.getCharRepresentation() == 'p'){
				
			}
			
			else{
				gameBoard[boardHeight-2][boardWidth/2-2+j] = c.getCharRepresentation();
				Point p = new Point(boardHeight-2, boardWidth/2-2+j);
				c.setLocation(p);
				j++;
			}
		}
		
		if(currentMap==1){
			setMapOne();
		}
		if(currentMap==2){
			setMapTwo();
		}
		
	}
	private void setMapTwo() {
		// TODO Auto-generated method stub
		
	}
	private void setMapOne() {
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
	/************************************************************************************/

	public ArrayList<Unit> getUserUnits(){
		return userUnits;
	}
	
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
	
	public boolean moveUp(String client,Unit u){
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
	
	public boolean moveDown(String client,Unit u){
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

	public boolean moveRight(String client,Unit u){
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
	
	public boolean moveLeft(String client,Unit u){
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
	
	public boolean checkOpenLineOfFire(Unit u, Point p){
		
		Point thisPoint = u.getLocation();
		int thisRange = u.getAttackRange();
		int thisX = (int)thisPoint.getX();
		int thisY = (int)thisPoint.getY();
		int otherX = (int)p.getX();
		int otherY = (int)p.getY();
		boolean isOpen = true;
		Point change = new Point(0, 0);
		
		if(thisX != otherX && thisY != otherY)
			return false;
		
		if(thisX == otherX){
			if(Math.abs(thisY - otherY) > thisRange)
				return false;
			
			if(thisY > otherY)
				for(int i = otherY; i<=thisY; i++)
				{
					change.setLocation((double)thisX, (double)i);
					if(!checkAvailable(change))
					{
						isOpen = false;
						return isOpen;
					}
					
				}
			else
				for(int i = thisY; i<=otherY ; i++)
				{
					change.setLocation((double)thisX, (double)i);
					if(!checkAvailable(change))
					{
						isOpen = false;
						return isOpen;
					}
					
				}
		} //end thisX == otherX
		
		else{
			if(Math.abs(thisX - otherX) > thisRange)
				return false;
			
			if(thisX > otherX)
				for(int i = otherX; i<=thisX; i++)
				{
					change.setLocation((double)i, (double)thisY);
					if(!checkAvailable(change))
					{
						isOpen = false;
						return isOpen;
					}
				}
			else
				for(int i = thisX; i<=otherX; i++)
				{
					change.setLocation((double)i, (double)thisY);
					if(!checkAvailable(change))
					{
						isOpen = false;
						return isOpen;
					}
				}
		}
		
		return isOpen;
	}
	public void removeItem(Point p) {
		gameBoard[(int) p.getY()][(int) p.getX()] = ' '; 
	}
	
	/************************************************************************************/
	/**
	 * 
	 * @param u - The Unit that needs to be moved
	 * @param p - The location where the unit needs to be moved to
	 * @return true if it moved successfully, false otherwise
	 */
	
	public boolean setUnitToThisSpot(Unit u, Point p){
		
		int x = p.x;
		int y = p.y;
		
		if(checkAvailable(p))
		{
			gameBoard[x][y] = u.getCharRepresentation();
			u.setLocation(p);
			return true;
		}
		
		return false;
	}
	
	public void useThisItem(String client, Unit u, Item item) {
		
	}

	public void setBoard(char[][] board) {
		gameBoard = board;
		
	}
	public ArrayList<Unit> getCompUnits() {
		return compUnits;
	}
	
	/************************************************************************************/
}