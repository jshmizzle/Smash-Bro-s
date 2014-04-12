
/*
 * Abstract item class that contains general methods for all items
 */
public abstract class Item implements Comparable<Item>{

	protected String name;
	protected int statBoost;
	
	/************************************************************************************/
	/**
	 * Constructor that creates item
	 * @param name Name of the item
	 * @param statBoost How much item affects the individual stats
	 */
	public Item(String name, int statBoost){
		this.name=name;
		this.statBoost=statBoost;
	}
	/************************************************************************************/
	
	/************************************************************************************/
	//Returns the name of the item
	public String getName(){
		return name;
	}
	/************************************************************************************/
	
	/************************************************************************************/
	//Returns the amount the item increases a specific stat
	public int getStatBoost(){
		return statBoost;
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
	abstract void activate();

}