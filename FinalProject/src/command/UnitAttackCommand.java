package command;

import java.awt.Point;
import java.util.Map;

import client.Client;
import client.TRPGClient;
import model.Item;
import model.Unit;
import server.TRPGServer;

public class UnitAttackCommand extends Command<Client>{
	
	private String source;
	private Unit from;
	private Unit to;
	
	
	/**
	 * Command that takes client name as string, item that changed
	 * and point it was at.
	 * 
	 * @param source
	 * @param itemChanges
	 */
	public UnitAttackCommand(String source,Unit from,Unit to) {
		super(source);
		this.from=from;
		this.to=to;
	}

	@Override
	public void execute(Client executeOn) {
		executeOn.attackUnit(source,from, to);
	}
	

}
