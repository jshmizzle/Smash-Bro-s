package command;

import java.awt.Point;
import java.util.Map;

import client.Client;
import client.TRPGClient;
import model.Item;
import model.Unit;
import server.TRPGServer;

public class PickUpItemCommand extends Command<Client>{
	
	/**
	 * Command that takes client name as string, and item location
	 * 
	 * @param source
	 * @param p
	 */
	public PickUpItemCommand(String source) {
		super(source);
	}

	@Override
	public void execute(Client executeOn) {
		executeOn.pickUpItem(getSource());
	}

	
	

}
