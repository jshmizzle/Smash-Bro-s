package command;

import client.Client;
import model.Map;
import model.Scenario;
import client.TRPGClient;

public class MapAndScenarioSelected extends Command<Client> {

	private Map map;
	private Scenario scenario;
	
	public MapAndScenarioSelected(String source, Map mapChoice, Scenario scenarioChoice) {
		super(source);
		this.map=mapChoice;
		this.scenario=scenarioChoice;
	}

	@Override
	public void execute(Client executeOn) {
		executeOn.setMapAndScenario(getSource(), map, scenario);
	}

}
