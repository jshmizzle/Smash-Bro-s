package command;

import java.awt.Point;
import java.util.ArrayList;

import client.Client;
import client.TRPGClient;

public class UnitMoved extends Command<Client>{

	private String source;
	private ArrayList<Point> moves;
	
	public UnitMoved(String source, ArrayList<Point> moves) {
		super(source);
		this.moves=moves;
	}

	@Override
	public void execute(Client executeOn) {
		// TODO Auto-generated method stub
		executeOn.unitMoved(source, moves);
	}

}
