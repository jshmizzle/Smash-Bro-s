package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.MainMenuPanel;

public class TRPGClient extends JFrame {

	JPanel mainMenuPanel;
	ObjectOutputStream output;
	
	public static void main(String[] args) {
		JFrame client=new TRPGClient();
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.setVisible(true);
	}
	
	public TRPGClient() {
		//set the size and location of the Frame
		this.setSize(500, 600);
		this.setLocation(425, 85);
		mainMenuPanel=new MainMenuPanel();
		this.add(mainMenuPanel);
	}

	
}
