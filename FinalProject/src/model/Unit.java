 package model;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/*
 * Unit class. Holds all the information and methods for each unit
 */
public class Unit implements Serializable{ 
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -8650763360508589614L;
	protected String name;
	protected int health;
	protected int moveDistance;
	protected ArrayList <Item> items;
	protected int [] stats;
	//protected Strategy strategy;
	protected boolean alive, alreadyAttacked;
	protected int attackPower;
	protected Point location;
	protected Image standingImage, headstone;
	protected char charRepresentation;
	protected int defenseAmount,healthFull,attackRange, movesLeft;

	
	/************************************************************************************/
	/**
	 * Sets up units initial stats
	 * @param n Name of the unit
	 * @param hp Units health
	 * @param distance How far unit can move
	 * @param attackPower Amount of damage the unit does
	 * @param i Image of unit
	 * @param p Location of unit
	 * @param c The character that represents the unit
	 */
	public Unit (String n, int hp, int distance, int attackPower, int attackRange, BufferedImage i, Point p, char c){

		this.name = n;
		this.health = hp;
		healthFull=hp;
		this.moveDistance = distance;
		this.attackPower=attackPower;
		location=p;
		attackRange=this.attackRange;
		charRepresentation=c;
		defenseAmount=1;
		alive=true;
		movesLeft = distance;
		
	}
	
	/************************************************************************************/
	/**
	 * Sets up units initial stats
	 * @param n Name of the unit
	 * @param hp Units health
	 * @param distance How far unit can move
	 * @param attack Amount of damage the unit does
	 * @param p Location of unit
	 * @param c The character that represents the unit
	 * @param i The standing image of the unit so that we could easily just call the draw method
	 * in the gui to make things easier over there.
	 */
	
	public Unit (String n, int hp, int distance, int attack, int attackRange, Point p, char c){

		this.name = n;
		this.health = hp;
		healthFull=hp;
		this.moveDistance = distance;
		this.attackPower=attack;
		location=p;
		this.attackRange = attackRange;
		charRepresentation=c;
		defenseAmount=1;
		alive=true;
		movesLeft = distance;
		
		
	}
	/************************************************************************************/
	//Returns the location on the board the unit is in 

	public Point getLocation(){
		return location;
	}
	
	/************************************************************************************/
	//sets the location of the unit to the point that is passed in
	
	public void setLocation(Point p){
		location.setLocation(p);
	}
	
	/************************************************************************************/
	//Returns the char that represents the unit
	
	public char getCharRepresentation(){
		return charRepresentation;
	}
	
	/************************************************************************************/
	//Returns the name of the unit
	
	public String getName(){
		return name;
	}
	
 	/************************************************************************************/
	//Returns the amount of health the unit has
	
	public int getHealth(){
		return health;
	}
	
	//Returns the Maximum health of the current unit
	public int getMaxHealth(){
		return healthFull;
	}
	
	/************************************************************************************/
	//Adds health to the unit (from item)

	public void setMaxHealt(){
		health=healthFull;
	}
	/************************************************************************************/
	//Returns the amount of distance the unit can move

	public int getDistance(){
		return moveDistance;
	}
	
	/************************************************************************************/
	//Makes unit be able to move farther (from item)
	
	public void addDistance(int amount){
		moveDistance+=amount;
	}
	
	/************************************************************************************/
	//Attacks a unit by giving that unit a specific amount of damage
	
	public void attack(Unit u){
		u.takeHit(attackPower);
		alreadyAttacked=true;
	}
	/************************************************************************************/
	
	public boolean checkIfAlreadyAttackedThisTurn(){
		return alreadyAttacked;
	}
	
	/************************************************************************************/
	//Adds to Amount of attack a unit can do (item)
	
	public void addAttack(int amount){
		attackPower+=amount;
	}
	
	/************************************************************************************/
	//Method that increases the attack Range (by item)
	
	public void increaseAttackRange(int amount){
		attackRange+=amount;
	}
	
	/************************************************************************************/
	//Returns the distance a unit can attack
	public int getAttackRange(){
		return attackRange;
	}
	
	/************************************************************************************/
	/**	Removes health based on the amount of attack an enemy does on the unit
		sets the units status to dead if the attack makes them lose all their health.
		Also checks if they have the second chance item and if they do they get to stay alive
		and get to have half of their max health
	*/
	public void takeHit(int damage){
		health=health-(damage/defenseAmount);
		boolean hasTwo=false;
		SecondChance two=new SecondChance();
		if (health<0){//checks if health goes to 0 or less
			dead();
		}

	}
	/************************************************************************************/

	public void increaseDefenseMultiplier(int amount){
		defenseAmount+=amount;
	}
	
	/************************************************************************************/
	//Sets the status of the unit to dead
	
	private void dead() {
		alive=false;
	}
	
	/************************************************************************************/
	//Returns whether is not the unit is dead or alive
	
	public boolean isAlive(){
		return alive;
	}

	/************************************************************************************/
	//Adds an item to the units inventory
	public void addItem (Item item){
		items.add(item);
	}
	
	/************************************************************************************/
	//Return a list of the current items a user has
	
	public ArrayList getInventory(){
		return items;
	}
	
	/************************************************************************************/
	//Unit gives one of their items to another unit
	
	public void trade(Unit u, Item I){
		u.addItem(I);
	}
	/************************************************************************************/
	//This method makes the item be used
	
	public void useItem(Item I){
		for(Item obj: items){
			if(I.compareTo(obj)==0){
				I.activate(this);
				removeItem(I);
				break;
			}
		}
	}
	/************************************************************************************/
	//Method gets rid of an item from the users inventory
	
	public void removeItem(Item I){
		for(Item obj: items){
			if(I.compareTo(obj)==0){
				items.remove(obj);
				break;
			}
		}
	}

	public int getMovesLeft() {
		return movesLeft;
	}

	public int getAttackPower() {
		return attackPower;
	}

	public void setMovesLeft(int distance) {
		movesLeft = distance;
	}
	
	public void moveTaken(){
		movesLeft--;
	}
	
	public void draw(Graphics2D g2, int height, int width){
		if(standingImage==null){
			initializeImages();
		}
		
		if(isAlive()){
			g2.drawImage(standingImage, location.y*width, location.x*height, width, height, null);
		}
		else{//this unit is dead
			g2.drawImage(headstone, location.y*width, location.x*height, width, height, null);
		}
	}
	/************************************************************************************/

	private void initializeImages() {
		//need to figure out which image the unit should have with them
		switch(charRepresentation){
		case 'l'://if it is good link or bad link it is the same image
		case 'L':
			try {
				standingImage=ImageIO.read(new File("images/linkStanding.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case 'g':
		case 'G':
			try {
				standingImage=ImageIO.read(new File("images/gokuStanding.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case 'w':
		case 'W':
			try {
				standingImage=ImageIO.read(new File("images/marioStanding.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case 'm':
		case 'M':
			try {
				standingImage=ImageIO.read(new File("images/MegaManStanding.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case 's':
		case 'S':
			try {
				standingImage=ImageIO.read(new File("images/SonicStanding.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case 'p':
		case 'P':
			try {
				standingImage=ImageIO.read(new File("images/Princess.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		//initialize the headstone image
		try {
			headstone=ImageIO.read(new File("images/headstone.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
