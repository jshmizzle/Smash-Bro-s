package command;

import java.awt.List;
import java.awt.Point;
import java.util.Map;

import client.Client;
import client.TRPGClient;
import model.Unit;
import server.TRPGServer;

public class UnitMovedDownCommand extends Command<Client>{
	
	public UnitMovedDownCommand(String source) {
		super(source);
	}

	private String source;
	private int index;
	private Point p;
	
	/**
	 * Moves client's unit down one point
	 * 
	 * @param source
	 * @param unit
	 * @param point
	 */
	
	public UnitMovedDownCommand(String source, int index, Point p) {
		super(source);
		this.index=index;
		this.p=p;
	}

	@Override
	public void execute(Client executeOn) {
		executeOn.moveUnitDown(source, index, p);
	}

}
