package client;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
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
import model.Sonic;
import model.Unit;
import GUI.CharacterSelectPanel;
import GUI.GameLobby;
import GUI.MainGamePanel;
import GUI.MainMenuPanel;
import GUI.SinglePlayerMapAndScenarioSelect;
import GUI.WaitingOnCharacterSelection;

import command.LobbyInfoCommand;

public class TRPGClient extends JFrame implements Client {

	private String host, userName;
	private int port = 0;
	private Map mapChoice; 
	private Scenario scenarioChoice;
	private Socket server;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private JPanel currentPanel;
	private GameBoard currentBoard;
	private boolean playingAlready = false, isHost = false,
			singlePlayer = false;
	private boolean myTurn = true;
	private ArrayList<Item> itemList = new ArrayList<Item>();
	private ArrayList<Unit> playerUnits;
	private ServerHandler handler;
	private ArrayList<Unit> myUnits;
	private ArrayList<Unit> opponentUnits;

	public static void main(String[] args) {
		// try {
		// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		TRPGClient client = new TRPGClient();
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public TRPGClient() {

		askUserForInfo();// now the client has been logged into the server'

		ServerHandler handler = new ServerHandler(this, inputStream);
		Thread t = new Thread(handler);
		t.start();

		this.setTitle("TRPG Final Project");

		currentPanel = new MainMenuPanel(userName, outputStream);
		this.add(currentPanel).setVisible(true);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * The purpose of this alternate constructor is to provide a way to
	 * automatically set up the connection to the server in a way that only has
	 * to be set up one time. This way we do not need to be bothered to
	 * constantly input the host, port, and username. We simply pass all of that
	 * information to this constructor to begin with and the rest is taken care
	 * of automatically. To be used mostly with TRPGQuickStart.
	 * 
	 * @param host
	 *            The IP address to connect to.
	 * @param port
	 *            The port to connect to at the specified port.
	 * @param username
	 *            The username which you will be logging into the server with.
	 * 
	 * @see TRPGQuickStart
	 */

	public TRPGClient(String host, int port, String username){
		this.host=host;
		this.port=port;
		this.userName=username;
		this.setTitle("TRPG Final Project");
		
		try {
			// create the connection to the server
			server = new Socket(host, port);
			inputStream = new ObjectInputStream(server.getInputStream());
			outputStream = new ObjectOutputStream(server.getOutputStream());

			// Now tell the server your username
			outputStream.writeObject(username);
			inputStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.out
					.println("You did a terrible job constructing this client");
			e.printStackTrace();
		}

		// perform the same operations that the original constructor did
		initializeFrame();
		handler = new ServerHandler(this, inputStream);
		Thread t = new Thread(handler);
		t.start();
	}
	
	/**
	 * Loads a saved GameBoard from file
	 */
	public void loadSavedGame() {
		try {
			FileInputStream filein = new FileInputStream("SmashBros.out");
			ObjectInputStream objectin = new ObjectInputStream(filein);

			currentBoard = (GameBoard) objectin.readObject();
			objectin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves the current GameBoard in a file to be read in at a later time
	 */
	private void saveGame() {
		File file = new File("SmashBros.out");
		try {
			FileOutputStream fileout = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileout);

			out.writeObject(currentBoard);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startSinglePlayerGame(){
		//obviously the player will host the single player game
		isHost=true;
		singlePlayer=true;
		
		//construct the computer client to play against
		ComputerClient computer=new ComputerClient();

		//now we need to switch over from the mainMenuPanel directly to the character select
		this.remove(currentPanel);
		this.setVisible(false);
		
		currentPanel=new SinglePlayerMapAndScenarioSelect(userName, outputStream);
		currentPanel.grabFocus();
		this.add(currentPanel).setVisible(true);
		currentPanel.requestFocus(true);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * When the player clicks 'Host Multiplayer Game' on the MainGamePanel, the command is 
	 * sent for this client to become the host, and start a lobby.
	 * @param source
	 */
	public void hostMultiPlayerGame(String source) {
		if(source.equals(userName)){
			//I want to start my own lobby and be the host to my own game
			
			//designate yourself as the HOST
			isHost=true;
			
			//get rid of the MainMenuPanel
			this.remove(currentPanel);
			this.setVisible(false);
			//replace it with the lobby panel
			currentPanel=new GameLobby(userName, outputStream, isHost);
			currentPanel.grabFocus();
			this.add(currentPanel).setVisible(true);
			currentPanel.requestFocus(true);
			this.pack();
			this.setVisible(true);
		}
		else{
			//somebody else is trying to host a game while I'm already either a host
			//or just don't care, do nothing
		}
	}
	
	public void joinMultiPlayerGame(String source){
		if(source.equals(userName)){
			//designate yourself not to be host
			isHost=false;
			
			//get rid of the MainMenuPanel
			this.remove(currentPanel);
			this.setVisible(false);
			//replace it with the lobby panel
			currentPanel=new GameLobby(userName, outputStream, isHost);
			currentPanel.grabFocus();
			this.add(currentPanel).setVisible(true);
			currentPanel.requestFocus(true);
			this.pack();
			this.setVisible(true);
		}
		else if(currentPanel instanceof GameLobby){
			((GameLobby)currentPanel).clientJoined(source);
		}
	}
	
	public void updateLobby(ArrayList<String> users){
		if(currentPanel instanceof GameLobby){
			((GameLobby)currentPanel).updateClients(users);
		}
	}
	
	public void setMapAndScenario(String source, Map map, Scenario scenario) {
		this.mapChoice=map;
		this.scenarioChoice=scenario;
		//we no longer want to be looking at the lobby panel because we already got all of 
		//the use out of this panel as we can.
		this.remove(currentPanel);

		// change the currentPanel to the character select panel so that we can
		// select our units for the current game and then notify the other client
		currentPanel = new CharacterSelectPanel(userName, outputStream, isHost, scenarioChoice);
		currentPanel.grabFocus();
		this.add(currentPanel);
		currentPanel.requestFocus(true);
		this.pack();
		this.setVisible(true);
	}

	public void setUserUnits(String source, ArrayList<Unit> userUnits){
		
		//it doesn't matter if you are the host or not, if you sent this command, then
		//you chose these units, and you have to be the one to live with your decisions 
		//and use them for the game
		//if(isHost){	
			if(userName.equals(source)){
				myUnits=userUnits;
			}
			else{
				opponentUnits=userUnits;
			}
			System.out.println("User units set: \nMy Units: " + myUnits);
			System.out.println("Opponent's Units: " + opponentUnits);
			
			if(singlePlayer && opponentUnits==null){
				//we no longer want to be looking at the unit select panel because we already got
				//the units we need to start a single player game.
				this.remove(currentPanel);
				this.currentPanel=new WaitingOnCharacterSelection(userName, myUnits, opponentUnits, Scenario.Princess, outputStream);
				currentPanel.grabFocus();
				this.add(currentPanel);
				currentPanel.requestFocus(true);
				this.pack();
				this.setVisible(true);
			}
			//now if the opponent just got set, then we now need to play the game 
			else if(singlePlayer){
				//change the currentPanel to the character select panel so that we can select our
				//units for the current game and then notify the other client
				((WaitingOnCharacterSelection)currentPanel).updateUnitLists(myUnits, opponentUnits);
			}
			//it is multiPlayer
			else if(source.equals(userName)){//if I sent the command open the waiting panel
				//we no longer want to be looking at the unit select panel because we already got
				//the units we need to start a single player game.
				this.remove(currentPanel);
				this.currentPanel=new WaitingOnCharacterSelection(userName, myUnits, opponentUnits, Scenario.Princess, outputStream);
				currentPanel.grabFocus();
				this.add(currentPanel);
				currentPanel.requestFocus(true);
				this.pack();
				this.setVisible(true);
				this.repaint();
			}
			else{//both unit lists have already been set
				//if we have already switched to the waiting panel, then we need to update 
				if(currentPanel instanceof WaitingOnCharacterSelection){
					((WaitingOnCharacterSelection)currentPanel).updateUnitLists(myUnits, opponentUnits);
				}
			}
		//}
	}
	
	public void beginGame(){
		this.remove(currentPanel);
		
		//we need to make sure that each client has the same exact setup as the other client
		//that means that we can't just put my units in first everytime. That results
		//in both clients having opposite boards
		if(isHost){
			//host units always gets to be at the bottom
			currentBoard =new GameBoard(myUnits, opponentUnits, mapChoice, scenarioChoice);
		}
		else{
			currentBoard =new GameBoard(opponentUnits, myUnits, mapChoice, scenarioChoice);
		}
		
		
		currentPanel=new MainGamePanel(userName, currentBoard, this, outputStream, isHost);
		currentPanel.grabFocus();
		this.add(currentPanel);
		currentPanel.requestFocus(true);
		this.pack();
		this.setVisible(true);
		this.repaint();
	}
	
	private void askUserForInfo() {
		host = JOptionPane.showInputDialog(null,
				"What host would you like to connect to?");
		while (port == 0) {
			try {
				port = Integer.parseInt((String) JOptionPane.showInputDialog(
						null, "What port are you trying to connect on?"));
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
				userName = JOptionPane
						.showInputDialog("Enter your TRPG username");
				outputStream.writeObject(userName);
				serverAccepted = (String) inputStream.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initializeFrame() {
		// mainMenuPanel = new MainMenuPanel(username, outputStream);
		// start with MainGamePanel for testing menus will be added later the
		// game comes first

		// this was only useful when we were not letting the players pick their
		// own units
		// initializeGameBoard();

		// gamePanel=new MainGamePanel(userName, currentBoard, this,
		// outputStream);
		// currentPanel=gamePanel;

		currentPanel = new GameLobby(userName, outputStream, isHost);
		this.add(currentPanel).setVisible(true);
		this.pack();
		this.setVisible(true);
	}

	public void useItem(String client, int index, Item item) {

		if (client.equals(userName)) {
			if (isHost)
				currentBoard.useThisItem(client, playerUnits.get(index), item);
			else
				currentBoard.useThisItem(client, opponentUnits.get(index), item);
		} else {
			if (isHost) {
				currentBoard.useThisItem(client, opponentUnits.get(index), item);
			} else
				currentBoard.useThisItem(client, playerUnits.get(index), item);

		}

	}

	public void newGame() {
		currentBoard.startNewGame();
	}

	public void attackUnit(String client, int fromIndex, int toIndex) {
		//if the attack was sent by you then the unit from your list attacks the unit from the
		//opponent's list, make sure to take that into account. Doesn't matter who's hosting.
		if (client.equals(userName)) {
			currentBoard.attackUnit(myUnits.get(fromIndex),opponentUnits.get(toIndex));
		} 
		else {
			currentBoard.attackUnit(opponentUnits.get(fromIndex), myUnits.get(toIndex));
		}
	}

	public void endTurn(String client) {
		if (client.equals(userName)) {
			myTurn = false;
			((MainGamePanel) currentPanel).myTurn();
			currentBoard.resetPlayerTwoMoves();
		} else {
			myTurn = true;
			((MainGamePanel) currentPanel).myTurn();
			currentBoard.resetPlayerOneMoves();
		}
	}

	public boolean myTurn() {
		return myTurn;
	}

	public void pickUpItem(String client, Point p) {
		if (client.equals(userName)) {
			ArrayList<Item> list = new ArrayList<>();
			Item rage = new Rage();
			Item potion = new Potion();
			Item shield = new Shield();
			Item sneakers = new Sneakers();
			// can add more
			list.add(rage);
			list.add(potion);
			list.add(shield);
			list.add(sneakers);

			Random random = new Random();
			int num = random.nextInt(list.size() - 1);

			Item item = (list.get(num));
			itemList.add(item);
			//System.out.println("Picked up item: " + item.getName());
			// currentBoard.removeItem(p);
		} else
			; // do nothing
	}

	/**
	 * This method is called by the UnitMoved Command and it will be used to
	 * make sure that the unit only moves up to as many times as it is able
	 * before its moves are up.
	 */
	public void unitMoved(String source, int unitIndex, ArrayList<Point> moves) {
		moving = true;
		int actualTotalMoveLength;

		//again, it doesn't matter who's hosting..
		Unit u;
		if (source.equals(userName)) {
			u=myUnits.get(unitIndex);
		} else {
			u=opponentUnits.get(unitIndex);
		}
		// first, determine how many moves from the chosen list can actually be
		// taken.
		if (u.getMovesLeft() <= moves.size() - 1) {
			actualTotalMoveLength = u.getMovesLeft();
		} else {
			actualTotalMoveLength = moves.size() - 1;
		}

		// loop through each point on the path and tell the gameBoard the unit
		// moved to each
		// new point. Only allow the unit to take its specified maxNum of moves
		for (int i = 0; i < actualTotalMoveLength; i++, u.moveTaken()) {

			int x = u.getLocation().x;
			int y = u.getLocation().y;
			int dx = moves.get(i + 1).x;
			int dy = moves.get(i + 1).y;

			if (x == dx && y == dy) {
				//System.out.println("same");
			}
			// if the move is upwards
			if (x > dx && y == dy) {
				currentBoard.moveUp(userName, u);
				((MainGamePanel) currentPanel).update(currentBoard);
				//System.out.println("move up");
			}
			// if the move is downwards
			else if (x < dx && y == dy) {
				currentBoard.moveDown(userName, u);
				((MainGamePanel) currentPanel).update(currentBoard);
				//System.out.println("move down");
			}
			// if the move is to the right
			else if (x == dx && y < dy) {
				currentBoard.moveRight(userName, u);
				((MainGamePanel) currentPanel).update(currentBoard);
				//System.out.println("move right");
			}
			// if the move is left
			else if (x == dx && y > dy) {
				currentBoard.moveLeft(userName, u);
				((MainGamePanel) currentPanel).update(currentBoard);
				//System.out.println("move left");
			} else {
				//System.out.println("awkward position");
				// just let the stupid AI jump wherever it wants to...
				currentBoard.setUnitToThisSpot(u, moves.get(i + 1));
				((MainGamePanel) currentPanel).update(currentBoard);
			}
		}
		((MainGamePanel) currentPanel).update(currentBoard);

		// if the game is over let us know!
		//System.out.println(currentBoard.getPlayerTwoUnits().get(0).getHealth());
		/*
		 * if(currentBoard.getCompUnits().get(0).getHealth()<=0){
		 * System.out.println("game over");
		 * if(currentBoard.getUserUnits().get(0).getHealth()<=0){
		 * JOptionPane.showMessageDialog(null,
		 * "YOU LOST IDIOT!! THE AI IS SO RANDOM IT'S NOT EVEN FUNNY...."); }
		 * //human won else
		 * if(currentBoard.getCompUnits().get(0).getHealth()<=0){
		 * JOptionPane.showMessageDialog(null,
		 * "You won...woooow. Good for you."); } }
		 */

		moving = false;
	}

	private boolean moving = true;

	public boolean isMoving() {
		return moving;
	}
}
