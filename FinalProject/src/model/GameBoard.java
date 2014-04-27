package model;

import java.awt.Point;
import java.util.ArrayList;

public class GameBoard {

	/************************************************************************************/

	private char[][] gameBoard;
	private int boardWidth = 20;
	private int boardHeight = 20;
	private ArrayList<Unit> playerOneUnits;
	private ArrayList<Unit> playerTwoUnits;
	private char[][] terrain;
	private int currentMap;
	private int currentScenario;
	private ArrayList<String> playerList;
	private boolean userLost = false;
	private boolean compLost = false;
	private boolean compWon;
	private boolean userWon;

	/************************************************************************************/

	/**
	 * GameBoard constructor, takes a list of users units, list of comp units,
	 * map level, and a scenario. Based on the arguments given, the board will
	 * create a 2D char array representing the locations of game objects on the
	 * board. Scenario 1 is Kill the Princess, scenario 2 is Death Match,
	 * scenario 3 is (dunno yet)
	 */

	public GameBoard(ArrayList<Unit> userUnits, ArrayList<Unit> compUnits,int map, int scenario) {

		gameBoard = new char[boardWidth][boardHeight];
		terrain = new char[boardWidth][boardHeight];
		this.playerOneUnits = userUnits;
		this.playerTwoUnits = compUnits;
		currentMap = map;
		currentScenario = scenario;
		playerList = new ArrayList<>();

		 //playerList.add(player1);
		// playerList.add(player2);

		for (int i = 0; i < gameBoard.length; i++)
			for (int j = 0; j < gameBoard[0].length; j++) {
				gameBoard[i][j] = ' ';
			}

		gameBoard[0][boardHeight / 2] = 'P';

		int i = 0;
		
		  for (Unit u : compUnits) { if (u.getCharRepresentation() == 'P' ||
		  u.getCharRepresentation() == 'p') {
		  
		  } else { gameBoard[1][boardHeight / 2 - 2 + i] = u
		  .getCharRepresentation(); Point p = new Point(1, boardHeight / 2 - 2
		  + i); u.setLocation(p); i++; } }
		  
		  int j = 0;
		  
		  gameBoard[boardHeight - 1][boardHeight / 2] = 'p'; for (Unit c :
		  userUnits) { if (c.getCharRepresentation() == 'P' ||
		  c.getCharRepresentation() == 'p') {
		  
		  }
		  
		  else { gameBoard[boardHeight - 2][boardWidth / 2 - 2 + j] = c
		  .getCharRepresentation(); Point p = new Point(boardHeight - 2,
		  boardWidth / 2 - 2 + j); c.setLocation(p); j++; } }
		 

		if (currentMap == 1) {
			setMapOne();
		}
		if (currentMap == 2) {
			setMapTwo();
		}

	}

	private void setMapTwo() {
		// TODO Auto-generated method stub

	}

	private void setMapOne() {

		// rocks
		gameBoard[boardHeight / 2 - 5][boardWidth / 2 - 2] = '#';
		gameBoard[boardHeight / 2 - 4][boardWidth / 2 - 2] = '#';
		gameBoard[boardHeight / 2 - 3][boardWidth / 2 - 2] = '#';
		gameBoard[boardHeight / 2 + 5][boardWidth / 2 - 2] = '#';
		gameBoard[boardHeight / 2 + 4][boardWidth / 2 - 2] = '#';
		gameBoard[boardHeight / 2 + 3][boardWidth / 2 - 2] = '#';

		gameBoard[boardHeight / 2 - 5][boardWidth / 2 + 2] = '#';
		gameBoard[boardHeight / 2 - 4][boardWidth / 2 + 2] = '#';
		gameBoard[boardHeight / 2 - 3][boardWidth / 2 + 2] = '#';
		gameBoard[boardHeight / 2 + 5][boardWidth / 2 + 2] = '#';
		gameBoard[boardHeight / 2 + 4][boardWidth / 2 + 2] = '#';
		gameBoard[boardHeight / 2 + 3][boardWidth / 2 + 2] = '#';

		gameBoard[boardHeight / 2 - 2][boardWidth / 2 - 5] = '#';
		gameBoard[boardHeight / 2 - 2][boardWidth / 2 - 4] = '#';
		gameBoard[boardHeight / 2 - 2][boardWidth / 2 - 3] = '#';
		gameBoard[boardHeight / 2 - 2][boardWidth / 2 + 5] = '#';
		gameBoard[boardHeight / 2 - 2][boardWidth / 2 + 4] = '#';
		gameBoard[boardHeight / 2 - 2][boardWidth / 2 + 3] = '#';

		gameBoard[boardHeight / 2 + 2][boardWidth / 2 - 5] = '#';
		gameBoard[boardHeight / 2 + 2][boardWidth / 2 - 4] = '#';
		gameBoard[boardHeight / 2 + 2][boardWidth / 2 - 3] = '#';
		gameBoard[boardHeight / 2 + 2][boardWidth / 2 + 5] = '#';
		gameBoard[boardHeight / 2 + 2][boardWidth / 2 + 4] = '#';
		gameBoard[boardHeight / 2 + 2][boardWidth / 2 + 3] = '#';

		gameBoard[boardHeight / 2 - 1][0] = '#';
		gameBoard[boardHeight / 2 - 1][1] = '#';
		gameBoard[boardHeight / 2 + 1][0] = '#';
		gameBoard[boardHeight / 2 + 1][1] = '#';

		gameBoard[boardHeight / 2 - 1][boardWidth - 1] = '#';
		gameBoard[boardHeight / 2 - 1][boardWidth - 2] = '#';
		gameBoard[boardHeight / 2 + 1][boardWidth - 1] = '#';
		gameBoard[boardHeight / 2 + 1][boardWidth - 2] = '#';

		// trees
		gameBoard[boardHeight / 4][boardWidth / 4] = '!';
		gameBoard[boardHeight / 4][boardWidth - (boardWidth / 4)] = '!';
		gameBoard[boardHeight - (boardHeight / 4)][boardWidth / 4] = '!';
		gameBoard[boardHeight - (boardHeight / 4)][boardWidth
				- (boardWidth / 4)] = '!';

		// items
		gameBoard[boardHeight / 2][0] = '@';
		gameBoard[boardHeight / 2][boardWidth-1] = '@';
		gameBoard[boardHeight/2][boardWidth /2] = '@';

		gameBoard[0][boardWidth / 2] = 'p';
		
		int i = 0;
		for (Unit u : playerTwoUnits) {
			if (u.getCharRepresentation() == 'P'
					|| u.getCharRepresentation() == 'p'){
				u.setLocation(new Point(0,boardWidth / 2));
			}
			
			else {
				gameBoard[1][boardHeight / 2 - 2 + i] = u
						.getCharRepresentation();
				Point p = new Point(1, boardWidth / 2 - 2 + i);
				u.setLocation(p);
				i++;
			}

		}

		int j = 0;

		gameBoard[boardHeight - 1][boardHeight / 2] = 'P';
		for (Unit c : playerOneUnits) {
			if (c.getCharRepresentation() == 'P'|| c.getCharRepresentation() == 'p') {
				c.setLocation(new Point(0,boardHeight / 2));
			}

			else {
				gameBoard[boardHeight - 2][boardWidth / 2 - 2 + j] = c
						.getCharRepresentation();
				Point p = new Point(boardHeight - 2, boardWidth / 2 - 2 + j);
				c.setLocation(p);
				j++;
			}
		}

	}

	/************************************************************************************/

	public ArrayList<Unit> getPlayerOneUnits() {
		return playerOneUnits;
	}
	
	public ArrayList<Unit> getPlayerTwoUnits() {
		return playerTwoUnits;
	}
	
	public boolean checkAvailable(Point point) {

		int x = (int) point.getX();
		int y = (int) point.getY();

		if (x >= boardWidth || x < 0)
			return false;

		if (y >= boardHeight || y < 0)
			return false;

		if (gameBoard[(int) point.getX()][(int) point.getY()] == ' ' || gameBoard[(int) point.getX()][(int) point.getY()] == '@')
			return true;

		return false;
	}

	/************************************************************************************/

	public char inspectPosition(Point p) {
		return gameBoard[(int) p.getX()][(int) p.getY()];
	}

	/************************************************************************************/

	public boolean moveUp(String client, Unit u) {
		Point uSpot = u.getLocation();
		int row = (int) uSpot.getX();
		int column = (int) uSpot.getY();
		Point nextSpot = new Point(row - 1, column);

		if (checkAvailable(nextSpot)) {
			u.setLocation(nextSpot);
			gameBoard[row][column] = ' ';
			gameBoard[row - 1][column] = u.getCharRepresentation();
			return true;
		}

		return false;
	}

	/************************************************************************************/

	public boolean moveDown(String client, Unit u) {
		Point uSpot = u.getLocation();
		int row = (int) uSpot.getX();
		int column = (int) uSpot.getY();
		Point nextSpot = new Point(row + 1, column);

		if (checkAvailable(nextSpot)) {
			u.setLocation(nextSpot);
			gameBoard[row][column] = ' ';
			gameBoard[row + 1][column] = u.getCharRepresentation();
			return true;
		}

		return false;
	}

	/************************************************************************************/

	public boolean moveRight(String client, Unit u) {
		Point uSpot = u.getLocation();
		int row = (int) uSpot.getX();
		int column = (int) uSpot.getY();
		Point nextSpot = new Point(row, column + 1);

		if (checkAvailable(nextSpot)) {
			u.setLocation(nextSpot);
			gameBoard[row][column] = ' ';
			gameBoard[row][column + 1] = u.getCharRepresentation();
			return true;
		}

		return false;

	}

	/************************************************************************************/

	public boolean moveLeft(String client, Unit u) {
		Point uSpot = u.getLocation();
		int row = (int) uSpot.getX();
		int column = (int) uSpot.getY();
		Point nextSpot = new Point(row, column - 1);

		if (checkAvailable(nextSpot)) {
			u.setLocation(nextSpot);
			gameBoard[row][column] = ' ';
			gameBoard[row][column - 1] = u.getCharRepresentation();
			return true;
		}

		return false;

	}

	/************************************************************************************/

	private void updateObservers() {

	}

	/************************************************************************************/

	public char[][] getGameBoard() {
		return gameBoard;
	}

	/************************************************************************************/

	public int getBoardWidth() {
		return boardWidth;
	}

	/************************************************************************************/

	public int getBoardHeight() {
		return boardHeight;
	}

	/************************************************************************************/

	public char[][] getTerrain() {
		return terrain;
	}

	/************************************************************************************/

	public String toString() {
		String result = "";
		for (int row = 0; row < gameBoard.length; row++) {
			for (int col = 0; col < gameBoard[0].length; col++) {
				if (gameBoard[row][col] != ' ') {
					result += "" + gameBoard[row][col];
				} else {
					result += " ";
				}
			}
			result += "\n";
		}
		return result;
	}

	/************************************************************************************/

	/**
	 * 
	 * @param currentUnit
	 *            This is the unit who is currently making an attack. Will be
	 *            compared to the Unit at the given point if applicable to
	 *            determine whether or not they are on the same team.
	 * @param p
	 *            The position in question.
	 * @return A boolean stating whether or not it is an enemy at that position.
	 */

	public boolean checkIfEnemy(Unit currentUnit, Point p) {
		int x = (int) p.getX();
		int y = (int) p.getY();
		char charRep = currentUnit.getCharRepresentation();

		if (gameBoard[x][y] == ' ' || gameBoard[x][y] == '#' || gameBoard[x][y] == '@'
				|| gameBoard[x][y] == '!')
			return false;
		else if (charRep <= 'z' && charRep >= 'a') {
			// the character is on the team represented by lowercase chars
			if (gameBoard[x][y] >= 'A' && gameBoard[x][y] <= 'Z')
				return true;
			else
				return false;
		} else {
			// the character is on the team represented by lowercase chars
			if (gameBoard[x][y] >= 'a' && gameBoard[x][y] <= 'z')
				return true;
			else
				return false;
		}
	}

	/************************************************************************************/

	public boolean checkOpenLineOfFire(Unit u, Point p) {

		Point thisPoint = u.getLocation();
		int thisRange = u.getAttackRange();
		int thisX = (int) thisPoint.getX();
		int thisY = (int) thisPoint.getY();
		int otherX = (int) p.getX();
		int otherY = (int) p.getY();
		boolean isOpen = true;
		Point change = new Point(0, 0);

		if (!(thisX == otherX || thisY == otherY))
			return false;

		if (thisX == otherX) {
			if (Math.abs(thisY - otherY) > thisRange)
				return false;


			if (thisY > otherY){
				if (thisY-otherY==1)
					return true;
				for (int i = thisY-1; i > otherY; i--) {
					change.setLocation((double) thisX, (double) i);
					if (!checkAvailable(change)) {
						isOpen = false;
						return isOpen;
					}

				}
			}
			else{
				if(otherY-thisY==1)
					return true;
				for (int i = thisY+1; i < otherY; i++) {
					change.setLocation((double) thisX, (double) i);
					if (!checkAvailable(change)) {
						isOpen = false;
						return isOpen;
					}

				}
			}
		} // end thisX == otherX

		else {
			if (Math.abs(thisX - otherX) > thisRange)
				return false;

			if(thisX-otherX==1){
				return true;
			}
			else if (thisX > otherX)
				for (int i = thisX-1; i > otherX; i--) {
					change.setLocation((double) i, (double) thisY);
					if (!checkAvailable(change)) {
						isOpen = false;
						return isOpen;
					}
				}
			else
				if(otherX-thisX==1){
					return true;
				}
				for (int i = thisX+1; i < otherX; i++) {
					change.setLocation((double) i, (double) thisY);
					if (!checkAvailable(change)) {
						isOpen = false;
						return isOpen;
					}
				}
		}

		return isOpen;
	}

	public void removeItem(Point p) {
		if(gameBoard[(int) p.getY()][(int) p.getX()] == '@')
			gameBoard[(int) p.getY()][(int) p.getX()] = ' ';
	}

	/************************************************************************************/
	/**
	 * 
	 * @param u
	 *            - The Unit that needs to be moved
	 * @param p
	 *            - The location where the unit needs to be moved to
	 * @return true if it moved successfully, false otherwise
	 */

	public boolean setUnitToThisSpot(Unit u, Point p) {

		int x = p.x;
		int y = p.y;
		int prevX=u.getLocation().x;
		int prevY=u.getLocation().y;

		if (checkAvailable(p)) {
			gameBoard[prevX][prevY]=' ';
			gameBoard[x][y] = u.getCharRepresentation();
			u.setLocation(p);
			return true;
		}

		return false;
	}

	public void useThisItem(String client, Unit u, Item item) {
		if (u.isAlive()) {
			u.useItem(item);
		}
	}

	public void setBoard(char[][] board) {
		gameBoard = board;

	}

	public void unitDied(Unit u) {
		gameBoard[(int) u.getLocation().getY()][(int) u.getLocation().getX()] = ' ';
	}

	public void attackUnit(Unit from, Unit to) {
		from.attack(to);
		if (to.getHealth() == 0) {
			unitDied(to);
		}
	}

	public void resetCompMoves() {
		for (Unit u : playerTwoUnits) {
			u.setMovesLeft(u.getDistance());
		}
	}

	public void resetUserMoves() {
		for (Unit u : playerOneUnits) {
			u.setMovesLeft(u.getDistance());
		}
	}

	/**
	 * This method checks if there are no more units alive. If a team has no
	 * units alive, it's lost boolean is set to true, the other teams' won
	 * boolean is set to true, and this method returns true. If current scenario
	 * is "Kill the Princess," it checks if either teams' princess is dead, then
	 * does likewise;
	 * 
	 * @return
	 */

	public boolean gameOver() {
		if (currentScenario == 1) {
				if (getPlayerOneUnits().get(0).getHealth()<=0) {
					userLost = true;
					compWon = true;
					return true;
				}
			
				if (getPlayerTwoUnits().get(0).getHealth()<=0) {
					compLost = true;
					userWon = true;
					return true;
				}
		}
		userLost = checkIfUserLost();
		compLost = checkIfCompLost();

		if (userLost) {
			compWon = true;
			return true;
		}
		if (compLost) {
			userWon = true;
			return true;
		}

		return false;
	}

	private boolean checkIfCompLost() {
		for (Unit unit : playerTwoUnits) {
			if (unit.getHealth() > 0) {
				return false;
			}
		}
		return true;
	}

	private boolean checkIfUserLost() {
		for (Unit unit : playerOneUnits) {
			if (unit.getHealth() > 0) {
				return false;
			}
		}
		return true;
	}

	public boolean userLost() {
		return userLost;
	}

	public boolean compLost() {
		return compLost;
	}

	public boolean compWon() {
		return compWon;
	}

	public boolean userWon() {
		return userWon;
	}

	/*
	 * private Point[] shortestPath = null;; private int shortestLength;
	 * 
	 * // Method that finds the shortest path from one point to another public
	 * Point[] shortestPath(Point start, Point end) { Point[] path = new
	 * Point[gameBoard.length * gameBoard[0].length]; int tempPath[] = new
	 * int[gameBoard.length * gameBoard[0].length]; // used to help track paths
	 * int length = 0; for(int i=0; i<tempPath.length; i++){ tempPath[i]=-1; }
	 * // initialize both arrays shortestPath = new Point [gameBoard.length *
	 * gameBoard[0].length]; path = null; shortestLength=30;
	 * 
	 * findShortestPath(start.x, start.y, end.x, end.y, path, tempPath, length);
	 * return shortestPath; }
	 * 
	 * private void findShortestPath(int row, int col, int endRow, int endCol,
	 * Point[] path, int[] tempPath, int length) { // checks to see if the space
	 * exists and if it is open to move to if (row < 0 || row > gameBoard.length
	 * || col < 0 || col > gameBoard[0].length || !checkAvailable(new Point(row,
	 * col))) { return; // returns if space cant be moved to } // check to see
	 * if we have already tried their for our path if (alreadyTried(row, col,
	 * tempPath, length)) { return; // return if already visited }
	 * 
	 * Point currentPath[] = new Point[length + 1]; // makes the size of the //
	 * current path bigger int currentTempPath[] = new int[length + 1]; if
	 * (length > 0) { // if length is greater then 0, get the old path and //
	 * put it into the new one for (int i = 0; i < length; i++) { currentPath[i]
	 * = path[i]; currentTempPath[i] = tempPath[i]; } }
	 * 
	 * currentPath[length] = new Point(row, col); currentTempPath[length] = row
	 * + col * 1000; // give spot a specific int
	 * 
	 * if (row == endRow && col == endCol) { if (length <= shortestLength) {
	 * shortestLength = length; shortestPath=new Point[length]; for (int i = 0;
	 * i < currentPath.length-1; i++) { shortestPath[i] = currentPath[i];
	 * 
	 * } return; } }
	 * 
	 * if(length>shortestLength){ return; }
	 * 
	 * //try to speed up process, logically determining which recursion to try
	 * first if(row<endRow){ findShortestPath(row + 1, col, endRow, endCol,
	 * currentPath, currentTempPath, length+1); findShortestPath(row - 1, col,
	 * endRow, endCol, currentPath, currentTempPath, length+1 ); } else{
	 * findShortestPath(row - 1, col, endRow, endCol, currentPath,
	 * currentTempPath, length+1 ); findShortestPath(row + 1, col, endRow,
	 * endCol, currentPath, currentTempPath, length+1); }
	 * 
	 * if(col <endCol){ findShortestPath(row, col + 1, endRow, endCol,
	 * currentPath, currentTempPath, length+1 ); findShortestPath(row, col - 1,
	 * endRow, endCol, currentPath, currentTempPath, length+1 ); } else{
	 * findShortestPath(row, col - 1, endRow, endCol, currentPath,
	 * currentTempPath, length+1 ); findShortestPath(row, col + 1, endRow,
	 * endCol, currentPath, currentTempPath, length+1 ); }
	 * 
	 * }
	 * 
	 * // returns whether or not the spot has been gone to already on the path
	 * private boolean alreadyTried(int row, int col, int[] tempPath, int
	 * length) { // TODO Auto-generated method stub int num = row + col * 1000;
	 * // this number gives each square on the maze // a unique number // go
	 * through path to make sure this specific pair is not in the list for (int
	 * i = 0; i < tempPath.length; i++) { if (tempPath[i] == num) { return true;
	 * } } return false; }
	 */

	// new faster algorithm
	public ArrayList<Point> findShortestPath(Point start, Point end) {
		Tiles tiles[][] = new Tiles[gameBoard.length][gameBoard[0].length];

		// sets the status of each space on the board
		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = 0; j < gameBoard[0].length; j++) {
				tiles[i][j] = new Tiles();
			}
		}
		tiles[start.x][start.y].distance = 0;
		tiles = findDistance(tiles, start);
		int currRow = start.x;

		int currCol = start.y;
		boolean marked = true;
		while (marked && !tiles[end.x][end.y].status.equals("marked")) {
			marked = false;
			for (int i = 0; i < gameBoard.length; i++) {
				for (int j = 0; j < gameBoard[0].length; j++) {
					if (tiles[i][j].status.equals("fringe")) {
						tiles = findDistance(tiles, new Point(i, j));
					} else if (!tiles[i][j].status.equals("marked")) {
						marked = true;
					}
				}
			}
		}
		return tiles[end.x][end.y].points;
	}

	private Tiles[][] findDistance(Tiles[][] tiles, Point p) {
		// TODO Auto-generated method stub
		tiles[p.x][p.y].status = "marked";

		tiles[p.x][p.y].points.add(new Point(p.x, p.y));
		if (p.x + 1 >= 0 && p.y >= 0 && p.x + 1 < gameBoard.length
				&& p.y < gameBoard[0].length && checkAvailable(new Point(p.x+1, p.y))) {
			if (tiles[p.x + 1][p.y].status.equals("unmarked"))
				tiles[p.x + 1][p.y].status = "fringe";
			if (tiles[p.x][p.y].distance + 1 < tiles[p.x + 1][p.y].distance) {
				tiles[p.x + 1][p.y].distance = tiles[p.x][p.y].distance + 1;
				tiles[p.x + 1][p.y].points.clear();
				for (int i = 0; i < tiles[p.x][p.y].points.size(); i++) {
					tiles[p.x + 1][p.y].points.add(tiles[p.x][p.y].points
							.get(i));
				}
				// tiles[p.x + 1][p.y].points.add(new Point(p.x + 1, p.y));
			}
		}
		if (p.x - 1 >= 0 && p.y >= 0 && p.x - 1 < gameBoard.length
				&& p.y < gameBoard[0].length && checkAvailable(new Point(p.x-1, p.y))) {
			if (tiles[p.x - 1][p.y].status.equals("unmarked"))
				tiles[p.x - 1][p.y].status = "fringe";
			if (tiles[p.x][p.y].distance + 1 < tiles[p.x - 1][p.y].distance) {
				tiles[p.x - 1][p.y].distance = tiles[p.x][p.y].distance + 1;
				tiles[p.x - 1][p.y].points.clear();
				for (int i = 0; i < tiles[p.x][p.y].points.size(); i++) {
					tiles[p.x - 1][p.y].points.add(tiles[p.x][p.y].points
							.get(i));
				}
				// tiles[p.x - 1][p.y].points.add(new Point(p.x - 1, p.y));
			}
		}
		if (p.x >= 0 && p.y + 1 >= 0 && p.x < gameBoard.length
				&& p.y + 1 < gameBoard[0].length && checkAvailable(new Point(p.x, p.y+1))) {
			if (tiles[p.x][p.y + 1].status.equals("unmarked"))
				tiles[p.x][p.y + 1].status = "fringe";
			if (tiles[p.x][p.y].distance + 1 < tiles[p.x][p.y + 1].distance) {
				tiles[p.x][p.y + 1].distance = tiles[p.x][p.y].distance + 1;
				tiles[p.x][p.y + 1].points.clear();
				for (int i = 0; i < tiles[p.x][p.y].points.size(); i++) {
					tiles[p.x][p.y + 1].points.add(tiles[p.x][p.y].points
							.get(i));

				}
				// tiles[p.x][p.y+1].points.add(new Point(p.x, p.y+1));
			}
		}
		if (p.x >= 0 && p.y - 1 >= 0 && p.x < gameBoard.length
				&& p.y - 1 < gameBoard[0].length && checkAvailable(new Point(p.x, p.y-1))) {
			if (tiles[p.x][p.y - 1].status.equals("unmarked"))
				tiles[p.x][p.y - 1].status = "fringe";
			if (tiles[p.x][p.y].distance + 1 < tiles[p.x][p.y - 1].distance) {
				tiles[p.x][p.y - 1].distance = tiles[p.x][p.y].distance + 1;
				tiles[p.x][p.y - 1].points.clear();
				for (int i = 0; i < tiles[p.x][p.y].points.size(); i++) {
					tiles[p.x][p.y - 1].points.add(tiles[p.x][p.y].points
							.get(i));

				}
			}
		}
		return tiles;
	}

	public ArrayList<Point> findAttackRange(Point p, int range) {
		Tiles tiles[][] = new Tiles[gameBoard.length][gameBoard[0].length];
		// sets the status of each space on the board
		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = 0; j < gameBoard[0].length; j++) {
				tiles[i][j] = new Tiles();
			}
		}
		tiles[p.x][p.y].distance = 0;
		tiles = findRange(p, tiles);
		boolean marked = true;
		
		//check to see which square to use as a reference
		int l,k;
		int row1=19-p.x;
		int row2=p.x-0;
		int col1=19-p.x;
		int col2=p.x-0;
		if(row1>row2)
			l=19;
		else
			l=0;
		if(col1>col2)
			k=19;
		else
			k=0;
	
		while (marked && !tiles[l][k].status.equals("marked")) {
			marked = false;
			for (int i = 0; i < gameBoard.length; i++) {
				for (int j = 0; j < gameBoard[0].length; j++) {
					if (tiles[i][j].status.equals("fringe")) {
						tiles = findRange(new Point(i, j), tiles);

					} else if (tiles[i][j].status.equals("unmarked")) {
						marked = true;
					}
				}
			}
		}
		ArrayList<Point> attRange = new ArrayList<>();
		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = 0; j < gameBoard[0].length; j++) {
				if (tiles[i][j].distance <= range
						&& checkAvailable(new Point(i, j))){
					attRange.add(new Point(i, j));
				}
			}
		}
		return attRange;
	}

	public void startNewGame() {

	}

	/************************************************************************************/

	private Tiles[][] findRange(Point p, Tiles[][] tiles) {
		tiles[p.x][p.y].status = "marked";
		tiles[p.x][p.y].points.add(new Point(p.x, p.y));
		if (p.x + 1 >= 0 && p.y >= 0 && p.x + 1 < gameBoard.length
				&& p.y < gameBoard[0].length) {
			/*
			 * if (!checkAvailable(new Point(p.x + 1, p.y))) { tiles =
			 * inWay(tiles, new Point(p.x + 1, p.y), 'd'); } else {
			 */
			if (tiles[p.x + 1][p.y].status.equals("unmarked"))
				tiles[p.x + 1][p.y].status = "fringe";
			if (tiles[p.x][p.y].distance + 1 < tiles[p.x + 1][p.y].distance) {
				tiles[p.x + 1][p.y].distance = tiles[p.x][p.y].distance + 1;
				tiles[p.x + 1][p.y].points.clear();
				for (int i = 0; i < tiles[p.x][p.y].points.size(); i++) {
					tiles[p.x + 1][p.y].points.add(tiles[p.x][p.y].points
							.get(i));
				}
				// }
			}
		}
		if (p.x - 1 >= 0 && p.y >= 0 && p.x - 1 < gameBoard.length
				&& p.y < gameBoard[0].length
				&& checkAvailable(new Point(p.x - 1, p.y))) {
			/*
			 * if(!checkAvailable(new Point(p.x - 1, p.y))) { tiles =
			 * inWay(tiles, new Point(p.x - 1, p.y), 'u'); } else {
			 */
			if (tiles[p.x - 1][p.y].status.equals("unmarked"))
				tiles[p.x - 1][p.y].status = "fringe";
			if (tiles[p.x][p.y].distance + 1 < tiles[p.x - 1][p.y].distance) {
				tiles[p.x - 1][p.y].distance = tiles[p.x][p.y].distance + 1;
				tiles[p.x - 1][p.y].points.clear();
				for (int i = 0; i < tiles[p.x][p.y].points.size(); i++) {
					tiles[p.x - 1][p.y].points.add(tiles[p.x][p.y].points
							.get(i));
				}
				// }
			}
		}
		if (p.x >= 0 && p.y + 1 >= 0 && p.x < gameBoard.length
				&& p.y + 1 < gameBoard[0].length
				&& checkAvailable(new Point(p.x, p.y + 1))) {
			/*
			 * if (!checkAvailable(new Point(p.x, p.y + 1))) { tiles =
			 * inWay(tiles, new Point(p.x, p.y + 1), 'r'); } else {
			 */
			if (tiles[p.x][p.y + 1].status.equals("unmarked"))
				tiles[p.x][p.y + 1].status = "fringe";
			if (tiles[p.x][p.y].distance + 1 < tiles[p.x][p.y + 1].distance) {
				tiles[p.x][p.y + 1].distance = tiles[p.x][p.y].distance + 1;
				tiles[p.x][p.y + 1].points.clear();
				for (int i = 0; i < tiles[p.x][p.y].points.size(); i++) {
					tiles[p.x][p.y + 1].points.add(tiles[p.x][p.y].points
							.get(i));

				}
				// }
			}
		}
		if (p.x >= 0 && p.y - 1 >= 0 && p.x < gameBoard.length
				&& p.y - 1 < gameBoard[0].length
				&& checkAvailable(new Point(p.x, p.y - 1))) {
			/*
			 * if (!checkAvailable(new Point(p.x, p.y - 1))) { tiles =
			 * inWay(tiles, new Point(p.x, p.y - 1), 'r'); } else {
			 */
			if (tiles[p.x][p.y - 1].status.equals("unmarked"))
				tiles[p.x][p.y - 1].status = "fringe";
			if (tiles[p.x][p.y].distance + 1 < tiles[p.x][p.y - 1].distance) {
				tiles[p.x][p.y - 1].distance = tiles[p.x][p.y].distance + 1;
				tiles[p.x][p.y - 1].points.clear();
				for (int i = 0; i < tiles[p.x][p.y].points.size(); i++) {
					tiles[p.x][p.y - 1].points.add(tiles[p.x][p.y].points
							.get(i));

				}
			}
			// }
		}
		return tiles;
	}

	// gets rid of objects that are not in range
	private Tiles[][] inWay(Tiles[][] tiles, Point p, char c) {
		tiles[p.x][p.y].open = "blocked";

		if (c == 'u') {
			for (int i = p.x; i >= 0; i--) {
				tiles[i][p.y].open = "blocked";
				for (int j = (p.y - (p.x - i)); j < (p.y + (p.x - i)); j++) {
					if (i >= 0 && j >= 0 && i < gameBoard.length
							&& j < gameBoard[0].length)
						tiles[i][j].open = "blocked";
				}
			}
		} else if (c == 'd') {
			for (int i = p.x; i < gameBoard.length; i++) {
				tiles[i][p.y].open = "blocked";
				for (int j = (p.y - (i - p.x)); j < (p.y + (i - p.x)); j++) {
					if (i >= 0 && j >= 0 && i < gameBoard.length
							&& j < gameBoard[0].length)
						tiles[i][j].open = "blocked";
				}
			}
		} else if (c == 'r') {
			for (int i = p.y; i < gameBoard[0].length; i++) {
				tiles[p.x][i].open = "blocked";
				for (int j = (p.x - (i - p.y)); j < (p.x + (i - p.y)); j++) {
					if (i >= 0 && j >= 0 && i < gameBoard.length
							&& j < gameBoard[0].length)
						tiles[j][i].open = "blocked";
				}
			}
		} else {
			for (int i = p.y; i >= 0; i--) {
				tiles[p.x][i].open = "blocked";
				for (int j = (p.x - (p.y - i)); j < (p.x + (p.y - i)); j++) {
					if (i >= 0 && j >= 0 && i < gameBoard.length
							&& j < gameBoard[0].length)
						tiles[j][i].open = "blocked";
				}
			}
		}
		return tiles;
	}
}