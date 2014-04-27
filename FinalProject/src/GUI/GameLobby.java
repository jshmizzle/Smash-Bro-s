package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class GameLobby extends JPanel {

	ArrayList<String> clients=new ArrayList<String>();
	JButton button;
	ObjectOutputStream serverOut;
	
	public GameLobby(ObjectOutputStream serverOut) {
		
		clients.add("Hello World");
		clients.add("Jimmy");
		clients.add("Jason");
		clients.add("Random");
		
		//save the connection to the server
		this.serverOut=serverOut;
		
		//determine the size of the JPanel
		this.setPreferredSize(new Dimension(600, 600));
		this.setSize(600, 600);
		
		//initialize the button
		button=new JButton("Continue");
		button.setEnabled(false);
		button.setLocation(getWidth()-100, getHeight()-40);
		button.setSize(90, 30);
		button.setVisible(true);
		this.add(button);
		
		
		this.setLayout(null);
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.setVisible(true);
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
	}
	
	public void clientJoined(String source){
		clients.add(source);
		if(clients.size()==2){
			button.setEnabled(true);
		}
	}
}
