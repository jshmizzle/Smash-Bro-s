package command;

import java.awt.List;
import java.util.Map;

import server.TRPGServer;

public class PlayerMovedCommand extends Command<TRPGServer>{
	
	private String source;
	private Map<Unit,Point> moves;
	
	/**
	 * PlayerMovedCommand could take the name of the player (or object) as a string,
	 * and a Map of units and the moves each unit made 
	 * @param source
	 * @param moves
	 */
	
	public PlayerMovedCommand(String source, Map<Unit,Point> moves) {
		super(source);
		this.moves = moves;
	}

	@Override
	public void execute(TRPGServer executeOn) {
		executeOn.movePlayer(source, moves);
	}

}
