package command;

import java.awt.List;
import java.awt.Point;
import java.util.Map;

import model.Unit;
import server.TRPGServer;

public class UnitMovedRightCommand extends Command<TRPGServer>{
	
	public UnitMovedRightCommand(String source) {
		super(source);
		// TODO Auto-generated constructor stub
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
	
	public UnitMovedRightCommand(String source, Unit u, Point p) {
		super(source);
		this.u=u;
		this.p=p;
	}

	@Override
	public void execute(TRPGServer executeOn) {
		executeOn.moveUnitRight(source, u, p);
	}

}
