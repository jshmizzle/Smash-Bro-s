package command;

import server.TRPGServer;

public class DisconnectCommand extends Command<TRPGServer>{
	
	private String source;
	
	/**
	 * Command disconnects the client based on the given string.
	 * @param source
	 */
	
	public DisconnectCommand(String source) {
		super(source);
	}

	@Override
	public void execute(TRPGServer executeOn) {
		executeOn.disconnect(source);
	}

}
