package command;

import java.awt.Point;
import java.util.ArrayList;

import model.Unit;
import client.Client;

public class UnitMovedCommand extends Command<Client>{

	private String source;
	private ArrayList<Point> moves;
	private Unit unit;
	
	public UnitMovedCommand(String source, Unit u, ArrayList<Point> moves) {
		super(source);
		this.moves=moves;
		this.unit=u;
	}

	@Override
	public void execute(Client executeOn) {
		executeOn.unitMoved(source, unit, moves);
	}

}
