package command;

import model.GameBoard;
import client.TRPGClient;

public class UpdateCommand extends Command<TRPGClient>{
	
	private String source;
	private GameBoard currentBoard;
	
	/**
	 * Command updates current gameboard.
	 * @param source
	 * @param currentBoard
	 */
	
	public UpdateCommand(String source, GameBoard currentBoard) {
		super(source);
		this.currentBoard = currentBoard;
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.update(currentBoard);
	}
	
}
