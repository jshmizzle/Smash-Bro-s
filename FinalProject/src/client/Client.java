package client;

import java.awt.Point;
import java.util.ArrayList;

import model.Item;
import model.Unit;

public interface Client {

	public void welcomeToLobby(String client);
	
	public void endTurn(String client);
	
	public void newGame();

	public void useItem(String source, int index, Item item);
	
	public void unitMoved(String source, int unitIndex, ArrayList<Point> moves);

	public void pickUpItem(String source, int index, Item item);

	public void attackUnit(String source, int fromIndex, int toIndex);

}
