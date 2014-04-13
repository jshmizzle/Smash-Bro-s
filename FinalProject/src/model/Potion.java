package model;
/*
 * This item restores the user back to their maximum health
 */
public class Potion extends Item{
		
		
		public Potion (){
			super("Potion");
		}

		@Override
		void activate(Unit u) {
			u.setMaxHealt();
		}
}
