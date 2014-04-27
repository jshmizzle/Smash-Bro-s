package command;

import java.util.ArrayList;

import model.Unit;
import client.TRPGClient;

public class SendMyUnitsCommand extends Command<TRPGClient>{
	
	private ArrayList<Unit> list;
	
	public SendMyUnitsCommand(String source, ArrayList<Unit> list) {
		super(source);
		this.list = list;
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.collectUnits(list);
	}

}
