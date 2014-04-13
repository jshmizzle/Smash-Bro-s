package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import model.UpdateCommand;

public class ServerHandler implements Runnable {

	ObjectInputStream inputStream;
	NetpaintClient client;
	UpdateCommand command;
	
	public ServerHandler(NetpaintClient client, ObjectInputStream in){
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
