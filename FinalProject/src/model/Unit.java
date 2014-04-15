package model;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/*
 * Unit class. Holds all the information and methods for each unit
 */
public class Unit{ 
		
	private String name;
	private int health;
	private int moveDistance;
	private ArrayList <Item> items;
	private int [] stats;
	//private Strategy strategy;
	private boolean alive;
	private int attack;
	private Point location;
	private BufferedImage image;
	private char charRepresentation;
	private int defenseAmount;
	private int healthFull;
	private int attackRange;
	
	/************************************************************************************/
	/**
	 * Sets up units initial stats
	 * @param n Name of the unit
	 * @param hp Units health
	 * @param distance How far unit can move
	 * @param attack Amount of damage the unit does
	 * @param i Image of unit
	 * @param p Location of unit
	 * @param c The character that represents the unit
	 */
	public Unit (String n, int hp, int distance, int attack, int attackRange, BufferedImage i, Point p, char c){

		this.name = n;
		this.health = hp;
		healthFull=hp;
		this.moveDistance = distance;
		this.attack=attack;
		image=i;
		location=p;
		attackRange=this.attackRange;
		charRepresentation=c;
		defenseAmount=1;
		alive=true;
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
	 */
	
	public Unit (String n, int hp, int distance, int attack, int attackRange, Point p, char c){

		this.name = n;
		this.health = hp;
		healthFull=hp;
		this.moveDistance = distance;
		this.attack=attack;
		location=p;
		this.attackRange = attackRange;
		charRepresentation=c;
		defenseAmount=1;
		alive=true;
	}
	/************************************************************************************/
	//Returns the location on the board the unit is in 

	public Point getLocation(){
		return location;
	}
	
	/************************************************************************************/
	//sets the location of the unit to the point that is passed in
	
	public void setLocation(Point p){
		this.location = p;
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
		u.takeHit(attack);
	}
	
	/************************************************************************************/
	//Adds to Amount of attack a unit can do (item)
	
	public void addAttack(int amount){
		attack+=amount;
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
			for(Item obj: items){
				if(two.compareTo(obj)==0){ //checks if any items in the list are secondchances
					items.remove(obj);
					hasTwo=true;
					break;
				}
			}
			if(hasTwo==true){ //if they have second chance they dont die
				health=healthFull/2;
			}
			else{ //if not they die
				dead();
			}
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
	
	/************************************************************************************/
}
