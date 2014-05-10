package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import command.HostMultiPlayerGame;
import command.JoinMultiPlayerGame;
import command.StartSinglePlayerGame;

@SuppressWarnings("serial")
public class MainMenuPanel extends JPanel {

	private JButton singlePlayer, joinGame, hostGame;
	private ObjectOutputStream output;
	private Image background;
	private ActionListener buttonListener;
	private String name;
	
	public MainMenuPanel(String name, ObjectOutputStream output) {
		this.output=output;
		this.name=name;
		this.setLayout(null);
		
		this.setSize(600, 600);
		this.setPreferredSize(new Dimension(600, 600));
		
		initializeBackground();
		initializeButtonChoices();
		this.setVisible(true);
	}
	
	private void initializeBackground(){
		try {
			background=ImageIO.read(new File("images/MainMenuBackground.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		//make sure the buttons are in the right place
		singlePlayer.setLocation(this.getWidth()/2-this.getWidth()/8, this.getWidth()/2-40);
		joinGame.setLocation(this.getWidth()/2-this.getWidth()/8, this.getWidth()/2+10);
		hostGame.setLocation(this.getWidth()/2-this.getWidth()/8, this.getWidth()/2+60);
		
		
		Graphics2D g2=(Graphics2D) g;
		g2.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
		
		g2.setColor(Color.red);
		g2.setFont(new Font("Algerian", Font.ITALIC, 48));
		g2.drawString("Final Project TRPG", 0, 40);
	}
	
	private void initializeButtonChoices(){
		//initialize the two buttons and add the button listener to them
		singlePlayer=new JButton("Single Player");
		joinGame=new JButton("Join Multi-Player Game");
		hostGame=new JButton("Host Multi-Player Game");
		
		buttonListener=new GameTypeButtonListener();
		singlePlayer.addActionListener(buttonListener);
		joinGame.addActionListener(buttonListener);
		hostGame.addActionListener(buttonListener);
		
		//add them to the panel
		singlePlayer.setSize(this.getWidth()/4, 30);
		joinGame.setSize(this.getWidth()/4, 30);
		hostGame.setSize(this.getWidth()/4, 30);
		
		singlePlayer.setLocation(this.getWidth()/2-this.getWidth()/8, this.getHeight()/2-40);
		joinGame.setLocation(this.getWidth()/2-this.getWidth()/8, this.getHeight()/2+10);
		hostGame.setLocation(this.getWidth()/2-this.getWidth()/8, this.getHeight()/2+60);
		
		singlePlayer.setVisible(true);
		joinGame.setVisible(true);
		hostGame.setVisible(true);
		
		singlePlayer.setEnabled(true);
		joinGame.setEnabled(true);
		hostGame.setEnabled(true);
		
		this.add(singlePlayer);
		this.add(joinGame);
		this.add(hostGame);
	}
	
	private class GameTypeButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//tell the server that the player has chosen the deathmatch mode
			if(e.getSource()==singlePlayer){
				try {
					StartSinglePlayerGame command=new StartSinglePlayerGame(name);
					output.writeObject(command);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else if(e.getSource()==joinGame){
				//tell the server that the player has chosen the target elimination mode
				try {					
					JoinMultiPlayerGame command = new JoinMultiPlayerGame(name);
					output.writeObject(command);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else if(e.getSource()==hostGame){
				try{
					HostMultiPlayerGame command=new HostMultiPlayerGame(name);
					output.writeObject(command);
				}catch(IOException e1){
					e1.printStackTrace();
				}
			}
		}
		
	}
	
}
