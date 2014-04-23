package client;

import java.awt.Point;
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
import model.Mario;
import model.MegaMan;
import model.Princess;
import model.Sonic;
import model.Unit;
import GUI.CharacterSelectPanel;
import GUI.GameState;
import GUI.MainGamePanel;
import GUI.MainMenuPanel;
import command.Command;
import command.EndTurnCommand;
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

		initializeGameBoard();

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
	private void initializeGameBoard() {
		// initialize the units and the GameBoard
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

	
	  /*private void initializeFrame() { 
	  initializeGameBoard();
	  
	  //gamePanel = new MainGamePanel("computer",currentBoard, outputStream);
	  //currentPanel = gamePanel; this.add(currentPanel).setVisible(true);
	  this.pack(); this.setVisible(true); }*/
	 

	private void update(Command<?> command) {
		this.gamePanel.update(currentBoard);
	}

	public void createGameBoard(ArrayList<Unit> userUnits,
			ArrayList<Unit> compUnits, int map, int scenario) {
		currentBoard = new GameBoard(userUnits, compUnits, map, scenario);
		playingAlready = true;
	}

	/*
	 * public void useItem(String client, Unit u, Item item) {
	 * currentBoard.useThisItem(client, u, item); }
	 */

	public void welcomeToLobby(String client) {
		// open lobby for whoever connected
		if (playingAlready == true) {
			; // do nothing
		} else {
			// load lobby (probably needs work)
			askUserForInfo();// now the client has been logged into the server
			// initializeFrame();
			ComputerServerHandler handler = new ComputerServerHandler(this,
					inputStream);
			Thread t = new Thread(handler);
			t.start();
		}
	}

	public void attackUnit(String client, int fromIndex, int toIndex) {
		if (!client.equals("Computer")) {
			currentBoard.attackUnit(currentBoard.getUserUnits().get(fromIndex),
					currentBoard.getCompUnits().get(toIndex));
		} else
			currentBoard.attackUnit(currentBoard.getCompUnits().get(fromIndex),
					currentBoard.getUserUnits().get(toIndex));
	}

	public void endTurn(String client) {
		if (client.equals(userName)) {
			myTurn = false;
		} else {
			myTurn = true;
			// TODO: gonna need to change this for multiplayer
			currentBoard.resetCompMoves();
			executeProtocol();
		}
	}

	private void executeProtocol() {
		// TODO Auto-generated method stub
		System.out.println("h");
		moveTurn();
		attackTurn();
		sendEndTurnCommand();
	}

	int florb=1;
	private void moveTurn() {
		ArrayList<Unit> compUnits = new ArrayList<>();
		compUnits = currentBoard.getCompUnits();
		Point princess = null;
		/*for (int j = 0; j < currentBoard.getGameBoard().length; j++) {
			for (int k = 0; k < currentBoard.getGameBoard()[0].length; k++) {
				if (currentBoard.getGameBoard()[j][k] == 'p') {
					princess = new Point(j, k);
					System.out.println(princess); 
					break;
				}
			}
		}*/
		
		for (int i = 1; i < compUnits.size(); i++) {
			ArrayList<Point> path = new ArrayList<>();
			ArrayList<Point> moves = new ArrayList<>();
			Unit u = compUnits.get(i);
			if(!u.isAlive()){
				return;
			}
			System.out.println(u.getName()+" "+u.getMovesLeft());
			Random random=new Random();
			boolean yes=true;
			int rand1=0;
			int rand2=0;
			while (yes){
				rand1 = random.nextInt(19);
				rand2 = random.nextInt(19);
				if (currentBoard.checkAvailable(new Point(rand1,rand2))){
					yes=false;
				}
			}
			path = currentBoard.findShortestPath(u.getLocation(), new Point(rand1,rand2));
			System.out.println("path :" +path.size());
			int use=0;
			if(path.size()> u.getDistance()+1){
				for (int j = 1; j < u.getDistance()+1 ; j++) {
					moves.add(path.get(j));
				}
			}
			else{
				for (int j = 0; j < path.size() ; j++) {
					moves.add(path.get(j));
				}
			}
			for(int h=0; h<moves.size(); h++){
				System.out.println("before " +moves.get(h).toString());
			}
			for (int j = 1; j < u.getDistance() ; j++) {
				moves.add(path.get(j));
			}
			UnitMovedCommand moveCommand = new UnitMovedCommand(userName, i, moves);
			try{
				outputStream.writeObject(moveCommand);
			}catch(IOException e){
				e.printStackTrace();
			}
			}
	}

	private void attackTurn() {
		ArrayList<Unit> compUnits = new ArrayList<>();
		compUnits = currentBoard.getCompUnits();
		ArrayList<Point> range = new ArrayList<>();
		for (int i = 0; i < compUnits.size(); i++) {
			Unit u = compUnits.get(i);
			range = currentBoard.findAttackRange(u.getLocation(), u.getAttackRange());
			for (int j = 0; j < range.size(); j++) {
				Point temp = new Point(range.get(j));
				if(currentBoard.checkIfEnemy(u, temp)){
					ArrayList <Unit> user= new ArrayList <>();
					user=currentBoard.getUserUnits();
					for(int l=0; l<user.size(); l++ ){
						if(temp == user.get(l).getLocation()){
							UnitAttackCommand attCommand= new UnitAttackCommand (userName,i, l );
							try{
								outputStream.writeObject(attCommand);
							}catch(IOException e){
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

	}

	private void sendEndTurnCommand() {
		// TODO Auto-generated method stub
		EndTurnCommand command = new EndTurnCommand(userName);
		try {
			outputStream.writeObject(command);
			// currentGameState=GameState.ChoosingAttack;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean moving=true;
	
	public boolean isMoving(){
		return moving;
	}

	public void newGame() {

	}

	@Override
	public void useItem(String source, int index, Item item) {
		// TODO Auto-generated method stub

	}
	
	public void unitAttacked(String source, int attackUnit, int defendUnit){
		ArrayList <Unit> comp= new ArrayList <>();
		comp=currentBoard.getCompUnits();
		ArrayList <Unit> user= new ArrayList <>();
		user=currentBoard.getUserUnits();
		if(source.equals(userName)){
			user.get(defendUnit).takeHit(comp.get(attackUnit).getAttackPower());
		}
		else{
			comp.get(defendUnit).takeHit(user.get(attackUnit).getAttackPower());
		}
	}

	@Override
	public void unitMoved(String source, int index, ArrayList<Point> moves) {
		
		for(int h=0; h<moves.size(); h++){
		System.out.println("after"+moves.get(h).toString());
		}
		int actualTotalMoveLength;
		
		Unit u;
		
		if(source.equals(userName)){
			u=currentBoard.getCompUnits().get(index);
		}
		else{
			u=currentBoard.getUserUnits().get(index);
		}
		//first, determine how many moves from the chosen list can actually be taken.
		u.setLocation(moves.get(index));
		this.currentBoard.getGameBoard()[u.getLocation().x][u.getLocation().y] =u.getCharRepresentation();
		
		//loop through each point on the path and tell the gameBoard the unit moved to each
		//new point. Only allow the unit to take its specified maxNum of moves
		for(int i=0; i<moves.size()-1; i++){
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
				System.out.println(u.getName()+"move up");
			}
			//if the move is downwards
			else if(x<dx && y==dy){
				currentBoard.moveDown(userName, u);
				System.out.println(u.getName()+"move down");
			}
			//if the move is to the right
			else if(x==dx && y<dy){
				currentBoard.moveRight(userName, u);
				System.out.println(u.getName()+"move right");
			}
			//if the move is left
			else if(x==dx && y>dy){
				currentBoard.moveLeft(userName, u);
				System.out.println(u.getName()+"move left");
			}
		}

		System.out.println(u.getLocation() + "test");
	System.out.println(u.getLocation() + "testComp");
		moving=false;


	}

	@Override
	public void pickUpItem(String source, Point p) {
		// TODO Auto-generated method stub

	}
}
