package command;

import java.awt.Point;
import java.util.Map;

import model.Item;
import server.TRPGServer;

public class ItemUsedCommand extends Command<TRPGServer>{
	
	private String source;
	private Item item;
	private Point p;
	
	
	/**
	 * Command that takes client name as string, item that changed
	 * and point it was at.
	 * 
	 * @param source
	 * @param itemChanges
	 */
	public ItemUsedCommand(String source,Item item, Point p) {
		super(source);
		this.item=item;
		this.p=p;
	}

	@Override
	public void execute(TRPGServer executeOn) {
		executeOn.useItem(source,item,p);
	}
	

}
