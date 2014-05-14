package command;

import java.awt.Point;
import java.util.Map;

import client.Client;
import client.TRPGClient;
import model.Item;
import model.Unit;
import server.TRPGServer;

public class UseItemCommand extends Command<Client>{
	
	private int itemIndex;
	private int index;
	
	
	/**
	 * Command that takes client name as string, item that changed
	 * and point it was at.
	 * 
	 * @param source
	 * @param itemChanges
	 */
	public UseItemCommand(String source,int index, int itemIndex) {
		super(source);
		this.itemIndex=itemIndex;
		this.index=index;
	}

	@Override
	public void execute(Client executeOn) {
		executeOn.useItem(getSource(),index, itemIndex);
	}
	

}
