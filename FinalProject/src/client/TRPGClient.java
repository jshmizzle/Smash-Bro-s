package client;

import java.awt.Point;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import command.ServerHandler;
import model.GameBoard;
import model.Item;
import model.Unit;
import GUI.CharacterSelectPanel;
import GUI.MainGamePanel;
import GUI.MainMenuPanel;

public class TRPGClient extends JFrame {

	private String host, username;
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

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
				username = JOptionPane
						.showInputDialog("Enter your TRPG username");
				outputStream.writeObject(username);
				serverAccepted = (String) inputStream.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initializeFrame() {
		mainMenuPanel = new MainMenuPanel(username, outputStream);
		this.add(mainMenuPanel).setVisible(true);
		this.setSize(1000, 650);
		this.setVisible(true);
	}

	public void update(GameBoard currentBoard) {
		this.gamePanel.update(currentBoard);
	}

	public void createGameBoard(ArrayList<Unit> userUnits,
			ArrayList<Unit> compUnits, int map, int scenario) {
		currentBoard = new GameBoard(userUnits, compUnits, map, scenario);
		playingAlready = true;
	}

	public void useItem(String client, Unit u, Item item) {
		currentBoard.useThisItem(client, u, item);
	}

	public void moveUnitLeft(String client, Unit u, Point p) {
		currentBoard.moveLeft(client, u);
	}

	public void moveUnitRight(String client, Unit u, Point p) {
		currentBoard.moveRight(client, u);
	}

	public void moveUnitDown(String client, Unit u, Point p) {
		currentBoard.moveDown(client, u);
	}

	public void moveUnitUp(String client, Unit u, Point p) {
		currentBoard.moveUp(client, u);
	}

	public void welcomeToLobby(String source) {
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
}
