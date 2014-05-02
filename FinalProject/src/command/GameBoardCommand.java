package command;

import java.util.ArrayList;

import client.TRPGClient;
import model.Map;
import model.Scenario;
import model.Unit;
import server.TRPGServer;

public class GameBoardCommand extends Command<TRPGClient>{
	
	private ArrayList<Unit> userUnits;
	private ArrayList<Unit> compUnits;
	private Map map;
	private Scenario scenario;
/**
 * Initial board command that takes all info from client
 * 
 * @param source
 * @param userUnits
 * @param compUnits
 * @param map
 * @param scenario
 */
	public GameBoardCommand(String source,ArrayList<Unit> userUnits,ArrayList<Unit> compUnits, Map map, Scenario scenario) {
		super(source);
		this.userUnits=userUnits;
		this.compUnits=userUnits;
		this.map=map;
		this.scenario=scenario;
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.createGameBoard(userUnits,compUnits,map,scenario);
	}

}
