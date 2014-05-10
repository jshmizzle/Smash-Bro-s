package command;

import client.TRPGClient;

public class JoinMultiPlayerGame extends Command<TRPGClient> {

	public JoinMultiPlayerGame(String source) {
		super(source);
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.joinMultiPlayerGame(getSource());
	}

}
