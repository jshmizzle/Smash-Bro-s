package command;

import java.awt.List;
import java.awt.Point;
import java.util.Map;

import client.TRPGClient;
import model.Unit;
import server.TRPGServer;

public class UnitMovedLeftCommand extends Command<TRPGClient>{
	
	public UnitMovedLeftCommand(String source) {
		super(source);
	}

	private String source;
	private Unit u;
	private Point p;
	
	/**
	 * Moves client's unit left one point
	 * 
	 * @param source
	 * @param unit
	 * @param point
	 */
	
	public UnitMovedLeftCommand(String source, Unit u, Point p) {
		super(source);
		this.u=u;
		this.p=p;
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.moveUnitLeft(source, u, p);
	}

}
