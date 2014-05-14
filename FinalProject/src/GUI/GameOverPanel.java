package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Goku;
import model.Link;
import model.Mario;
import model.MegaMan;
import model.Princess;
import model.Scenario;
import model.Sonic;
import model.Unit;

public class GameOverPanel extends JPanel {

	private ArrayList<Unit> userUnits;
	private boolean isWinner;
	private Scenario scenario;
	private Image link, goku, mario, megaman, sonic, princess, princessDead, speechBubble;
	
//	public static void main(String [] args){
//		JFrame frame=new JFrame();
//		ArrayList<Unit> myList=new ArrayList<>();
////		myList.add(new Princess('p'));
//		myList.add(new Sonic('s'));
//		myList.add(new Goku('s'));
//		myList.add(new Link('s'));
//		myList.add(new MegaMan('s'));
//		myList.add(new Mario('s'));
//		
//
//		frame.setSize(1000, 600);
//		
//		GameOverPanel panel=new GameOverPanel(false, Scenario.Death, myList);
//		panel.setSize(1000, 600);
//		panel.setLocation(0, 0);
//		panel.setVisible(true);
//		
//		frame.setLayout(null);
//		frame.add(panel);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);
//	}
	
	public GameOverPanel(boolean isWinner, Scenario scenario, ArrayList<Unit> myUnits){
		this.userUnits=myUnits;
		this.isWinner=isWinner;
		this.scenario=scenario;
		
		//set the size of the panel
		this.setSize(1000, 600);
		this.setPreferredSize(new Dimension(1000, 600));
		initializeImages();
		
		this.setVisible(true);
		
	}
	
	public void paintComponent(Graphics g){
		if(isWinner==true)
			showWinningUnits(g);
		else 
			drawLosingUnitsInAnguish(g);
	}
	
	private void showWinningUnits(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		
		g2.drawImage(speechBubble, 0, 0, this.getWidth(), this.getHeight(),null);
		//draw their speech bubble to say they lost
		
		if(scenario==Scenario.Princess){
			g2.setColor(Color.blue);
			g2.setFont(new Font("Algerian", Font.BOLD, getWidth()/20));
			g2.drawString("We saved our princess!!", getWidth()/6, getHeight()/4 - getHeight()/25);
		}
		else{
			g2.setColor(Color.blue);
			g2.setFont(new Font("Algerian", Font.BOLD, getWidth()/15));
			g2.drawString("We survived!!", getWidth()/4, getHeight()/4);
		}
		drawTheUnits(g2);
	}

	private void drawLosingUnitsInAnguish(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		
		g2.drawImage(speechBubble, 0, 0, getWidth(), getHeight(),null);
		//draw their speech bubble to say they lost
		
		if(scenario==Scenario.Princess){
			g2.setColor(Color.red);
			g2.setFont(new Font("Algerian", Font.BOLD, getWidth()/15));
			g2.drawString("Nooooooooo!!!!", getWidth()/5 , getHeight()/3 -getHeight()/20);
			g2.drawString("PRINCESS!!!!", getWidth()/5+getWidth()/20, getHeight()/5-getHeight()/20);
		}
		else{
			g2.setColor(Color.red);
			g2.setFont(new Font("Algerian", Font.BOLD, getWidth()/15));
			g2.drawString("WHYYYYYYYY?!?!?!?!", getWidth()/7 +getWidth()/50, getHeight()/5);
		}
		
		drawTheUnits(g2);
	}

	public void drawTheUnits(Graphics2D g2){
		//set the size of the units 
		int	unitHeight= getHeight() / 5;
		int	unitWidth= getWidth() / 12;
		
		for(int i=0; i<userUnits.size(); i++){
			Unit curr=userUnits.get(i);
			if(curr instanceof Sonic){
				Point placement = getLocation(i);
				g2.drawImage(sonic, placement.x, placement.y, unitWidth, unitHeight, null);
			}
			else if(curr instanceof Mario){
				Point placement = getLocation(i);
				g2.drawImage(mario, placement.x, placement.y, unitWidth, unitHeight, null);
			}
			else if(curr instanceof MegaMan){
				Point placement = getLocation(i);
				g2.drawImage(megaman, placement.x, placement.y, unitWidth, unitHeight, null);
			}
			else if(curr instanceof Goku){
				Point placement = getLocation(i);
				g2.drawImage(goku, placement.x, placement.y, unitWidth, unitHeight, null);
			}
			else if(curr instanceof Link){
				Point placement = getLocation(i);
				g2.drawImage(link, placement.x, placement.y, unitWidth, unitHeight, null);
			}
			else if(curr instanceof Princess){
				Point placement = getLocation(i);
				if(isWinner)
					g2.drawImage(princess, placement.x, placement.y, unitWidth, unitHeight, null);
				else
					g2.drawImage(princessDead, placement.x, placement.y, unitWidth, unitHeight, null);
				
					
			}
		}
	}
	
	private Point getLocation(int index){
		if(scenario==Scenario.Death){
			switch(index){
				case 0: return new Point(getWidth()/12, 3*getHeight()/5);
				case 1: return new Point(3*getWidth()/12+getWidth()/60, 3*getHeight()/5-getHeight()/16);
				case 2: return new Point(5*getWidth()/12 +getWidth()/24, 2*getHeight()/5+getHeight()/13);
				case 3: return new Point(7*getWidth()/12+getWidth()/15, 3*getHeight()/5 -getHeight()/13);
				case 4: return new Point(10*getWidth()/12 , 3*getHeight()/5);
				default: return null;
			}
		}
		else{
			switch(index){			
			case 0: return new Point(5*getWidth()/12 +getWidth()/24, 3*getHeight()/5+getHeight()/10);
			case 1: return new Point(getWidth()/12, 3*getHeight()/5);
			case 2: return new Point(3*getWidth()/12+getWidth()/60, 3*getHeight()/5-getHeight()/16);
			case 3: return new Point(5*getWidth()/12 +getWidth()/24, 2*getHeight()/5+getHeight()/13);
			case 4: return new Point(7*getWidth()/12+getWidth()/15, 3*getHeight()/5 -getHeight()/13);
			case 5: return new Point(10*getWidth()/12 , 3*getHeight()/5);
			default: return null;
		}
		}
	}

	private void initializeImages() {
		try {
			link=ImageIO.read(new File("images/linkStanding.png"));
			goku=ImageIO.read(new File("images/gokuStanding.png"));
			mario=ImageIO.read(new File("images/marioStanding.png"));
			megaman=ImageIO.read(new File("images/MegaManStanding.png"));
			sonic=ImageIO.read(new File("images/SonicStanding.png"));
			princess=ImageIO.read(new File("images/Princess.png"));
			princessDead=ImageIO.read(new File("images/princessDead.png"));
			speechBubble=ImageIO.read(new File("images/GameOverSpeechBubble.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
