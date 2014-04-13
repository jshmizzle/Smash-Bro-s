package model;
/*
 * This item restores a unit back to life immediately with half of their health
 * if they die with it
 */
public class SecondChance extends Item{

	public SecondChance() {
		super("Second Chance");
	}

	@Override
	void activate(Unit u) {
		//method is activated automatically when person dies
	}
	
}
