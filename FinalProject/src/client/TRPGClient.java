package client;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.GameBoard;
import model.Goku;
import model.Item;
import model.Link;
import model.Mario;
import model.MegaMan;
import model.Potion;
import model.Princess;
import model.Rage;
import model.Shield;
import model.Sneakers;
import model.Sonic;
import model.Unit;
import GUI.CharacterSelectPanel;
import GUI.MainGamePanel;
import GUI.MainMenuPanel;
import command.Command;

public class TRPGClient extends JFrame implements Client{

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
	private boolean myTurn = true;
	private ArrayList<Item> itemList=new ArrayList<Item>();

	public static void main(String[] args) {
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		TRPGClient client = new TRPGClient();
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public TRPGClient() {
		askUserForInfo();// now the client has been logged into the server'
		initializeFrame();
		ServerHandler handler = new ServerHandler(this, inputStream);
		Thread t = new Thread(handler);
		t.start();
	}
	
	/**
	 * The purpose of this alternate constructor is to provide a way to automatically
	 * set up the connection to the server in a way that only has to be set up one time.
	 * This way we do not need to be bothered to constantly input the host, port, and 
	 * username. We simply pass all of that information to this constructor to begin 
	 * with and the rest is taken care of automatically. To be used mostly with 
	 * TRPGQuickStart.
	 * 
	 * @param host The IP address to connect to.
	 * @param port The port to connect to at the specified port.
	 * @param username The username which you will be logging into the server with.
	 * 
	 * @see  TRPGQuickStart
	 */
	public TRPGClient(String host, int port, String username){
		this.host=host;
		this.port=port;
		this.userName=username;
		
		try {
			//create the connection to the server
			server=new Socket(host, port);
			inputStream=new ObjectInputStream(server.getInputStream());
			outputStream=new ObjectOutputStream(server.getOutputStream());
			
			//Now tell the server your username
			outputStream.writeObject(username);
			inputStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("You did a terrible job constructing this client");
			e.printStackTrace();
		}
		
		//perform the same operations that the original constructor did
		initializeFrame();
		ServerHandler handler = new ServerHandler(this, inputStream);
		Thread t = new Thread(handler);
		t.start();
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
	
	//temp method
	private void initializeGameBoard() {
		//initialize the units and the GameBoard
		 ArrayList<Unit> playerUnits = new ArrayList<Unit>();
		 ArrayList<Unit> compUnits = new ArrayList<Unit>();
		 
	    Sonic S = new Sonic('S');
	  	Goku G = new Goku('G');
	  	Mario W = new Mario('W');
	 	Link l = new Link('l');
		Mario w = new Mario('w');
		MegaMan m = new MegaMan('m');
		Princess P = new Princess('P');
		Princess p = new Princess('p');
			
		playerUnits.add(P);
		playerUnits.add(W);
		playerUnits.add(S);
		playerUnits.add(G);
		compUnits.add(p);
		compUnits.add(l);
		compUnits.add(m);
		currentBoard = new GameBoard(playerUnits, compUnits, 1, 0); 
	}

	private void initializeFrame() {
		//mainMenuPanel = new MainMenuPanel(username, outputStream);
		//start with MainGamePanel for testing menus will be added later the game comes first
		initializeGameBoard();
		gamePanel=new MainGamePanel(userName, currentBoard, this, outputStream);
		currentPanel=gamePanel;
		this.add(currentPanel).setVisible(true);
		this.pack();
		this.setVisible(true);
	}

	private void update(Command<?> command) {
		this.gamePanel.update(currentBoard);
	}

	public void createGameBoard(ArrayList<Unit> userUnits,
			ArrayList<Unit> compUnits, int map, int scenario) {
		currentBoard = new GameBoard(userUnits, compUnits, map, scenario);
		playingAlready = true;
	}

	public void useItem(String client, int index, Item item) {
		if(!client.equals("Computer")){
			currentBoard.useThisItem(client, currentBoard.getUserUnits().get(index), item);
		}
		else
			; // do nothing
	}


	public void welcomeToLobby(String client) {
		// open lobby for whoever connected
		if (playingAlready == true) {
			; // do nothing
		} else {
			// load lobby (probably needs work)
			askUserForInfo();// now the client has been logged into the server
			initializeFrame();
			ServerHandler handler = new ServerHandler(this, inputStream);
			Thread t = new Thread(handler);
			t.start();
		}
	}
	
	public void newGame(){
		currentBoard.startNewGame();
	}



	public void attackUnit(String client, int fromIndex, int toIndex) {
		if(!client.equals("Computer")){
			currentBoard.attackUnit(currentBoard.getUserUnits().get(fromIndex),currentBoard.getCompUnits().get(toIndex) );
			System.out.println(currentBoard.getCompUnits().get(2).getHealth());
		}
		else
			currentBoard.attackUnit(currentBoard.getCompUnits().get(fromIndex),currentBoard.getUserUnits().get(toIndex));
	}

	public void endTurn(String client) {
		if(client.equals(userName)){
			myTurn = false;
			gamePanel.myTurn();
			currentBoard.resetCompMoves();
		}
		else{
			myTurn = true;
			gamePanel.myTurn();
			if(!userName.equals("Computer"))
				currentBoard.resetUserMoves();
			currentBoard.resetCompMoves();
		}
	}
	
	public boolean myTurn(){
		return myTurn;
	}

	public void pickUpItem(String client, Point p) {
		if(client.equals(userName)){
			ArrayList<Item> list = new ArrayList<>();
			Item rage = new Rage();
			Item potion = new Potion();
			Item shield = new Shield();
			Item sneakers = new Sneakers();
			//can add more
			list.add(rage);
			list.add(potion);
			list.add(shield);
			list.add(sneakers);
			
			Random random = new Random();
			int num = random.nextInt(list.size()-1);
			
			Item item = (list.get(num));
			itemList.add(item);
			System.out.println("Picked up item: " + item.getName());
			currentBoard.removeItem(p);
		}
		else
			; // do nothing
	}

	/**
	 * This method is called by the UnitMoved Command and it will be used to make sure that the
	 * unit only moves up to as many times as it is able before its moves are up.
	 */
	public void unitMoved(String source, int unitIndex, ArrayList<Point> moves) {
		//indicate that the movement has begun
		for(int h=0; h<moves.size(); h++){
			System.out.println("after"+moves.get(h).toString());
			}
		moving=true;
		int actualTotalMoveLength;
		
		System.out.println(source + ":  ");
		for(int i=0; i<moves.size(); i++){
			System.out.println(moves.get(i));
		}
		
		Unit u;
		if(source.equals(userName)){
			u=currentBoard.getUserUnits().get(unitIndex);
		}
		else{
			u=currentBoard.getCompUnits().get(unitIndex);
		}
		//first, determine how many moves from the chosen list can actually be taken.
		if(u.getMovesLeft()<=moves.size()-1){
			actualTotalMoveLength=u.getMovesLeft();
		}
		else{ 
			actualTotalMoveLength=moves.size()-1;
		}
		
		//loop through each point on the path and tell the gameBoard the unit moved to each
		//new point. Only allow the unit to take its specified maxNum of moves
		for(int i=0; i<actualTotalMoveLength; i++, u.moveTaken()){
			
			int x=u.getLocation().x;
			int y=u.getLocation().y;
			int dx=moves.get(i+1).x;
			int dy=moves.get(i+1).y;
			
			
			if(x==dx && y==dy){
				System.out.println("same");
			}
			//if the move is upwards
			if(x>dx && y==dy){
				currentBoard.moveUp(userName, u);
				gamePanel.update(currentBoard);
				System.out.println("move up");
			}
			//if the move is downwards
			else if(x<dx && y==dy){
				currentBoard.moveDown(userName, u);
				gamePanel.update(currentBoard);
				System.out.println("move down");
			}
			//if the move is to the right
			else if(x==dx && y<dy){
				currentBoard.moveRight(userName, u);
				gamePanel.update(currentBoard);
				System.out.println("move right");
			}
			//if the move is left
			else if(x==dx && y>dy){
				currentBoard.moveLeft(userName, u);
				gamePanel.update(currentBoard);
				System.out.println("move left");
			}
			else{
				System.out.println("awkward position");
				//just let the stupid AI jump wherever it wants to...
				currentBoard.setUnitToThisSpot(u, moves.get(i+1));
				gamePanel.update(currentBoard);
			}
		}
		gamePanel.update(currentBoard);
		
		//if the game is over let us know!
		System.out.println(currentBoard.getCompUnits().get(0).getHealth());
				if(currentBoard.getCompUnits().get(0).getHealth()<=0){
					System.out.println("game over");
					if(currentBoard.getUserUnits().get(0).getHealth()<=0){
						JOptionPane.showMessageDialog(null, "YOU LOST IDIOT!! THE AI IS SO RANDOM IT'S NOT EVEN FUNNY....");
					}
					//human won
					else if(currentBoard.getCompUnits().get(0).getHealth()<=0){
						JOptionPane.showMessageDialog(null, "You won...woooow. Good for you.");
					}
				}
		
		moving=false;
	}
	private boolean moving=true;
	
	public boolean isMoving(){
		return moving;
	}



}
