package command;

import client.Client;

public class BeginGameCommand extends Command<Client> {

	public BeginGameCommand(String source) {
		super(source);
	}

	@Override
	public void execute(Client executeOn) {
		executeOn.beginGame();
	}

}
