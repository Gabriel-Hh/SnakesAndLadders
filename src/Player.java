import java.util.Scanner;

// -----------------------------------------------------
// Assignment #1
// Question: Part 1b
// Written by: Gabriel Horth, 40186942
// -----------------------------------------------------
/**
 * The Player class makes players for the LadderAndSnake automated board game.
 * @author Gabriel Horth
 *@version 1.0
 * @see LadderAndSnake
 * @see PlayLadderAndSnake
 *
 *@
 */
public class Player {
  	static String Throwaway;    //Used to advance Scanner
	private String playerName;
	private Character playerChar;
	private int position = -1;
//	private int displayPosition = position + 1;
	
	/**
	 *   Represents the last space on the a LadderAndSnake boardLS.
	 */
	public final static int MAX_POSITION = 99; // Represents the last space on the a LadderAndSnake boardLS.
	
	/**
	 * Array of default Player names.
	 */
	public final static String[] DEFAULT_NAMES = {"Player 1", "Player 2","Player 3","Player 4"}; 
	
	/**
	 * Array of default Player Characters.
	 */
	public final static Character[] DEFAULT_CHARS = {'A','B','C','D'}; 

	static Scanner keyboard = new Scanner(System.in);
	
	/**
	 * Player constructor intiates attributes to default Java values
	 */
	public Player() {}
	
	/**
	 * Placer constructor creates new Player.
	 * @param newName Player's name
	 * @param newChar Player's character
	 */
	public Player(String newName, Character newChar) {
		this.setName(newName);
		this.setChar(newChar);
	}
	
	/**
	 * SetName method assigns a name to a Player.
	 * @param newName
	 */
	public void setName(String newName) {
		this.playerName = newName;
	}
	
	/**
	 * getName method returns a Players name.
	 * @return playername
	 */
	public String getName() {
		return this.playerName;
	}
	
	/**
	 * SetPosition methods sets a Player position.
	 * @param newPosition Position to set
	 */
	public void setPosition(int newPosition) {
	  	if(newPosition > MAX_POSITION) {newPosition = MAX_POSITION - (newPosition - MAX_POSITION);}//Adjustment for needing exact position to win
	  	this.position = newPosition;
//		this.displayPosition = this.position + 1;
	}
	
	/**
	 * updatePositionForLadderOrSnake methods checks if player is at a ladder or snake and set new position accordly.
	 * @see getPosition
	 * @see setPosition
	 */
	public void updatePositionForLadderOrSnake() {
	  int positionToCheck = getPosition();
	  
	  switch(positionToCheck){
		case 0:setPosition(37);break;
		case 3:setPosition(13);break;
		case 8:setPosition(30);break;
		case 15:setPosition(5);break;
		case 20:setPosition(41);break;
		case 27:setPosition(83);break;
		case 35:setPosition(43);break;
		case 47:setPosition(29);break;
		case 50:setPosition(66);break;
		case 63:setPosition(59);break;
		case 70:setPosition(90);break;
		case 78:setPosition(18);break;
		case 79:setPosition(99);break;
		case 92:setPosition(67);break;
		case 94:setPosition(23);break;
		case 96:setPosition(75);break;
		case 97:setPosition(77);break;
	  }
	  
	}
	
	/**
	 * getPosition method returns a Players position.
	 * @return position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * setChar() method prompts user for a char and assigns it to a Player.
	 * @see setChar(Character), setChar() calls this method
	 */
	public void setChar() {
		System.out.print("\nSet character for " + getName()+ " ('#','*', and 'space' prohibited): ");
		Character userChar = keyboard.next().charAt(0);
		Throwaway = keyboard.nextLine();
		setChar(userChar);
	}
	
	/**
	 * seChar(Character) method assigns a new Character to a Player.
	 * @param newChar Character assigned
	 * @see setChar(), if newChar is invalid, calls this method
	 */
	public void setChar(Character newChar) {
		if(newChar == null) {setChar();return;}
		this.playerChar = newChar;
		
		Character f = new Character('F');
		Character pound = new Character('#');
		Character asterix = new Character('*');
		Character space = new Character(' ');
		if((newChar.charValue() == pound.charValue())||(newChar.charValue() == asterix.charValue())||(newChar.charValue() == space.charValue())||(newChar.charValue() == f.charValue())){
			System.out.print("\nCharacter forbidden!");
			setChar();
		}
	}
	
	/**
	 * getChar method returns a Player character.
	 * @return playerChar
	 */
	public Character getChar() {
		return this.playerChar;
	}
	
	/**
	 * toString method returns a String with a Players attributes and hashCode.
	 * @return playerName, playerChar, position, hashCode()
	 */
	@Override
	public String toString() {
		int reference = this.hashCode();
		return "Player [playerName=" + playerName + ", playerChar=" + playerChar + ", position=" + position + "]"
			 + reference
			 ;
	}

}

