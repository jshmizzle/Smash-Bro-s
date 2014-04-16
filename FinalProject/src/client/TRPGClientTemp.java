package client;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.GameBoard;
import model.Goku;
import model.Link;
import model.Mario;
import model.MegaMan;
import model.Princess;
import model.Sonic;
import model.Unit;
import GUI.MainGamePanel;
import GUI.MainMenuPanel;

public class TRPGClientTemp extends JFrame {

	MainGamePanel mainGamePanel;
	ObjectOutputStream output;
	public static GameBoard gameBoard;
	
	public static void main(String[] args) {
		JFrame client=new TRPGClientTemp("Destroy the Princess");
		client.setVisible(true);
	}
	
	
	
	private void initializeGameBoard() {
		//initialize the units and the GameBoard
		 ArrayList<Unit> playerUnits = new ArrayList<Unit>();
		 ArrayList<Unit> compUnits = new ArrayList<Unit>();
		 
	    Sonic S = new Sonic('S');
	  	Goku G = new Goku('G');
	 	Link l = new Link('l');
		Mario w = new Mario('w');
		MegaMan m = new MegaMan('m');
		Princess P = new Princess('P');
		Princess p = new Princess('p');
			
		playerUnits.add(P);
		playerUnits.add(S);
		playerUnits.add(G);
		compUnits.add(p);
		compUnits.add(l);
		compUnits.add(m);
		gameBoard = new GameBoard(playerUnits, compUnits, 1, 0); 
	}



	public TRPGClientTemp(String name) {
		//set the size and location of the Frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 600);
		this.setLocation(425, 85);
		this.setTitle(name);
		initializeGameBoard();
		mainGamePanel=new MainGamePanel(gameBoard);
		this.add(mainGamePanel);
		int count = 0;
		ArrayList<Unit> userUnits = new ArrayList<Unit>();
		ArrayList<Unit> computerUnits = new ArrayList<Unit>();
		userUnits = gameBoard.getUserUnits();
		computerUnits = gameBoard.getCompUnits();
		char [][] board = gameBoard.getGameBoard();
		Scanner scan = new Scanner (System.in);
		String check = "";
		/*while(userUnits.get(0).isAlive() && computerUnits.get(0).isAlive()){

			for(int i = 1; i<userUnits.size(); i++){
			
				System.out.println("Would you like to move " + userUnits.get(i).getName() + "?");
				System.out.println("Type 'Y' or 'N'");
				check = scan.next();
				Unit x = userUnits.get(i);
				int movesLeft = x.getMovesLeft();
				
				for(int j = x.getMovesLeft(); j>=0; j--){
						System.out.println(x.getName() + " has " + j + " moves left");
						System.out.println("Type 'U' for up, 'D' for down, 'L' for left, or 'R' for down, or 'S' "
								+ "for done moving this unit");
						check = scan.next();

						if(check.equalsIgnoreCase("U"))
							gameBoard.moveUp(x);
						if(check.equalsIgnoreCase("D"))
							gameBoard.moveDown(x);
						if(check.equalsIgnoreCase("L"))
							gameBoard.moveLeft(x);
						if(check.equalsIgnoreCase("R"))
							gameBoard.moveRight(x);
						if(check.equalsIgnoreCase("S"));
							break;
			} // for with j
		}//for with i */		
			mainGamePanel.update(gameBoard); 
		//}// while
	}


}
