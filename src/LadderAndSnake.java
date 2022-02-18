import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
// -----------------------------------------------------
// Assignment #1
// Question: Part 1
// Written by: Gabriel Horth, 40186942
// -----------------------------------------------------



/**
 * 
 *The LadderAndSnake creates an instance of the "Ladder and Snake" board game.
 *
 * @author Gabriel Horth
 *@version 1.0
 * @see Player
 * @see PlayLadderAndSnake
 *
 */
public class LadderAndSnake {
  static String Throwaway;
  static Scanner keyboard = new Scanner(System.in);

  private boardLS currentBoard = new boardLS();
  
  /*EMPTY Board 
   * 100||F| |*|*| |*| |*| | ||<  
   *   >|| | | | | | | | | | ||90 
   *  80||#|*| | | | | | | |#||<  
   *   >|| | | |*| | | | | | ||70 
   *  60|| | | | | | | | | |#||<  
   *   >|| | | | | | | |*| | ||50 
   *  40|| | | | |#| | | | | ||<  
   *   >||#| | | | | | |#| | ||30 
   *  20|| | | | |*| | | | | ||<  
   *   >||#| | |#| | | | |#| ||10 
	}*/
  
  private boolean gameWon = false;
  
  private int winnerIndex = -1;
  private Player[] playerArray;
  private int[][] lastRound;    //lastRound[][0] holds a players roll, lastround[][1] holds a player last position
  private Map<Character,String> charMap = new HashMap<>(); //Used to check the character present at Finish once gameWon. 
  static public boolean[] settings= new boolean[3];       //Game setting saved for session, applied to each new game. Can be changed with setSettings().
  private boolean setPlayerNamesAndChars = false;          //When false, default names and chars will be kept
  private boolean waitBetweenRounds = false;               //Implemented by playTillWon(). When false, entire game played and displayed without stopping for input
  private boolean printOutRoundResults = false;            //Implemented by playTurn(). When true, roll values and position change will be displayed after each round

  
  private Player[] unsortedPlayerArray; //USED by decidePlayerOrder() and sub-methods
  private int[][] orderArray; //USED by decidePlayerOrder() and sub-methods
  private int orderAssignment = 0; //USED by assignOrder()
  /**
   * InnerClass boardLS generates and manipulates a board character array for LadderAndSnake class.
   * @author user
   *
   */
  static class boardLS{ //***********************************************************************

	private Character[][] board;
	
	/**
	 * getBoard returns the board Character array.
	 * @return board
	 */
	public Character[][] getBoard() {
	  return board;
	}

	/**
	 * Default Constructor initalises to a new game board.
	 */
	public boardLS() {
	  board = new Character[10][10]; ///Board rows {9,7,5,3,1} are printed right to left
	  board[9]= new Character[]{' ',' ','*',' ','*',' ','*','*',' ','F'};
	  board[8]= new Character[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
	  board[7]= new Character[]{'#',' ',' ',' ',' ',' ',' ',' ','*','#'};
	  board[6]= new Character[]{' ',' ',' ','*',' ',' ',' ',' ',' ',' '};
	  board[5]= new Character[]{'#',' ',' ',' ',' ',' ',' ',' ',' ',' '};
	  board[4]= new Character[]{' ',' ',' ',' ',' ',' ',' ','*',' ',' '};
	  board[3]= new Character[]{' ',' ',' ',' ',' ','#',' ',' ',' ',' '};
	  board[2]= new Character[]{'#',' ',' ',' ',' ',' ',' ','#',' ',' '};
	  board[1]= new Character[]{' ',' ',' ',' ',' ','*',' ',' ',' ',' '};
	  board[0]= new Character[]{'#',' ',' ','#',' ',' ',' ',' ','#',' '};
	
	  /*EMPTY Board 
		 * 100||F| |*|*| |*| |*| | ||<  
		 *   >|| | | | | | | | | | ||90 
		 *  80||#|*| | | | | | | |#||<  
		 *   >|| | | |*| | | | | | ||70 
		 *  60|| | | | | | | | | |#||<  
		 *   >|| | | | | | | |*| | ||50 
		 *  40|| | | | |#| | | | | ||<  
		 *   >||#| | | | | | |#| | ||30 
		 *  20|| | | | |*| | | | | ||<  
		 *   >||#| | |#| | | | |#| ||10 
			}*/
	}
	
	/**
	 * toString method of boardLS class, returns board Array as 2D gameboard.
	 */
	@Override
	public String toString() {
	  int rowLabel = 100;
	  String toPrint = "";
	  System.out.println();
	  for (int i = board.length-1;i >= 0;i--) {

		//"even rows":{10,8,6,4,2}, these are the board[odd] rows
		if(i%2!=0) { //row Header 
		  if (rowLabel==100) {toPrint += rowLabel + "||";}
		  else {toPrint += " " + rowLabel + "||";} //aligns rows with only 2-digit rowLabel

		  for(int j = board[i].length-1; j >= 0; j--) {
			toPrint += board[i][j].toString() + "|";	//main elements
		  }
		  toPrint+= "|<"; //  row footer
		}
		//"odd rows"
		else {
		  toPrint+= "  >||";			//row header
		  for(int j = 0 ; j < board[i].length ; j++) {
			toPrint += board[i][j].toString() + "|";	//main elements	
		  }
		  toPrint += "|" + rowLabel; //row footer
		}

		toPrint += "\n";
		rowLabel-=10;
	  }
	  return toPrint;
	}
	
	/**
	 * setBoardChar methods places a Players character at the corresponding position in board array.
	 * @param playerPosition Players index position in board array.
	 * @param playerChar
	 */
	public void setBoardChar(Integer playerPosition, Character playerChar) {
	  String charIndexString = playerPosition.toString();
	  int rowIndex = 0;
	  int columnIndex ;

	  //Parses row and column to set
//	  if((charIndexString.charAt(0) != '-')) {
		if((charIndexString.length() == 2)&&(charIndexString.charAt(0) != '-')) {
		  rowIndex = Integer.parseInt(charIndexString.substring(0, 1)); //get row
		  columnIndex = Integer.parseInt(charIndexString.substring(1)); //get column
		}
		else {
		  columnIndex = Integer.parseInt(charIndexString); //account for single digit charIndex
		} 
		board[rowIndex][columnIndex] = playerChar; //Sets playerChar to position in currentBoard
//	  }
	}

  }//********************************END OF boardLS CLASS***************************************
  
  /**
   * getSettings return previously select game settings.
   * @return setting[] either default or chosen by PlayLadderAndSnake -> mainMenu() -> setSettings()
   * @see PlayLadderAndSnake
   * @setSettings()
   */
  public static boolean[] getSettings() {
	return settings;
  }
  
  /**
   * Prompt user to pick default settings for LadderAndSnake game.
   * Setting stay active unless changed.
   * Setting also affect decidePlayerOrder() method.
   * @see applySettingsToGame()
   * @see decidePlayerOrder()
   */
  public static void setSettings() {
	System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	System.out.println("                               Settings:");
	System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	//Option 1: setPlayerNamesAndChars
	int choice = 0;
	while((choice!=1)&&(choice!=2)) {
	  System.out.println("\nWould you like to name and assign a character to each player?"
		  + "\n1.Yes"
		  + "\n2.No");
	  choice = keyboard.nextInt();
	  keyboard.nextLine();
	}
	switch(choice) {
	case 1:{settings[0] = true;break;}
	case 2:{settings[0] = false;break;}
	default: choice= 0;
	}
	//Option 2: waitBetweensRounds
	choice=0;
	while((choice!=1)&&(choice!=2)) {
	  System.out.println("\nWould you like the game to wait for input between rounds?"
		  + "\n1.Yes"
		  + "\n2.No");
	  choice = keyboard.nextInt();
	  keyboard.nextLine();
	}
	switch(choice) {
	case 1:{settings[1] = true;break;}
	case 2:{settings[1] = false;break;}
	default: choice= 0;
	}
	//Option 3: printOutRoundResults
	choice=0;
	while((choice!=1)&&(choice!=2)) {
	  System.out.println("\nWould you like to display each rounds results?"
		  + "\n1.Yes"
		  + "\n2.No");
	  choice = keyboard.nextInt();
	  keyboard.nextLine();
	}
	switch(choice) {
	case 1:{settings[2] = true;break;}
	case 2:{settings[2] = false;break;}
	default: choice= 0;
	}
	//End message
	System.out.println("\nSettings have been changed.");
  }

  /**
   * Applies chosen setting to current game session.
   * @see setSettings()
   */
  public void applySettingsToGame() {
	setSetPlayerNamesAndChars(getSettings()[0]);
	setWaitBetweenRounds(getSettings()[1]);
	setPrintOutRoundResults(getSettings()[2]);
  }

  /**
   * Create hashmap between a Player character and a Players name.
   * @see declareWinner()
   */
  public void fillHashMap() {
	for(Player player:playerArray) {
	  charMap.put(player.getChar(),player.getName());
	}
  }
 
  /**
   * Maps winners Character back to winners name, return String.
   * @return playerName "has won the game!!!!"
   * @see fillHashMap()
   */
  public String declareWinner() {
	Character winnerChar = playerArray[winnerIndex].getChar();
	return charMap.get(winnerChar) +" has won the game!!!!!";
  }    

  /**
   * Checks if setting to wait for user input between rounds is active.
   * @return boolean
   */
  public boolean isWaitBetweenRounds() {
	return waitBetweenRounds;
  }

  /**
   * Activates or disactivates setting to wait for user input between rounds.
   * @param waitBetweenRounds true or false
   */
  public void setWaitBetweenRounds(boolean waitBetweenRounds) {
	this.waitBetweenRounds = waitBetweenRounds;
  }

  /**
   * Checks if setting to maunally enter Player names and characters is active.
   * @return boolean
   */
  public boolean isSetPlayerNamesAndChars() {
	return setPlayerNamesAndChars;
  }
 
  /**
   * Activates or disactivates setting to maunally enter Player names and characters
   * @param setPlayerNamesAndChars true or false
   */
  public void setSetPlayerNamesAndChars(boolean setPlayerNamesAndChars) {
	this.setPlayerNamesAndChars = setPlayerNamesAndChars;
  }
  
  /**
   * Constructor instantiates LadderAndSnake game to default settings.
   * Creates Player[] playerArray
   * Assings default names and character to Players
   * Prints out playerArray unless user will modify Players with setPlayerNamesAndChars()
   * @param players number of Players for game.
   */
  public LadderAndSnake(int players) {



	playerArray = new Player[players];

	for(int i = 0; i < players; i++) {
	  playerArray[i]= new Player();
	  playerArray[i].setName(Player.DEFAULT_NAMES[i]);	
	}

	for(int i = 0; i < players; i++) {
	  playerArray[i].setChar(Player.DEFAULT_CHARS[i]);
	}
	
	//Assign settings[] to current game
	applySettingsToGame();
	lastRound = new int[players][2];
	
	System.out.println("\nGame initialised for " +players+ " players!");
	if(!isSetPlayerNamesAndChars()) {printPlayerArray();} //If setPlayerNamesAndChars is false, will print default values now. 
  }                                                       //Otherwise,they are printed by setPlayerNamesAndChars()}
  
  /**
   * Generates a random int between one and six.
   * @return int 
   */
  public static int flipDice(){
	int roll = ((int)(Math.random()*6))+1;
	return roll;
  }
  
  /**
   * Plays one round and update current board.
   * Prints board
   * If printOutRoundResults() is true will print out text of each players turn
   */
  public void playTurn() { ///!!!!!!!!!!!!!!!!!!Modified since upload!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	for(int i = 0; i < playerArray.length; i++) {
	  if(!isGameWon()) {
		int increment = flipDice();
		lastRound[i][0] = increment; //
		lastRound[i][1] = playerArray[i].getPosition();
		int newPosition = playerArray[i].getPosition() + increment;
		playerArray[i].setPosition(newPosition);
		playerArray[i].updatePositionForLadderOrSnake();
		if(playerArray[i].getPosition()==Player.MAX_POSITION) { //ADDED after upload, before was seperate method-> checkIFGameWon() 
		  winnerIndex = i; //USED BY printOutRoundResults()
		  setGameWon(true);
		}
	  }
	}
	updateCurrentBoard();

	System.out.println(getCurrentBoard());
	//Prints out results of round if boolean printOutRoundResults.
	if(isPrintOutRoundResults()) {printOutRoundResults();}
  }
  
  /**
   * Updates board for new Player position.
   * @see setBoardChar()
   */
  public void updateCurrentBoard() {
	boardLS newBoard = new boardLS();
	for (Player player:playerArray) {
	  newBoard.setBoardChar(player.getPosition(),player.getChar());
	}
	currentBoard = newBoard;
  }
 
  /**
   * Play method, calls playTurn() until game is won.
   * @see playTurn()
   * @see roundStall
   * 
   */
  public void playTillWon() {
	while(isGameWon()==false){
	  playTurn(); 
	  if(isWaitBetweenRounds()&&(isGameWon()==false)) {roundStall();}
	}
  }

  /**
 * Sets boolean gameWon to true.
 * @param gameWon 
 */
  public void setGameWon(boolean gameWon) {
	this.gameWon = gameWon;
  }

  /**
   * Checks if Game has been won.
   * @return true or false
   */
  public boolean isGameWon() {
	return gameWon;
  }

  /**
   * Gets board for current game.
   * @return currentBoard
   */
  public boardLS getCurrentBoard() {
	return currentBoard;
  }

  /**
   * Checks if printOutRoundResult setting is active.
   * @return boolean
   */
  public boolean isPrintOutRoundResults() {
	return printOutRoundResults;
  }

  /**
   * Activates or disactivates setting printOutRoundResults.
   * @param printOutRoundResults boolean
   */
  public void setPrintOutRoundResults(boolean printOutRoundResults) {
	this.printOutRoundResults = printOutRoundResults;
  }

  /**
   * Prompt user to assign Players new names and characters.
   * @see setName
   * @see setChar()
   */
  public void setPlayerNamesAndChars() {
	//Assigns newName to players
	for(int i = 0; i < playerArray.length; i++) {

	  System.out.print("\nEnter Player " + (i+1) + "'s name: ");
	  String newName = keyboard.nextLine();
	  playerArray[i].setName(newName);	

	}
	//Assigns newChar to players
	System.out.println("\nThe characters 'F', '#', '*', 'space' are prohibited.");  
	for( int i = 0; i < playerArray.length; i++) {

	  String newChar;
	  System.out.print("\nEnter " + playerArray[i].getName() + "'s character: ");
	  newChar = keyboard.next();
	  playerArray[i].setChar(newChar.charAt(0));
	  Throwaway = keyboard.nextLine();

	}
	//Printout playerArray
	printPlayerArray();
  }

  /**
   * Prints current Players names and characters.
   */
  public void printPlayerArray() {
	int i = 0;
	for(Player player: playerArray) {
	  System.out.print("\nPlayer " + (i+1) + ": " + player.getName() + " '" + player.getChar() + "'.");
		i++;
	}
	System.out.println();
  }

 /**
  * Prints out last rounds results as text.
  */
  public void printOutRoundResults() {

	//Special Print procedure if game was won that round.
	if(isGameWon()) {
	  //Print Normally, for players who played that round.
	  for(int i = 0; i<=winnerIndex;i++) { 
		int roll = lastRound[i][0];
		int lastPosition = lastRound[i][1];
		int position = playerArray[i].getPosition();
		String snake = ""; //Invisible if player didn't hit a snake
		String ladder = ""; //Invisible if player didn't hit a ladder
		
		if((position-lastPosition)>roll){ladder = " to " +(lastPosition+1+roll) + ", and climbed a ladder";} // When Player hit a ladder
		if(((position-lastPosition)<roll)&&((lastPosition+roll)<Player.MAX_POSITION)){                       // When player only hit a snake
		  snake = " to " +(lastPosition+1+roll) + ", and slid down a snake";
		  } 
		if(((position-lastPosition)<roll)&&((lastPosition+roll)>Player.MAX_POSITION)) {                      //When player pasted MAX_POSITION and hit a snake
		  snake = " to " +((Player.MAX_POSITION+1)-((lastPosition+roll)-Player.MAX_POSITION)) + ", and slid down a snake";
		}

		System.out.println(playerArray[i].getName() + " rolled a " + roll + ", moved from space "
			+ (lastPosition + 1) + snake+ladder +" to " + (position + 1) );
	  }
	  
	  // Print this for players who didn't play last round 
	  if(winnerIndex != (playerArray.length-1)) { //i.e. So long as winner is not the last player -> Avoids OutOfBounds.
		for(int i = (winnerIndex + 1); i < playerArray.length; i++) {
		  int position = playerArray[i].getPosition();
		  System.out.println(playerArray[i].getName() + " did not roll, ended at " + (position + 1));
		}
	  } 
	}
	//Normal print procedure
	else {
	  int i=0;
	  for(Player player:playerArray) {
		int roll = lastRound[i][0];
		int lastPosition = lastRound[i][1];
		int position = player.getPosition();
		String snake = "";
		String ladder = "";
		if((position-lastPosition)>roll){ladder = " to " +(lastPosition+1+roll) + ", and climbed a ladder";} // When Player hit a ladder
		if(((position-lastPosition)<roll)&&((lastPosition+roll)<Player.MAX_POSITION)){                       // When player only hit a snake
		  snake = " to " +(lastPosition+1+roll) + ", and slid down a snake";
		  } 
		if(((position-lastPosition)<roll)&&((lastPosition+roll)>Player.MAX_POSITION)) {                      //When player pasted MAX_POSITION and hit a snake
		  snake = " to " +((Player.MAX_POSITION+1)-((lastPosition+roll)-Player.MAX_POSITION)) + ", and slid down a snake";
		}
		
		System.out.println(player.getName() + " rolled a " + roll + ", moved from space "
			+ (lastPosition + 1) + snake+ladder +" to " + (position + 1) );
		i++;
	  }
	}
  }
   
  /**
   * Stalls game at start and waits for user input.
   */
  public static void gameStartStall() {
	System.out.println("Press 'Enter' to begin.");
	while(!keyboard.hasNextLine()) {} //Stalls until user hits ENTER
	Throwaway = keyboard.nextLine();
  }
  
  /**
   * Stalls game between rounds and waits for user input.
   */
  public static void roundStall() {
	System.out.println("Press 'Enter' to continue.");
	while(!keyboard.hasNextLine()) {} //Stalls until user hits ENTER
	Throwaway = keyboard.nextLine();
  }
  
  /**
   * Stalls game at start of decidePlayerOrder() method and waits for user input.
   */
  public static void playerOrderStall() {
	System.out.println("\nPlayer order must be determined:");
	System.out.println("Press 'Enter' to continue.");
	while(!keyboard.hasNextLine()) {} //Stalls until user hits ENTER
	Throwaway = keyboard.nextLine();
  }
  
  /**
   * Picks the order in which Players will play current game through dice rolls.
   * @see playerOrderStall()
   * @see rollForRemainingPlayers();
   * @see assignOrder();
   * @see findAndTagAllTies();
   * @see updateToSortedPlayerArray();
   * @see isOrdered()
   */
  public void decidePlayerOrder() {

	playerOrderStall();
	//Make a copy of playerArray's player references
	unsortedPlayerArray = new Player[playerArray.length];
	for(int i = 0; i < playerArray.length; i++) {
	  unsortedPlayerArray[i] = playerArray[i];
	}
	
	orderArray = new int[playerArray.length][3]; //USED BY rollForRemainingPlayers(), assignOrder(), findAndTagAllTies()
	//orderArray[playerIndex][0]= playing order attributed {0,1,2,3, etc} with 1 starting first. If value: -1 -> the player has tied in current round
	//orderArray[playerIndex][1]= last dice value
	//orderArray[playerIndex][2]= value:0 -> a tie, therefore will reroll, value:1 -> not a tie, therefore playing order will be kept
	
	
	while(!isOrdered()) {
	  rollForRemainingPlayers();
	  assignOrder();
	  findAndTagAllTies();
	  if(isWaitBetweenRounds()&&(!isOrdered())) {roundStall();}
	}
	updateToSortedPlayerArray();
	
	System.out.println("\nPlayer order determined:");
	printPlayerArray();
  }
  
  /**
   * Rolls dice for all remaining player for decidePlayerOrder().
   * @see decidePlayerOrder()
   */
  public void rollForRemainingPlayers(){
	for(int i = 0; i < orderArray.length; i++) {
	  if(orderArray[i][2]==0) {
		orderArray[i][1] = flipDice();
		if(isPrintOutRoundResults()) {
		  System.out.println(unsortedPlayerArray[i].getName() + " rolled a " + orderArray[i][1] );
		}
	  }
	}
  }

  /**
   * Assign the working order and final order for decidePlayerOrder().
   * @see decidePlayerOrder()
   */
  public void assignOrder() {
	orderAssignment = 1;
	for(int nextHighestRoll = 6; nextHighestRoll > 0;) {
	  if(orderAssignment <= orderArray.length) {
		checkAlreadyAttributed();
		for(int i = 0; i < orderArray.length; i++) {

	
		  if((orderArray[i][1] == nextHighestRoll)&&(orderArray[i][2] == 0)) {
			orderArray[i][0] = orderAssignment;
			orderAssignment++;
			//			checkAlreadyAttributed();//Might not be needed here, ties are already grouped together
		  }
		}
		nextHighestRoll--; 
	  }
	  else {return;}
	}
  }
  /**
   * Methods checks if order is already assigned for a non-tied player before assigning.
   * @see decidePlayerOrder()
   * @see assignOrder()
   */
  public void checkAlreadyAttributed() {
	for(int j = 0; j < orderArray.length; j++) {
	  if ((orderArray[j][0] == orderAssignment)&&(orderArray[j][2] == 1)) {
		orderAssignment++;
		checkAlreadyAttributed();
	  }
	}
  }
  /**
   * Identifies and marks players who have rolled a tie for decidePlayerOrder()
   * @see decidePlayerOrder().
   */
  public void findAndTagAllTies() {
	int j=0;// This j-value is not used, makes complier happy
	for(int i = 0; i < orderArray.length-1; i++) {
	  for(j = (i+1); j  < orderArray.length;j++) {
		if((orderArray[i][1] == orderArray[j][1])&(orderArray[i][2]==0)&&(orderArray[j][2]==0)) {
		  orderArray[i][0]=-1;
		  orderArray[j][0]=-1;
		  if(isPrintOutRoundResults()) {
			System.out.println(unsortedPlayerArray[i].getName() + " and " + unsortedPlayerArray[j].getName() + " will play a tiebreaker");
		  }
		}
	  }//END of j-loop
	  if (orderArray[i][0]!=-1) { orderArray[i][2]= 1;}
	  j--; // Needed because j-loop increments j on completion -> OutOfBounds
	  if((orderArray[j][0] != -1)&&(i == (orderArray.length - 2))&&(j == (orderArray.length-1))) {orderArray[j][2] = 1;}
	}//END of i-loop
//	j--; // Needed because j-loop increments j on completion -> OutOfBounds
//	if((orderArray[j][0] != -1)&&(j == (orderArray.length-1))) {orderArray[j][2] = 1;}
  }

  /**
   * Checks if Players are now ordered for decidePlayerOrder().
   * @return boolean
   * @see decidePlayerOrder()
   */
  public boolean isOrdered() {
	for(int i = 0; i < orderArray.length; i++) {
	  if (orderArray[i][2]==0) {return false; }
	}
	return true;
  }
  
  /**
   * Update current game's playerArray to the sorted array for decidePlayerOrder().
   * @see decidePlayerOrder()
   */
  public void updateToSortedPlayerArray() {
	Player[] sortedPlayerArray = new Player[playerArray.length];
	for(int i = 0; i < sortedPlayerArray.length; i++) {
	  int playerNewIndex =(orderArray[i][0] - 1);
	  sortedPlayerArray[playerNewIndex] = playerArray[i];
	}
	playerArray = sortedPlayerArray;
  }

}//END LadderAndSnake

