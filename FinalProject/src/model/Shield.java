package model;
/*
 * This item increases a units defense
 */
public class Shield extends Item {
	
	public Shield(){
		super("Shield");
	}

	@Override
	void activate(Unit u) {
		u.increaseDefenseMultiplier(2);
	}

}
