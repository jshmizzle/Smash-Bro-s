package command;

import java.awt.Point;
import java.util.Map;

import model.Item;
import server.TRPGServer;

public class SomethingChangedCommand extends Command<TRPGServer>{
	
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
	public SomethingChangedCommand(String source,Item item, Point p) {
		super(source);
		this.item=item;
		this.p=p;
	}

	@Override
	public void execute(TRPGServer executeOn) {
		// TODO Auto-generated method stub
		executeOn.removeItem(source,item,p);
	}
	

}
