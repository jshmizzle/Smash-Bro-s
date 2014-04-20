package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.Unit;

public class UnitStatusPanel extends JPanel {

	private Unit unit;
	private Image health;
	
	/**
	 * This panel will display the MainGamePanel's currentUnit's current stats while the 
	 * player is cycling through units.
	 * @param unit The unit who's current stats should be displayed.
	 */
	public UnitStatusPanel(Unit unit, int gameTileWidth, int gameTileHeight) {
		this.unit=unit;
		initializeImage();
		this.setPreferredSize(new Dimension((getWidth()/20)*2, (getHeight()/20)*2));
		this.setVisible(true);
	}
	
	private void initializeImage(){
		try {
			health=ImageIO.read(new File("images/healthPoint.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D) g;
		
		//fill in the background to be all black
		g2.setColor(Color.black);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g2.setColor(Color.GREEN);
		g2.drawString(unit.getName(), 2, 10);
		
		//represent the unit's health visually
		calculateAndDrawHealth(g2);
		
		//represent the unit's attack range
		g2.drawString("Range:" + unit.getAttackRange(), 2, 40);
		
		//represent the unit's attack damage
		g2.drawString("Damage:" +unit.getAttackPower(), 2, 54);
	}
	
	
	public void calculateAndDrawHealth(Graphics2D g){
		int currentHealth=unit.getHealth();
		int maxHealth=unit.getMaxHealth();
		int quarterHealth=maxHealth/4;
		int halfHealth=quarterHealth*2;
		int threeQuarterHealth=halfHealth+quarterHealth;
		int heartsToDraw=0;
		//now determine how many hearts to draw
		if(currentHealth==maxHealth)
			heartsToDraw=4;
		else if(currentHealth>threeQuarterHealth)
			heartsToDraw=3;
		else if(currentHealth>halfHealth)
			heartsToDraw=2;
		else if(currentHealth>quarterHealth)
			heartsToDraw=1;
		
		
		//now you know how many hearts to draw
		switch(heartsToDraw){
		case(4): g.drawImage(health, 2+(this.getWidth()/4)*3, 14, this.getWidth()/4, this.getHeight()/4, null);
		case(3): g.drawImage(health, 2+(this.getWidth()/4)*2, 14, this.getWidth()/4, this.getHeight()/4, null);
		case(2): g.drawImage(health, 2+this.getWidth()/4, 14, this.getWidth()/4, this.getHeight()/4, null);
		case(1): g.drawImage(health, 2, 14, this.getWidth()/4, this.getHeight()/4, null);

		}
	}
}
