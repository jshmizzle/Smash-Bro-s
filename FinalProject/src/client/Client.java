package client;

import java.awt.Point;
import java.util.ArrayList;

import model.Item;
import model.Unit;

public interface Client {
	
	public void useItem(String client, Unit u, Item item);
	
	public void moveUnitLeft(String client, Unit u, Point p);
	
	public void moveUnitRight(String client, Unit u, Point p);
	
	public void moveUnitDown(String client, Unit u, Point p);
	
	public void moveUnitUp(String client, Unit u, Point p);

	public void welcomeToLobby(String client);

	public void unitDied(String client, Unit u);
	
	public void attackUnit(String client, Unit from, Unit to);
	
	public void endTurn(String client);
	
	public void pickUpItem(String client, Unit u, Item item);
	
	public void unitMoved(String source, ArrayList<Point> moves);



}
