package command;

import java.awt.Point;
import java.util.Map;

import client.TRPGClient;
import model.Item;
import model.Unit;
import server.TRPGServer;

public class ItemUsedCommand extends Command<TRPGClient>{
	
	private String source;
	private Item item;
	private Unit u;;
	
	
	/**
	 * Command that takes client name as string, item that changed
	 * and point it was at.
	 * 
	 * @param source
	 * @param itemChanges
	 */
	public ItemUsedCommand(String source,Unit u,Item item) {
		super(source);
		this.item=item;
		this.u=u;
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.useItem(source,u, item);
	}
	

}
