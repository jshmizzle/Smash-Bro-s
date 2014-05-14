package command;

import client.TRPGClient;

public class LoadSavedGame extends Command<TRPGClient> {

	public LoadSavedGame(String source) {
		super(source);
	}


	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.loadSavedGame();
	}

}