package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.GameBoard;
import model.Unit;
import client.TRPGClient;

import command.EndTurnCommand;
import command.UnitAttackCommand;
import command.UnitMovedCommand;

public class MainGamePanel extends JPanel {

	char [][] currentBoard;
	private GameBoard gameBoard;
	int gameTileWidth, gameTileHeight;
	
	private Image boulder, megaman, sonic, grass, mario, headstone, goku, link, princess, waypoint, tree, invalidMove, chest; 

	private Point cursorLocation;
	private ObjectOutputStream serverOut;
	private GameState currentGameState;
	private Unit currentUnit;
	private UnitStatusPanel statsPanel;
	private String source;
	private TRPGClient client;
	private ArrayList<Unit> localUserUnitList;
	
	public MainGamePanel(String source, GameBoard startingBoard, TRPGClient client, ObjectOutputStream serverOut) {
		this.serverOut=serverOut;
		this.source=source;
		this.client=client;
		
		//determine the size of the JPanel
		this.setPreferredSize(new Dimension(600, 600));
		
		//initialize the game board that will be represented on the screen
		this.currentBoard=startingBoard.getGameBoard();
		this.gameBoard=startingBoard;
		this.localUserUnitList=gameBoard.getUserUnits();
		this.currentUnit=localUserUnitList.get(1);
//		this.currentUnit=gameBoard.getUserUnits().get(1);
		
		//Using the size of the panel determine the dimensions of tiles
		this.gameTileWidth=getWidth()/currentBoard[0].length;
		this.gameTileHeight=getHeight()/currentBoard.length;
		
		Point tempPoint=gameBoard.getUserUnits().get(1).getLocation();
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
	
	public void initializeImages(){
		try {
			boulder=ImageIO.read(new File("images/Boulder.png"));
			megaman=ImageIO.read(new File("images/MegamanStanding.png"));
			sonic=ImageIO.read(new File("images/SonicStanding.png"));
			grass=ImageIO.read(new File("images/TRPGgrass.png"));
			mario=ImageIO.read(new File("images/marioStanding.png"));
			goku=ImageIO.read(new File("images/gokuStanding.png"));
			link=ImageIO.read(new File("images/linkStanding.png"));
			princess=ImageIO.read(new File("images/princess.png"));
			waypoint=ImageIO.read(new File("images/1GlowingOrb.png"));
			invalidMove=ImageIO.read(new File("images/notValidCursor.png"));
			tree=ImageIO.read(new File("images/TreeSprites1.png"));
			chest=ImageIO.read(new File("images/chestClosed.png"));
			headstone=ImageIO.read(new File("images/headstone.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D) g;
		
		this.gameTileWidth=getWidth()/currentBoard[0].length;
		this.gameTileHeight=getHeight()/currentBoard.length;
		
		//loop through the current gameBoard and draw the images based on the current positions
		//of the units currently in the game
		for(int row=0; row<currentBoard.length; row++){
			for(int col=0; col<currentBoard[0].length; col++){
				if(currentBoard[row][col]==' '){
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
				}
				else if(currentBoard[row][col]=='#'){
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					g2.drawImage(boulder, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
				}
				else if(currentBoard[row][col]=='S' || currentBoard[row][col]=='s'){
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					g2.drawImage(sonic, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
				}
				else if(currentBoard[row][col]=='M' || currentBoard[row][col]=='m'){
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					
					//check if this unit is dead
					if(gameBoard.getCompUnits().get(2).isAlive()){
					g2.drawImage(megaman, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					}else{
						g2.drawImage(headstone, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					}
				}
				else if(currentBoard[row][col]=='W' || currentBoard[row][col]=='w'){
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					g2.drawImage(mario, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
				}
				else if(currentBoard[row][col]=='G' || currentBoard[row][col]=='g'){
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					g2.drawImage(goku, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
				}
				else if(currentBoard[row][col]=='L'|| currentBoard[row][col]=='l'){
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					
					//check if this unit is dead
					if(gameBoard.getCompUnits().get(1).isAlive()){
					g2.drawImage(link, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					}else{
						g2.drawImage(headstone, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					}
				}
				else if(currentBoard[row][col]=='P'|| currentBoard[row][col]=='p'){
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					
					//check if this unit is dead
					if(currentBoard[row][col]=='P'){
						if(gameBoard.getCompUnits().get(0).isAlive()){
						g2.drawImage(princess, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
						}else{
							g2.drawImage(headstone, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
						}
					}
					else{//then this is the friendly princess
						if(gameBoard.getUserUnits().get(0).isAlive()){
							g2.drawImage(princess, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
							}else{
								g2.drawImage(headstone, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
							}
					}
				}
				else if(currentBoard[row][col]=='!'){
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					g2.drawImage(tree, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
				}
				else if(currentBoard[row][col]=='@'){
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					g2.drawImage(chest, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
				}
			}
		}
		drawCursor(g2);
		if(currentGameState==GameState.ChoosingMove){
			drawShortestPathLineToCursor(g2);
		}
		if(currentGameState==GameState.CyclingThroughUnits){
			if(this.showStats==true){
				drawStatsPanel();
			}
		}
	}
	
	private void drawCursor(Graphics2D g2){
		g2.setColor(Color.RED);
		g2.drawRect(cursorLocation.x*gameTileWidth, cursorLocation.y*gameTileHeight, gameTileWidth-1, gameTileHeight-1);
	}
	
	private void drawStatsPanel(){
		if(statsPanel!=null){
			MainGamePanel.this.remove(statsPanel);
		}
		statsPanel=new UnitStatusPanel(currentUnit);
		Point temp=currentUnit.getLocation();
		statsPanel.setLocation( (temp.y+1)*gameTileWidth, (temp.x-2)*gameTileHeight);
		statsPanel.setSize(gameTileWidth*2, gameTileHeight*3);
		MainGamePanel.this.add(statsPanel).setVisible(true);
	}
	
	private ArrayList<Point> previousPath;
	private void drawShortestPathLineToCursor(Graphics g){
		Graphics2D g2=(Graphics2D)g;
		
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
					g2.drawImage(waypoint, x * gameTileWidth, y * gameTileHeight, null);
				}
			}
		}
		else if(previousPath!=null){
			for (int i = 0; i < previousPath.size(); i++) {
				System.out.println("drawing");
				int x = previousPath.get(i).y;
				int y = previousPath.get(i).x;
				g2.drawImage(waypoint, x * gameTileWidth, y * gameTileHeight, null);
			}
		}
		
		if(!gameBoard.checkAvailable(new Point(cursorLocation.y, cursorLocation.x))){
			if(!(currentUnit.getLocation().x==cursorLocation.y && currentUnit.getLocation().y==cursorLocation.x))
				g2.drawImage(invalidMove, cursorLocation.x * gameTileWidth, cursorLocation.y * gameTileHeight, gameTileWidth, gameTileHeight, null);
			
		}
	}
		
	private boolean showStats=false;
	private int unitIndex=1;
	private boolean myTurn=true;


	private class KeyManager implements KeyListener{

		
		@Override
		public void keyPressed(KeyEvent arg0) {
			int key=arg0.getKeyCode();
			if(myTurn){
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
						Point unitPoint=gameBoard.getUserUnits().get(unitIndex).getLocation();
						cursorLocation.setLocation(unitPoint.y, unitPoint.x);
						if(statsPanel!=null){
							MainGamePanel.this.add(statsPanel);
						}
						repaint();
					}
					else if(key==KeyEvent.VK_ENTER){
						//The player wants this unit to move to this location so we have to go and
						//check if that is a valid destination.
						if(gameBoard.checkAvailable(cursorLocation)){
							Point offsetCorrectedCursor=new Point(cursorLocation.y, cursorLocation.x);
							ArrayList<Point> path=gameBoard.findShortestPath(currentUnit.getLocation(), offsetCorrectedCursor);
							if(path!=null){
								UnitMovedCommand moveCommand =new UnitMovedCommand(source, unitIndex, path);
								try {
									serverOut.writeObject(moveCommand);
									//the progression should be to know have the user select an attack 
									//but for now for testing purposes we will jump straight
									//to choosing another unit's move
	
									gameBoard.getUserUnits().get(unitIndex).setLocation(currentUnit.getLocation());
	
									currentGameState=GameState.ChoosingAttack;
									previousPath=null;
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
					else if(key==KeyEvent.VK_E){
						EndTurnCommand endTurn=new EndTurnCommand(source);
						try{
							serverOut.writeObject(endTurn);
							currentGameState=GameState.CyclingThroughUnits;
						}catch(IOException e){
							e.printStackTrace();
						}
					}
				}
				//if the player is trying to just choose his current unit then the cursor will 
				//just jump to the location of the unit next or previously in line.
				else if(currentGameState==GameState.CyclingThroughUnits){
					
					if(key==KeyEvent.VK_RIGHT && cursorLocation.x<19){
						if(unitIndex<gameBoard.getUserUnits().size()-1){
							unitIndex++;
						}
						else if(unitIndex==gameBoard.getUserUnits().size()-1){
							unitIndex=1;
						}
	
	//					currentUnit=gameBoard.getUserUnits().get(unitIndex);
						currentUnit=localUserUnitList.get(unitIndex);
						
						Point unitPoint=gameBoard.getUserUnits().get(unitIndex).getLocation();
						cursorLocation.setLocation(unitPoint.y, unitPoint.x);
						repaint();
					}
					else if(key==KeyEvent.VK_LEFT && cursorLocation.x>0){
						if(unitIndex>1){
							unitIndex--;
						}
						else if (unitIndex==1){
							unitIndex=gameBoard.getUserUnits().size()-1;
						}
	//					currentUnit=gameBoard.getUserUnits().get(unitIndex);
						currentUnit=localUserUnitList.get(unitIndex);
						
						Point unitPoint=gameBoard.getUserUnits().get(unitIndex).getLocation();
						cursorLocation.setLocation(unitPoint.y, unitPoint.x);
						repaint();
					}
					//if the user presses enter while cycling through units
					else if(key==KeyEvent.VK_ENTER){
	//					currentUnit=gameBoard.getUserUnits().get(unitIndex);
						currentUnit=localUserUnitList.get(unitIndex);
	
						currentGameState=GameState.ChoosingMove;
						if(MainGamePanel.this.statsPanel!=null){
							MainGamePanel.this.remove(statsPanel);
						}
						repaint();
					}
					//if the user presses 's' the stats panel will be brought up for the current unit
					else if(key==KeyEvent.VK_S){
						if(showStats==true){
							showStats=false;
							MainGamePanel.this.remove(statsPanel);
							repaint();
						}
						else{
							showStats=true;
							repaint();
						}
					}
					else if(key==KeyEvent.VK_E){
						EndTurnCommand endTurn=new EndTurnCommand(source);
						try{
							serverOut.writeObject(endTurn);
							currentGameState=GameState.CyclingThroughUnits;
						}catch(IOException e){
							e.printStackTrace();
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
						if(gameBoard.checkIfEnemy(currentUnit,new Point(cursorLocation.y, cursorLocation.x))){
							System.out.println("found enemy");
							int enemyIndex=-99;
							ArrayList<Unit> temp=gameBoard.getCompUnits();
							for(int i=0; i<temp.size(); i++){
								if(temp.get(i).getLocation().equals(new Point(cursorLocation.y, cursorLocation.x))){
									enemyIndex=i;
								}
							}
							//only actually send the command if the unit is within range and its
							//line of fire is not obstructed
							if (gameBoard.checkOpenLineOfFire(currentUnit, new Point(cursorLocation.y, cursorLocation.x))) {
								UnitAttackCommand moveCommand = new UnitAttackCommand(source, unitIndex, enemyIndex);
								System.out.println("attacking");
								try {
									serverOut.writeObject(moveCommand);
	
									gameBoard.getUserUnits().get(unitIndex).setLocation(currentUnit.getLocation());
	
									currentGameState = GameState.CyclingThroughUnits;
									previousPath = null;
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
					else if(key==KeyEvent.VK_E){
						EndTurnCommand endTurn=new EndTurnCommand(source);
						try{
							serverOut.writeObject(endTurn);
							currentGameState=GameState.CyclingThroughUnits;
						}catch(IOException e){
							e.printStackTrace();
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
}
