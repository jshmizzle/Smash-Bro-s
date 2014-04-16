package command;

import java.util.ArrayList;

import model.Unit;
import server.TRPGServer;

public class GameBoardCommand extends Command<TRPGServer>{
	
	private ArrayList<Unit> userUnits;
	private ArrayList<Unit> compUnits;
	private int map;
	private int scenario;

	public GameBoardCommand(String source,ArrayList<Unit> userUnits,ArrayList<Unit> compUnits, int map, int scenario) {
		super(source);
		// TODO Auto-generated constructor stub
		this.userUnits=userUnits;
		this.compUnits=userUnits;
		this.map=map;
		this.scenario=scenario;
	}

	@Override
	public void execute(TRPGServer executeOn) {
		// TODO Auto-generated method stub
		executeOn.createGameBoard(userUnits,compUnits,map,scenario);
	}

}
