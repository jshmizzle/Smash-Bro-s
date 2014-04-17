package command;

import java.awt.Point;

import model.Unit;
import client.TRPGClient;

public class LobbyInfoCommand extends Command<TRPGClient>{
	
	private String source;
	
	/**
	 * PlayerMovedCommand could take the name of the player as a string,
	 * the unit that moved, and the point it moved to.
	 * 
	 * @param source
	 * @param moves
	 */
	
	public LobbyInfoCommand(String source) {
		super(source);
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.welcomeToLobby(source);
	}

}
