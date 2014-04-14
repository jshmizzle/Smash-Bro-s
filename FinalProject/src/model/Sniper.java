package model;

/*
 * This item increases a units attack range
 */
public class Sniper extends Item{

	public Sniper() {
		super("Sniper");
		
	}

	@Override
	void activate(Unit u) {
		u.increaseAttackRange(2);
	}

}
