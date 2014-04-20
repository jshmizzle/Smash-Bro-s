package command;

import java.awt.Point;
import java.util.Map;

import client.TRPGClient;
import model.Item;
import model.Unit;
import server.TRPGServer;

public class UnitDiedCommand extends Command<TRPGClient>{
	
	private String source;
	private Unit u;;
	
	
	/**
	 * Command that takes client name as string, item that changed
	 * and point it was at.
	 * 
	 * @param source
	 * @param itemChanges
	 */
	public UnitDiedCommand(String source,Unit u) {
		super(source);
		this.u=u;
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.unitDied(source,u);
	}
	

}
