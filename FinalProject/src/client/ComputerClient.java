package client;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.GameBoard;
import model.Goku;
import model.Item;
import model.Link;
import model.Map;
import model.Mario;
import model.MegaMan;
import model.Potion;
import model.Princess;
import model.Rage;
import model.Scenario;
import model.Shield;
import model.Sneakers;
import model.Sniper;
import model.Sonic;
import model.Unit;
import GUI.CharacterSelectPanel;
import GUI.GameState;
import GUI.MainGamePanel;
import GUI.MainMenuPanel;
import command.Command;
import command.EndTurnCommand;
import command.SetUserUnits;
import command.UnitAttackCommand;
import command.UnitMovedCommand;
/**
 * ComputerClient is meant for computer AI only. It is used only in a 
 * single player game, and connects to the server and receives commands 
 * needed to make the necessary changes client-side.
 * @author The Other Guys
 */
public class ComputerClient extends JFrame implements Client {

	private String host, userName;
	private int port = 0;
	private int unitIndex;
	private Socket server;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private MainGamePanel gamePanel;
	private GameBoard currentBoard;
	private boolean playingAlready = false;
	private boolean myTurn = false;
	private boolean moving = true;
	private ArrayList<Unit> playerUnits;
	private ArrayList<Unit> compUnits;
	private boolean isHost = false;
	private Scenario gameType = null;
	private Map map = null;
	private ArrayList<Item> compItemList=new ArrayList<>(), playerItemList=new ArrayList<>();

	public static void main(String[] args) {
		// try {
		// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		ComputerClient client = new ComputerClient();
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public ComputerClient() {
		askUserForInfo();// now the client has been logged into the server'

		ComputerServerHandler handler = new ComputerServerHandler(this,
				inputStream);
		Thread t = new Thread(handler);
		t.start();
	}

	private void askUserForInfo() {
		host = "localhost";
		while (port == 0) {
			try {
				port = 9001;
				server = new Socket(host, port);
				inputStream = new ObjectInputStream(server.getInputStream());
				outputStream = new ObjectOutputStream(server.getOutputStream());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"Not a valid port: enter a number");
				port = 0;
			}
		}
		try {

			String serverAccepted = "reject";
			while (serverAccepted.equals("reject")) {
				userName = "Computer";
				outputStream.writeObject(userName);
				serverAccepted = (String) inputStream.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new unit list for the computer.
	 * Called when a new AI team needs to be created.
	 */
	private void chooseRandomCompUnits() {
		// initialize the units and the GameBoard
		compUnits = new ArrayList<Unit>();

		ArrayList<Unit> temp = new ArrayList<>();

		Random rand = new Random();
		if (gameType == Scenario.Princess) {
			Princess p = new Princess('p');
			temp.add(p);
		}

		// initialize random computer units
		for (int i = 0; i < 5; i++) {
			int num = rand.nextInt(5);
			if (num == 0) {
				temp.add(new Link('l'));
			} else if (num == 1) {
				temp.add(new Goku('g'));
			} else if (num == 2) {
				temp.add(new MegaMan('m'));
			} else if (num == 3) {
				temp.add(new Mario('w'));
			} else {
				temp.add(new Sonic('s'));
			}
		}

		SetUserUnits command = new SetUserUnits(userName, temp);
		// send the command that sets the user units for computer and the user
		try {
			outputStream.writeObject(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * This method gets called from MapAndScenarioSelected command when the host
	 * has chosen which map and scenario he wants.
	 * @param source = user name
	 * @param map = map selected
	 * @param scenario = scenario selected
	 */
	public void setMapAndScenario(String source, Map map, Scenario scenario) {
		this.map = map;
		gameType = scenario;
	}
	/**
	 * This method gets called from the GUI after the player has selected his team.
	 * It places the unit list in the variables for this class, then moves to the
	 * next panel in the GUI.
	 * @param source = client name
	 * @param userUnits = arraylist of units
	 */
	public void setUserUnits(String source, ArrayList<Unit> userUnits) {
		if (userName.equals(source)) {
			compUnits = userUnits;
			currentBoard = new GameBoard(playerUnits, compUnits, map, gameType);
			for (int i = 0; i < compUnits.size(); i++) {
				// System.out.println(compUnits.get(i).getName()+" "+compUnits.get(i).getLocation()+" yah");
			}
		} else {
			playerUnits = userUnits;
			chooseRandomCompUnits();
		}
	}
	
	/**
	 * This method gets called when the user decides to 
	 * load a previous game. It gets passed all the information
	 * needed for the AI to pick up right where it left off.
	 * @param currentBoard = current GameBoard object
	 * @param itemList = human user's item list
	 * @param opponentItemList = computer's item list
	 */
	public void loadSavedComputer(GameBoard currentBoard,
			ArrayList<Item> itemList, ArrayList<Item> opponentItemList) {
		
		this.currentBoard = currentBoard;
		playerItemList = itemList;
		compItemList = opponentItemList;
		map = currentBoard.getMap();
		gameType = currentBoard.getScenario();
		playerUnits = currentBoard.getPlayerOneUnits();
		compUnits = currentBoard.getPlayerTwoUnits();		
	}

	/*
	 * private void initializeFrame() { initializeGameBoard();
	 * 
	 * //gamePanel = new MainGamePanel("computer",currentBoard, outputStream);
	 * //currentPanel = gamePanel; this.add(currentPanel).setVisible(true);
	 * this.pack(); this.setVisible(true); }
	 */
	private void update() {
		this.gamePanel.update(currentBoard);
	}

	public void createGameBoard(ArrayList<Unit> userUnits,
			ArrayList<Unit> compUnits, Map map, Scenario scenario) {
		currentBoard = new GameBoard(userUnits, compUnits, map, scenario);
		playingAlready = true;
	}
	public ArrayList <Item> getItemList(){
		return null;
	}

	/*
	 * public void useItem(String client, Unit u, Item item) {
	 * currentBoard.useThisItem(client, u, item); }
	 */
	/**
	 * This method gets called when a unit has been attacked. It makes the
	 * proper adjustments in health based on which unit attacked, and which unit
	 * received the damage.
	 * 
	 * @param client
	 *            = user name
	 * @param fromIndex
	 *            = indice of unit in the unit list attacking from
	 * @param toIndex
	 *            = indice of unit in the unit list attacking
	 */
	public void attackUnit(String client, int fromIndex, int toIndex) {
		if (client.equals(userName)) {
			if (isHost)
				currentBoard.attackUnit(playerUnits.get(fromIndex),
						compUnits.get(toIndex));
			// System.out.println(currentBoard.getCompUnits().get(2).getHealth());
			else
				currentBoard.attackUnit(compUnits.get(fromIndex),
						playerUnits.get(toIndex));
		} else {// not coming from me
			if (isHost) {
				currentBoard.attackUnit(compUnits.get(fromIndex),
						playerUnits.get(toIndex));
			} else
				currentBoard.attackUnit(playerUnits.get(fromIndex),
						compUnits.get(toIndex));
		}
	}
	/**
	 * This method gets called from EndTurnCommand when the user hits the "e" key
	 * on the keyboard and ends the current turn, allowing
	 * either the computer client to take its turn, or the 
	 * other human user.
	 * @param client = user name
	 */
	public void endTurn(String client) {
		if (client.equals(userName)) {
			myTurn = false;
			//System.out.println(currentBoard.g());
		} else {
			myTurn = true;
			currentBoard.resetPlayerTwoMoves();
			executeTurn();
		}
	}
	/**
	 * This method is called when the human player ends his turn.
	 * It will decide what to do based on the scenario.
	 */
	private void executeTurn() {
		if (gameType == Scenario.Princess)
			princessTurn();
		else
			meleTurn();
		//sendEndTurnCommand();
	}

	public void princessTurn() {
		unitIndex = 0;
		moveTurnPrincess();
	}

	public void meleTurn() {
		hasAttacked=false;
		unitIndex = 0;
		moveTurnMele();
	}



	private void moveTurnPrincess() {
		ArrayList<Unit> compUnits = new ArrayList<>();
		compUnits = currentBoard.getPlayerTwoUnits();
		Point princess = null;
		int i = unitIndex;
		/*
		 * for (int j = 0; j < currentBoard.getGameBoard().length; j++) { for
		 * (int k = 0; k < currentBoard.getGameBoard()[0].length; k++) { if
		 * (currentBoard.getGameBoard()[j][k] == 'p') { princess = new Point(j,
		 * k); System.out.println(princess); break; } } }
		 */
		if(i>5){
			attackTurnPrincess();
		}
		else{
		Unit u = compUnits.get(i);

		if (i == 3 || !u.isAlive() || u.getName().equals("Princess")) {
			unitIndex++;
			moveTurnPrincess();
		} else {
			ArrayList<Point> path = new ArrayList<>();
			ArrayList<Point> moves = new ArrayList<>();

			// System.out.println(u.getName()+" "+u.getMovesLeft());
			Random random = new Random();
			boolean yes = true;
			int xPoint = 0;
			int yPoint = 0;
			int userPointX = 0;
			int userPointY = 0;
			int z = 0;
			// while (yes){
			// System.out.println(compUnits.get(i).getName()+" moves left "+compUnits.get(i).getMovesLeft());
			// System.out.println(compUnits.get(i).getName()+" at "+compUnits.get(i).getLocation());

			if (u.getName().equals("Sonic")) {
				// go toward princess if path to her is open
				if (u.getLocation().equals(new Point(18, 10))) {
					xPoint = 0;
					yPoint = 0;
				} else if (currentBoard.checkAvailable(new Point(18, 10))) {
					xPoint = 18;
					yPoint = 10;
					userPointX = 19;
					userPointY = 10;
				} else {
					// else run away!!
					int hello = 0;
					while (yes && hello < 10) {
						hello++;
						// move from Q1 to Q2 or Q4
						if (currentBoard.inQuadrantOne(u)) {
							xPoint = currentBoard.getBoardWidth()
									/ 2
									+ random.nextInt(currentBoard
											.getBoardWidth() / 2 - 1);
							yPoint = random.nextInt(currentBoard
									.getBoardHeight() - 1);
							if (currentBoard.checkAvailable(new Point(xPoint,
									yPoint))) {
								yes = false;
							}
						}
						// move from Q2 to Q1 or Q3
						if (currentBoard.inQuadrantTwo(u)) {
							xPoint = random.nextInt(currentBoard
									.getBoardWidth() / 2);
							yPoint = random.nextInt(currentBoard
									.getBoardHeight() - 1);
							if (currentBoard.checkAvailable(new Point(xPoint,
									yPoint))) {
								yes = false;
							}
						}
						// move from Q3 to Q2 or Q4
						if (currentBoard.inQuadrantThree(u)) {
							xPoint = currentBoard.getBoardWidth()
									/ 2
									+ random.nextInt(currentBoard
											.getBoardWidth() / 2 - 1);
							yPoint = random.nextInt(currentBoard
									.getBoardHeight() - 1);
							if (currentBoard.checkAvailable(new Point(xPoint,
									yPoint))) {
								yes = false;
							}
						}
						// move from Q4 to Q1 or Q3
						if (currentBoard.inQuadrantFour(u)) {
							xPoint = random.nextInt(currentBoard
									.getBoardWidth() / 2);
							yPoint = random.nextInt(currentBoard
									.getBoardHeight() - 1);
							if (currentBoard.checkAvailable(new Point(xPoint,
									yPoint))) {
								yes = false;
							}
						}
					}
				}
			}
			if (u.getName().equals("MegaMan")) {
				// go towards enemies, attack
				// for(int x=0; x<currentBoard.getBoardHeight(); x++){
				// for(int y=0; y<currentBoard.getBoardWidth(); y++){
				for (int x = (int) u.getLocation().getX()
						- (u.getDistance() + u.getAttackRange()); x < ((u
						.getDistance() + u.getAttackRange()) + (int) u
						.getLocation().getX()); x++) {
					for (int y = (int) u.getLocation().getY()
							- (u.getDistance() + u.getAttackRange()); y < (u
							.getDistance() + u.getAttackRange())
							+ (int) u.getLocation().getY(); y++) {
						Point currentPoint = new Point(x, y);
						if (x < 0 || x > currentBoard.getBoardWidth() - 1
								|| y < 0
								|| y > currentBoard.getBoardHeight() - 1) {
							continue;
						}
						if (currentBoard.checkIfEnemy(u, currentPoint)) {
							userPointX = x;
							userPointY = y;

							if (currentBoard
									.checkAvailable(new Point(x - 1, y))) {
								xPoint = x - 1;
								yPoint = y;
							} else if (currentBoard.checkAvailable(new Point(x,
									y - 1))) {
								xPoint = x;
								yPoint = y - 1;
							} else if (currentBoard.checkAvailable(new Point(x,
									y + 1))) {
								xPoint = x;
								yPoint = y + 1;
							} else if (currentBoard.checkAvailable(new Point(
									x + 1, y))) {
								xPoint = x + 1;
								yPoint = y;
							}
							System.out.println(u.getName());
							// System.out.println(u.getLocation());
							System.out.println(new Point(xPoint, yPoint));
							System.out.println(new Point(userPointX, userPointY));
							break;
							// move towards fist enemy seen until enemy is
							// within attackRange

							/*
							 * if(currentBoard.checkOpenLineOfFire(u,
							 * currentPoint)){ //attack if possible }
							 */
						}
					}
				}

			}
			if (u.getName().equals("Goku")) {
				boolean enemy=false;
				// go towards enemies, attack
				// for(int x=0; x<currentBoard.getBoardHeight(); x++){
				// for(int y=0; y<currentBoard.getBoardWidth(); y++){
				for (int x = (int) u.getLocation().getX()
						- (u.getDistance() + u.getAttackRange()); x < ((u
						.getDistance() + u.getAttackRange()) + (int) u
						.getLocation().getX()); x++) {
					for (int y = (int) u.getLocation().getY()
							- (u.getDistance() + u.getAttackRange()); y < (u
							.getDistance() + u.getAttackRange())
							+ (int) u.getLocation().getY(); y++) {
						Point currentPoint = new Point(x, y);
						if (x < 0 || x > currentBoard.getBoardWidth() - 1
								|| y < 0
								|| y > currentBoard.getBoardHeight() - 1) {
							continue;
						}
						if (currentBoard.checkIfEnemy(u, currentPoint)) {
							userPointX = x;
							userPointY = y;

							if (currentBoard
									.checkAvailable(new Point(x - 1, y))) {
								xPoint = x - 1;
								yPoint = y;
							} else if (currentBoard.checkAvailable(new Point(x,
									y - 1))) {
								xPoint = x;
								yPoint = y - 1;
							} else if (currentBoard.checkAvailable(new Point(x,
									y + 1))) {
								xPoint = x;
								yPoint = y + 1;
							} else if (currentBoard.checkAvailable(new Point(
									x + 1, y))) {
								xPoint = x + 1;
								yPoint = y;
							}
							enemy=true;
//							System.out.println(u.getName());
//							// System.out.println(u.getLocation());
//							System.out.println(new Point(xPoint, yPoint));
//							System.out.println(new Point(userPointX, userPointY));
							break;
							// move towards fist enemy seen until enemy is
							// within attackRange

							/*
							 * if(currentBoard.checkOpenLineOfFire(u,
							 * currentPoint)){ //attack if possible }
							 */
						}
					}
				}
				if(enemy!=true){
					userPointX=19;
					userPointY=19;
					if(currentBoard.checkAvailable(new Point(1,10))){
						xPoint=1;
						yPoint=10;
					}
					else if(currentBoard.checkAvailable(new Point(2,10))){
						xPoint=2;
						yPoint=10;
					}
					else if(currentBoard.checkAvailable(new Point(1,8))){
						xPoint=1;
						yPoint=8;
					}
					else if(currentBoard.checkAvailable(new Point(1,9))){
						xPoint=1;
						yPoint=9;
					}
					else if(currentBoard.checkAvailable(new Point(2,8))){
						xPoint=1;
						yPoint=10;
					}
					else if(currentBoard.checkAvailable(new Point(2,9))){
						xPoint=1;
						yPoint=10;
					}
				}
			}
			if (u.getName().equals("Link")) {
				boolean enemy=false;
				// go towards enemies, attack (stay on edge of range)

				// go towards enemies, attack
				// for(int x=0; x<currentBoard.getBoardHeight(); x++){
				// for(int y=0; y<currentBoard.getBoardWidth(); y++){
				for (int x = (int) u.getLocation().getX()
						- (u.getDistance() + u.getAttackRange()); x < ((u
						.getDistance() + u.getAttackRange()) + (int) u
						.getLocation().getX()); x++) {
					for (int y = (int) u.getLocation().getY()
							- (u.getDistance() + u.getAttackRange()); y < (u
							.getDistance() + u.getAttackRange())
							+ (int) u.getLocation().getY(); y++) {
						Point currentPoint = new Point(x, y);
						if (x < 0 || x > currentBoard.getBoardWidth() - 1
								|| y < 0
								|| y > currentBoard.getBoardHeight() - 1) {
							continue;
						}
						if (currentBoard.checkIfEnemy(u, currentPoint)) {
							// move towards fist enemy seen until enemy is
							// within attackRange
							userPointX = x;
							userPointY = y;

							if (currentBoard
									.checkAvailable(new Point(x - 1, y))) {
								xPoint = x - 1;
								yPoint = y;
							} else if (currentBoard.checkAvailable(new Point(x,
									y - 1))) {
								xPoint = x;
								yPoint = y - 1;
							} else if (currentBoard.checkAvailable(new Point(x,
									y + 1))) {
								xPoint = x;
								yPoint = y + 1;
							} else if (currentBoard.checkAvailable(new Point(
									x + 1, y))) {
								xPoint = x + 1;
								yPoint = y;
							}
							enemy=true;
//							System.out.println(u.getName());
//							// System.out.println(u.getLocation());
//							System.out.println(new Point(xPoint, yPoint));
//							System.out.println(new Point(userPointX, userPointY));
							break;
							/*
							 * if(currentBoard.checkOpenLineOfFire(u,
							 * currentPoint)){ //attack if possible }
							 */
						}
					}
				}
				if(enemy!=true){
					userPointX=19;
					userPointY=19;
					if(currentBoard.checkAvailable(new Point(1,10))){
						xPoint=1;
						yPoint=10;
					}
					else if(currentBoard.checkAvailable(new Point(2,10))){
						xPoint=2;
						yPoint=10;
					}
					else if(currentBoard.checkAvailable(new Point(1,8))){
						xPoint=1;
						yPoint=8;
					}
					else if(currentBoard.checkAvailable(new Point(1,9))){
						xPoint=1;
						yPoint=9;
					}
					else if(currentBoard.checkAvailable(new Point(2,8))){
						xPoint=1;
						yPoint=10;
					}
					else if(currentBoard.checkAvailable(new Point(2,9))){
						xPoint=1;
						yPoint=10;
					}
				}
			}
			if (u.getName().equals("Mario")) {
				// go towards enemies, attack
				// for(int x=0; x<currentBoard.getBoardHeight(); x++){
				// for(int y=0; y<currentBoard.getBoardWidth(); y++){
				for (int x = (int) u.getLocation().getX()
						- (u.getDistance() + u.getAttackRange()); x < ((u
						.getDistance() + u.getAttackRange()) + (int) u
						.getLocation().getX()); x++) {
					for (int y = (int) u.getLocation().getY()
							- (u.getDistance() + u.getAttackRange()); y < (u
							.getDistance() + u.getAttackRange())
							+ (int) u.getLocation().getY(); y++) {
						Point currentPoint = new Point(x, y);
						if (x < 0 || x > currentBoard.getBoardWidth() - 1
								|| y < 0
								|| y > currentBoard.getBoardHeight() - 1) {
							continue;
						}
						if (currentBoard.checkIfEnemy(u, currentPoint)) {

							userPointX = x;
							userPointY = y;
							// System.out.println(u.getName());
							// System.out.println(u.getLocation());
							// System.out.println( new Point(xPoint,yPoint));
							// move towards fist enemy seen until enemy is
							// within attackRange
							if (currentBoard
									.checkAvailable(new Point(x - 1, y))) {
								xPoint = x - 1;
								yPoint = y;
							} else if (currentBoard.checkAvailable(new Point(x,
									y - 1))) {
								xPoint = x;
								yPoint = y - 1;
							} else if (currentBoard.checkAvailable(new Point(x,
									y + 1))) {
								xPoint = x;
								yPoint = y + 1;
							} else if (currentBoard.checkAvailable(new Point(
									x + 1, y))) {
								xPoint = x + 1;
								yPoint = y;
							}
							else if(currentBoard.checkAvailable(new Point(
									x + 1, y+1))){
								xPoint = x + 1;
								yPoint = y+1;
							}
							else if(currentBoard.checkAvailable(new Point(
									x - 1, y-1))){
								xPoint = x - 1;
								yPoint = y-1;
							}
							else if(currentBoard.checkAvailable(new Point(
									x + 1, y-1))){
								xPoint = x + 1;
								yPoint = y-1;
							}
							else if(currentBoard.checkAvailable(new Point(
									x - 1, y+1))){
								xPoint = x - 1;
								yPoint = y+1;
							}
							
//							System.out.println(u.getName());
//							// System.out.println(u.getLocation());
//							System.out.println(new Point(xPoint, yPoint));
//							System.out.println(new Point(userPointX, userPointY));
							break;
							/*
							 * if(currentBoard.checkOpenLineOfFire(u,
							 * currentPoint)){ //attack if possible }
							 */
						}
					}
				}
				// }

				/*
				 * xPoint = random.nextInt(19); yPoint = random.nextInt(19); if
				 * (currentBoard.checkAvailable(new Point(xPoint,yPoint))){
				 * yes=false; } if(z==10){ return; } z++;
				 */
			}

//			 System.out.println(compUnits.get(i).getName()+" get point"+
//			 xPoint +yPoint);
//			System.out.println(u.getName());
//  			System.out.println(u.getLocation());
			if ((xPoint == 0 && yPoint == 0)) {
				unitIndex++;
				moveTurnPrincess();
			} else {
//				System.out.println(u.getName() + " " + new Point(xPoint, yPoint));
//				System.out.println(u.getName() + " "
//						+ new Point(userPointX, userPointY));
				int use = 0;
				if (currentBoard.checkOpenLineOfFire(u, new Point(userPointX,
						userPointY))) {
					unitIndex++;
					moveTurnPrincess();
				} else {

					path = currentBoard.findShortestPath(u.getLocation(),
							new Point(xPoint, yPoint));
					if(path==null){
						unitIndex++;
						moveTurnPrincess();
					}
					else{
					if (path.size() > u.getDistance() + 1) {
						for (int j = 1; j < u.getDistance() + 1; j++) {
							moves.add(path.get(j));
							if (currentBoard.checkOpenLineOfFire(u,
									path.get(j), new Point(userPointX,
											userPointY))) {
								break;
							}
						}
					}
					/*
					 * else if(u.getAttackRange()<path.size()){ for (int j = 1;
					 * j < path.size() ; j++) {
					 * //if(!currentBoard.checkOpenLineOfFire(u,new
					 * Point(userPointX, userPointY))) moves.add(path.get(j));
					 * 
					 * } }
					 */
					else {

						for (int j = 1; j < path.size(); j++) {
							// if(!currentBoard.checkOpenLineOfFire(u,new
							// Point(userPointX, userPointY)))
							moves.add(path.get(j));
							if (currentBoard.checkOpenLineOfFire(u,
									path.get(j), new Point(userPointX,
											userPointY))) {
								break;
							}
						}
						/*
						 * else{ for (int j = 1; j < u.getDistance()+1 ; j++) {
						 * //if(!currentBoard.checkOpenLineOfFire(u,new
						 * Point(userPointX, userPointY)))
						 * moves.add(path.get(j));
						 * 
						 * 
						 * } }
						 */
						for (int h = 0; h < moves.size(); h++) {
							// System.out.println("before "
							// +moves.get(h).g());
						}
					}
					for (int j = 1; j < path.size(); j++) {
						moves.add(path.get(j));
					}
					UnitMovedCommand moveCommand = new UnitMovedCommand(
							userName, i, moves);
					if (moves != null) {
						try {
							outputStream.writeObject(moveCommand);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				}
			}
		}
		}
	}

	private boolean hasAttacked;

	private void moveTurnMele() {
		ArrayList<Unit> compUnits = new ArrayList<>();
		compUnits = currentBoard.getPlayerTwoUnits();

		if(unitIndex>4){
			attackTurnMele();
		}
		else{
		for (int i = 0; i < compUnits.size(); i++) {
			ArrayList<Point> path = new ArrayList<>();
			ArrayList<Point> moves = new ArrayList<>();
			Unit u = compUnits.get(i);

			if (!u.isAlive()) {
				continue;
			}
			Random random = new Random();
			boolean yes = true;
			int xPoint = 0;
			int yPoint = 0;
			int userPointX = 0;
			int userPointY = 0;
			int z = 0;
			boolean someone = false;
			// while (yes){
			// System.out.println(compUnits.get(i).getName()+" moves left "+compUnits.get(i).getMovesLeft());
			// System.out.println(compUnits.get(i).getName()+" at "+compUnits.get(i).getLocation());

			if (u.getName().equals("Sonic")) {
				// run away!!
				int hello = 0;
				while (yes && hello < 10) {
					hello++;
					// move from Q1 to Q2 or Q4
					if (currentBoard.inQuadrantOne(u)) {
						xPoint = currentBoard.getBoardWidth()
								/ 2
								+ random.nextInt(currentBoard.getBoardWidth() / 2 - 1);
						yPoint = random
								.nextInt(currentBoard.getBoardHeight() - 1);
						if (currentBoard.checkAvailable(new Point(xPoint,
								yPoint))) {
							yes = false;
						}
					}
					// move from Q2 to Q1 or Q3
					if (currentBoard.inQuadrantTwo(u)) {
						xPoint = random
								.nextInt(currentBoard.getBoardWidth() / 2);
						yPoint = random
								.nextInt(currentBoard.getBoardHeight() - 1);
						if (currentBoard.checkAvailable(new Point(xPoint,
								yPoint))) {
							yes = false;
						}
					}
					// move from Q3 to Q2 or Q4
					if (currentBoard.inQuadrantThree(u)) {
						xPoint = currentBoard.getBoardWidth()
								/ 2
								+ random.nextInt(currentBoard.getBoardWidth() / 2 - 1);
						yPoint = random
								.nextInt(currentBoard.getBoardHeight() - 1);
						if (currentBoard.checkAvailable(new Point(xPoint,
								yPoint))) {
							yes = false;
						}
					}
					// move from Q4 to Q1 or Q3
					if (currentBoard.inQuadrantFour(u)) {
						xPoint = random
								.nextInt(currentBoard.getBoardWidth() / 2);
						yPoint = random
								.nextInt(currentBoard.getBoardHeight() - 1);
						if (currentBoard.checkAvailable(new Point(xPoint,
								yPoint))) {
							yes = false;
						}
					}
				}
			}
			if (u.getName().equals("MegaMan")) {
				// go towards enemies, attack
				for (int x = (int) u.getLocation().getX() - u.getDistance(); x < (u
						.getDistance() + (int) u.getLocation().getX()); x++) {
					for (int y = (int) u.getLocation().getY() - u.getDistance(); y < (u
							.getDistance() + (int) u.getLocation().getY()); y++) {
						Point currentPoint = new Point(x, y);
						if (x < 0 || x > currentBoard.getBoardWidth() - 1
								|| y < 0
								|| y > currentBoard.getBoardHeight() - 1) {
							continue;
						}
						if (currentBoard.checkIfEnemy(u, currentPoint)) {
							userPointX = x;
							userPointY = y;
							if (currentBoard
									.checkAvailable(new Point(x - 1, y))) {
								xPoint = x - 1;
								yPoint = y;
							} else if (currentBoard.checkAvailable(new Point(x,
									y - 1))) {
								xPoint = x;
								yPoint = y - 1;
							} else if (currentBoard.checkAvailable(new Point(x,
									y + 1))) {
								xPoint = x;
								yPoint = y + 1;
							} else if (currentBoard.checkAvailable(new Point(
									x + 1, y))) {
								xPoint = x + 1;
								yPoint = y;
							}
							someone = true;
							break;
							// move towards fist enemy seen until enemy is
							// within attackRange

							/*
							 * if(currentBoard.checkOpenLineOfFire(u,
							 * currentPoint)){ //attack if possible }
							 */
						}
					}
				}

				if (someone == false) {
					for (int x = 0; x < currentBoard.getBoardHeight(); x++) {
						for (int y = 0; y < currentBoard.getBoardWidth(); y++) {
							Point currentPoint = new Point(x, y);
							if (currentBoard.checkIfEnemy(u, currentPoint)) {
								userPointX = x;
								userPointY = y;
								if (currentBoard.checkAvailable(new Point(
										x - 1, y))) {
									xPoint = x - 1;
									yPoint = y;
								} else if (currentBoard
										.checkAvailable(new Point(x, y - 1))) {
									xPoint = x;
									yPoint = y - 1;
								} else if (currentBoard
										.checkAvailable(new Point(x, y + 1))) {
									xPoint = x;
									yPoint = y + 1;
								} else if (currentBoard
										.checkAvailable(new Point(x + 1, y))) {
									xPoint = x + 1;
									yPoint = y;
								}
								break;
							}
						}

					}
				}
			}
			if (u.getName().equals("Goku")) {
				// go towards enemies, attack
				// for(int x=0; x<currentBoard.getBoardHeight(); x++){
				// for(int y=0; y<currentBoard.getBoardWidth(); y++){
				for (int x = (int) u.getLocation().getX() - u.getDistance(); x < (u
						.getDistance() + (int) u.getLocation().getX()); x++) {
					for (int y = (int) u.getLocation().getY() - u.getDistance(); y < (u
							.getDistance() + (int) u.getLocation().getY()); y++) {
						Point currentPoint = new Point(x, y);
						if (x < 0 || x > currentBoard.getBoardWidth() - 1
								|| y < 0
								|| y > currentBoard.getBoardHeight() - 1) {
							continue;
						}
						if (currentBoard.checkIfEnemy(u, currentPoint)) {
							userPointX = x;
							userPointY = y;
							if (currentBoard
									.checkAvailable(new Point(x - 1, y))) {
								xPoint = x - 1;
								yPoint = y;
							} else if (currentBoard.checkAvailable(new Point(x,
									y - 1))) {
								xPoint = x;
								yPoint = y - 1;
							} else if (currentBoard.checkAvailable(new Point(x,
									y + 1))) {
								xPoint = x;
								yPoint = y + 1;
							} else if (currentBoard.checkAvailable(new Point(
									x + 1, y))) {
								xPoint = x + 1;
								yPoint = y;
							}
							break;
							// move towards fist enemy seen until enemy is
							// within attackRange

							/*
							 * if(currentBoard.checkOpenLineOfFire(u,
							 * currentPoint)){ //attack if possible }
							 */
						}
					}
				}

				if (someone == false) {
					for (int x = 0; x < currentBoard.getBoardHeight(); x++) {
						for (int y = 0; y < currentBoard.getBoardWidth(); y++) {
							Point currentPoint = new Point(x, y);
							if (currentBoard.checkIfEnemy(u, currentPoint)) {
								userPointX = x;
								userPointY = y;
								if (currentBoard.checkAvailable(new Point(
										x - 1, y))) {
									xPoint = x - 1;
									yPoint = y;
								} else if (currentBoard
										.checkAvailable(new Point(x, y - 1))) {
									xPoint = x;
									yPoint = y - 1;
								} else if (currentBoard
										.checkAvailable(new Point(x, y + 1))) {
									xPoint = x;
									yPoint = y + 1;
								} else if (currentBoard
										.checkAvailable(new Point(x + 1, y))) {
									xPoint = x + 1;
									yPoint = y;
								}
								break;
							}
						}
					}

				}
			}
			// move towards fist enemy seen until enemy is within attackRange

			/*
			 * if(currentBoard.checkOpenLineOfFire(u, currentPoint)){ //attack
			 * if possible }
			 */

			if (u.getName().equals("Link")) {
				// go towards enemies, attack
				// for(int x=0; x<currentBoard.getBoardHeight(); x++){
				// for(int y=0; y<currentBoard.getBoardWidth(); y++){
				for (int x = (int) u.getLocation().getX() - u.getDistance(); x < (u
						.getDistance() + (int) u.getLocation().getX()); x++) {
					for (int y = (int) u.getLocation().getY() - u.getDistance(); y < (u
							.getDistance() + (int) u.getLocation().getY()); y++) {
						Point currentPoint = new Point(x, y);
						if (x < 0 || x > currentBoard.getBoardWidth() - 1
								|| y < 0
								|| y > currentBoard.getBoardHeight() - 1) {
							continue;
						}
						if (currentBoard.checkIfEnemy(u, currentPoint)) {
							userPointX = x;
							userPointY = y;
							// move towards fist enemy seen until enemy is
							// within attackRange
							if (currentBoard
									.checkAvailable(new Point(x - 1, y))) {
								xPoint = x - 1;
								yPoint = y;
							} else if (currentBoard.checkAvailable(new Point(x,
									y - 1))) {
								xPoint = x;
								yPoint = y - 1;
							} else if (currentBoard.checkAvailable(new Point(x,
									y + 1))) {
								xPoint = x;
								yPoint = y + 1;
							} else if (currentBoard.checkAvailable(new Point(
									x + 1, y))) {
								xPoint = x + 1;
								yPoint = y;
							}
							break;
							/*
							 * if(currentBoard.checkOpenLineOfFire(u,
							 * currentPoint)){ //attack if possible }
							 */
						}
					}
				}
				if (someone == false) {
					for (int x = 0; x < currentBoard.getBoardHeight(); x++) {
						for (int y = 0; y < currentBoard.getBoardWidth(); y++) {
							Point currentPoint = new Point(x, y);
							if (currentBoard.checkIfEnemy(u, currentPoint)) {
								userPointX = x;
								userPointY = y;
								if (currentBoard.checkAvailable(new Point(
										x - 1, y))) {
									xPoint = x - 1;
									yPoint = y;
								} else if (currentBoard
										.checkAvailable(new Point(x, y - 1))) {
									xPoint = x;
									yPoint = y - 1;
								} else if (currentBoard
										.checkAvailable(new Point(x, y + 1))) {
									xPoint = x;
									yPoint = y + 1;
								} else if (currentBoard
										.checkAvailable(new Point(x + 1, y))) {
									xPoint = x + 1;
									yPoint = y;
								}
								break;
							}
						}

					}
				}
			}

			if (u.getName().equals("Mario")) {
				// go towards enemies, attack
				// for(int x=0; x<currentBoard.getBoardHeight(); x++){
				// for(int y=0; y<currentBoard.getBoardWidth(); y++){
				for (int x = (int) u.getLocation().getX() - u.getDistance(); x < (u
						.getDistance() + (int) u.getLocation().getX()); x++) {
					for (int y = (int) u.getLocation().getY() - u.getDistance(); y < (u
							.getDistance() + (int) u.getLocation().getY()); y++) {
						Point currentPoint = new Point(x, y);
						if (x < 0 || x > currentBoard.getBoardWidth() - 1
								|| y < 0
								|| y > currentBoard.getBoardHeight() - 1) {
							continue;
						}
						if (currentBoard.checkIfEnemy(u, currentPoint)) {
							userPointX = x;
							userPointY = y;
							// move towards fist enemy seen until enemy is
							// within attackRange
							if (currentBoard
									.checkAvailable(new Point(x - 1, y))) {
								xPoint = x - 1;
								yPoint = y;
							} else if (currentBoard.checkAvailable(new Point(x,
									y - 1))) {
								xPoint = x;
								yPoint = y - 1;
							} else if (currentBoard.checkAvailable(new Point(x,
									y + 1))) {
								xPoint = x;
								yPoint = y + 1;
							} else if (currentBoard.checkAvailable(new Point(
									x + 1, y))) {
								xPoint = x + 1;
								yPoint = y;
							}
							break;
							/*
							 * if(currentBoard.checkOpenLineOfFire(u,
							 * currentPoint)){ //attack if possible }
							 */
						}
					}

				}
				if (someone == false) {
					for (int x = 0; x < currentBoard.getBoardHeight(); x++) {
						for (int y = 0; y < currentBoard.getBoardWidth(); y++) {
							Point currentPoint = new Point(x, y);
							if (currentBoard.checkIfEnemy(u, currentPoint)) {
								userPointX = x;
								userPointY = y;
								if (currentBoard.checkAvailable(new Point(
										x - 1, y))) {
									xPoint = x - 1;
									yPoint = y;
								} else if (currentBoard
										.checkAvailable(new Point(x, y - 1))) {
									xPoint = x;
									yPoint = y - 1;
								} else if (currentBoard
										.checkAvailable(new Point(x, y + 1))) {
									xPoint = x;
									yPoint = y + 1;
								} else if (currentBoard
										.checkAvailable(new Point(x + 1, y))) {
									xPoint = x + 1;
									yPoint = y;
								}
								break;
							}
						}

					}

				}
			}

			if ((xPoint == 0 && yPoint == 0)) {
				unitIndex++;
				moveTurnMele();
			} else {
//				System.out.println(u.getName() + " " + new Point(xPoint, yPoint));
//				System.out.println(u.getName() + " "
//						+ new Point(userPointX, userPointY));
				int use = 0;
				if (currentBoard.checkOpenLineOfFire(u, new Point(userPointX,
						userPointY))) {
					unitIndex++;
					moveTurnMele();
				} else {

					path = currentBoard.findShortestPath(u.getLocation(),
							new Point(xPoint, yPoint));
					if(path==null){
						unitIndex++;
						moveTurnMele();
					}
					else{
					if (path.size() > u.getDistance() + 1) {
						for (int j = 1; j < u.getDistance() + 1; j++) {
							moves.add(path.get(j));
							if (currentBoard.checkOpenLineOfFire(u,
									path.get(j), new Point(userPointX,
											userPointY))) {
								break;
							}
						}
					}
					/*
					 * else if(u.getAttackRange()<path.size()){ for (int j = 1;
					 * j < path.size() ; j++) {
					 * //if(!currentBoard.checkOpenLineOfFire(u,new
					 * Point(userPointX, userPointY))) moves.add(path.get(j));
					 * 
					 * } }
					 */
					else {

						for (int j = 1; j < path.size(); j++) {
							// if(!currentBoard.checkOpenLineOfFire(u,new
							// Point(userPointX, userPointY)))
							moves.add(path.get(j));
							if (currentBoard.checkOpenLineOfFire(u,
									path.get(j), new Point(userPointX,
											userPointY))) {
								break;
							}
						}
						/*
						 * else{ for (int j = 1; j < u.getDistance()+1 ; j++) {
						 * //if(!currentBoard.checkOpenLineOfFire(u,new
						 * Point(userPointX, userPointY)))
						 * moves.add(path.get(j));
						 * 
						 * 
						 * } }
						 */
						for (int h = 0; h < moves.size(); h++) {
							// System.out.println("before "
							// +moves.get(h).g());
						}
					}
					for (int j = 1; j < path.size(); j++) {
						moves.add(path.get(j));
					}
					UnitMovedCommand moveCommand = new UnitMovedCommand(
							userName, i, moves);
					System.out.println(unitIndex);
						try {
							outputStream.writeObject(moveCommand);
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
				}
			}
		}
		}
	}

	
		private void attackTurnPrincess() {
			compUnits = currentBoard.getPlayerTwoUnits();
			playerUnits = currentBoard.getPlayerOneUnits();
			//go through all comp units except for indice one (princess can't attack)
			for (int i = 1; i < compUnits.size(); i++) {
				Unit u = compUnits.get(i);
				//grab each player unit one by one and see if comp can attack
				for(int j = 0; j < playerUnits.size(); j++){
					Point toCheck = new Point(playerUnits.get(j).getLocation());
					// if the comp unit is in range and has a shot, TAKE IT!
					if(!u.checkIfAlreadyAttackedThisTurn() && currentBoard.checkOpenLineOfFire(u, toCheck) && u.isAlive() && playerUnits.get(j).isAlive()){
						UnitAttackCommand command = new UnitAttackCommand(userName,i,j);
						try {
							outputStream.writeObject(command);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		
		sendEndTurnCommand();
	}



	private void attackTurnMele() {
		if(!hasAttacked){
		compUnits = currentBoard.getPlayerTwoUnits();
		playerUnits = currentBoard.getPlayerOneUnits();
		// go through all comp units
		for (int i = 0; i < compUnits.size(); i++) {
			Unit u = compUnits.get(i);
			// grab each player unit one by one and see if comp can attack
			for (int j = 0; j < playerUnits.size(); j++) {
				Point toCheck = new Point(playerUnits.get(j).getLocation());
				// if the comp unit is in range and has a shot, TAKE IT!
				if (!u.checkIfAlreadyAttackedThisTurn() &&currentBoard.checkOpenLineOfFire(u, toCheck) && u.isAlive() && playerUnits.get(j).isAlive() ) {
					UnitAttackCommand command = new UnitAttackCommand(userName,
							i, j);
					try {
						outputStream.writeObject(command);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		hasAttacked=true;
		sendEndTurnCommand();
		}
	}

	private void sendEndTurnCommand() {
		EndTurnCommand command = new EndTurnCommand(userName);
		try {
			outputStream.writeObject(command);
			// currentGameState=GameState.ChoosingAttack;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isMoving() {
		return moving;
	}

	/**
	 * This method activates an item and applies it to the chosen unit.
	 * @param client = user name
	 * @param unitIndex = indice in unit list
	 * @param itemIndex = indice in item list
	 */
	@Override
	public void useItem(String source, int unitIndex, int itemIndex) {
		Item item;
		
		if (source.compareTo(userName)==0) {
			item=compItemList.get(itemIndex);
			currentBoard.useThisItem(source, compUnits.get(unitIndex), item);
			compItemList.remove(item);
		} else {
			item=playerItemList.get(itemIndex);
			currentBoard.useThisItem(source, playerUnits.get(unitIndex), item);
			playerItemList.remove(item);
		}
	}

	public void unitAttacked(String source, int attackUnit, int defendUnit) {
		ArrayList<Unit> comp = new ArrayList<>();
		comp = currentBoard.getPlayerTwoUnits();
		ArrayList<Unit> user = new ArrayList<>();
		user = currentBoard.getPlayerOneUnits();
		if (source.equals(userName)) {
			user.get(defendUnit).takeHit(comp.get(attackUnit).getAttackPower());
		} else {
			comp.get(defendUnit).takeHit(user.get(attackUnit).getAttackPower());
		}
	}
	/**
	 * This method is called by the UnitMovedCommand and it will be used to
	 * make sure that the unit only moves up to as many times as it is able
	 * before its moves are up.
	 * @param source = user name
	 * @param unitIndex = indice in unit list
	 * @param moves = points to move to
	 */
	@Override
	public void unitMoved(String source, int index, ArrayList<Point> moves) {

		for (int h = 0; h < moves.size(); h++) {
			// System.out.println("after"+moves.get(h).g());
		}
		int actualTotalMoveLength;

		Unit u;

		if (source.equals(userName)) {
			u = currentBoard.getPlayerTwoUnits().get(index);
		} else {
			u = currentBoard.getPlayerOneUnits().get(index);
		}
		// first, determine how many moves from the chosen list can actually be
		// taken.

		// u.setLocation(moves.get(index));
		// this.currentBoard.getGameBoard()[u.getLocation().x][u.getLocation().y]
		// =u.getCharRepresentation();

		// u.setLocation(moves.get(index));

//		this.currentBoard.getGameBoard()[u.getLocation().x][u.getLocation().y] = u
//				.getCharRepresentation();
		if (u.getMovesLeft() <= moves.size() - 1) {
			actualTotalMoveLength = u.getMovesLeft();
		} else {
			actualTotalMoveLength = moves.size() - 1;
		}
		// loop through each point on the path and tell the gameBoard the unit
		// moved to each
		// new point. Only allow the unit to take its specified maxNum of moves
		for (int i = 0; i < actualTotalMoveLength; i++) {

			int x = u.getLocation().x;
			int y = u.getLocation().y;
			int dx = moves.get(i + 1).x;
			int dy = moves.get(i + 1).y;

			if (x == dx && y == dy) {
				// System.out.println("same");
			}
			// if the move is upwards
			if (x > dx && y == dy) {

				currentBoard.moveUp(userName, u);
				// System.out.println(u.getName()+"move up");
			}
			// if the move is downwards
			else if (x < dx && y == dy) {

				currentBoard.moveDown(userName, u);
				// System.out.println(u.getName()+"move down");
			}
			// if the move is to the right
			else if (x == dx && y < dy) {
				currentBoard.moveRight(userName, u);
				// System.out.println(u.getName()+"move right");
			}
			// if the move is left
			else if (x == dx && y > dy) {
				currentBoard.moveLeft(userName, u);
				// System.out.println(u.getName()+"move left");
			}
		}
		// System.out.println(u.getLocation());


		//System.out.println(u.getName() + " at " + u.getLocation());
		// System.out.println(u.getLocation() + "testComp");
		moving = false;
		
		if (gameType == Scenario.Princess && source.equals(userName)) {
			unitIndex++;
			if (unitIndex > 5) {
				attackTurnPrincess();
			} else {
				moveTurnPrincess();
			}
		} else if(source.equals(userName)){
			unitIndex++;
			//System.out.println("end " +unitIndex);
			if (unitIndex > 4) {
				attackTurnMele();
			} else {
				moveTurnMele();
			}
		}



	}
	/**
	 * When an item is walked over, this method gets called from PickUpItemCommand
	 * to place a randomly generated item into the item list
	 * belonging to the user whose unit grabbed the item.
	 * @param client
	 */
	@Override
	public void pickUpItem(String source) {
		ArrayList<Item> list = new ArrayList<>();
		Item rage = new Rage();
		Item potion = new Potion();
		Item shield = new Shield();
		Item sneakers = new Sneakers();
		Item sniper = new Sniper();
		// can add more
		list.add(rage);
		list.add(potion);
		list.add(shield);
		list.add(sneakers);
		list.add(sniper);

		Random random = new Random();
		int num = random.nextInt(list.size() - 1);

		Item item = (list.get(num));
		if (source.equals(userName)) {
			compItemList.add(item);
		} else{
			playerItemList.add(item);
		}
	}

	@Override
	public void beginGame() {
		// THIS METHOD DOES NOT NEED TO BE USED IN THIS CLASS. IT MUST BE IN
		// HERE SO THAT
		// IF A COMMAND IS SENT TO BEGIN GAME, THE PLAYER CLIENT CAN FIND OUT
		// BUT THIS CLASS'S
		// SERVER HANDLER DOES NOT HAVE TO RUN INTO A RUNTIME CASTING EXCEPTION

	}
	/**
	 * This method gets called from TelePortUnitCommand when a unit steps on a wormhole.
	 * It teleports the unit to a randomly chosen location on the map.
	 * @param source = user name
	 * @param unitIndex = indice in unit list
	 * @param teleLocation = point to teleport to
	 */
	public void teleportUnit(String source, int unitIndex, Point teleLocation){
		if(this.userName.equals(source)){
			Unit unit=compUnits.get(unitIndex);
			currentBoard.getGameBoard()[unit.getLocation().x][unit.getLocation().y]=' ';
			unit.setLocation(teleLocation);
			currentBoard.getGameBoard()[unit.getLocation().x][unit.getLocation().y]=unit.getCharRepresentation();
		}
		else{
		Unit unit=playerUnits.get(unitIndex);
		currentBoard.getGameBoard()[unit.getLocation().x][unit.getLocation().y]=' ';
		unit.setLocation(teleLocation);
		currentBoard.getGameBoard()[unit.getLocation().x][unit.getLocation().y]=unit.getCharRepresentation();
		}
	}
}
