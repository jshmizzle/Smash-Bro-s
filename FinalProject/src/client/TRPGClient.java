package client;

import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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

import command.PickUpItemCommand;
import model.GameBoard;
import model.Item;
import model.Map;
import model.Potion;
import model.Rage;
import model.Scenario;
import model.Shield;
import model.Sneakers;
import model.Sniper;
import model.Unit;
import GUI.CharacterSelectPanel;
import GUI.GameLobby;
import GUI.MainGamePanel;
import GUI.MainMenuPanel;
import GUI.SinglePlayerMapAndScenarioSelect;
import GUI.WaitingOnCharacterSelection;

/**
 * TRPGClient is the main GUI meant for human players only. 
 * It connects to the server and receives commands needed to make 
 * the necessary changes client-side.
 * @author The Other Guys
 */
public class TRPGClient extends JFrame implements Client {

	public String host, userName;
	private int port = 0;
	private Map mapChoice; 
	private Scenario scenarioChoice;
	private Socket server;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private JPanel currentPanel;
	private GameBoard currentBoard;
	private boolean isHost = false,singlePlayer = false;
	private boolean myTurn = true, moving = true;
	private ArrayList<Item> itemList=new ArrayList<>(), opponentItemList=new ArrayList<>();
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
	/**
	 * This client is for human players only. It connects to the server 
	 * and receives commands needed to make the necessary changes.
	 */
	public TRPGClient() {
		askUserForInfo();// now the client has been logged into the server'
		ServerHandler handler = new ServerHandler(this, inputStream);
		Thread t = new Thread(handler);
		t.start();

		this.setTitle("Smash Bro's");
		
		currentPanel = new MainMenuPanel(userName, outputStream);
		this.addWindowListener(new WindowClosingListener());
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
	 *            The user name which you will be logging into the server with.
	 * 
	 * @see TRPGQuickStart
	 */

	public TRPGClient(String host, int port, String username){
		this.host=host;
		this.port=port;
		this.userName=username;
		this.setTitle("Smash Bro's");
		
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

	
	/*****************************************************************************************************/
	/**
	 * Window listener that listens for when the user closes the game
	 * window. When the window is closed, the method to save the game
	 * is called.
	 */
	private class WindowClosingListener implements WindowListener {
		// most of these not used, but "WindowClosing" needed
		@Override
		public void windowActivated(WindowEvent arg0) {

		}

		@Override
		public void windowClosed(WindowEvent arg0) {
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			saveGame();
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {

		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {

		}

		@Override
		public void windowIconified(WindowEvent arg0) {

		}

		@Override
		public void windowOpened(WindowEvent arg0) {

		}

	}

	/*****************************************************************************************************/

	
	/**
	 * Called from the LoadSavedGame command.
	 * Loads a saved GameBoard from file and assigns the proper variables
	 * from the loaded game.  Creates a computer client and calls a method
	 * from its class to similarly assign the needed variables for the game
	 * to pick up where it left off.
	 */
	public void loadSavedGame() {
		try {
			FileInputStream filein = new FileInputStream("SmashBros.out");
			ObjectInputStream objectin = new ObjectInputStream(filein);
			
			//these might be out of order..
			userName = (String) objectin.readObject();
			currentBoard = (GameBoard) objectin.readObject();
			itemList = (ArrayList<Item>) objectin.readObject();
			opponentItemList = (ArrayList<Item>) objectin.readObject();
			objectin.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		isHost=true;
		singlePlayer=true;
		mapChoice = currentBoard.getMap();
		scenarioChoice = currentBoard.getScenario();
		myUnits = currentBoard.getPlayerOneUnits();
		opponentUnits = currentBoard.getPlayerTwoUnits();
		
		//create a new computer client and give it the game info it needs from previous game
		ComputerClient compClient = new ComputerClient();
		compClient.loadSavedComputer(currentBoard, itemList, opponentItemList);
		
		this.remove(currentPanel);
		this.setVisible(false);
		currentPanel=new MainGamePanel(userName, currentBoard, this, outputStream, isHost);
		currentPanel.grabFocus();
		this.add(currentPanel);
		currentPanel.requestFocus(true);
		this.pack();
		this.setVisible(true);
		this.repaint();
	}
	
	/**
	 * Saves the current GameBoard in a file to be read in at a later time
	 */
	private void saveGame() {
		//if it's single player and the game is NOT over
		if ((singlePlayer == true) && (!currentBoard.gameOver())) {
			File file = new File("SmashBros.out");
			try {
				FileOutputStream fileout = new FileOutputStream(file);
				ObjectOutputStream out = new ObjectOutputStream(fileout);
				out.writeObject(userName);
				out.writeObject(currentBoard);
				out.writeObject(itemList);
				out.writeObject(opponentItemList);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Called from StartSinglePlayerGame command when a new single player
	 * game needs to be created.
	 */
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
	/**
	 * This method is called from JoinMultiPlayerGameCommand when a user
	 * clicks "Join a Game."
	 * @param source = user name
	 */
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
	/**
	 * Updates the lobby for multiplayer.
	 * @param users
	 */
	public void updateLobby(ArrayList<String> users){
		if(currentPanel instanceof GameLobby){
			((GameLobby)currentPanel).updateClients(users);
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
	/**
	 * This method gets called from SetUserUnits command after the player has selected his team.
	 * It places the unit list in the variables for this class, then moves to the
	 * next panel in the GUI.
	 * @param source = client name
	 * @param userUnits = arraylist of units
	 */
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
				this.currentPanel=new WaitingOnCharacterSelection(userName, myUnits, opponentUnits, scenarioChoice, outputStream);
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
				this.currentPanel=new WaitingOnCharacterSelection(userName, myUnits, opponentUnits, scenarioChoice, outputStream);
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
	/**
	 * Starts a new game.
	 */
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
	/**
	 * Attempts to collect the ip address, port, and user name from the user.
	 */
	private void askUserForInfo() {
		while (port == 0) {
			try {
				host = JOptionPane.showInputDialog(null,
						"What host would you like to connect to?");
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
						.showInputDialog("Enter your Smash Bro's user name");
				outputStream.writeObject(userName);
				serverAccepted = (String) inputStream.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Not used. This was only useful when we were not letting the 
	 * players pick their own units
	 */
	private void initializeFrame() {
		// mainMenuPanel = new MainMenuPanel(username, outputStream);
		// initializeGameBoard();
		// gamePanel=new MainGamePanel(userName, currentBoard, this,
		// outputStream);
		// currentPanel=gamePanel;
		currentPanel = new GameLobby(userName, outputStream, isHost);
		this.add(currentPanel).setVisible(true);
		this.pack();
		this.setVisible(true);
	}
	/**
	 * This method activates an item and applies it to the chosen unit.
	 * @param client = user name
	 * @param unitIndex = indice in unit list
	 * @param itemIndex = indice in item list
	 */
	public void useItem(String client, int unitIndex, int itemIndex) {
		Item item;
		
		if (client.compareTo(userName)==0) {
			item=itemList.get(itemIndex);
			currentBoard.useThisItem(client, myUnits.get(unitIndex), item);
			itemList.remove(item);
		} else {
			item=opponentItemList.get(itemIndex);
			currentBoard.useThisItem(client, opponentUnits.get(unitIndex), item);
			opponentItemList.remove(item);
		}
	}

	/**
	 * This method gets called from UnitAttackCommand when a unit has been attacked.
	 * It makes the proper adjustments in health based on which
	 * unit attacked, and which unit received the damage.
	 * @param client = user name
	 * @param fromIndex = indice of unit in the unit list attacking from
	 * @param toIndex = indice of unit in the unit list attacking
	 */
	public void attackUnit(String client, int fromIndex, int toIndex) {
		//if the attack was sent by you then the unit from your list attacks the unit from the
		//opponent's list, make sure to take that into account. Doesn't matter who's hosting.
		if (client.equals(userName)) {
			currentBoard.attackUnit(myUnits.get(fromIndex),opponentUnits.get(toIndex));
		} 
		else {
			currentBoard.attackUnit(opponentUnits.get(fromIndex), myUnits.get(toIndex));
		}
		currentPanel.repaint();
		// if the game is over let us know!	
		if (scenarioChoice == Scenario.Princess) {
			if (myUnits.get(0).getHealth() <= 0 || opponentUnits.get(0).getHealth() <= 0) {
				if (myUnits.get(0).getHealth() <= 0) {
					// client lost
					currentPanel.repaint();
					JOptionPane.showMessageDialog(null,"Game Over, you lose!");
				}
				if (opponentUnits.get(0).getHealth() <= 0) {
					// client won
					currentPanel.repaint();
					JOptionPane.showMessageDialog(null,"Congratulations, you won!");
				}
			}
		}
		else{//it is the deathmatch scenario
			if(allUnitsDead(myUnits)){
				// client lost
				currentPanel.repaint();
				JOptionPane.showMessageDialog(null,"Game Over, you lose!");
			}
			else if(allUnitsDead(opponentUnits)){
				// client won
				currentPanel.repaint();
				JOptionPane.showMessageDialog(null,"Congratulations, you won!");
			}
		}
	}
	/**
	 * Checks if all units are dead, returns false if any are alive.
	 * @param units = list of units
	 * @return
	 */
	private boolean allUnitsDead(ArrayList<Unit> units){
		for(Unit curr: units){
			if(curr.isAlive())
				return false;
		}
		return true;
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
	
	/**
	 * When an item is walked over, this method gets called from PickUpItemCommand
	 * to place a randomly generated item into the item list
	 * belonging to the user whose unit grabbed the item.
	 * @param client
	 */
	public void pickUpItem(String client) {
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
		if (client.equals(userName)) {
			itemList.add(item);
		} else{
			opponentItemList.add(item);
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

			//before you move to the next point and replace the char that was there with your 
			//own, check if you should be receiving an item!
			if(source.equals(userName))	
				checkIfTheUnitWalkedOverAnItem(source, dx, dy);
			
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
		 
		moving = false;
	}

	/**
	 * Checks if a unit walked over an item, picks it up
	 * and puts it in the proper item list if needed.
	 * @param source = user name
	 * @param x = row
	 * @param y = col
	 */
	private void checkIfTheUnitWalkedOverAnItem(String source,int x, int y) {
		if(currentBoard.getGameBoard()[x][y]=='@'){
			//the unit is currently on top of an item
			PickUpItemCommand command = new PickUpItemCommand(source);
			try {
				outputStream.writeObject(command);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(opponentItemList);
		}
	}

	public boolean isMoving() {
		return moving;
	}
	
	public ArrayList<Item> getItemList(){
		return itemList;
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
			Unit unit=myUnits.get(unitIndex);
			currentBoard.getGameBoard()[unit.getLocation().x][unit.getLocation().y]=' ';
			unit.setLocation(teleLocation);
			currentBoard.getGameBoard()[unit.getLocation().x][unit.getLocation().y]=unit.getCharRepresentation();
		}
		else{
			Unit unit=opponentUnits.get(unitIndex);
			currentBoard.getGameBoard()[unit.getLocation().x][unit.getLocation().y]=' ';
			unit.setLocation(teleLocation);
			currentBoard.getGameBoard()[unit.getLocation().x][unit.getLocation().y]=unit.getCharRepresentation();
		}
	}
} // End Class TRPGClient
