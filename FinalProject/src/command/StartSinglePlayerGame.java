package command;

import client.TRPGClient;

public class StartSinglePlayerGame extends Command<TRPGClient> {

	public StartSinglePlayerGame(String source) {
		super(source);
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.startSinglePlayerGame();
	}

}
