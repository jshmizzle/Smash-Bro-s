package command;

import java.awt.Point;
import java.util.ArrayList;

import client.TRPGClient;

public class UnitMoved extends Command<TRPGClient>{

	private String source;
	private ArrayList<Point> moves;
	
	public UnitMoved(String source, ArrayList<Point> moves) {
		super(source);
		this.moves=moves;
	}

	@Override
	public void execute(TRPGClient executeOn) {
		// TODO Auto-generated method stub
		executeOn.unitMoved(source, moves);
	}

}
