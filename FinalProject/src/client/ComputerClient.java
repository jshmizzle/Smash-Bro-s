package client;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
import command.UnitMovedCommand;

public class ComputerClient extends JFrame implements Client{

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
		//initializeFrame();
		ComputerServerHandler handler = new ComputerServerHandler(this, inputStream);
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
				userName = "computer";
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
		// mainMenuPanel = new MainMenuPanel(username, outputStream);
		// start with MainGamePanel for testing menus will be added later the
		// game comes first
		initializeGameBoard();

		gamePanel = new MainGamePanel("computer",currentBoard, outputStream);
		currentPanel = gamePanel;
		this.add(currentPanel).setVisible(true);
		this.pack();
		this.setVisible(true);
	}*/

	private void update(Command<?> command) {
		this.gamePanel.update(currentBoard);
	}

	public void createGameBoard(ArrayList<Unit> userUnits,
			ArrayList<Unit> compUnits, int map, int scenario) {
		currentBoard = new GameBoard(userUnits, compUnits, map, scenario);
		playingAlready = true;
	}

	/*public void useItem(String client, Unit u, Item item) {
		currentBoard.useThisItem(client, u, item);
	}*/

	public void welcomeToLobby(String client) {
		// open lobby for whoever connected
		if (playingAlready == true) {
			; // do nothing
		} else {
			// load lobby (probably needs work)
			askUserForInfo();// now the client has been logged into the server
			//initializeFrame();
			ComputerServerHandler handler = new ComputerServerHandler(this, inputStream);
			Thread t = new Thread(handler);
			t.start();
		}
	}

	public void attackUnit(String client, int fromIndex, int toIndex) {
		if(!client.equals("Computer")){
			currentBoard.attackUnit(currentBoard.getUserUnits().get(fromIndex),currentBoard.getCompUnits().get(toIndex) );
		}
		else
			currentBoard.attackUnit(currentBoard.getCompUnits().get(fromIndex),currentBoard.getUserUnits().get(toIndex));
	}

	public void endTurn(String client) {
		if(client.equals(userName)){
			myTurn = false;
		}
		else{
			myTurn = true;
			//TODO: gonna need to change this for multiplayer
			if(userName.equals("Computer"))
				currentBoard.resetCompMoves();
				executeProtocol();
			if(!userName.equals("Computer"))
				currentBoard.resetUserMoves();
			else
				;  //stuff to fill in for multiplayer
		}
	}



	private void executeProtocol() {
		// TODO Auto-generated method stub
		
		
		sendEndTurnCommand();
	}

	private void sendEndTurnCommand() {
		// TODO Auto-generated method stub
		EndTurnCommand command =new EndTurnCommand("Computer");
		try {
			serverOut.writeObject(moveCommand);
			currentGameState=GameState.ChoosingAttack;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void unitMoved(String source, ArrayList<Point> moves) {
		// TODO Auto-generated method stub
		
	}

	
	public void newGame(){
		
	}

	@Override
	public void useItem(String source, int index, Item item) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void unitMoved(String source, int index, ArrayList<Point> moves) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pickUpItem(String source, int index, Item item) {
		// TODO Auto-generated method stub
		
	}
}
