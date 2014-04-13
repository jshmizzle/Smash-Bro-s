
/*
 * This item increases a units attack
 */
public class Rage extends Item{
	
	public Rage(){
		super("Rage");
	}

	@Override
	void activate(Unit u) {
		u.addAttack(5);	
	}
}
