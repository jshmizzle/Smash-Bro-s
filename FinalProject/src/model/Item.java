package model;

/*
 * Abstract item class that contains general methods for all items
 */
public abstract class Item implements Comparable<Item>{

	protected String name;
	
	/************************************************************************************/
	/**
	 * Constructor that creates item
	 * @param name Name of the item
	 * @param statBoost How much item affects the individual stats
	 */
	public Item(String name){
		this.name=name;
	}
	/************************************************************************************/
	
	/************************************************************************************/
	//Returns the name of the item
	public String getName(){
		return name;
	}
	/************************************************************************************/
	
	/************************************************************************************/
	//Compares to items by name to see if they are the same
	public int compareTo(Item obj) {
		if(name.equals(obj.getName())){
			return 0;
		}
		return 1;
	}
	/************************************************************************************/
	
	//Abstract method that will activate when the item is used and carry out the desired operations
	abstract void activate(Unit u);

}