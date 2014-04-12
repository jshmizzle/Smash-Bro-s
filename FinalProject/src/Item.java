
public abstract class Item implements Comparable<Item>{

	private String name;
	private int statBoost;
	
	public Item(String name, int statBoost){
		this.name=name;
		this.statBoost=statBoost;
	}
	
	public String getName(){
		return name;
	}
	
	public int getStatBoost(){
		return statBoost;
	}
	
	public int compareTo(Item obj) {
		if(name.equals(obj.getName())){
			return 0;
		}
		return 1;
	}
	
	abstract void activate();

}