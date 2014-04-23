package command;

import java.awt.Point;
import java.util.Map;

import client.Client;
import client.TRPGClient;
import model.Item;
import model.Unit;
import server.TRPGServer;

public class PickUpItemCommand extends Command<Client>{
	
	private String source;
	private Point p;
	
	
	/**
	 * Command that takes client name as string, and item location
	 * 
	 * @param source
	 * @param p
	 */
	public PickUpItemCommand(String source,Point p) {
		super(source);
		this.p=p;
	}

	@Override
	public void execute(Client executeOn) {
		executeOn.pickUpItem(source, p);
	}

	
	

}
