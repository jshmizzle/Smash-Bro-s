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
	private int fromIndex;
	private int toIndex;
	
	
	/**
	 * Command that takes client name as string, item that changed
	 * and point it was at.
	 * 
	 * @param source
	 * @param itemChanges
	 */
	public UnitAttackCommand(String source,int fromIndex,int toIndex) {
		super(source);
		this.fromIndex=fromIndex;
		this.toIndex=toIndex;
	}

	@Override
	public void execute(Client executeOn) {
		executeOn.attackUnit(source,fromIndex, toIndex);
	}
	

}
