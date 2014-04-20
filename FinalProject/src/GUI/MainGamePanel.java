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

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.GameBoard;
import model.Unit;

public class MainGamePanel extends JPanel {

	char [][] currentBoard;
	private GameBoard gameBoard;
	int gameTileWidth, gameTileHeight;
	private Image boulder, megaman, sonic, grass, mario, goku, link, princess; 
	private Point cursorLocation;
	private ObjectOutputStream serverOut;
	private GameState currentGameState;
	private Unit currentUnit;
	private UnitStatusPanel statsPanel;
	
	public MainGamePanel(GameBoard startingBoard, ObjectOutputStream serverOut) {
		this.serverOut=serverOut;
		
		//determine the size of the JPanel
		this.setPreferredSize(new Dimension(600, 600));
		
		//initialize the game board that will be represented on the screen
		this.currentBoard=startingBoard.getGameBoard();
		this.gameBoard=startingBoard;
		this.currentUnit=gameBoard.getUserUnits().get(1);
		
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
					g2.drawImage(megaman, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
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
					g2.drawImage(link, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
				}
				else if(currentBoard[row][col]=='P'|| currentBoard[row][col]=='p'){
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					g2.drawImage(princess, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
				}
			}
		}
		drawCursor(g2);
	}
	
	private void drawCursor(Graphics2D g2){
		g2.setColor(Color.RED);
		g2.drawRect(cursorLocation.x*gameTileWidth, cursorLocation.y*gameTileHeight, gameTileWidth-1, gameTileHeight-1);
	}
	
	private void drawStatsPanel(){
		if(statsPanel!=null){
			MainGamePanel.this.remove(statsPanel);
		}
		statsPanel=new UnitStatusPanel(currentUnit, gameTileWidth, gameTileHeight);
		Point temp=currentUnit.getLocation();
		statsPanel.setLocation( (temp.y+1)*gameTileHeight, (temp.x-2)*gameTileWidth);
		statsPanel.setSize(gameTileWidth*2, gameTileHeight*2);
		MainGamePanel.this.add(statsPanel).setVisible(true);
		repaint();
	}
		
	private class KeyManager implements KeyListener{

		private int unitIndex=1;
		private boolean showStats=false;
		
		@Override
		public void keyPressed(KeyEvent arg0) {
			int key=arg0.getKeyCode();
			
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
					MainGamePanel.this.add(statsPanel);
					repaint();
				}
				else if(key==KeyEvent.VK_ENTER){
					
					currentGameState=GameState.ChoosingAttack;
				}
			}
			//if the player is trying to just choose his current unit then the cursor will 
			//just jump to the location of the unit next or previously in line.
			else if(currentGameState==GameState.CyclingThroughUnits){
				
				if(key==KeyEvent.VK_RIGHT && cursorLocation.x<19){
					if(unitIndex<gameBoard.getUserUnits().size()-1){
						unitIndex++;
					}

					currentUnit=gameBoard.getUserUnits().get(unitIndex);
					Point unitPoint=gameBoard.getUserUnits().get(unitIndex).getLocation();
					cursorLocation.setLocation(unitPoint.y, unitPoint.x);
					repaint();

					if(showStats==true){
						drawStatsPanel();
					}
				}
				else if(key==KeyEvent.VK_LEFT && cursorLocation.x>0){
					if(unitIndex>1){
						unitIndex--;
					}
					currentUnit=gameBoard.getUserUnits().get(unitIndex);

					Point unitPoint=gameBoard.getUserUnits().get(unitIndex).getLocation();
					cursorLocation.setLocation(unitPoint.y, unitPoint.x);
					repaint();
					if(showStats==true){
						drawStatsPanel();
					}
				}
				//if the user presses enter while cycling through units
				else if(key==KeyEvent.VK_ENTER){
					currentUnit=gameBoard.getUserUnits().get(unitIndex);
					currentGameState=GameState.ChoosingMove;
					MainGamePanel.this.remove(statsPanel);
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
						drawStatsPanel();
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
		this.repaint();
	}
	
	
}
