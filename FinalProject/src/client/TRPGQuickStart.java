package client;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import server.TRPGServer;

public class TRPGQuickStart {

	public static void main(String[] args) {
		TRPGServer server=new TRPGServer(9001);
		ComputerClient compClient=new ComputerClient();
		TRPGClient userClient=new TRPGClient("localhost", 9001, "User");
		TRPGClient userClient2=new TRPGClient("localhost", 9001, "User2");
	}
}

