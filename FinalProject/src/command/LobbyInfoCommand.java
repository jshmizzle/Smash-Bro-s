package command;

import java.awt.Point;

import model.Unit;
import client.TRPGClient;

public class LobbyInfoCommand extends Command<TRPGClient>{
	
	private String source;
	
	/**
	 * New client logs into server and main menu opens
	 * 
	 * @param source
	 * 
	 */
	
	public LobbyInfoCommand(String source) {
		super(source);
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.welcomeToLobby(source);
	}

}
