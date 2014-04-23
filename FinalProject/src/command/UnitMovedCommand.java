package command;

import java.awt.Point;
import java.util.ArrayList;

import model.Unit;
import client.Client;

public class UnitMovedCommand extends Command<Client>{

	private String source;
	private ArrayList<Point> moves;
	private int unitIndex;
	
	public UnitMovedCommand(String source, int unitIndex, ArrayList<Point> moves) {
		super(source);
		this.source=source;
		this.moves=moves;
		this.unitIndex=unitIndex;
	}

	@Override
	public void execute(Client executeOn) {
		System.out.println("command sent");
		executeOn.unitMoved(source, unitIndex, moves);
	}

}
