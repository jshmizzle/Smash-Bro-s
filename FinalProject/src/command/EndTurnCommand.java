package command;

import client.Client;
import client.TRPGClient;

public class EndTurnCommand extends Command<Client>{
	
	private String source;
	
	
	/**
	 * Command that takes client name as string, item that changed
	 * and point it was at.
	 * 
	 * @param source
	 * @param itemChanges
	 */
	public EndTurnCommand(String source) {
		super(source);
	}

	@Override
	public void execute(Client executeOn) {
		executeOn.endTurn(getSource());
	}
	

}
