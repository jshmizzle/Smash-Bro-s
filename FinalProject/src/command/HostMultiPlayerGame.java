package command;

import client.TRPGClient;

public class HostMultiPlayerGame extends Command<TRPGClient> {

	public HostMultiPlayerGame(String source) {
		super(source);
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.hostMultiPlayerGame(getSource());
	}

}
