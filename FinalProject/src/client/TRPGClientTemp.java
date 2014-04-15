package client;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.GameBoard;
import model.Goku;
import model.Mario;
import model.MegaMan;
import model.Sonic;
import model.Unit;
import GUI.MainGamePanel;
import GUI.MainMenuPanel;

public class TRPGClientTemp extends JFrame {

	JPanel mainGamePanel;
	ObjectOutputStream output;
	GameBoard gameBoard;
	
	public static void main(String[] args) {
		JFrame client=new TRPGClientTemp();
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.setVisible(true);
	}
	
	
	
	private void initializeGameBoard() {
		//initialize the units and the GameBoard
		 Point b = new Point(0, 0);
		 Point x = new Point(0, 10);
		 
		 ArrayList<Unit> player = new ArrayList<Unit>();
		 ArrayList<Unit> computer = new ArrayList<Unit>();
		 
		 Sonic s = new Sonic("Sonic", 10000, 10, b);
		 MegaMan t = new MegaMan(65, 5, 4, new Point(0, 0));
		 Mario r = new Mario(100, 7, 3, 3, new Point(0, 0));
		 Goku e = new Goku(10, 5, 500, 1, new Point(0, 0));
		 Sonic sComputer = new Sonic("Sonic", 10000, 10, x);
		 player.add(s);
		 player.add(t);
		 player.add(r);
		 player.add(e);
		 computer.add(sComputer);
		 gameBoard = new GameBoard(player, computer, 1, 0);
		 
	}



	public TRPGClientTemp() {
		//set the size and location of the Frame
		this.setSize(500, 600);
		this.setLocation(425, 85);
		
		initializeGameBoard();

		mainGamePanel=new MainGamePanel(gameBoard);
		this.add(mainGamePanel);
	}

	
}
