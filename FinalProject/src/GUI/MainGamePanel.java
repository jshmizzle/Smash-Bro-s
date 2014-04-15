package GUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.GameBoard;

public class MainGamePanel extends JPanel {

	char [][] currentBoard;
	int gameTileWidth, gameTileHeight;
	Image boulder, megaman, sonic, grass, mario, goku; 
	
	public MainGamePanel(GameBoard startingBoard) {
		this.currentBoard=startingBoard.getGameBoard();
		this.gameTileWidth=getWidth()/currentBoard[0].length;
		this.gameTileHeight=getHeight()/currentBoard.length;
		initializeImages();
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
				else if(currentBoard[row][col]=='G'){
					g2.drawImage(grass, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
					g2.drawImage(goku, col*gameTileWidth, row*gameTileHeight, gameTileWidth, gameTileHeight, null);
				}
			}
		}
	}
	
	public void update(GameBoard currentGameBoard){
		this.currentBoard=currentGameBoard.getGameBoard();
		this.repaint();
	}
	
}
