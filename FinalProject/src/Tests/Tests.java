package Tests;
import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;

import model.GameBoard;
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
		 assertFalse(g.moveDown(e)); //Testing the edge of the board	
		 assertEquals(sComputer.getLocation(), new Point(19, 9));
		 System.out.println(g.toString());
		 assertTrue(g.moveUp(sComputer));
		 assertTrue(g.moveUp(sComputer));
		 assertTrue(g.moveUp(sComputer));
		 assertEquals(sComputer.getLocation(), new Point(16, 9));
	
	//	 assertTrue(g.moveLeft(sComputer));
	//	 assertTrue(g.moveRight(sComputer));
		 //assertEquals(sComputer.getLocation(), new Point(19, 10));
		 
		 System.out.println("\n\n\n\n" +g.toString());
		
	 }
	 
	 public void testPaintingTheGameBoardToAGUI(){
		 //initialize the units and the GameBoard
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
		 
		 
	 }
	
}
