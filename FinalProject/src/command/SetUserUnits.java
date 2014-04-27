package command;

import java.util.ArrayList;

import model.Unit;
import client.TRPGClient;

public class SetUserUnits extends Command<TRPGClient> {

	ArrayList<Unit> userUnitList;
	
	public SetUserUnits(String source, ArrayList<Unit> userUnitList) {
		super(source);
		this.userUnitList=userUnitList;
	}

	@Override
	public void execute(TRPGClient executeOn) {
		executeOn.setUserUnits(getSource(), userUnitList);
	}

}
