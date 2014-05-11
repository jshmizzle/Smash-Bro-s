package command;

import java.awt.Point;

import client.Client;

public class TeleportUnitCommand extends Command<Client> {

	private int unitIndex;
	private Point teleLocation;
	
	public TeleportUnitCommand(String source, int unitIndex, Point teleLocation) {
		super(source);
		this.unitIndex=unitIndex;
		this.teleLocation=teleLocation;
	}

	@Override
	public void execute(Client executeOn) {
		executeOn.teleportUnit(getSource(), unitIndex, teleLocation);
	}

}
