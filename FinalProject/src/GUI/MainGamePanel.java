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

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.GameBoard;

public class MainGamePanel extends JPanel {

	char [][] currentBoard;
	int gameTileWidth, gameTileHeight;
	Image boulder, megaman, sonic, grass, mario, goku, link, princess; 
	private Point cursorLocation;
	
	public MainGamePanel(GameBoard startingBoard) {
		this.setPreferredSize(new Dimension(600, 600));
		this.currentBoard=startingBoard.getGameBoard();
		this.gameTileWidth=getWidth()/currentBoard[0].length;
		this.gameTileHeight=getHeight()/currentBoard.length;
		cursorLocation=new Point(0,0);
		this.addKeyListener(new KeyManager());
		initializeImages();
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.setVisible(true);
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
		
	private class KeyManager implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			int key=arg0.getKeyCode();
		
			if(key==KeyEvent.VK_RIGHT && cursorLocation.x<19){
				cursorLocation.translate(1, 0);
				repaint();
			}
			if(key==KeyEvent.VK_DOWN && cursorLocation.y<19){
				cursorLocation.translate(0,1);
				repaint();
			}
			if(key==KeyEvent.VK_UP && cursorLocation.y>0){
				cursorLocation.translate(0, -1);
				
				repaint();
			}
			if(key==KeyEvent.VK_LEFT && cursorLocation.x>0){
				cursorLocation.translate(-1, 0);
				repaint();
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
