import java.util.ArrayList;


public abstract class Unit extends GameObject{
		
	private String name;
	private int health;
	private int moveDistance;
	private ArrayList <Item> items;
	private int [] stats;
	private Strategy strategy;
	
	
	public Unit (String n, int hp, int distance){
		super(this);
		this.name = n;
		this.health = hp;
		this.moveDistance = distance;
	}
	
	public String getName(){
		return name;
	}
	

	public int getHealth(){
		return health;
	}
	
	public int getDistance(){
		return moveDistance;
	}
	
	public int takeHit(int damage){
		health=health-damage;
		if (health<0){
			dead();
		}
	}

	private void dead() {
		// TODO Auto-generated method stub
		
	}
	
	public void addItem (Item item){
		items.add(item);
	}
	
	public ArrayList getInventory(){
		return items;
	}
	
	public void trade(Unit u, Item I){
		u.addItem(I);
	}
	
	public void removeItem(Item I){
		for(Item obj: items){
			if(I.compareTo(obj)==0){
				items.remove();
			}
		}
	}
	
}
