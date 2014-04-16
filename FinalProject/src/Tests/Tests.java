package Tests;
import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;

import model.GameBoard;
import model.MegaMan;
import model.Sonic;
import model.Unit;

import org.junit.Test;


public class Tests {
	

	 @Test
     public void testUnit() {
		 Point b = new Point(0,0);
		 Sonic x = new Sonic("Sonic", 10000, 10, b);
		 assertEquals(x.getName(), "Sonic");
		 assertEquals(x.getDistance(), 20);
		 assertEquals(x.getCharRepresentation(), 'S');
		 assertEquals(x.getHealth(), 10000); 
   }
	 
	 @Test
     public void testMakingANewGameBoard() {
		
		 Point b = new Point(0, 0);
		 Point x = new Point(0, 10);
		 ArrayList<Unit> player = new ArrayList<Unit>();
		 ArrayList<Unit> computer = new ArrayList<Unit>();
		 
		 Sonic s = new Sonic("Sonic", 10000, 10, b);
		 Sonic t = new Sonic("Sonic", 10000, 10, b);
		 Sonic r = new Sonic("Sonic", 10000, 10, b);
		 Sonic e = new Sonic("Sonic", 10000, 10, b);
		 Sonic sComputer = new Sonic("Sonic", 10000, 10, x);
		 player.add(s);
		 player.add(t);
		 player.add(r);
		 player.add(e);
		 computer.add(sComputer);
		 GameBoard g = new GameBoard(player, computer, 1, 0);
		 Point edgeTop = new Point(-1, 0);
		 Point bottom = new Point(0, 21);
		 e.setLocation(bottom);
		 assertFalse(g.checkAvailable(edgeTop)); //Testing the edge of the board
		 //assertFalse(g.moveDown(e)); //Testing the edge of the board	
		 assertEquals(sComputer.getLocation(), new Point(19, 9));
		 System.out.println(g.toString());
		 //assertTrue(g.moveUp(sComputer));
		 //assertTrue(g.moveUp(sComputer));
		 //assertTrue(g.moveUp(sComputer));
		 assertEquals(sComputer.getLocation(), new Point(16, 9));
		 //assertTrue(g.moveUp(sComputer));
		 assertFalse(g.checkOpenLineOfFire(sComputer, new Point(15,8)));
		 assertFalse(g.checkOpenLineOfFire(sComputer, new Point(15,9)));
		 System.out.println("\n\n\n\n" +g.toString());
		
		
	 }

	 @Test
     public void testOpenLineOfFire() {
		
		 Point b = new Point(0, 0);
		 Sonic sonic = new Sonic("Sonic", 50, 10, b);
		 MegaMan m = new MegaMan();
		 Sonic sComputer = new Sonic("Sonic", 50, 10, b);
		 ArrayList<Unit> player = new ArrayList<Unit>();
		 ArrayList<Unit> computer = new ArrayList<Unit>();
		 player.add(sonic);
		 player.add(m);
		 computer.add(sComputer);
		 
		 GameBoard g = new GameBoard(player, computer, 1, 0);
		 sonic.setLocation(new Point(0, 1));
		 sComputer.setLocation(new Point(0, 2));
		 
		 /*
		  * Sonic is at (0, 1) so he should be able to attack enemies at:
		  * (0, 0), (0, 2), (1, 1)
		  */
		 
		 assertTrue(g.checkOpenLineOfFire(sonic, new Point(0, 2)));
		 assertTrue(g.checkOpenLineOfFire(sonic, new Point(0, 0)));
		 assertTrue(g.checkOpenLineOfFire(sonic, new Point(1, 1)));
		 assertFalse(g.checkOpenLineOfFire(sonic, new Point(0, 3)));
		 assertFalse(g.checkOpenLineOfFire(sonic, new Point(1, 3)));
		 assertFalse(g.checkOpenLineOfFire(sonic, new Point(1, 2)));
		 assertFalse(g.checkOpenLineOfFire(sonic, new Point(0, -1)));
		 assertFalse(g.checkOpenLineOfFire(sonic, new Point(-999, -1)));
		 
		 m.setLocation(new Point(0, 18));
		 sComputer.setLocation(new Point(0, 17));
		 /*
		  * MegaMan is at (0, 18) so he can attack units
		  * that are at (0, 19), (1, 18), (2, 18), (0, 17),
		  * and (0, 16)
		  */
		// assertTrue(g.checkAvailable(new Point( 0, 20)));
		 assertTrue(g.checkOpenLineOfFire(m, new Point(0, 19)));
		 assertTrue(g.checkOpenLineOfFire(m, new Point(1, 18)));
		 assertTrue(g.checkOpenLineOfFire(m, new Point(2, 18)));
		 assertTrue(g.checkOpenLineOfFire(m, new Point(0, 17)));
		 assertTrue(g.checkOpenLineOfFire(m, new Point(0, 16)));
		 assertFalse(g.checkOpenLineOfFire(m, new Point(0, 15)));
		 assertFalse(g.checkOpenLineOfFire(m, new Point(-1, 18)));
		 assertFalse(g.checkOpenLineOfFire(m, new Point(3, 18)));
		 assertFalse(g.checkOpenLineOfFire(m, new Point(2, 19)));
		 
   }
	
}
