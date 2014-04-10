
public abstract class Unit {
		
	private String name;
	private int health;
	private int moveDistance;
	
	public Unit (String n, int hp, int distance){
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
	
}
