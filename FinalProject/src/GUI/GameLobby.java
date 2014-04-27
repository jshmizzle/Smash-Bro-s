package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JButton;
import javax.swing.JPanel;

public class GameLobby extends JPanel {

	ArrayList<String> clients=new ArrayList<String>();
	JButton button;
	ObjectOutputStream serverOut;
	Image map1, princess, grass, deathmatchIcon;
	Point cursorLocation, mapOption1, mapOption2, scenario1, scenario2, scenario3;
	int currentState=1, mapChoice, scenarioChoice;
	
	public GameLobby(ObjectOutputStream serverOut) {
		//determine the size of the JPanel
		this.setPreferredSize(new Dimension(600, 600));
		this.setSize(600, 600);
		
		mapOption1=new Point(getWidth()-130, 60);
		mapOption2=new Point(getWidth()-130, 200);
		scenario1=new Point(getWidth()-130, 360);
		scenario2=new Point(getWidth()-130, 410);
		scenario3=new Point(getWidth()-130, 460);
		
		
		clients.add("Hello World");
		clients.add("Jimmy");
		clients.add("Jason");
		clientJoined("Random");
		
		//save the connection to the server
		this.serverOut=serverOut;
		
		//initialize the button
		button=new JButton("Continue");
		button.setEnabled(false);
		button.setLocation(getWidth()-100, getHeight()-40);
		button.setSize(90, 30);
		button.setVisible(true);
		this.add(button);
		
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
	}
	
	public void clientJoined(String source){
		clients.add(source);
		if(clients.size()>=2){
			button.setEnabled(true);
		}
	}
	
	private class KeyManager implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			int key=arg0.getKeyCode();

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
				currentState=2;
				if(cursorLocation.equals(new Point(getWidth()-130, 40))){
					mapChoice=1;
				}
				else
					mapChoice=2;
				cursorLocation=scenario1;
				repaint();
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
}
