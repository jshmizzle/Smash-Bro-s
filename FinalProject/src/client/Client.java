package client;

import java.awt.Point;
import java.util.ArrayList;

import model.Item;
import model.Map;
import model.Scenario;
import model.Unit;

public interface Client {

	public void endTurn(String client);
	
	public void newGame();

	public void useItem(String source, int index, int itemIndex);
	
	public void unitMoved(String source, int unitIndex, ArrayList<Point> moves);

	public void pickUpItem(String source);

	public void attackUnit(String source, int fromIndex, int toIndex);

	public void setUserUnits(String source, ArrayList<Unit> userUnits);
	
	public void setMapAndScenario(String source, Map map, Scenario scenario);
	
	public void beginGame();
	
	public void teleportUnit(String source, int unitIndex, Point teleLocation);

}
