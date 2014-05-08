package command;

import java.util.ArrayList;

import model.Unit;
import client.Client;
import client.TRPGClient;

public class SetUserUnits extends Command<Client> {

	ArrayList<Unit> userUnitList;
	
	public SetUserUnits(String source, ArrayList<Unit> userUnitList) {
		super(source);
		this.userUnitList=userUnitList;
	}

	@Override
	public void execute(Client executeOn) {
		executeOn.setUserUnits(getSource(), userUnitList);
	}

}
