/*
 * This item increases the distance a unit can go during the move stage
 */
public class Sneakers extends Item{

	public Sneakers (){
		super("Sneakers");
	}

	@Override
	void activate(Unit u) {
		u.addDistance(2);
	}
	
}
