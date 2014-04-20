package command;

import client.TRPGClient;

public class EndTurnCommand extends Command<TRPGClient>{
	
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
	public void execute(TRPGClient executeOn) {
		executeOn.endTurn(source);
	}
	

}
