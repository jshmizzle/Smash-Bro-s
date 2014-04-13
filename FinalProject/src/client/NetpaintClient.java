package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import GUI.MainMenuPanel;

public class NetpaintClient extends JFrame{

	private String host, username;
	private int port=0;
	private Socket server;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private MainMenuPanel mainMenuPanel;
	private CharacterSelectPanel charSelectPanel;

	public static void main(String[] args) {
		NetpaintClient client=new NetpaintClient();
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public NetpaintClient() {
		askUserForInfo();//now the client has been logged into the server
		initializeFrame();
		ServerHandler handler=new ServerHandler(this, inputStream);
		Thread t=new Thread(handler);
		t.start();
	}

	private void askUserForInfo() {
		host = JOptionPane.showInputDialog(null,
				"What host would you like to connect to?");
		while (port == 0) {
			try {
				port = Integer.parseInt((String) JOptionPane.showInputDialog(null, "What port are you trying to connect on?"));
				server=new Socket(host, port);
				inputStream=new ObjectInputStream(server.getInputStream());
				outputStream=new ObjectOutputStream(server.getOutputStream());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,"Not a valid port: enter a number");
				port=0;
			}
		}
		try{
		
		String serverAccepted="reject";
		while(serverAccepted.equals("reject")){
			username=JOptionPane.showInputDialog("Enter your Netpaint username");
			outputStream.writeObject(username);
			serverAccepted=(String)inputStream.readObject();
		}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void initializeFrame(){
		drawingPanel=new DrawingPanel(username, outputStream);
		this.add(drawingPanel).setVisible(true);
		this.setSize(1000,650);
		this.setVisible(true);
	}
	
	public void update(List<PaintObject> objects){
		this.drawingPanel.update(objects);
	}
}
