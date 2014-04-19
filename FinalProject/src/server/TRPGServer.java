package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import model.GameBoard;
import client.TRPGClient;

import command.Command;
import command.DisconnectCommand;

/**
 * TRPG Server
 * 
 * <p> This class is the server side of TRPG. It is responsible for
 * managing connections to clients, sending and executing commands, and
 * holds the GameBoard. <P>
 * 
 * @author The Other Guys
 *
 */

public class TRPGServer {
	private ServerSocket socket;
	private GameBoard currentBoard;
	
	private Map<String, Deque<Command<TRPGServer>>> histories;
	private Map<String, ObjectInputStream> inputs;
	private Map<String, ObjectOutputStream> outputs;
	
	/**
	 *	ClientHandler
	 * 
	 *	This class handles executing command from a single client. 
	 */
	private class ClientHandler implements Runnable{
		
		private String clientId; // name of the client
		private Deque<Command<TRPGServer>> history; // history of executed commands
		private ObjectInputStream input; // input stream to read command from
		
		public ClientHandler(String id, Deque<Command<TRPGServer>> history)
		{
			clientId = id;
			this.history=history;
			input = inputs.get(id);
			
			System.out.println("New Client " + id + " connected");
			
			//updateClients();
		}

		public void run() {
			while(true){
				try{
					Object ob = input.readObject();
					if (ob instanceof Command<?>){
						
						if(ob instanceof DisconnectCommand){
							break;
						}
						else{
						Command<TRPGClient> command = (Command<TRPGClient>)ob; // cast the object // grab a command off the queue
						
						//TODO: we do not want to execute the commands on the server side. 
						//we simply want the commands to be passed on to the clients where 
						//they will be executed correctly.
						
						//command.execute(TRPGServer.this); // execute the command on the server
						updateClients(command);//the clients will be passed the command
						
//						else if (!(command instanceof UndoLastCommand)) // undo commands can't be undone
//							history.push(command);
						}
					}
				}
				catch(Exception e){
					//System.err.println("In Client Handler:");
					//e.printStackTrace();
					break;
				}
			}
		}
	}
	
	/**
	 * 	ClientAccepter
	 * 
	 * 	This class is responsible for listening for new clients and subsequently setting up
	 * 	a ClientHandler for the new client.
	 */
	private class ClientAccepter implements Runnable{
		public void run() {
			while (true){
				try{
					Socket s = socket.accept(); // wait for a new client
					
					// grab the output and input streams for the new client
					ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
					ObjectInputStream input = new ObjectInputStream(s.getInputStream());
					

					String name;
					do{
						// read the client's name
						name = (String)input.readObject();
						
						// if that name is already connected, reject
						if (outputs.containsKey(name))
							output.writeObject("reject");
					}while (outputs.containsKey(name));
					
					// tell the client their name is accepted
					output.writeObject("accept");
					
					// add the output, input streams to the correct maps
					outputs.put(name, output);
					inputs.put(name, input);
					
					// create a command history queue for the new client
					histories.put(name, new LinkedBlockingDeque<Command<TRPGServer>>());
					
					// start a new ClientHandler for this new client
					new Thread(new ClientHandler(name, histories.get(name))).start();
				}catch(Exception e){
					System.err.println("In Client Accepter:");
					e.printStackTrace();
					break;
				}
			}
		}
	}
	
	public TRPGServer(int port){
		try{
			socket = new ServerSocket(port); // create a new server
			
			// setup hashmaps
			histories = new ConcurrentHashMap<String, Deque<Command<TRPGServer>>>();
			inputs = new ConcurrentHashMap<String, ObjectInputStream>();
			outputs = new ConcurrentHashMap<String, ObjectOutputStream>();
			
			System.out.println("Server started on port " + port);
			
			// begin accepting clients
			new Thread(new ClientAccepter()).start();
		}catch(Exception e){
			System.err.println("Error creating server:");
			e.printStackTrace();
		}
	}
	
/*	*//**
	 * This method undoes the last command of a client
	 * 
	 * @param clientName 	name of the client whose command should be undone
	 *//*
	public void undoLast(String clientName) {
		Deque<Command<TRPGServer>> commands = histories.get(clientName);
		if (commands.isEmpty())
			return;
		commands.pop().undo(TRPGServer.this);
	}*/

	/**
	 *	This method updates all connected clients with the most recent command that was 
	 *  sent by one of the clients.
	 */
	public void updateClients(Command<TRPGClient> command){
		Command<TRPGClient> update = command;
		for (ObjectOutputStream out: outputs.values())
			try{
				out.writeObject(update);
			}catch(Exception e){
				//System.err.println("Error updating clients");
				//e.printStackTrace();
				outputs.remove(out);
			}
	}

	/**
	 * Adds a PaintObject to the canvas
	 * @param object	a PaintObject to add to the canvas
	 */
/*	public void addObject(PaintObject object) {
		System.out.println(objects.size());
		System.out.println("Adding new Object" + object.getClass().toString());
		objects.add(object);
		System.out.println(objects.size());
		updateClients();
	}
	
	*//**
	 * Removes a PaintObject from the canvas
	 * @param object	a PaintObject to be removed
	 *//*
	public void removeObject(PaintObject object) {
		objects.remove(object);
		updateClients();
	}
	
	public List<PaintObject> getObjects() {
		return objects;
	}*/
	
	public GameBoard getGameBoard(){
		return currentBoard;
	}

	/**
	 *  Disconnects a connected user
	 * @param source	user to disconnect
	 */
	public void disconnect(String source) {
		System.out.printf("Client \'%s\' disconnecting\n", source);
		try{
			inputs.remove(source).close();
			outputs.remove(source).close();
			histories.remove(source);
		} catch(Exception e){e.printStackTrace();}
	}
	
	public static void main(String[] args)
	{
		new TRPGServer(9001);
	}

}
