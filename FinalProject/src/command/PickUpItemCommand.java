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
	private Item item;
	private int index;
	
	
	/**
	 * Command that takes client name as string, item that changed
	 * and point it was at.
	 * 
	 * @param source
	 * @param itemChanges
	 */
	public PickUpItemCommand(String source,int index,Item item) {
		super(source);
		this.item=item;
		this.index=index;
	}

	@Override
	public void execute(Client executeOn) {
		executeOn.pickUpItem(source, index, item);
	}

	
	

}
