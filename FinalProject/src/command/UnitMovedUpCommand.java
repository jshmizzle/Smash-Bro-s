package command;

import java.awt.Point;

import model.Unit;
import client.TRPGClient;

public class UnitMovedUpCommand extends Command<TRPGClient>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8595717000330684074L;

	public UnitMovedUpCommand(String source) {
		super(source);
	}

	private String source;
	private Unit unit;
	private Point point;
	
	/**
	 * Moves client's unit up one point
	 * 
	 * @param source The unique name of the client who connected to the server.
	 * @param u The Unit which will be moving up.	
	 * @param p I'm not sure if this point is necessary. keeping it until we discuss why it's here.
	 */
	
	public UnitMovedUpCommand(String source, Unit u, Point p) {
		super(source);
		this.unit=u;
		this.point=p;
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.moveUnitUp(source, unit, point);
	}

}
