package command;

import client.Client;

public class NewGameCommand extends Command<Client> {

	public NewGameCommand(String source) {
		super(source);
	}

	@Override
	public void execute(Client executeOn) {
		// TODO Auto-generated method stub
		executeOn.newGame();
	}

}
