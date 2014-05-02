package command;

import client.Client;

public class MapAndScenarioSelected extends Command<Client> {

	private int map, scenario;
	
	public MapAndScenarioSelected(String source, int mapChoice, int scenarioChoice) {
		super(source);
		this.map=mapChoice;
		this.scenario=scenarioChoice;
	}

	@Override
	public void execute(Client executeOn) {
		executeOn.setMapAndScenario(getSource(), map, scenario);
	}

}
