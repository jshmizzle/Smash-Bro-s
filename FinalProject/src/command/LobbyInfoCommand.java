package command;

import java.util.ArrayList;

import client.TRPGClient;

public class LobbyInfoCommand extends Command<TRPGClient>{
	
	private String source;
	
	/**
	 * New client logs into server and chooses the join/start game option on the main menu.
	 * I was thinking that this command could be sent by the mainMenuPanel whenever somebody new
	 * connects to a multiplayer game and switches to the lobby panel. This way the other 
	 * clients know to update the panel.
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
