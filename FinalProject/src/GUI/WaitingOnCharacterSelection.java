package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Goku;
import model.Link;
import model.Mario;
import model.MegaMan;
import model.Scenario;
import model.Sonic;
import model.Unit;
import client.Client;
import command.BeginGameCommand;
import command.Command;

@SuppressWarnings("serial")
public class WaitingOnCharacterSelection extends JPanel {

	private ArrayList<Unit> playerOneUnits, playerTwoUnits;
	private Image link, goku, mario, megaman, sonic, VS;
	private JButton begin;
	private String source;
	private ObjectOutputStream serverOut;
	private int unitWidth, unitHeight, timeRemaining=10;
	private Scenario scenario;
	
	public WaitingOnCharacterSelection(String source, ArrayList<Unit> playerOneUnits, ArrayList<Unit> playerTwoUnits, Scenario scenario, ObjectOutputStream serverOut) {
		this.playerOneUnits=playerOneUnits;
		this.playerTwoUnits=playerTwoUnits;
		this.source=source;
		this.serverOut=serverOut;
		this.scenario=scenario;
		
		//set the size of the panel
		this.setSize(1000, 600);
		this.setPreferredSize(new Dimension(1000, 600));
		
		
		//initialize the JButton and its location. Add to it the ButtonListener
		//so that we know when to send the arraylist of units in the command to the server
		//initialize the button
		begin=new JButton("BEGIN!");
		begin.setEnabled(false);
		begin.setLocation(getWidth()/2-getWidth()/12, getHeight()-getHeight()/5);
		begin.setSize(getWidth()/6, getHeight()/8);
		begin.setVisible(true);
		begin.addActionListener(new ButtonListener());
		this.add(begin);
						
		initializeImages();
		
		this.setLayout(null);
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.grabFocus();
		this.setVisible(true);
		
		//on certain occasions, this panel will be constructed for the first time with 
		//both unitLists already set in place, if that is the case, just start the 
		//countdown right away in order to keep the countdowns in sync
		if(playerOneUnits!=null && playerTwoUnits!=null){
			startCountDown();
		}
	}
	
	private void initializeImages() {
		try {
			link=ImageIO.read(new File("images/linkStanding.png"));
			goku=ImageIO.read(new File("images/gokuStanding.png"));
			mario=ImageIO.read(new File("images/marioStanding.png"));
			megaman=ImageIO.read(new File("images/MegaManStanding.png"));
			sonic=ImageIO.read(new File("images/SonicStanding.png"));
			VS=ImageIO.read(new File("images/versus.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D) g;
		
		//set the size of the units 
		unitHeight= getHeight() / 5;
		unitWidth= getWidth() / 12;
		
		//set the size of the button
		begin.setLocation(getWidth()/2-getWidth()/12, 4*getHeight()/5+20);
		begin.setSize(getWidth()/6, getHeight()/7);
		
		//fill in another black background
		g2.setColor(Color.black);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		//draw the player's chosen units at the correct points
		int temp;
		
		if(playerOneUnits!=null){				
			for(int i=scenario==Scenario.Princess ? 1:0; i<playerOneUnits.size(); i++){
				Unit curr=playerOneUnits.get(i);
				if(curr instanceof Sonic){
					Point placement = getLocation(i, 1);
					g2.drawImage(sonic, placement.x, placement.y, unitWidth, unitHeight, null);
				}
				else if(curr instanceof Mario){
					Point placement = getLocation(i, 1);
					g2.drawImage(mario, placement.x, placement.y, unitWidth, unitHeight, null);
				}
				else if(curr instanceof MegaMan){
					Point placement = getLocation(i, 1);
					g2.drawImage(megaman, placement.x, placement.y, unitWidth, unitHeight, null);
				}
				else if(curr instanceof Goku){
					Point placement = getLocation(i, 1);
					g2.drawImage(goku, placement.x, placement.y, unitWidth, unitHeight, null);
				}
				else if(curr instanceof Link){
					Point placement = getLocation(i, 1);
					g2.drawImage(link, placement.x, placement.y, unitWidth, unitHeight, null);
				}
			}
		}
		//draw the playerTwo units at the correct points ASSUMING that they have already been chosen 
		if(playerTwoUnits!=null){
			for(int i=scenario==Scenario.Princess ? 1:0; i<playerTwoUnits.size(); i++){
				Unit curr=playerTwoUnits.get(i);
				if(curr instanceof Sonic){
					Point placement = getLocation(i, 2);
					g2.drawImage(sonic, placement.x, placement.y, unitWidth, unitHeight, null);
				}
				else if(curr instanceof Mario){
					Point placement = getLocation(i, 2);
					g2.drawImage(mario, placement.x, placement.y, unitWidth, unitHeight, null);
				}
				else if(curr instanceof MegaMan){
					Point placement = getLocation(i, 2);
					g2.drawImage(megaman, placement.x, placement.y, unitWidth, unitHeight, null);
				}
				else if(curr instanceof Goku){
					Point placement = getLocation(i, 2);
					g2.drawImage(goku, placement.x, placement.y, unitWidth, unitHeight, null);
				}
				else if(curr instanceof Link){
					Point placement = getLocation(i, 2);
					g2.drawImage(link, placement.x, placement.y, unitWidth, unitHeight, null);
				}
			}
		}
		else if(playerTwoUnits==null){
			//let the user know that they are waiting on the other player to choose their units
			g2.setColor(Color.red);
			g2.drawString("Waiting...", getWidth()-getWidth()/4, getHeight()/2);
		}
		
		//always draw the VERSUS image in the middle
		g2.drawImage(VS, getWidth()/2-5*unitWidth/2, getHeight()/2-3*unitHeight/2, 5*unitWidth, 3*unitHeight, null);
		
		//always draw the countdown as based on the current timeRemaining
		if(timeRemaining<11){
			String timeString=Integer.toString(timeRemaining);
			g2.setColor(Color.red);
			g2.setFont(new Font("Algerian", Font.BOLD, getWidth()/7));
			//make sure it is centered
			if(timeRemaining==10){
				g2.drawString(timeString, getWidth()/2-unitWidth, getHeight()/6);
			}
			else if (timeRemaining>0){
				g2.drawString(timeString, getWidth()/2-unitWidth/2, getHeight()/6);
			}
			else{
				g2.setFont(new Font("Algerian", Font.ITALIC, getWidth()/19));
				g2.drawString("Begin!", getWidth()/2-unitWidth, getHeight()/8);
			}
		}
	}
	
	private Point getLocation(int index, int player){
		if(scenario.compareTo(Scenario.Princess)!=0){
			if(player==1){
				switch(index){
				case 0: return new Point(0, 4*getHeight()/5);
				case 1: return new Point(getWidth()/12, 3*getHeight()/5);
				case 2: return new Point(2*getWidth()/12, 2*getHeight()/5);
				case 3: return new Point(3*getWidth()/12, getHeight()/5);
				case 4: return new Point(4*getWidth()/12 , 0);
				default: return null;
				}
			}
			else{
				switch(index){
				case 0: return new Point(getWidth()-5*getWidth()/12, 0);
				case 1: return new Point(getWidth()-4*getWidth()/12, getHeight()/5);
				case 2: return new Point(getWidth()-3*getWidth()/12, 2*getHeight()/5);
				case 3: return new Point(getWidth()-2*getWidth()/12, 3*getHeight()/5);
				case 4: return new Point(getWidth()-getWidth()/12 , 4*getHeight()/5);
				default: return null;
				}
			}
		}
		else{
			if(player==1){
				switch(index){
				case 1: return new Point(0, 4*getHeight()/5);
				case 2: return new Point(getWidth()/12, 3*getHeight()/5);
				case 3: return new Point(2*getWidth()/12, 2*getHeight()/5);
				case 4: return new Point(3*getWidth()/12, getHeight()/5);
				case 5: return new Point(4*getWidth()/12 , 0);
				default: return null;
				}
			}
			else{
				switch(index){
				case 1: return new Point(getWidth()-5*getWidth()/12, 0);
				case 2: return new Point(getWidth()-4*getWidth()/12, getHeight()/5);
				case 3: return new Point(getWidth()-3*getWidth()/12, 2*getHeight()/5);
				case 4: return new Point(getWidth()-2*getWidth()/12, 3*getHeight()/5);
				case 5: return new Point(getWidth()-getWidth()/12 , 4*getHeight()/5);
				default: return null;
				}
			}
		}
	}
	
	public void updateUnitLists(ArrayList<Unit> player1Units, ArrayList<Unit> player2Units){
		this.playerOneUnits=player1Units;
		this.playerTwoUnits=player2Units;
		System.out.println("updating");
		repaint();
		startCountDown();
	}
	
	Timer timer;
	public void startCountDown(){
		CountDown countDown=new CountDown();
		timer =new Timer(1000, countDown);
		timer.start();
	}
	
	private class CountDown implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			timeRemaining--;
			repaint();
			if(timeRemaining==0){
				getGraphics().setColor(Color.blue);
				getGraphics().drawString("Begin!", getWidth()/2-getWidth()/12, 0+getHeight()/5);
				begin.setEnabled(true);
				timer.stop();
			}
		}

	}
	
	private class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			//again, there is only one button on this panel and so if this method
			//is triggered then we can just immediately set off and send the command
			Command<Client> begin=new BeginGameCommand(source);
			
			try {
				serverOut.writeObject(begin);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
