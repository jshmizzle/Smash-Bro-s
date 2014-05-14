package GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Goku;
import model.Link;
import model.Mario;
import model.MegaMan;
import model.Princess;
import model.Sonic;
import model.Unit;

public class AttackPanel extends JPanel{

	public static final int MAX_FRAMES = 2;

	int x, y;
	private BufferedImage[] attackerImages;
	private BufferedImage[] attackerAttackingImages;
	private BufferedImage[] enemyGettingHit;
	private BufferedImage bigImg, background;
	private BufferedImage currentAttacker;
	private BufferedImage other;
	private BufferedImage defender;
	private String attacker;
	private int numFrames;
	private int height;
	private int width, length = 0;
	private int n = 1;
	private int i = 0, t = 0, j = 0, shift = 0;
	private boolean moved = true, isProjectile = false, isMario = false, needToStayPut = false;
	public static boolean isAnimating = true;
	
	public AttackPanel(Unit one, Unit two) {
		
		
		try {
			background =  ImageIO.read(new File("images/background.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/******************************************************************************************************/
		
		if(one instanceof Sonic){
			attacker = one.getName();
			
			if(two instanceof Goku)
					setTwoGoku();
			if(two instanceof Link)
				setTwoLink();
			if(two instanceof MegaMan)
				setTwoMegaMan();
			if(two instanceof Mario)
				setTwoMario();
			if(two instanceof Link)
				setTwoLink();
			if(two instanceof Sonic)
				setTwoSonic();
			if(two instanceof Princess)
				setTwoPrincess();
			
			animateSonic();
		} //end of sonic
		
		/******************************************************************************************************/
		
		else if(one instanceof Goku){
			
			attacker = one.getName();
			isProjectile = true;
			if(two instanceof Goku)
				setTwoGoku();
			if(two instanceof Link)
				setTwoLink();
			if(two instanceof MegaMan)
				setTwoMegaMan();				
			if(two instanceof Mario)
				setTwoMario();
			if(two instanceof Sonic)
				setTwoSonic();
			if(two instanceof Princess)
				setTwoPrincess();
			animateGoku();
		}// END OF GOKU
		
		/******************************************************************************************************/
		
		else if(one instanceof Mario){
			
			attacker = one.getName();
			isProjectile = true;	
			if(two instanceof Goku)
				setTwoGoku();
			if(two instanceof MegaMan)
				setTwoMegaMan();				
			if(two instanceof Mario)
				setTwoMario();
			if(two instanceof Link)
				setTwoLink();
			if(two instanceof Sonic)
				setTwoSonic();
			if(two instanceof Princess)
				setTwoPrincess();
			
			animateMario();
		}//END OF MARIO
		
		/******************************************************************************************************/
		
		else if(one instanceof MegaMan){
			attacker = one.getName();
			if(two instanceof Goku)
				setTwoGoku();
			if(two instanceof Link)
				setTwoLink();
			if(two instanceof MegaMan)
				setTwoMegaMan();				
			if(two instanceof Mario)
				setTwoMario();
			if(two instanceof Sonic)
				setTwoSonic();
			if(two instanceof Princess)
				setTwoPrincess();
			
			animateMegaMan();
		}
		else if(one instanceof Link){
			attacker = one.getName();
			if(two instanceof Goku)
				setTwoGoku();
			if(two instanceof Link)
				setTwoLink();
			if(two instanceof MegaMan)
				setTwoMegaMan();				
			if(two instanceof Mario)
				setTwoMario();
			if(two instanceof Sonic)
				setTwoSonic();
			if(two instanceof Princess)
				setTwoPrincess();
			
			animateLink();
		}
		this.setLocation(0, getHeight()/3);
		
	}
	
	private void setTwoPrincess() {
		try {
			defender = ImageIO.read(new File("images/princess.png"));
			enemyGettingHit = new BufferedImage[1];
			enemyGettingHit[0] = ImageIO.read(new File("images/princessDead.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			j = 0;
	}

	private void animateMegaMan() {
		try{	
			
			bigImg = ImageIO.read(new File("images/megamanAttacking.png"));
			isProjectile = true;
			attackerImages = new BufferedImage[6];
			attackerAttackingImages = new BufferedImage[1];
		
		for (int i = 0; i <6; i++)
	        attackerImages[i] = bigImg.getSubimage( (257/6) *i, 0, (257/6), 39);
		
		
	        attackerAttackingImages[0] = ImageIO.read(new File("images/MegaManBullets.png"));
	}catch (IOException e) {
		e.printStackTrace();
	}

	numFrames = 0;
	i = 0;
	Timer t = new Timer(50, new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			moveMegaMan();
			repaint();
		}		
	});
	t.start();
		
	}

	protected void moveMegaMan() {
		if(moved){
			numFrames++;
			if(numFrames % MAX_FRAMES == 0){
				if(i < attackerImages.length-1)
					i++;
				else
					i = 0;
				currentAttacker = attackerImages[i];
							
				if(isProjectile)
					x += 10;
			}
		}
		if(x >= this.getWidth()/2)
		{
			x += 10;
			other = attackerAttackingImages[0];
			if(j > 0)
			{
				defender = enemyGettingHit[j];
				j--;
			}
			else 
			{
				j = 0;
				defender = enemyGettingHit[j];
				//isAnimating = false;
			}
			moved = false;
		}
		
	}

	private void setTwoLink() {
		try {
			defender = ImageIO.read(new File("images/linkStanding.png"));
			enemyGettingHit = new BufferedImage[4];
			enemyGettingHit[0] = ImageIO.read(new File("images/LinkStanding.png"));
			enemyGettingHit[1] = ImageIO.read(new File("images/linkLookingRight.png"));
			enemyGettingHit[2] = ImageIO.read(new File("images/linkTurnedAround.png"));
			enemyGettingHit[3] = ImageIO.read(new File("images/linkLookingRight.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			j = 3;
	}

	private void setTwoMario() {
	try {
		defender = ImageIO.read(new File("images/MarioStandingBackwards.png"));
		enemyGettingHit = new BufferedImage[1];
		enemyGettingHit[0] = ImageIO.read(new File("images/MarioGettingHit.png"));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		j = 0;
	}

	/******************************************************************************************************/
	
	private void animateLink() {
		try{	
			isProjectile = true;
			bigImg = ImageIO.read(new File("images/linkShootingArrow.png"));
			other = ImageIO.read(new File("images/linkArrow.png"));
			attackerImages = new BufferedImage[6];
			attackerAttackingImages = new BufferedImage[2];
		
			attackerImages[0] = bigImg;
			attackerImages[1] = bigImg;
			attackerImages[2] = bigImg;
			bigImg = ImageIO.read(new File("images/linkShootingWithoutArrow.png"));
			attackerImages[3] = bigImg;
			attackerImages[4] = bigImg;
			attackerImages[5] = bigImg;
		
	        attackerAttackingImages[0] = other;
	        other = ImageIO.read(new File("images/linkArrowHalf.png"));
	        attackerAttackingImages[1] = other;
	        other = ImageIO.read(new File("images/linkArrow.png"));
		i = 0;
	}catch (IOException e) {
		e.printStackTrace();
	}

	numFrames = 0;
	i = 0;
	Timer t = new Timer(70, new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			moveLink();
			repaint();
		}		
	});
	t.start();

	}
	
	/******************************************************************************************************/

	public void moveLink() {
		if(moved){
			numFrames++;
			if(numFrames % MAX_FRAMES == 0){
				if(i < attackerImages.length-1)
					i++;
				currentAttacker = attackerImages[i];
							
				if(isProjectile)
					x += 25;
			}
			if(x % 2 == 0)
				y += 3;
			else{
				y -= 3;
			}
		}
		if(x >= this.getWidth()/2)
		{
			other = attackerAttackingImages[1];
			if(j > 0)
			{
				defender = enemyGettingHit[j];
				j--;
			}
			else 
			{
				j = 0;
				defender = enemyGettingHit[j];
			//	isAnimating = false;
			}
			moved = false;
			n = 3;
			needToStayPut = true;
		}
		
	}

	/******************************************************************************************************/

	private void animateMario() {
		try{	
			
			bigImg = ImageIO.read(new File("images/marioWalking.png"));
			other = ImageIO.read(new File("images/fireBalls.png"));
			attackerImages = new BufferedImage[3];
			attackerAttackingImages = new BufferedImage[4];
		
		for (int i = 0; i <3; i++)
	        attackerImages[i] = bigImg.getSubimage( (92/3) *i, 0, (92/3), 34);
		
		for (int i = 0; i <4; i++)
	        attackerAttackingImages[i] = other.getSubimage( (83/4) *i, 0, (83/4), 8);
		
		other = ImageIO.read(new File("images/marioFlower.png"));
	}catch (IOException e) {
		e.printStackTrace();
	}

	numFrames = 0;
	i = 0;
	Timer t = new Timer(50, new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			moveMario();
			repaint();
		}		
	});
	t.start();
		
	}

	/******************************************************************************************************/
	
	public void moveMario() {
		if(moved){
			numFrames++;
			if(numFrames % MAX_FRAMES == 0){
				if(i == 2 || i == 1)
					i--;
				else if( i == 0)
					i = 2;
				currentAttacker = attackerImages[i];
				if(x > 130 && x < 140)
				{
					try {
						bigImg = ImageIO.read(new File("images/whiteMarioWalking.png"));
						BufferedImage[] temp = new BufferedImage[3];
						isMario = true;
					for (int k = 0; k <3; k++)
				        temp[k] = bigImg.getSubimage( (70/3) *k, 0, (70/3), 33);
					attackerImages = temp;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				else if( x > 131){
						try {
							other = ImageIO.read(new File("images/marioAttacking.png"));
							moved = false;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						currentAttacker = other;
				}
				if(moved)
					x += 8;
			}
		}
		else{	
		numFrames++;
			if(numFrames % MAX_FRAMES == 0){
				if(i<2)
					i++;
				else
					i = 0;
				
				if(length >= this.getWidth()/16)
				{	
					if(j < enemyGettingHit.length -1)
						defender = enemyGettingHit[j];
					if(j-1 != -1)
						j--;
					else 
					{
						j = 0;
						defender = enemyGettingHit[j];
					//	isAnimating = false;
					}
				
					isProjectile = false;
					other = attackerAttackingImages[i];
				}
					
			}
				
				other = attackerAttackingImages[i];
				length += 4;
		}
	}
	
	/******************************************************************************************************/
	
	private void setTwoSonic() {
		
		try {
			defender = ImageIO.read(new File("images/SonicStandingBackwards.png"));
		
		enemyGettingHit = new BufferedImage[6];
		bigImg = ImageIO.read(new File("images/SonicGettingAttacked.png"));
		for(int j = 5; j >= 0; j--){
			enemyGettingHit[j] = bigImg.getSubimage( 37 *j, 0, 37,  41);
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		j = 5;
	}

	/******************************************************************************************************/
	
	private void setTwoMegaMan() {
		
		try {
			defender = ImageIO.read(new File("images/MegaManStandingBackwards.png"));
		
		
		enemyGettingHit = new BufferedImage[5];
		bigImg = ImageIO.read(new File("images/megamanGettingAttacked.png"));
		for(int j = 3; j >= 0; j--){
			enemyGettingHit[j] = bigImg.getSubimage( (151/4) *j, 0, (151/4),  51);
		}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		j = 3;
		
	}

	/******************************************************************************************************/

	private void setTwoGoku() {
		try {
			defender = ImageIO.read(new File("images/gokuStandingBackwards.png"));
		 
			enemyGettingHit = new BufferedImage[5];
			bigImg = ImageIO.read(new File("images/gokuGettingAttacked.png"));
			
			for(int j = 4; j >= 0; j--){
				enemyGettingHit[j] = bigImg.getSubimage( 39 *j, 0, 39,  51);
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		j = 4;
		
	}

	/******************************************************************************************************/
	
	private void animateGoku() {
		try{
			bigImg = ImageIO.read(new File("images/gokuAttacking.png"));
			other = ImageIO.read(new File("images/gokuBullet.png"));
			attackerImages = new BufferedImage[3];
			
			for (int i = 0; i <3; i++)
		        attackerImages[i] = bigImg.getSubimage( (128/3) *i, 0, (128/3), 51);
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		i = 0;
		numFrames = 0;
		
		Timer t = new Timer(45, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				moveGoku();
				repaint();
			}
		});
		t.start();
		
	}
	
	/******************************************************************************************************/
	
	public void moveGoku() {
		if(moved){
			numFrames++;
			if(numFrames % MAX_FRAMES == 0){
				if(i == 0 || i == 1)
					i++;
				if(length >= this.getWidth()/4)
				{
					
					if(j < enemyGettingHit.length -1)
						defender = enemyGettingHit[j];
					
					if(j-1 != -1)
						j--;
					else {
						j = 0;
						defender = enemyGettingHit[j];
					}
					
					isProjectile = false;
					
				}
				currentAttacker = attackerImages[i];
				
				if(isProjectile)
					length += 8;
			}
		}
	}
		
	/******************************************************************************************************/
	
	private void animateSonic() {
		try {
			bigImg = ImageIO.read(new File("images/SonicWalking.png"));
			other = ImageIO.read(new File("images/SonicAttacking.png"));
			attackerImages = new BufferedImage[4];
			attackerAttackingImages = new BufferedImage[8];
			isProjectile = true;
			n = 4;
			
			for (int i = 0; i <4; i++)
			        attackerImages[i] = bigImg.getSubimage( 40 *i, 0, 40,  44);
			
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
		
		if(moved){
			numFrames++;
			if(numFrames % MAX_FRAMES == 0){
				if(i < n-1)
					i++;
				else
					i = 2;
				if(x >= this.getWidth()/2)
				{
					attackerImages = attackerAttackingImages;
					
					if(j < enemyGettingHit.length -1)
						defender = enemyGettingHit[j];
					
					if(j-1 != -1)
						j--;
					else 
					{
						j = 0;
						defender = enemyGettingHit[j];
						//isAnimating = false;
					}
					
				}
				currentAttacker = attackerImages[i];
				
				if(isProjectile)
					x += 15;
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
	if(attacker.equals("Link") && t > 900)
		notifyListeners();
	
	if(attacker.equals("Goku") && t > 1400)
		notifyListeners();
	
	if(t > 2000)
		notifyListeners();
		
	t += 25;
	if(t >= 2100)
	{
		t = 0;
		t += 25;
	}

	g.drawImage (background, 0 - t, 0, 2800, 1000, null);
	
	if(attacker.equals("Sonic")){
		g.drawImage (defender, getWidth()-150, y, 150, 200, null);
		g.drawImage(currentAttacker, x, y, 150, 200, null);
	}
	if(attacker.equals("Goku")){
		g.drawImage (defender, getWidth()-150, y, 150, 200, null);
		g.drawImage(currentAttacker, 0, y, 150, 200, null);
		g.drawImage(other, 150 , getHeight()/4 -length/4, 150 + length, 100 + length/2, null);
	}
	
	if(attacker.equals("Mario")){
		g.drawImage (defender, getWidth()-150, y, 150, 200, null);
		if(x <140)
			g.drawImage(currentAttacker, x, y, 140, 180, null);
		else{
			g.drawImage(currentAttacker, 200, 0, 140, 180, null);
			g.drawImage(other,currentAttacker.getWidth() + 250 + length*4, y, 50, 50, null);
		}
		if(!isMario)
			g.drawImage(other, 150, getHeight() -40, 50, 50, null);
	}
	
	if(attacker.equals("Link")){
		
		if(needToStayPut){
			g.drawImage(other, 450, 65, (150/n), (170/n), null);
		}
		g.drawImage (defender, getWidth()-150, 0, 150, 200, null);
		g.drawImage(currentAttacker, 0, 10, 150, 170, null);
		
		if(i == 5 && !needToStayPut){
			g.drawImage(other, x, y, (150/n), (170/n), null);
		}

	}
	
	if(attacker.equals("MegaMan")){
		
		if(x<150){
			g.drawImage (defender, getWidth()-150, y, 150, 200, null);
			g.drawImage(currentAttacker, x, y, 140, 180, null);
		}
		else{
			g.drawImage (defender, getWidth()-150, y, 150, 200, null);
			g.drawImage(currentAttacker, 150, y, 140, 180, null);
			g.drawImage(other, x, 0, 140, 180, null);
		}
			
	}
	}

public boolean isAnimating() {
	// TODO Auto-generated method stub
	return isAnimating;
}

private void notifyListeners(){
	for(ActionListener curr: listenerList){
		curr.actionPerformed(null);
	}
}

public ArrayList<ActionListener> listenerList=new ArrayList<>();
public void addListener(ActionListener l){
		listenerList.add(l);
	}

}
