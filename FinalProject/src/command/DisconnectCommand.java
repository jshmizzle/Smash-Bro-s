package command;

import server.TRPGServer;
import client.ServerHandler;

public class DisconnectCommand extends Command<TRPGServer>{
	
	private String source;
	
	public DisconnectCommand(String source) {
		super(source);
	}

	@Override
	public void execute(TRPGServer executeOn) {
		executeOn.disconnect(source);
	}

}
