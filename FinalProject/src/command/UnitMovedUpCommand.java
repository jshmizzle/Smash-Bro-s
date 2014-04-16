package command;

import java.awt.List;
import java.awt.Point;
import java.util.Map;

import client.TRPGClient;
import model.Unit;
import server.TRPGServer;

public class UnitMovedUpCommand extends Command<TRPGClient>{
	
	public UnitMovedUpCommand(String source) {
		super(source);
	}

	private String source;
	private Unit u;
	private Point p;
	
	/**
	 * PlayerMovedCommand could take the name of the player as a string,
	 * the unit that moved, and the point it moved to.
	 * 
	 * @param source
	 * @param moves
	 */
	
	public UnitMovedUpCommand(String source, Unit u, Point p) {
		super(source);
		this.u=u;
		this.p=p;
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.moveUnitUp(source, u, p);
	}

}
