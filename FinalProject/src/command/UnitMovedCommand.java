package command;

import java.awt.Point;
import java.util.ArrayList;

import model.Unit;
import client.Client;

public class UnitMovedCommand extends Command<Client>{

	private String source;
	private ArrayList<Point> moves;
	private int index;
	
	public UnitMovedCommand(String source, int index, ArrayList<Point> moves) {
		super(source);
		this.moves=moves;
		this.index=index;
	}

	@Override
	public void execute(Client executeOn) {
		executeOn.unitMoved(source, index, moves);
	}

}
