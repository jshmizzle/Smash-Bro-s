package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Goku;
import model.Link;
import model.Mario;
import model.MegaMan;
import model.Sonic;
import model.Unit;

public class AttackPanel extends JPanel{

	public static final int MAX_FRAMES = 2;

	private int x, y;
	private BufferedImage[] attackerImages;
	private BufferedImage[] attackerAttackingImages;
	private BufferedImage[] enemyGettingHit;
	private BufferedImage bigImg;
	private BufferedImage currentAttacker;
	private BufferedImage other;
	private BufferedImage defender;
	private String attacker;
	private int numFrames;
	private int height;
	private int width;
	private int n;
	private int i = 0, j = 0;
	private boolean moved = true, isProjectile = false;
	
	public AttackPanel(Unit one, Unit two) {
		
		this.setVisible(true);
		
		
		
		if(one instanceof Sonic){
			attacker = one.getName();
			
			try {	
			if(two instanceof Goku)
			{	
					defender = ImageIO.read(new File("images/gokuStandingBackwards.png"));
					enemyGettingHit = new BufferedImage[5];
					bigImg = ImageIO.read(new File("images/gokuGettingAttacked.png"));
					for(int j = 4; j >= 0; j--){
						enemyGettingHit[j] = bigImg.getSubimage( 39 *j, 0, 39,  51);
					}
					j = 4;
			}

			if(two instanceof MegaMan)
				defender = ImageIO.read(new File("images/MegaManStanding.png"));
			if(two instanceof Mario)
				defender = ImageIO.read(new File("images/marioStanding.png"));
			if(two instanceof Link)
				defender = ImageIO.read(new File("images/linkStanding.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
			
			animateSonic();
		}
		else if(one instanceof Goku){
			attacker = one.getName();
		}
		else if(one instanceof Mario){
			attacker = one.getName();
		}
		else if(one instanceof MegaMan){
			attacker = one.getName();
		}
		else if(one instanceof Link){
			attacker = one.getName();
		}
		
		JFrame frame = new JFrame();
		frame.setSize(new Dimension(1000, 1000));
		frame.setLocation(new Point(100, 100));
		this.setX(0);
		this.setY(0);
	}

	private void animateSonic() {
		try {
			bigImg = ImageIO.read(new File("images/SonicWalking.png"));
			other = ImageIO.read(new File("images/SonicAttacking.png"));
			attackerImages = new BufferedImage[4];
			attackerAttackingImages = new BufferedImage[8];
			isProjectile = true;
			n = 4;
			
			for (int i = 0; i <4; i++)
			{
			        attackerImages[i] = bigImg.getSubimage( 40 *i, 0, 40,  44);
			}
			
			for( int i = 0; i < 8; i++ ){
				attackerAttackingImages[i] = other.getSubimage( (301/8) *i, 0, 301/8, 37);
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		i = 0;
		numFrames = 0;
		
		Timer t = new Timer(25, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				moveSonic();
				repaint();
			}
		});
		t.start();
		
	}
	public void moveSonic() {
		//int j = 4;
		
		if(moved){
			numFrames++;
			if(numFrames % MAX_FRAMES == 0){
				if(i < n-1)
					i++;
				else{
					//if(attackerImages.length <= 2)
						i = 2;
					//moveLeft = true;
					//else 
					//	i--;
				}
				
				if(x >= this.getWidth()/2)
				{
					attackerImages = attackerAttackingImages;
					
					if(j < enemyGettingHit.length -1)
						defender = enemyGettingHit[j];
					
					if(j-1 != -1)
						j--;
					else 
						j = 0;
					
				}
				currentAttacker = attackerImages[i];
				
				if(isProjectile)
					x += 8;
			}
		}
	}
	

	public BufferedImage getCurrent() {
		return currentAttacker;
	}

	public void setCurrent(BufferedImage current) {
		this.currentAttacker = current;
	}

	public int getAttackerX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int x){
		width = x;
	}
	
	public void setHeight(int x){
		height = x;
	}

@Override
public void paintComponent(Graphics g) {
	super.paintComponent(g);
	
	g.drawImage (defender, getWidth()-150, y, 150, 200, null);
	g.drawImage(currentAttacker, x, y, 150, 200, null);
	

		}
	

}
