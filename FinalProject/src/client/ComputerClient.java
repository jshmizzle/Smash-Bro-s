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
import model.Princess;
import model.Scenario;
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

public class ComputerClient extends JFrame implements Client {

	private String host, userName;
	private int port = 0;
	private Socket server;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private MainMenuPanel mainMenuPanel;
	private CharacterSelectPanel charSelectPanel;
	private JPanel currentPanel;
	private MainGamePanel gamePanel;
	private GameBoard currentBoard;
	private boolean playingAlready = false;
	private boolean myTurn = false;
	private ArrayList<Unit> playerUnits;
	private ArrayList<Unit> compUnits;
	private boolean isHost = false;
	private Scenario gameType = null;
	private Map map = null;

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

	// temp method
	private void chooseRandomCompUnits() {
		// initialize the units and the GameBoard
		compUnits = new ArrayList<Unit>();

		ArrayList<Unit> choices = new ArrayList<>(
				Arrays.asList(new Link('l'), new Goku('g'), new Mario('w'),
						new MegaMan('m'), new Sonic('s')));
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
		Command command = new SetUserUnits(userName, temp);
		// send the command that sets the user units for computer and the user
		try {
			outputStream.writeObject(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setMapAndScenario(String source, Map map, Scenario scenario) {
		this.map = map;
		gameType = scenario;
	}

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

	/*
	 * public void useItem(String client, Unit u, Item item) {
	 * currentBoard.useThisItem(client, u, item); }
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

	public void endTurn(String client) {
		if (client.equals(userName)) {
			myTurn = false;
		} else {
			myTurn = true;
			currentBoard.resetPlayerTwoMoves();
			executeTurn();
		}
	}

	private void executeTurn() {
		if (gameType == Scenario.Princess)
			princessTurn();
		else
			meleTurn();
		sendEndTurnCommand();
	}

	public void princessTurn() {
		moveTurnPrincess();
		attackTurnPrincess();
	}

	public void meleTurn() {
		moveTurnMele();
		attackTurnMele();
	}

	private void moveTurnPrincess() {
		ArrayList<Unit> compUnits = new ArrayList<>();
		compUnits = currentBoard.getPlayerTwoUnits();
		Point princess = null;
		/*
		 * for (int j = 0; j < currentBoard.getGameBoard().length; j++) { for
		 * (int k = 0; k < currentBoard.getGameBoard()[0].length; k++) { if
		 * (currentBoard.getGameBoard()[j][k] == 'p') { princess = new Point(j,
		 * k); System.out.println(princess); break; } } }
		 */

		for (int i = 0; i < compUnits.size(); i++) {
			if (i == 3) {
				continue;
			}
			ArrayList<Point> path = new ArrayList<>();
			ArrayList<Point> moves = new ArrayList<>();
			Unit u = compUnits.get(i);
			if (!u.isAlive() || u.getName().equals("Princess")) {
				continue;
			}
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
				if (currentBoard.checkAvailable(new Point(18, 10))) {
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
			}
			if (u.getName().equals("Link")) {

				// go towards enemies, attack (stay on edge of range)

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
							break;
							/*
							 * if(currentBoard.checkOpenLineOfFire(u,
							 * currentPoint)){ //attack if possible }
							 */
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
				// }

				/*
				 * xPoint = random.nextInt(19); yPoint = random.nextInt(19); if
				 * (currentBoard.checkAvailable(new Point(xPoint,yPoint))){
				 * yes=false; } if(z==10){ return; } z++;
				 */
			}

			// System.out.println(compUnits.get(i).getName()+" get point"+
			// xPoint +yPoint);
			// System.out.println(u.getName());
			if (xPoint != 0 && yPoint != 0) {
				path = currentBoard.findShortestPath(u.getLocation(),
						new Point(xPoint, yPoint));
				// System.out.println("path :" +path.size());
				// System.out.println(u.getName());
				int use = 0;
				if (u.getAttackRange() > path.size()
						&& currentBoard.checkOpenLineOfFire(u, new Point(
								userPointX, userPointY))) {
					// dont move only attack
				} else {
					if (path.size() > u.getDistance() + 1
							&& u.getAttackRange() < path.size()) {
						for (int j = 1; j < u.getDistance() + 1; j++) {
							moves.add(path.get(j));
						}
					} else if (u.getAttackRange() < path.size()) {
						for (int j = 0; j < path.size(); j++) {
							// if(!currentBoard.checkOpenLineOfFire(u,new
							// Point(userPointX, userPointY)))
							moves.add(path.get(j));

						}
					} else { // /makes sure they dont move to close to the enemy
						int k = 1;
						for (int j = path.size(); j < u.getDistance() + 1; j++) {
							// if(!currentBoard.checkOpenLineOfFire(u,new
							// Point(userPointX, userPointY)))
							moves.add(path.get(k));
							k++;

						}
					}
					for (int h = 0; h < moves.size(); h++) {
						// System.out.println("before "
						// +moves.get(h).toString());
					}
				}
				for (int j = 1; j < path.size(); j++) {
					moves.add(path.get(j));
				}
				UnitMovedCommand moveCommand = new UnitMovedCommand(userName,
						i, moves);
				try {
					outputStream.writeObject(moveCommand);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void moveTurnMele() {

		ArrayList<Unit> compUnits = new ArrayList<>();
		compUnits = currentBoard.getPlayerTwoUnits();

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

			/*
			 * xPoint = random.nextInt(19); yPoint = random.nextInt(19); if
			 * (currentBoard.checkAvailable(new Point(xPoint,yPoint))){
			 * yes=false; } if(z==10){ return; } z++;
			 */

			// System.out.println(compUnits.get(i).getName()+" get point"+
			// xPoint +yPoint);
			path = currentBoard.findShortestPath(u.getLocation(), new Point(
					xPoint, yPoint));
			// System.out.println("path :" +path.size());
			int use = 0;
			if (path.size() > u.getDistance() + 1) {
				for (int j = 1; j < u.getDistance() + 1; j++) {
					moves.add(path.get(j));
				}
			} else {
				for (int j = 0; j < path.size(); j++) {
					moves.add(path.get(j));
				}
			}
			for (int h = 0; h < moves.size(); h++) {
				// System.out.println("before " +moves.get(h).toString());
			}
			// for (int j = 1; j < u.getDistance() ; j++) {
			// moves.add(path.get(j));
			// }
			UnitMovedCommand moveCommand = new UnitMovedCommand(userName, i,
					moves);
			try {
				outputStream.writeObject(moveCommand);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void attackTurnPrincess() {
		compUnits = currentBoard.getPlayerTwoUnits();
		//go through all comp units except for indice one (princess can't attack)
		for (int i = 1; i < compUnits.size(); i++) {
			Unit u = compUnits.get(i);
			//grab each player unit one by one and see if comp can attack
			for(int j = 0; j < playerUnits.size(); j++){
				Point toCheck = new Point(playerUnits.get(j).getLocation());
				// if the comp unit is in range and has a shot, TAKE IT!
				if(!u.checkIfAlreadyAttackedThisTurn() && currentBoard.checkOpenLineOfFire(u, toCheck)){
					UnitAttackCommand command = new UnitAttackCommand(userName,i,j);
					try {
						outputStream.writeObject(command);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void attackTurnMele() {
		compUnits = currentBoard.getPlayerTwoUnits();
		// go through all comp units
		for (int i = 0; i < compUnits.size(); i++) {
			Unit u = compUnits.get(i);
			// grab each player unit one by one and see if comp can attack
			for (int j = 0; j < playerUnits.size(); j++) {
				Point toCheck = new Point(playerUnits.get(j).getLocation());
				// if the comp unit is in range and has a shot, TAKE IT!
				if (!u.checkIfAlreadyAttackedThisTurn() &&currentBoard.checkOpenLineOfFire(u, toCheck)) {
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

	private boolean moving = true;

	public boolean isMoving() {
		return moving;
	}

	public void newGame() {

	}

	@Override
	public void useItem(String source, int index, Item item) {

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

	@Override
	public void unitMoved(String source, int index, ArrayList<Point> moves) {

		for (int h = 0; h < moves.size(); h++) {
			// System.out.println("after"+moves.get(h).toString());
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
		this.currentBoard.getGameBoard()[u.getLocation().x][u.getLocation().y] = u
				.getCharRepresentation();

		// loop through each point on the path and tell the gameBoard the unit
		// moved to each
		// new point. Only allow the unit to take its specified maxNum of moves
		for (int i = 0; i < moves.size() - 1; i++) {
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

		System.out.println(u.getName() + " at " + u.getLocation());
		// System.out.println(u.getLocation() + "testComp");
		moving = false;

	}

	@Override
	public void pickUpItem(String source, Point p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beginGame() {
		// THIS METHOD DOES NOT NEED TO BE USED IN THIS CLASS. IT MUST BE IN
		// HERE SO THAT
		// IF A COMMAND IS SENT TO BEGIN GAME, THE PLAYER CLIENT CAN FIND OUT
		// BUT THIS CLASS'S
		// SERVER HANDLER DOES NOT HAVE TO RUN INTO A RUNTIME CASTING EXCEPTION
	}
}
