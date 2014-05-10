package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.Item;
import model.Potion;
import model.Rage;
import model.Shield;
import model.Sneakers;
import model.Sniper;

@SuppressWarnings("serial")
public class ItemPanel extends JPanel {

	private Image shield, potion, bag, rage, sniper, background, sneakers;
	private ArrayList<Item> items;
	
	public ItemPanel(ArrayList<Item> items) {
		this.items=items;
		
		initializeImages();
		this.setPreferredSize(new Dimension(getWidth()/4, (getHeight()/20)*2));
		this.setVisible(true);
		this.setBackground(new Color(0,0,0,0));
	}
	
	private void initializeImages(){
		try {
			shield=ImageIO.read(new File("images/shield.png"));
			sniper=ImageIO.read(new File("images/crosshairs.png"));
			potion=ImageIO.read(new File("images/potion.png"));
			bag=ImageIO.read(new File("images/inventory.png"));
			rage=ImageIO.read(new File("images/rage.png"));
			sneakers=ImageIO.read(new File("images/wingShoes.png"));
			background=ImageIO.read(new File("images/itemPanelBackground.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		
		g2.drawImage(background, 0, 0, getWidth(), getHeight(), null);
		
		//draw the bag that will always be present
		g2.drawImage(bag, 0, 0, getWidth()/4, getHeight(), null);
		
		for(int i=1; i<=items.size(); i++){
			if(items.get(i-1) instanceof Shield){
				g2.drawImage(shield, getWidth()/4*i + getWidth()/20, getHeight()/6, getWidth()/5, 2*getHeight()/3, null);
			}
			else if(items.get(i-1) instanceof Rage){
				g2.drawImage(rage, getWidth()/4*i +getWidth()/20, getHeight()/6, getWidth()/5, 2*getHeight()/3, null);
			}
			else if(items.get(i-1) instanceof Sniper){
				g2.drawImage(sniper, getWidth()/4*i +getWidth()/20, getHeight()/6, getWidth()/5, 2*getHeight()/3, null);
			}
			else if(items.get(i-1) instanceof Potion){
				g2.drawImage(potion, getWidth()/4*i +getWidth()/20, getHeight()/6, getWidth()/5, 2*getHeight()/3, null);
			}
			else if(items.get(i-1) instanceof Sneakers){
				g2.drawImage(potion, getWidth()/4*i +getWidth()/20, getHeight()/6, getWidth()/5, 2*getHeight()/3, null);
			}
		}
	}
}
