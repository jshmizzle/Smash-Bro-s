package command;

import server.TRPGServer;

public class ConnectCommand extends Command<TRPGServer>{
	
	private String source;
	
	/**
	 * Command connects the client based on the given string.
	 * @param source
	 */
	
	public ConnectCommand(String source) {
		super(source);
	}

	@Override
	public void execute(TRPGServer executeOn) {
		executeOn.disconnect(source);
	}

}
