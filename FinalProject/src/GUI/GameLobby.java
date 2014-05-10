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

import model.Map;
import model.Scenario;

import command.LobbyInfoCommand;
import command.MapAndScenarioSelected;

@SuppressWarnings("serial")
public class GameLobby extends JPanel {

	private ArrayList<String> clients=new ArrayList<String>();
	private JButton button;
	private ObjectOutputStream serverOut;
	private Image map1, princess, grass, deathmatchIcon;
	private Point cursorLocation, mapOption1, mapOption2, scenario1, scenario2, scenario3, selectedMapPoint, selectedScenarioPoint;
	private int currentState=1;
	private Map mapChoice;
	private Scenario scenarioChoice;
	private String sourceUserName;
	private boolean isHost;
	
	public GameLobby(String source, ObjectOutputStream serverOut, boolean isHost) {
		this.sourceUserName=source;
		this.isHost=isHost;
		
		//start out by listing the client who just connected to the screen
		clients.add(sourceUserName);
		
		
		//determine the size of the JPanel
		this.setPreferredSize(new Dimension(600, 600));
		this.setSize(600, 600);
		
		//initialize the button
		button=new JButton("Continue");
		button.setEnabled(false);
		button.setLocation(getWidth()-100, getHeight()-40);
		button.setSize(90, 30);
		button.setVisible(true);
		button.addActionListener(new ButtonListener());
		this.add(button);
		
		mapOption1=new Point(getWidth()-130, 60);
		mapOption2=new Point(getWidth()-130, 200);
		scenario1=new Point(getWidth()-130, 360);
		scenario2=new Point(getWidth()-130, 410);
		scenario3=new Point(getWidth()-130, 460);
		
		button=new JButton("Continue");
		button.setEnabled(false);
		button.setLocation(getWidth()-100, getHeight()-40);
		button.setSize(90, 30);
		button.setVisible(true);
		this.add(button);
		
		
		
		clients.add("Hello World");
		clients.add("Jimmy");
		clients.add("Jason");
		clientJoined("Random");
		

		//save the connection to the server
		this.serverOut=serverOut;
		//initialize images
		initializeImages();
		
		cursorLocation=new Point(getWidth()-130, 60);
		
		this.addKeyListener(new KeyManager());
		
		this.setLayout(null);
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.setVisible(true);
	}

	public void initializeImages(){
		try {
			map1=ImageIO.read(new File("images/Map1.png"));
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
		g2.setFont(new Font("Verdana", Font.BOLD, 20));
		g2.setColor(Color.BLUE);
		g2.drawString("Lobby", 20, 30);
		
		//draw rectangle around client names area
		g2.setColor(Color.blue);
		g2.drawRect(10, 35, getWidth()-150, getHeight()-85);
		
		
		g2.setColor(Color.yellow);
		for(int i=0; i<clients.size(); i++){
			g2.drawString(clients.get(i), 20, 60+(30*i));
		}
		
		//draw the map selcetion label
		g2.setFont(new Font("Verdana", Font.ITALIC, 16));
		g2.drawString("Map Select", getWidth()-130, 50);
		//draw the map1 selection
		g2.drawImage(map1, getWidth()-130, 60, 120, 120, null);
		//draw the map2 selection
		g2.drawImage(map1, getWidth()-130, 200, 120, 120, null);
		
		
		//draw the scenario select label
		g2.drawString("Scenario Select", getWidth()-130, 350);
		//draw the princess scenario selection
		g2.drawImage(grass, getWidth()-130, 360, 120, 40, null);
		g2.drawImage(princess, getWidth()-85, 360, 40, 40, null);
		//draw the deathmatch option
		g2.drawImage(deathmatchIcon, getWidth()-130, 410, 120, 40, null);
		//draw the temp 3rd option
		g2.drawImage(grass, getWidth()-130, 460, 120, 40, null);
		
		
		//draw the cursor at the location of the current selection so the player knows what
		//option he would be choosing
		if(currentState==1)
			g2.drawRect(cursorLocation.x, cursorLocation.y, 120, 120);
		else 
			g2.drawRect(cursorLocation.x, cursorLocation.y, 120, 40);
		
		//now if the player has already selected the map to play on, draw a red square around
		//the choice they made so that there is a visual representation of that choice
		if(currentState>=2){
			g2.setColor(Color.red);
			g2.drawRect(selectedMapPoint.x, selectedMapPoint.y, 120, 120);
		}
		
		//do the same thing for the scenario
		if(currentState==3)
			g2.drawRect(selectedScenarioPoint.x, selectedScenarioPoint.y, 120, 40);

	}
	
	public void clientJoined(String source){
		clients.add(source);
		if(clients.size()>=2 && currentState==3){
			button.setEnabled(true);
		}
		
		//notify the client who just joined that people were already in the lobby waiting
		try{
			LobbyInfoCommand command = new LobbyInfoCommand(sourceUserName, clients);
			serverOut.writeObject(command);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		repaint();
	}
	
	public void updateClients(ArrayList<String> users){
		this.clients=users;
		repaint();
	}
	
	private class KeyManager implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			int key=arg0.getKeyCode();

			//only allow the HOST to actually select the gametype!
			if(isHost){
				if(key==KeyEvent.VK_DOWN){
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
						else if(cursorLocation.equals(scenario2)){
							cursorLocation=scenario3;
						}
						else
							cursorLocation=scenario1;
						repaint();
					}
				}
				
				else if(key==KeyEvent.VK_UP){
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
							cursorLocation=scenario3;
						}
						else if(cursorLocation.equals(scenario2)){
							cursorLocation=scenario1;
						}
						else
							cursorLocation=scenario2;
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
						if(cursorLocation.equals(scenario2)){
							scenarioChoice=Scenario.Death;
							selectedScenarioPoint=scenario2;
						}
						//repaint so that you can see the selected scenario get highlighted red
						repaint();
						
						//if there is a sufficient number of users in the lobby, you can allow the
						//host to continue to the character select panel
						if(clients.size()>=2)
							button.setEnabled(true);
					}
				}
			}
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
			//there is only one button on this JPanel, if the event is heard then 
			//we need to send the command to the server telling the clients what 
			//map and scenario was selected
			MapAndScenarioSelected command =new MapAndScenarioSelected(sourceUserName, mapChoice, scenarioChoice);
			
			try {
				serverOut.writeObject(command);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
