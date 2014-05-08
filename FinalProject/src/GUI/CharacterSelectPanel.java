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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import model.Goku;
import model.Link;
import model.Mario;
import model.MegaMan;
import model.Princess;
import model.Scenario;
import model.Sonic;
import model.Unit;

import command.SetUserUnits;

public class CharacterSelectPanel extends JPanel {

	private ObjectOutputStream serverOut;
	private String source;
	private Point cursorLocation, unit1Point, unit2Point, unit3Point, unit4Point, unit5Point;
	private ArrayList<Unit> unitList=new ArrayList<>();
	private JButton button;
	private Image link, goku, mario, megaman, sonic, characterSelectLogo;
	private boolean isHost, hasEnoughUnits=false;
	private Scenario scenario;
	
	public CharacterSelectPanel(String source, ObjectOutputStream serverOut, boolean isHost, Scenario scenario) {
		this.source=source;
		this.serverOut=serverOut;
		this.isHost=isHost;
		this.scenario=scenario;
		
		//set the size of the panel
		this.setSize(1000, 600);
		this.setPreferredSize(new Dimension(1000, 600));
		
		//initialize the points for each of the units
		unit1Point=new Point(10, 100);
		cursorLocation=unit1Point;
		unit2Point=new Point(10+(getWidth()-60)/5, 100);
		unit3Point=new Point((10+(getWidth()-60)/5)*2, 100);
		unit4Point=new Point((10+(getWidth()-60)/5)*3, 100);
		unit5Point=new Point((10+(getWidth()-60)/5)*4, 100);
		
		//initialize the JButton and its location. Add to it the ButtonListener
		//so that we know when to send the arraylist of units in the command to the server
		//initialize the button
		button=new JButton("Continue");
		button.setEnabled(false);
		button.setLocation(getWidth()-200, getHeight()-80);
		button.setSize(190, 70);
		button.setVisible(true);
		button.addActionListener(new ButtonListener());
		this.add(button);
		
		this.addKeyListener(new KeyManager());
		
		initializeImages();
		
		this.setLayout(null);
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.grabFocus();
		this.setVisible(true);
	}
	
	private void initializeImages() {
		try {
			link=ImageIO.read(new File("images/linkStanding.png"));
			goku=ImageIO.read(new File("images/gokuStanding.png"));
			mario=ImageIO.read(new File("images/marioStanding.png"));
			megaman=ImageIO.read(new File("images/MegaManStanding.png"));
			sonic=ImageIO.read(new File("images/SonicStanding.png"));
			characterSelectLogo=ImageIO.read(new File("images/characterSelectLogo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;

		updateEverythingsPosition();
		
		//fill in a solid black background
		g2.setColor(Color.black);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		//display the title!
		g2.drawImage(characterSelectLogo, getWidth()/8, -5, getWidth()-(getWidth()/5)-20, 110, null);
		
		//draw the unit options

		//start by drawing link
		g2.drawImage(link, 10, 100, (getWidth()-60)/5, getHeight()/3-20, null);
		//goku
		g2.drawImage(goku, 10+(getWidth()-60)/5, 100, (getWidth()-60)/5, getHeight()/3-20, null);
		//mario
		g2.drawImage(mario, 13+((getWidth()-60)/5)*2, 100, (getWidth()-60)/5, getHeight()/3-20, null);
		//megaman
		g2.drawImage(megaman, 25+((getWidth()-60)/5)*3, 100, (getWidth()-60)/5, getHeight()/3-20, null);
		//sonic
		g2.drawImage(sonic, 25+((getWidth()-60)/5)*4, 100, (getWidth()-60)/5, getHeight()/3-20, null);
		
		//now draw the cursor over the currently selected unit
		g2.setColor(Color.white);
		g2.drawRect(cursorLocation.x, cursorLocation.y, (getWidth()-60)/5, getHeight()/3-20);
		
		//draw the display of the user's currently selected units
		g2.setFont(new Font(Font.SERIF, Font.ITALIC , 30));
		g2.drawString("Your Chosen Units", (getWidth()/2)-(getWidth()/7)*3+getWidth()/8, getHeight()-getHeight()/6-25);
		g2.setFont(new Font("Algerian", Font.BOLD, 40));
		//first thing draw a grid for the selected units to go into
		//fill the unselected spaces with question marks to indicate that the 
		//player should continue choosing their units.
		for(int i=0; i<5; i++){
			g2.setColor(Color.red);
			g2.drawRect((getWidth()/2)-(getWidth()/10)*3+getWidth()/10*i, getHeight()-getHeight()/6-20, getWidth()/10, getHeight()/10);
			if(unitList.size()>=i+1){
				//draw nothing because we will draw the unit there
			}
			else{
				g2.setColor(Color.yellow);
				g2.drawString("?", (getWidth()/2)-(getWidth()/10)*3+getWidth()/10*i+30, getHeight()-getHeight()/6+30);
			}
		}
		//adjust for the larger size of the unit list when the game mode is princess
		int adjust;
		if(hasEnoughUnits==true && scenario==Scenario.Princess)
			adjust=1;
		else 
			adjust=0;
		
		switch(unitList.size()-1-adjust){
			case 4:	drawInTheRightPosition(g2, 4, adjust);
			case 3:	drawInTheRightPosition(g2, 3, adjust);
			case 2:	drawInTheRightPosition(g2, 2, adjust);
			case 1:	drawInTheRightPosition(g2, 1, adjust);
			case 0:	drawInTheRightPosition(g2, 0, adjust);
		}
	}
	
	/**
	 * This method draws the sprite that we want based on whatthe player has selected but it 
	 * keeps us from having to repeat this enormous amount of code again in the switch statement.
	 * @param g2 The graphics object to paint with.
	 * @param i The index in the unitList to look into. Also helps to determine what part of the
	 * screen to draw it to.
	 */
	private void drawInTheRightPosition(Graphics2D g2, int i, int adjust){
		int adjustedi=i+adjust;
		if (unitList.get(adjustedi) instanceof Sonic) {
			g2.drawImage(sonic, (getWidth() / 2)- (getWidth() / 10) * 3 + getWidth() / 10 * i,getHeight() - getHeight() / 6-20, getWidth() / 10,getHeight() / 10, null);
		} else if (unitList.get(adjustedi) instanceof MegaMan) {
			g2.drawImage(megaman, (getWidth() / 2)- (getWidth() / 10) * 3 + getWidth() / 10 * i,getHeight() - getHeight() / 6-20, getWidth() / 10,getHeight() / 10, null);
		} else if (unitList.get(adjustedi) instanceof Mario) {
			g2.drawImage(mario, (getWidth() / 2)- (getWidth() / 10) * 3 + getWidth() / 10 * i,getHeight() - getHeight() / 6-20, getWidth() / 10,getHeight() / 10, null);
		} else if (unitList.get(adjustedi) instanceof Goku) {
			g2.drawImage(goku, (getWidth() / 2) - (getWidth() / 10)* 3 + getWidth() / 10 * i, getHeight()- getHeight() / 6-20, getWidth() / 10,getHeight() / 10, null);
		} else if (unitList.get(adjustedi) instanceof Link) {
			g2.drawImage(link, (getWidth() / 2) - (getWidth() / 10)* 3 + getWidth() / 10 * i, getHeight()- getHeight() / 6-20, getWidth() / 10,getHeight() / 10, null);
		}
	}
	
	private void updateEverythingsPosition() {
		//make sure that our points are at the correct position so that our cursor 
		//keeps getting drawn correctly no matter how the frame is resized
		unit1Point=new Point(10, 100);
		unit2Point=new Point(10+(getWidth()-60)/5, 100);
		unit3Point=new Point((10+(getWidth()-60)/5)*2, 100);
		unit4Point=new Point((10+(getWidth()-60)/5)*3, 100);
		unit5Point=new Point((10+(getWidth()-60)/5)*4, 100);
		//make sure that the button is also in the right location
		button.setLocation(getWidth()-200, getHeight()-80);		
	}

	private void addSelectedUnitToList(int selectionIndex){
		//we have to determine whether or not the player is the host b/c if they are then we
		//must give the units the player chooses a capital letter so that lorenzo's
		//AI will work for the single player games, and so that the checkIfEnemy() method
		//and other like methods in gameBoard will work during both single and multiplayer games.
		if(isHost==true){
			if(selectionIndex==1){
				unitList.add(new Link('L'));
			}
			else if(selectionIndex==2){
				unitList.add(new Goku('G'));
			}
			else if(selectionIndex==3){
				unitList.add(new Mario('W'));
			}
			else if(selectionIndex==4){
				unitList.add(new MegaMan('M'));
			}
			else if(selectionIndex==5){
				unitList.add(new Sonic('S'));
			}
		}
		else{
			if(selectionIndex==1){
				unitList.add(new Link('l'));
			}
			else if(selectionIndex==2){
				unitList.add(new Goku('g'));
			}
			else if(selectionIndex==3){
				unitList.add(new Mario('w'));
			}
			else if(selectionIndex==4){
				unitList.add(new MegaMan('m'));
			}
			else if(selectionIndex==5){
				unitList.add(new Sonic('s'));
			}
		}
		
	}
	
	private class KeyManager implements KeyListener{
		int selectionIndex=1;
		
		@Override
		public void keyPressed(KeyEvent arg0) {
			int key=arg0.getKeyCode();
			if(!hasEnoughUnits){
				if(key==KeyEvent.VK_LEFT){
					if(selectionIndex==1){
						selectionIndex=5;
						cursorLocation=unit5Point;
					}
					else if(selectionIndex==2){
						selectionIndex=1;
						cursorLocation=unit1Point;
					}
					else if(selectionIndex==3){
						selectionIndex=2;
						cursorLocation=unit2Point;
					}
					else if(selectionIndex==4){
						selectionIndex=3;
						cursorLocation=unit3Point;
					}
					else if(selectionIndex==5){
						selectionIndex=4;
						cursorLocation=unit4Point;
					}
					repaint();
				}
				else if(key==KeyEvent.VK_RIGHT){
					if(selectionIndex==1){
						selectionIndex=2;
						cursorLocation=unit2Point;
					}
					else if(selectionIndex==2){
						selectionIndex=3;
						cursorLocation=unit3Point;
					}
					else if(selectionIndex==3){
						selectionIndex=4;
						cursorLocation=unit4Point;
					}
					else if(selectionIndex==4){
						selectionIndex=5;
						cursorLocation=unit5Point;
					}
					else if(selectionIndex==5){
						selectionIndex=1;
						cursorLocation=unit1Point;
					}
					repaint();
				}
				else if(key==KeyEvent.VK_ENTER){
					addSelectedUnitToList(selectionIndex);
					repaint();
					
					//make sure that we only allow the player to continue when they have chosen
					//the correct number of units
					if(unitList.size()==5){
						if(scenario==Scenario.Princess){
							if(isHost){
								unitList.add(0, new Princess('P'));
							}
							else{
								unitList.add(0, new Princess('p'));
							}
						}
						
						hasEnoughUnits=true;
						button.setEnabled(true);
					}
					System.out.println(unitList);
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			//NOT USED
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			//NOT USED
		}
		
	}
	
	private class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			//again, there is only one button on this panel and so if this method
			//is triggered then we can just immediately set off and send the command
			SetUserUnits command= new SetUserUnits(source, unitList);
			
			try {
				serverOut.writeObject(command);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	
}
