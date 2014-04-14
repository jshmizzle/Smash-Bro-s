package command;

import java.util.Map;

import server.TRPGServer;

public class SomethingChangedCommand extends Command<TRPGServer>{
	
	private String source;
	private Map<Item,Point> itemChanges;
	
	
	/**
	 * This command would take the name of the client as a string, and a map of the item that changed (
	 * 
	 * @param source
	 * @param itemChanges
	 */
	public SomethingChangedCommand(String source,Map<Item,Point> itemChanges) {
		super(source);
		this.itemChanges=itemChanges;
	}

	@Override
	public void execute(TRPGServer executeOn) {
		// TODO Auto-generated method stub
		executeOn.removeItems(itemChanges);
	}
	

}
