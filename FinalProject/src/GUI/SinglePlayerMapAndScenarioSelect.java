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

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import model.Map;
import model.Scenario;
import client.Client;
import command.Command;
import command.MapAndScenarioSelected;

@SuppressWarnings("serial")
public class SinglePlayerMapAndScenarioSelect extends JPanel {

	private JButton button;
	private ObjectOutputStream serverOut;
	private Image map1, map2, princess, grass, deathmatchIcon;
	private Point cursorLocation, mapOption1, mapOption2, scenario1, scenario2, selectedMapPoint, selectedScenarioPoint;
	private int currentState=1;
	private Map mapChoice;
	private Scenario scenarioChoice;
	private String sourceUserName;
	
	public SinglePlayerMapAndScenarioSelect(String source, ObjectOutputStream serverOut) {
		this.sourceUserName=source;
		
		//determine the size of the JPanel
		this.setPreferredSize(new Dimension(600, 600));
		this.setSize(600, 600);
		
		//initialize the button
		button=new JButton("Continue");
		button.setEnabled(false);
		button.setLocation(getWidth()/2-90, getHeight()-80);
		button.setSize(180, 70);
		button.setVisible(true);
		button.addActionListener(new ButtonListener());
		this.add(button);
		
		mapOption1=new Point(getWidth()/8, getHeight()/4);
		mapOption2=new Point(getWidth()/8, 2*getHeight()/4+10);
		scenario1=new Point(5*getWidth()/8, getHeight()/4);
		scenario2=new Point(5*getWidth()/8, 2*getHeight()/4+10);
		
		
		//save the connection to the server
		this.serverOut=serverOut;
		
		//initialize images
		initializeImages();
		
		cursorLocation=mapOption1;
		
		this.addKeyListener(new KeyManager());
		
		this.setLayout(null);
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.grabFocus();
		this.setVisible(true);
	}

	
	private void initializeImages() {
		try {
			map1=ImageIO.read(new File("images/Map1.png"));
			map2=ImageIO.read(new File("images/mapTwo.png"));
			princess=ImageIO.read(new File("images/Princess.png"));
			grass=ImageIO.read(new File("images/TRPGgrass.png"));
			deathmatchIcon=ImageIO.read(new File("images/scenario2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2=(Graphics2D) g;
		
		//draw the background
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		//draw title
		g2.setFont(new Font("Verdana", Font.BOLD, getWidth()/15));
		g2.setColor(Color.BLUE);
		g2.drawString("Game Select", getWidth()/8, 60);
	
		
		//draw the map selcetion label
		g2.setColor(Color.yellow);
		g2.setFont(new Font("Verdana", Font.ITALIC, 16));
		g2.drawString("Map Select", mapOption1.x, mapOption1.y);
		//draw the map1 selection
		g2.drawImage(map1, mapOption1.x, mapOption1.y, getWidth()/4, getHeight()/4, null);
		//draw the map2 selection
		g2.drawImage(map2, mapOption2.x, mapOption2.y, getWidth()/4, getHeight()/4, null);
		
		
		//draw the scenario select label
		g2.drawString("Scenario Select", scenario1.x, scenario1.y);
		//draw the princess scenario selection
		g2.drawImage(grass, scenario1.x, scenario1.y, getWidth()/4, getHeight()/4, null);
		g2.drawImage(princess, scenario1.x, scenario1.y, getWidth()/4, getHeight()/4, null);
		//draw the deathmatch option
		g2.drawImage(deathmatchIcon, scenario2.x, scenario2.y, getWidth()/4, getHeight()/4, null);
		
		
		//draw the cursor at the location of the current selection so the player knows what
		//option he would be choosing
		g2.setColor(Color.yellow);
		g2.drawRect(cursorLocation.x, cursorLocation.y, getWidth()/4, getHeight()/4);
		
		//now if the player has already selected the map to play on, draw a red square around
		//the choice they made so that there is a visual representation of that choice
		if(currentState>=2){
			g2.setColor(Color.red);
			g2.drawRect(selectedMapPoint.x, selectedMapPoint.y, getWidth()/4, getHeight()/4);
		}
		
		//do the same thing for the scenario
		if(currentState==3)
			g2.drawRect(selectedScenarioPoint.x, selectedScenarioPoint.y, getWidth()/4, getHeight()/4);

	}
	
	
	private class KeyManager implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			int key=arg0.getKeyCode();

			//only allow the HOST to actually select the gametype!
			//if(isHost){
				if(key==KeyEvent.VK_DOWN || key==KeyEvent.VK_UP){
					//the host begins by selecting the map
					if(currentState==1){
						if(cursorLocation.equals(mapOption1)){
							cursorLocation=mapOption2;
						}
						else
							cursorLocation=mapOption1;
						repaint();
					}
					
					//The host will now be selecting the scenario instead of the map
					else if(currentState==2){
						if(cursorLocation.equals(scenario1)){
							cursorLocation=scenario2;
						}
						else
							cursorLocation=scenario1;
						repaint();
					}
				}
				
				else if(key==KeyEvent.VK_ENTER){
					if(currentState==1){	
						currentState=2;
						if(cursorLocation.equals(mapOption1)){
							mapChoice=Map.First;
							selectedMapPoint=mapOption1;
						}
						else{
							mapChoice=Map.Second;
							selectedMapPoint=mapOption2;	
						}
						
						cursorLocation=scenario1;
						repaint();
					}
					else if(currentState==2){
						currentState=3;
						if(cursorLocation.equals(scenario1)){
							scenarioChoice=Scenario.Princess;
							selectedScenarioPoint=scenario1;
						}
						else {
							scenarioChoice=Scenario.Death;
							selectedScenarioPoint=scenario2;
						}
						//repaint so that you can see the selected scenario get highlighted red
						repaint();
						
						//the user has now selected both the scenario and the map so let them move on
						button.setEnabled(true);
					}
				}
			//}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	private class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			//again, there is only one button on this panel and so if this method
			//is triggered then we can just immediately set off and send the command
			Command<Client> command=new MapAndScenarioSelected(sourceUserName, mapChoice, scenarioChoice);
			
			try {
				serverOut.writeObject(command);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
