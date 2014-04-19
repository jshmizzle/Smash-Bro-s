package command;

import java.awt.List;
import java.awt.Point;
import java.util.Map;

import client.TRPGClient;
import model.Unit;
import server.TRPGServer;

public class UnitMovedDownCommand extends Command<TRPGClient>{
	
	public UnitMovedDownCommand(String source) {
		super(source);
	}

	private String source;
	private Unit u;
	private Point p;
	
	/**
	 * Moves client's unit down one point
	 * 
	 * @param source
	 * @param unit
	 * @param point
	 */
	
	public UnitMovedDownCommand(String source, Unit u, Point p) {
		super(source);
		this.u=u;
		this.p=p;
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.moveUnitDown(source, u, p);
	}

}
