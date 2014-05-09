package model;
import java.awt.Point;


@SuppressWarnings("serial")
public class Sonic extends Unit { 
	
	private char [][] game;
	
	public Sonic(char c) {	
		super("Sonic", 100, 20, 99, 1, new Point(0, 0), c);
	}
	
	public void easyMove(GameBoard board){
		game=board.getGameBoard();
		boolean princess= false;
		Point p = null;
		int movesLeft=this.getDistance();
		//check to see if playing the princess game
		for(int i=1; i<game.length; i++){
			for( int j=0; j< game[0].length; j++){
				if (game[i][j]=='p'){
					princess=true;
					p=new Point(i,j);
					break;
				}
			}
		}
		//move for princess game
		/*if (princess){
			//checks whether or not they have moves left
			while(movesLeft>0){
				//checks if next space down is available
				if(location.x - p.x> 0 && board.checkAvailable(new Point(location.x+movesLeft,location.y) ){
					movesLeft --;
					this.setLocation(new Point(location.x, p.y));
				}
				else{
					
				}
			}
				
			}
		}*/
	}
}
