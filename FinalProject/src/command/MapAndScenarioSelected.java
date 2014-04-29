package command;

import client.TRPGClient;

public class MapAndScenarioSelected extends Command<TRPGClient> {

	private int map, scenario;
	
	public MapAndScenarioSelected(String source, int mapChoice, int scenarioChoice) {
		super(source);
		this.map=mapChoice;
		this.scenario=scenarioChoice;
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.setMapAndScenario(map, scenario);
	}

}
