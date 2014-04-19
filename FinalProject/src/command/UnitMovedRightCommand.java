package command;

import java.awt.List;
import java.awt.Point;
import java.util.Map;

import client.TRPGClient;
import model.Unit;
import server.TRPGServer;

public class UnitMovedRightCommand extends Command<TRPGClient>{
	
	public UnitMovedRightCommand(String source) {
		super(source);
	}

	private String source;
	private Unit u;
	private Point p;
	
	/**
	 * Moves client's unit right one point
	 * 
	 * @param source
	 * @param unit
	 * @param point
	 */
	
	public UnitMovedRightCommand(String source, Unit u, Point p) {
		super(source);
		this.u=u;
		this.p=p;
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.moveUnitRight(source, u, p);
	}

}
