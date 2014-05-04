package command;

import model.Map;
import model.Scenario;
import client.TRPGClient;

public class MapAndScenarioSelected extends Command<TRPGClient> {

	private Map map;
	private Scenario scenario;
	
	public MapAndScenarioSelected(String source, Map mapChoice, Scenario scenarioChoice) {
		super(source);
		this.map=mapChoice;
		this.scenario=scenarioChoice;
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.setMapAndScenario(map, scenario);
	}

}
