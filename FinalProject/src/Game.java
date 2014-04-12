import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class Game extends Observable{

	private ArrayList<Unit> friendlies;
	private ArrayList<Unit> enemies;
	private GameBoard levelOneBoard;//These must be initialized at the start to be how we 
	private GameBoard levelTwoBoard;//want to design the levels
	private GameBoard currentBoard;
	private BotPlayer bot;
	private int turnNumber;
	private int currentScenario;
	
	/**
	 * Creates the game class that initializes every part of the game including the units.
	 * The units will be created by whichever view we are using so that the player can choose
	 * whatever units he wants.
	 */
	public Game(ArrayList<Unit> friendly, ArrayList<Unit> enemy, int startingScenario) {
		currentScenario=startingScenario;
		this.friendlies=friendly;
		this.enemies=enemy;
		levelOneBoard=new GameBoard(friendlies, enemies, 1, currentScenario);
		currentBoard=levelOneBoard;
		bot=new BotPlayer();
		turnNumber=0;
	}

	
	/**
	 * This method allows the GUI to change the level when the player wins
	 * @param friendlies The list of Units that the User chose
	 * @param enemies The list of Units that the BotPlayer chose
	 * @param scenario With this the Game will be able to construct a GameBoard that has 
	 * the correct scenario displayed.
	 * @return
	 */
	public GameBoard nextLevel(ArrayList<Unit> friendlies, ArrayList<Unit> enemies, int scenario){
		this.friendlies=friendlies;
		this.enemies=enemies;
		this.currentScenario=scenario;
		this.turnNumber=0;
		levelTwoBoard=new GameBoard(friendlies, enemies, 2);
		this.currentBoard=levelTwoBoard;
		return currentBoard;
	}
	
	public int hasWon(){
		if (currentScenario==1){
			//this is the game mode where the target needs to be killed
			
		}
		else if (currentScenario==2){
			//this is the deathmatch game mode
		}
	}

	
}
