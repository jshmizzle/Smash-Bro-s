package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.GameBoard;
import model.Item;
import model.Scenario;
import model.Unit;
import client.Client;
import client.TRPGClient;
import command.Command;
import command.EndTurnCommand;
import command.TeleportUnitCommand;
import command.UnitAttackCommand;
import command.UnitMovedCommand;
import command.UseItemCommand;

@SuppressWarnings("serial")
public class MainGamePanel extends JPanel {

	char [][] currentBoard;
	private GameBoard gameBoard;
	int gameTileWidth, gameTileHeight;
	
	private Image redOrb, swordCursor, portal, boulder, grass, waypoint, 
	              tree, invalidMove, chest, attackRange, redDiamond, blueDiamond; 
	private Point cursorLocation;
	private ObjectOutputStream serverOut;
	private GameState currentGameState;
	private Unit currentUnit;
	private String source;
	private ArrayList<Unit> localUserUnitList, localOpponentUnitList;
	private boolean isHost, myTurn,showInventory, isAnimating;
	private JPanel inventoryPanel, statsPanel;
	private TRPGClient client;
	
	public MainGamePanel(String source, GameBoard startingBoard, TRPGClient client, ObjectOutputStream serverOut, boolean isHost) {
		this.serverOut=serverOut;
		this.source=source;
		this.isHost=isHost;
		this.client=client;
		
		//decide if the game starts off on our turn based on who is the host
		if(this.isHost)
			myTurn=true;
		else 
			myTurn=false;
		
		
		//determine the size of the JPanel
		this.setPreferredSize(new Dimension(600, 600));
		
		//initialize the game board that will be represented on the screen
		this.currentBoard=startingBoard.getGameBoard();
		this.gameBoard=startingBoard;
		this.localUserUnitList=gameBoard.getPlayerOneUnits();
		
		//we cannot assume that the current client's units are always going to be
		//the player one units. Check whether or not they are the host in order to know which player they are
		if(isHost){
			this.localUserUnitList=gameBoard.getPlayerOneUnits();
			this.localOpponentUnitList=gameBoard.getPlayerTwoUnits();
		}
		else{
			this.localUserUnitList=gameBoard.getPlayerTwoUnits();
			this.localOpponentUnitList=gameBoard.getPlayerOneUnits();
		}
		
		this.currentUnit=localUserUnitList.get(0);

		//Using the size of the panel determine the dimensions of tiles
		this.gameTileWidth=getWidth()/currentBoard[0].length;
		this.gameTileHeight=getHeight()/currentBoard.length;
		
		Point tempPoint=localUserUnitList.get(0).getLocation();
		cursorLocation=new Point(tempPoint.y, tempPoint.x);
				
		//add the key listener to allow the cursor to send 
		this.addKeyListener(new KeyManager());
		
		initializeImages();
		
		this.setLayout(null);
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.setVisible(true);
		this.currentGameState=GameState.CyclingThroughUnits;
	}
	
	private void initializeImages(){
		String baseDir = "C:"+System.getProperty("file.separator")
				+ "Users"+System.getProperty("file.separator") +
				"Jason"+System.getProperty("file.separator") +
				"CSC335"+System.getProperty("file.separator") +
				"FinalProjectRepository"+System.getProperty("file.separator") +
				"FinalProject"+System.getProperty("file.separator") +
				"images"+System.getProperty("file.separator");
		try {
			boulder=ImageIO.read(new File("images/Boulder.png"));
			grass=ImageIO.read(new File("images/TRPGgrass.png"));
			waypoint=ImageIO.read(new File("images/1GlowingOrb.png"));
			invalidMove=ImageIO.read(new File("images/notValidCursor.png"));
			tree=ImageIO.read(new File("images/TreeSprites1.png"));
			chest=ImageIO.read(new File("images/chestClosed.png"));
			swordCursor=ImageIO.read(new File("images/redSwords.png"));
			redOrb=ImageIO.read(new File("images/redOrb.png"));
			attackRange=ImageIO.read(new File("images/attackRange.png"));
			portal=ImageIO.read(new File("images/marioPipe.png"));
			redDiamond=ImageIO.read(new File("images/redGlowingDiamond.png"));
			blueDiamond=ImageIO.read(new File("images/blueGlowingDiamond.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D) g;
		
		this.gameTileWidth=getWidth()/currentBoard[0].length;
		this.gameTileHeight=getHeight()/currentBoard.length;
		
		
		for(int row=0; row<currentBoard.length; row++){
			
			//first thing to do, is to draw the attack range of the unit if it is choosing
			//its attack that way, the units will get drawn above the range
			
			for(int col=0; col<currentBoard[0].length; col++){
				if(currentBoard[row][col]==' '){
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
				}
				else if(currentBoard[row][col]=='#'){
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					g2.drawImage(boulder, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
				}
				else if(currentBoard[row][col]=='!'){
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					g2.drawImage(tree, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
				}
				else if(currentBoard[row][col]=='@'){
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					g2.drawImage(chest, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
				}
				else if(currentBoard[row][col]=='%'){
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					g2.drawImage(portal, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
				}
				else{
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
				}
			}
		}
		//now draw the units individually by accessing the lists, not by looking at their 
		//char on the board, this will prevent from thinking every unit with the same char 
		//dies at the same time
		drawTheUnits(g2);
		
		drawCursor(g2);
		if(currentGameState==GameState.ChoosingMove){
			drawShortestPathLineToCursor(g2);
		}
		else if(currentGameState==GameState.CyclingThroughUnits){
			if(this.showStats==true){
				drawStatsPanel();
			}
		}
		else if(currentGameState==GameState.ChoosingAttack){
			drawAttackCursor(g2);
			drawAttackRange(g2);
			if(this.showStats==true){
				drawStatsPanel();
			}
		}
		//if the user ever wants to see the inventory they can
		if(this.showInventory==true){
			drawInventory();
		}
		
	}

	private void drawTheUnits(Graphics2D g2) {
		for(Unit curr: localUserUnitList){
			//draw the unit itself
			if(!curr.isAlive()){
			curr.draw(g2, gameTileHeight, gameTileWidth);
			//now draw the blue diamond above its head to designate it as one of ours
			g2.drawImage(blueDiamond, curr.getLocation().y*gameTileWidth, curr.getLocation().x*gameTileHeight-2*gameTileHeight/3, gameTileWidth, gameTileHeight, null);
			}
		}
		for(Unit curr: localOpponentUnitList){
			//draw the unit itself
			if(!curr.isAlive()){
			curr.draw(g2, gameTileHeight, gameTileWidth);
			//now draw the red diamond above its head to show it's an enemy
			g2.drawImage(redDiamond, curr.getLocation().y*gameTileWidth, curr.getLocation().x*gameTileHeight-2*gameTileHeight/3, gameTileWidth, gameTileHeight, null);
			}
		}
		for(Unit curr: localUserUnitList){
			//draw the unit itself
			if(curr.isAlive()){
			curr.draw(g2, gameTileHeight, gameTileWidth);
			//now draw the blue diamond above its head to designate it as one of ours
			g2.drawImage(blueDiamond, curr.getLocation().y*gameTileWidth, curr.getLocation().x*gameTileHeight-2*gameTileHeight/3, gameTileWidth, gameTileHeight, null);
			}
		}
		for(Unit curr: localOpponentUnitList){
			//draw the unit itself
			if(curr.isAlive()){
			curr.draw(g2, gameTileHeight, gameTileWidth);
			//now draw the red diamond above its head to show it's an enemy
			g2.drawImage(redDiamond, curr.getLocation().y*gameTileWidth, curr.getLocation().x*gameTileHeight-2*gameTileHeight/3, gameTileWidth, gameTileHeight, null);
			}
		}
		
	}

	private void drawCursor(Graphics2D g2){
		g2.setColor(Color.RED);
		g2.drawRect(cursorLocation.x*gameTileWidth, cursorLocation.y*gameTileHeight, gameTileWidth-1, gameTileHeight-1);
	}
	
	private void drawAttackCursor(Graphics2D g2){
		if(!currentUnit.getLocation().equals(new Point(cursorLocation.y, cursorLocation.x))){
			g2.drawImage(swordCursor, cursorLocation.x*gameTileWidth, cursorLocation.y*gameTileHeight, gameTileWidth, gameTileHeight, null);
		}
	}
	
	private void drawAttackRange(Graphics2D g2){
		int attackRange=currentUnit.getAttackRange();
		int x=currentUnit.getLocation().y;
		int y=currentUnit.getLocation().x;
		
		//draw in all directions
		for(int i=attackRange, y1=y+1, x1=x+1, y2=y-1, x2=x-1; i>0; i--, y1++, x1++, y2--, x2--){
			//draw up
			g2.drawImage(this.attackRange, x*gameTileWidth, y1*gameTileHeight, gameTileWidth, gameTileHeight, null);
			//draw down
			g2.drawImage(this.attackRange, x*gameTileWidth, y2*gameTileHeight, gameTileWidth, gameTileHeight, null);
			//draw left
			g2.drawImage(this.attackRange, x2*gameTileWidth, y*gameTileHeight, gameTileWidth, gameTileHeight, null);
			//draw right
			g2.drawImage(this.attackRange, x1*gameTileWidth, y*gameTileHeight, gameTileWidth, gameTileHeight, null);
		}
	}
	
	ArrayList<JPanel> statsPanelList=new ArrayList<>();
	private void drawStatsPanel(){
		if(!(currentUnit.getLocation().y>=15) && !(currentUnit.getLocation().y<3)){
			if(statsPanel!=null){
				MainGamePanel.this.remove(statsPanel);
			}
			statsPanel=new UnitStatusPanel(currentUnit);
			
			setStatsPanelLocationBasedOnContext();
			
			statsPanel.setSize(gameTileWidth*2, gameTileHeight*3);
			MainGamePanel.this.add(statsPanel).setVisible(true);
		}
		
		
		
		for(int i=gameBoard.getScenario()==Scenario.Princess ? 1:0; i<(gameBoard.getScenario()==Scenario.Princess ? 6:5); i++){
			int shift=gameBoard.getScenario()==Scenario.Princess ? 1:0;
			
			Unit currUnit=localUserUnitList.get(i);
			JPanel panel=new UnitStatusPanel(currUnit);
			panel.setSize(getWidth()/8, getHeight()/5);
			panel.setLocation(0, getHeight()/5*(i-shift));
			this.add(panel).setVisible(true);
			
			statsPanelList.add(panel);
			
			currUnit=localOpponentUnitList.get(i);
			panel=new UnitStatusPanel(currUnit);
			panel.setSize(getWidth()/8, getHeight()/5);
			panel.setLocation(7*getWidth()/8, getHeight()/5*(i-shift));
			this.add(panel).setVisible(true);
			
			statsPanelList.add(panel);
		}
	}
	
 
	private void setStatsPanelLocationBasedOnContext(){
		//we need to make sure that the panel is not drawn off the screen at the very top
		//or the very right
		Point temp=currentUnit.getLocation();		
		
		if(temp.x>2){//not near top edge
			if(temp.y<=17)//not near right edge
				statsPanel.setLocation( (temp.y+1)*gameTileWidth, (temp.x-2)*gameTileHeight);
			else//near right edge
				statsPanel.setLocation((temp.y-2)*gameTileWidth, (temp.x-2)*gameTileHeight);
		}
		else{//near top edge
			if(temp.y<=17)//not near right edge
				statsPanel.setLocation( (temp.y+1)*gameTileWidth, 0);
			else//near right edge
				statsPanel.setLocation((temp.y-2)*gameTileWidth, 0);
		}
	}
	
	private void drawInventory() {
		if(inventoryPanel!=null){
			MainGamePanel.this.remove(inventoryPanel);
		}
		inventoryPanel=new ItemPanel(client.getItemList());
				
		inventoryPanel.setSize(getWidth()/2, gameTileHeight*3);
		inventoryPanel.setLocation(getWidth()/4, getHeight()/2-(gameTileHeight-2));
		MainGamePanel.this.add(inventoryPanel).setVisible(true);
	}
	
	private ArrayList<Point> previousPath;
	
	private void drawShortestPathLineToCursor(Graphics g){
		Graphics2D g2=(Graphics2D)g;
		
		//draw the path as blue up to where the unit can travel, and red beyond that point
		int moveDistance=currentUnit.getMovesLeft();
		
		Point temp=new Point(cursorLocation.y, cursorLocation.x);
		if(gameBoard.checkAvailable(temp)){
			ArrayList<Point> path = gameBoard.findShortestPath(currentUnit.getLocation(), temp);
			previousPath=path;
			
			
			//loop through the points on the path that the player will follow and draw a waypoint
			//icon at each of those points on the board to visualize it for the player
			if(path!=null){
				for (int i = 0; i < path.size(); i++) {
					int x = path.get(i).y;
					int y = path.get(i).x;
					
					if(moveDistance>=0)
						g2.drawImage(waypoint, x * gameTileWidth, y * gameTileHeight, null);
					else
						g2.drawImage(redOrb, (x-2) * gameTileWidth, (y-1) * gameTileHeight -7, null);
					
					moveDistance--;
				}
			}
		}
		else if(previousPath!=null){
			for (int i = 0; i < previousPath.size(); i++) {
				System.out.println("drawing");
				int x = previousPath.get(i).y;
				int y = previousPath.get(i).x;
				
				//still give the effect of drawing blue orbs where they can walk and red orbs 
				//at all of the points they cannot reach
				if(moveDistance>=0)
					g2.drawImage(waypoint, x * gameTileWidth, y * gameTileHeight, null);
				else
					g2.drawImage(redOrb, (x-2) * gameTileWidth, (y-1) * gameTileHeight -7, null);

				moveDistance--;
			}
		}
		
		if(!gameBoard.checkAvailable(new Point(cursorLocation.y, cursorLocation.x))){
			if(!(currentUnit.getLocation().x==cursorLocation.y && currentUnit.getLocation().y==cursorLocation.x))
				g2.drawImage(invalidMove, cursorLocation.x * gameTileWidth, cursorLocation.y * gameTileHeight, gameTileWidth, gameTileHeight, null);
			
		}
	}
		
	private boolean showStats=false;
	private int unitIndex=0;


	private class KeyManager implements KeyListener{

		
		@Override
		public void keyPressed(KeyEvent arg0) {
			int key=arg0.getKeyCode();
			if(myTurn){
				//just allow them to use their hard earned items any time they have the 
				//inventory open
				if(showInventory==true){
					int temp=0;
					switch(key){
					case KeyEvent.VK_3:temp++; 
					case KeyEvent.VK_2:temp++;
					case KeyEvent.VK_1:temp++;
						if(client.getItemList().size()>=temp){
							try{
								Item item=client.getItemList().get(temp-1);
								Command<Client> command=new UseItemCommand(source, unitIndex, temp-1);
								serverOut.writeObject(command);
								repaint();
							}catch(IOException e){
								e.printStackTrace();
							}
						}
					}
				}
				if(currentGameState==GameState.ChoosingMove){
					if(key==KeyEvent.VK_RIGHT && cursorLocation.x<19){
						cursorLocation.translate(1, 0);
						repaint();
					}
					else if(key==KeyEvent.VK_DOWN && cursorLocation.y<19){
						cursorLocation.translate(0,1);
						repaint();
					}
					else if(key==KeyEvent.VK_UP && cursorLocation.y>0){
						cursorLocation.translate(0, -1);
						repaint();
					}
					else if(key==KeyEvent.VK_LEFT && cursorLocation.x>0){
						cursorLocation.translate(-1, 0);
						repaint();
					}
					else if(key==KeyEvent.VK_BACK_SPACE){
						currentGameState=GameState.CyclingThroughUnits;
						Point unitPoint=gameBoard.getPlayerOneUnits().get(unitIndex).getLocation();
						cursorLocation=swap(unitPoint);
						if(statsPanel!=null){
							MainGamePanel.this.add(statsPanel);
						}
						repaint();
					}
					else if(key==KeyEvent.VK_ENTER){
						//The player wants this unit to move to this location so we have to go and
						//check if that is a valid destination.
						if(gameBoard.checkAvailable(swap(cursorLocation))){
							
							Point offsetCorrectedCursor=swap(cursorLocation);
							ArrayList<Point> path=gameBoard.findShortestPath(currentUnit.getLocation(), offsetCorrectedCursor);
							if(path!=null){
								UnitMovedCommand moveCommand =new UnitMovedCommand(source, unitIndex, path);
								try {
									serverOut.writeObject(moveCommand);
		
									//don't just go straight to letting them attack they can
									//manually specify if they want to attack or not
									currentGameState=GameState.CyclingThroughUnits;
									
									previousPath=null;
									
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							
							
							//now if the unit had just landed on a portal then randomly move it
							//and also tell the other client
							int lastPointIndex;
							if(path.size()>currentUnit.getMovesLeft()){
								lastPointIndex=currentUnit.getMovesLeft();
							}
							else
								lastPointIndex=path.size()-1;
							
							int finalX=path.get(lastPointIndex).x;
							int finalY=path.get(lastPointIndex).y;
							if(currentBoard[finalX][finalY] == '%'){
								Random random = new Random();
								
								int xPoint = random.nextInt(19);
								int yPoint = random.nextInt(19);
								if (gameBoard.checkAvailable(new Point(xPoint,yPoint))){
									
								}
								else{
									do{
										xPoint = random.nextInt(19);
										yPoint = random.nextInt(19);
									}while(!gameBoard.checkAvailable(new Point(xPoint, yPoint)));
								}
								Point teleportLocation=new Point(xPoint, yPoint);
								
								try{
									Command command = new TeleportUnitCommand(source, unitIndex, teleportLocation);
									serverOut.writeObject(command);
								}catch(IOException e){
									e.printStackTrace();
								}
								cursorLocation=new Point(yPoint, xPoint);
								repaint();
							}
						}
					}
					else if(key==KeyEvent.VK_E){
						EndTurnCommand endTurn=new EndTurnCommand(source);
						try{
							serverOut.writeObject(endTurn);
							currentGameState=GameState.CyclingThroughUnits;

							showInventory=false;
							repaint();
						}catch(IOException e){
							e.printStackTrace();
						}
					}
					else if(key==KeyEvent.VK_I){
						if(showInventory){
							showInventory=false;
							MainGamePanel.this.remove(inventoryPanel);
							repaint();
						}
						else{
							showInventory=true;
							repaint();
						}
					}
				}
				//if the player is trying to just choose his current unit then the cursor will 
				//just jump to the location of the unit next or previously in line.
				else if(currentGameState==GameState.CyclingThroughUnits){
					
					if(key==KeyEvent.VK_RIGHT){
						if(unitIndex<localUserUnitList.size()-1){
							unitIndex++;
						}
						else if(unitIndex==localUserUnitList.size()-1){
							unitIndex=0;
						}
	
	//					currentUnit=gameBoard.getUserUnits().get(unitIndex);
						currentUnit=localUserUnitList.get(unitIndex);
						
						Point unitPoint=localUserUnitList.get(unitIndex).getLocation();
						cursorLocation=swap(unitPoint);
						repaint();
					}
					else if(key==KeyEvent.VK_LEFT){
						if(unitIndex>0){
							unitIndex--;
						}
						else if (unitIndex==0){
							unitIndex=localUserUnitList.size()-1;
						}
	//					currentUnit=gameBoard.getUserUnits().get(unitIndex);
						currentUnit=localUserUnitList.get(unitIndex);
						
						Point unitPoint=localUserUnitList.get(unitIndex).getLocation();
						cursorLocation=swap(unitPoint);
						repaint();
					}
					//if the user presses enter while cycling through units
					else if(key==KeyEvent.VK_ENTER){
						
						if(gameBoard.getScenario().getValue() == 1){
							if(unitIndex==0)
								return;
						}
						if(localUserUnitList.get(unitIndex).isAlive()){
							currentUnit=localUserUnitList.get(unitIndex);
		
							currentGameState=GameState.ChoosingMove;
							if(MainGamePanel.this.statsPanel!=null){
								MainGamePanel.this.remove(statsPanel);
							}
							repaint();
						}
					}
					//if the user presses 's' the stats panel will be brought up for the current unit
					else if(key==KeyEvent.VK_S){
						if(showStats==true){
							showStats=false;
							if(statsPanel!=null)
								MainGamePanel.this.remove(statsPanel);
							//also remove all of the other stats panels that are on the edges of the screen
							for(int i=0; i<statsPanelList.size(); i++){
								MainGamePanel.this.remove(statsPanelList.get(i));
							}
							statsPanelList.clear();
							repaint();
						}
						else{
							showStats=true;
							repaint();
						}
					}
					else if(key==KeyEvent.VK_I){
						if(showInventory){
							showInventory=false;
							MainGamePanel.this.remove(inventoryPanel);
							repaint();
						}
						else{
							showInventory=true;
							repaint();
						}
					}
					else if(key==KeyEvent.VK_E){
						EndTurnCommand endTurn=new EndTurnCommand(source);
						try{
							serverOut.writeObject(endTurn);
							currentGameState=GameState.CyclingThroughUnits;
							if(showStats==true){
								showStats=false;
								MainGamePanel.this.remove(statsPanel);
								
								showInventory=false;
								repaint();
							}
						}catch(IOException e){
							e.printStackTrace();
						}
					}
					//allow the player to manually make the decision to attack
					else if(key==KeyEvent.VK_A && currentUnit.isAlive()){
						if(!currentUnit.checkIfAlreadyAttackedThisTurn()){
							currentGameState=GameState.ChoosingAttack;
							repaint();
						}
					}
				}
				//now after the player has had his unit move, he freely moves the cursor to 
				//attempt to make an attack selection
				else if(currentGameState==GameState.ChoosingAttack){
					if(key==KeyEvent.VK_RIGHT && cursorLocation.x<19){
						cursorLocation.translate(1, 0);
						repaint();
					}
					else if(key==KeyEvent.VK_DOWN && cursorLocation.y<19){
						cursorLocation.translate(0,1);
						repaint();
					}
					else if(key==KeyEvent.VK_UP && cursorLocation.y>0){
						cursorLocation.translate(0, -1);
						repaint();
					}
					else if(key==KeyEvent.VK_LEFT && cursorLocation.x>0){
						cursorLocation.translate(-1, 0);
						repaint();
					}
					else if(key==KeyEvent.VK_ENTER){
						if(gameBoard.checkIfEnemy(currentUnit,swap(cursorLocation)) && currentUnit.isAlive()){
							System.out.println("found enemy");
							int enemyIndex=-99;

							for(int i=0; i<localOpponentUnitList.size(); i++){
								Unit curr=localOpponentUnitList.get(i);
								if(curr.getLocation().equals(swap(cursorLocation)) && curr.isAlive()){
									enemyIndex=i;
								}
							}
							//only actually send the command if the unit is within range and its
							//line of fire is not obstructed
							if (gameBoard.checkOpenLineOfFire(currentUnit, swap(cursorLocation)) && enemyIndex!=-99) {
								UnitAttackCommand moveCommand = new UnitAttackCommand(source, unitIndex, enemyIndex);
								System.out.println("attacking");
								try {
									serverOut.writeObject(moveCommand);
	
									currentGameState = GameState.CyclingThroughUnits;
									previousPath = null;
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
					else if(key==KeyEvent.VK_BACK_SPACE){
						currentGameState=GameState.CyclingThroughUnits;
						cursorLocation=swap(currentUnit.getLocation());
						repaint();
					}
					else if(key==KeyEvent.VK_E){
						EndTurnCommand endTurn=new EndTurnCommand(source);
						try{
							serverOut.writeObject(endTurn);
							currentGameState=GameState.CyclingThroughUnits;

							showInventory=false;
							repaint();
						}catch(IOException e){
							e.printStackTrace();
						}
					}
					else if(key==KeyEvent.VK_I){
						if(showInventory){
							showInventory=false;
							MainGamePanel.this.remove(inventoryPanel);
							repaint();
						}
						else{
							showInventory=true;
							repaint();
						}
					}
					else if(key==KeyEvent.VK_S){
						if(showStats==true){
							showStats=false;
							MainGamePanel.this.remove(statsPanel);
							for(JPanel curr: statsPanelList)
								MainGamePanel.this.remove(curr);
							
							repaint();
						}
						else{
							showStats=true;
							repaint();
						}
					}
					//allow the player to toggle off of the attack mode
					else if(key==KeyEvent.VK_A){
						if(!currentUnit.checkIfAlreadyAttackedThisTurn()){
							currentGameState=GameState.CyclingThroughUnits;
							cursorLocation=swap(currentUnit.getLocation());
							repaint();
						}
					}
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}
		
	}
	
	public void update(GameBoard currentGameBoard){
		this.currentBoard=currentGameBoard.getGameBoard();
		this.gameBoard=currentGameBoard;
		
		if(!isAnimating)
			this.repaint();
	}
	
	public void updateCurrentUnitAfterMove(Unit u){
		this.localUserUnitList.set(unitIndex, u);
	}
	
	public void myTurn(){
		if(myTurn==false)
			myTurn=true;
		else 
			myTurn=false;
	}
	
	/**
	 * Return takes a Point and returns a new one with the x and y coordinates swapped. 
	 * @param original The original Point to have its points swapped.
	 * @return
	 */
	private Point swap(Point original){
		return new Point(original.y, original.x);
	}
	
	private ArrayList<UnitHolder> pendingAnimationUnits=new ArrayList<>();
	private AttackPanel attackPanel;
	public void animateAttack(Unit attacker, Unit attacked){
		
		if(pendingAnimationUnits.size()>=1){
			pendingAnimationUnits.add(new UnitHolder(attacker, attacked));
		}
		else{
			pendingAnimationUnits.add(new UnitHolder(attacker, attacked));

			attackPanel= new AttackPanel(attacker, attacked);
			attackPanel.setLocation(0,getHeight()/3);
			attackPanel.setSize(600, 200);
			attackPanel.addListener(new AnimationOverListener());
			
			
			this.add(attackPanel).setVisible(true);
			isAnimating=true;
		}
	}
	
	
	public class AnimationOverListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			MainGamePanel.this.remove(attackPanel);
			
			pendingAnimationUnits.remove(0);//pop the "stack"
			
			
			if(pendingAnimationUnits.size()>=1){
				Unit attacker=pendingAnimationUnits.get(0).getFirst();
				Unit attacked=pendingAnimationUnits.get(0).getSecond();

				attackPanel= new AttackPanel(attacker, attacked);
				attackPanel.setLocation(0,getHeight()/3);
				attackPanel.setSize(600, 200);
				attackPanel.addListener(new AnimationOverListener());
				
				
				MainGamePanel.this.add(attackPanel).setVisible(true);
			}

			else{
				isAnimating=false;
				repaint();
			}
		}
		
	}
	
	private class UnitHolder{
		private Unit first, second;
		public UnitHolder(Unit first, Unit second){
			this.first=first;
			this.second=second;
		}
		
		public Unit getFirst(){
			return first;
		}
		
		public Unit getSecond(){
			return second;
		}
	}
}
