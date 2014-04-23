package command;

import java.awt.Point;
import java.util.Map;

import client.Client;
import client.TRPGClient;
import model.Item;
import model.Unit;
import server.TRPGServer;

public class UnitDiedCommand extends Command<Client>{
	
	private String source;
	private int index;
	
	
	/**
	 * Command that takes client name as string, item that changed
	 * and point it was at.
	 * 
	 * @param source
	 * @param itemChanges
	 */
	public UnitDiedCommand(String source,int index) {
		super(source);
		this.index=index;
	}

	@Override
	public void execute(Client executeOn) {
		executeOn.unitDied(source,index);
	}
	

}
