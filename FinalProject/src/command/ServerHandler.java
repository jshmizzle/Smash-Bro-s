package command;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import client.TRPGClient;

public class ServerHandler implements Runnable {

	ObjectInputStream inputStream;
	TRPGClient client;
	UpdateCommand command;
	
	public ServerHandler(TRPGClient client, ObjectInputStream in){
		this.client=client;
		this.inputStream=in;
	}

	@Override
	public void run() {
		System.out.println("before while true");
		while(true){
			try {
				this.command=(UpdateCommand)inputStream.readObject();
				command.execute(client);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}

}
