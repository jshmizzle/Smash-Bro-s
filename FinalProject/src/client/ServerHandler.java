package client;


import java.io.IOException;
import java.io.ObjectInputStream;

import command.Command;

public class ServerHandler implements Runnable {

	ObjectInputStream inputStream;
	TRPGClient client;
	Command<TRPGClient> command;
	
	public ServerHandler(TRPGClient client, ObjectInputStream in){
		this.client=client;
		this.inputStream=in;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		while(true){
			try {
				this.command=(Command<TRPGClient>)inputStream.readObject();
				command.execute(client);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}

}
