package client;

import java.awt.Point;
import java.util.ArrayList;

import model.Item;
import model.Unit;

public interface Client {
	
	public void useItem(String client, Unit u, Item item);

	public void welcomeToLobby(String client);

	public void unitDied(String client, Unit u);
	
	public void attackUnit(String client, Unit from, Unit to);
	
	public void endTurn(String client);
	
	public void pickUpItem(String client, Unit u, Item item);
	
	public void unitMoved(String source, int unitIndex, ArrayList<Point> moves);

	public void newGame();


}
