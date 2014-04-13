package GUI;

import java.awt.Color;
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

public class MainMenuPanel extends JPanel {

	private JButton deathMatchButton;
	private JButton killTargetButton;
	private ObjectOutputStream output;
	private Image background;
	private ActionListener buttonListener;
	private String name;
	
	public MainMenuPanel(String name, ObjectOutputStream output) {
		this.output=output;
		this.setLayout(null);
		this.setSize(this.getWidth(), this.getHeight());
		initializeBackground();
//		initializeButtonChoices();
	}
	
	private void initializeBackground(){
		try {
			background=ImageIO.read(new File("images/MainMenuBackground.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2=(Graphics2D) g;
		g2.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
		
		g2.setColor(Color.red);
		g2.setFont(new Font("Algerian", Font.ITALIC, 48));
		g2.drawString("Final Project TRPG", 0, 40);
		initializeButtonChoices();
	}
	
	private void initializeButtonChoices(){
		//initialize the two buttons and add the button listener to them
		deathMatchButton=new JButton("Death Match");
		killTargetButton=new JButton("Target Elimination");
		buttonListener=new GameTypeButtonListener();
		deathMatchButton.addActionListener(buttonListener);
		killTargetButton.addActionListener(buttonListener);
		
		//add them to the panel
		deathMatchButton.setSize(this.getWidth()/4, 30);
		killTargetButton.setSize(this.getWidth()/4, 30);
		deathMatchButton.setLocation(this.getWidth()/2-this.getWidth()/8, this.getWidth()/2-40);
		killTargetButton.setLocation(this.getWidth()/2-this.getWidth()/8, this.getWidth()/2+10);
		deathMatchButton.setVisible(true);
		killTargetButton.setVisible(true);
		deathMatchButton.setEnabled(true);
		killTargetButton.setEnabled(true);
		this.add(deathMatchButton);
		this.add(killTargetButton);
	}
	
	private class GameTypeButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//tell the server that the player has chosen the deathmatch mode
			if(e.getSource()==deathMatchButton){
				try {
					output.writeObject(new Integer(2));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else if(e.getSource()==killTargetButton){
				//tell the server that the player has chosen the target elimination mode
				try {
					output.writeObject(new Integer(1));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}
	
}
