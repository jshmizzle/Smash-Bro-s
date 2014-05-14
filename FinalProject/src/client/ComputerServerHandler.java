package client;

import java.io.IOException;
import java.io.ObjectInputStream;

import command.Command;

public class ComputerServerHandler implements Runnable{
	ObjectInputStream inputStream;
	ComputerClient client;
	Command<ComputerClient> command;
	
	public ComputerServerHandler(ComputerClient client, ObjectInputStream in){
		this.client=client;
		this.inputStream=in;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		while(true){
			try {
				this.command=(Command<ComputerClient>)inputStream.readObject();
				System.out.println(command);
				command.execute(client);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}
