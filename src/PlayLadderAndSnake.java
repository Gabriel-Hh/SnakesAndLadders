import java.util.Scanner;
//-----------------------------------------------------
//Assignment #1
//Question: Part 2, Driver
//Written by: Gabriel Horth, 40186942
//-----------------------------------------------------


/**
 * Driver Class for LadderAndSnakes game.
 * 
 *@author Gabriel Horth
 *@version 1.2
 *@see Player
 *@see LadderAndSnake
 */
public class PlayLadderAndSnake {
  static String Throwaway;    //Used to advance Scanner
  static Scanner keyboard = new Scanner(System.in);
 
  /**
   * Runs mainMenu().
   * @param args
   */
  public static void main(String[] args) {
	mainMenu();
  }
  
  /**
   * Engine Of the game.
   * Calls all methods in referenced classes.
   * @see LadderAndSnake
   * @see Player
   */
  public static void mainMenu() {

	//Welcome
	int option = 0;
	System.out.println(""
		+ "\n======================================================="
		+ "\n Welcome to \"Ladder and Snakes\" automated board game!"
		+ "\n======================================================="
		+ "\n"
		+ "\n						Main Menu:"
		+ "\n1. Start Game"
		+ "\n2. Board info"
		+ "\n3. Settings"
		+ "\n4. Exit");
	
	option = keyboard.nextInt();
	Throwaway = keyboard.nextLine();
	
	switch(option) {
	case 2:System.out.println("Option not initialized, you must return to main menu.");
		LadderAndSnake.roundStall();
		mainMenu();
	break;
	
	case 3:
	  LadderAndSnake.setSettings();
	  mainMenu();
	  break;
	  
	case 4:System.out.println("Thanks for playing, program closing");
	System.exit(0);
	break;
	
	case 1:{
	  int numberOfPlayers = playerQuantitySelect();
//	  LadderAndSnake.settings[2] = new Boolean(true); //By Default, printOutRoundResults = true
	  
	  LadderAndSnake currentGame = new LadderAndSnake(numberOfPlayers);
	  
	  if(currentGame.isSetPlayerNamesAndChars()) {currentGame.setPlayerNamesAndChars();}
	  
	  currentGame.fillHashMap(); //Maps Character playerChar to String playerName
	  
	  currentGame.decidePlayerOrder();
	  
	  System.out.println("\n" + currentGame.getCurrentBoard());//Print initial board
	  
	  LadderAndSnake.gameStartStall();

	  currentGame.playTillWon();

//	  Character winnerChar = currentGame.getCurrentBoard().getBoard()[9][9];
	  String winnerString = currentGame.declareWinner();
	  System.out.println(winnerString);

	  //PlayAgain?
	  playAgain();

	}
	}
  }

  /**
   * Prompts user to select to play again or exit the program.
   * @see mainMenu()
   */
  public static void playAgain() {
	System.out.println("\nPlay Again?"
		+ "\n1.Yes"
		+ "\n2.No");
	int playAgainInt = keyboard.nextInt();
	Throwaway = keyboard.nextLine();
	switch(playAgainInt) {
	case 1:mainMenu();break;
	case 2:System.out.println("Thanks for playing, program closing");
	break;
	default:System.out.println("-Invalid Entry-");
	playAgain();
	}
  }

  /**
   * Prompt user to choose the number of player.
   * Checks for invalid number of players.
   * @return int numberOfPlayer
   */
  public static int playerQuantitySelect() {
	//		@SuppressWarnings("resource")
	//		//Scanner keyboard = new Scanner(System.in);

	int numberOfPlayer = 0;
	int trial = 1;

	do {
	  switch(trial) {
	  case 1:break;
	  case 2:
	  case 3:
	  case 4:System.out.println("\nBad Attempt " + (trial-1) +" - Invalid # of players. ");
	  break;
	  case 5:System.out.println("\nBad Attempt 4! You have exhausted all your chances."
		  + "\nProgram will terminate!");
	  System.exit(0);
	  }

	  System.out.print("Please enter the number of players (2-4): ");
	  numberOfPlayer = keyboard.nextInt();
	  Throwaway = keyboard.nextLine();
	  trial++;
	}
	while((numberOfPlayer < 2 || numberOfPlayer > 4) && trial <= 5);
	return numberOfPlayer;
  }	



}

