package de.htwsaar.esch.Codeopolis.Presentation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import de.htwsaar.esch.Codeopolis.DomainModel.CityState;
import de.htwsaar.esch.Codeopolis.DomainModel.Game;
import de.htwsaar.esch.Codeopolis.DomainModel.GameConfig;
import de.htwsaar.esch.Codeopolis.DomainModel.TurnResult;

/**
 * The Dialog class represents a command line interface for the Codeopolis game.
 * It extends the UserInterface class and provides methods for interacting with the user via command line interface.
 */
public class Dialog extends UserInterface{
	private Scanner input;
	private static final int NEW_GAME = 1;
    private static final int LOAD_GAME = 2;
	private static final int END_PROGRAM = 3;
		
	private static final int EASY = 1;
	private static final int MEDIUM = 2;
	private static final int HARD = 3;
	
	private Game currentGame;
	
	/**
     * Initializes a new instance of the Dialog class.
     */
    public Dialog() {
    	input = new Scanner(System.in);
    	currentGame = null;
    	
    }

    /**
     * Starts the command line user interface.
     */
    @Override
    public void start() {
    	int funktion = 0;

    	do {
    		try {
    			showMenu();
    			funktion = readFunction();
    			executeFunction(funktion);
    		}
    		catch(IllegalArgumentException e)
    		{
    			System.out.println(e);
    			e.printStackTrace(System.out);
    		}
    		catch(InputMismatchException e)
    		{
    			System.out.println(e);
    			e.printStackTrace(System.out);
    		}
    		catch(Exception e)
    		{
    			System.out.println(e);
    			e.printStackTrace(System.out);
    		}
    	}
    	while (funktion != END_PROGRAM);        

    }

    /**
	 * Executes the function selected by the user in the main menu.
	 *
	 * @return The selected function as an integer.
	 */
    private void executeFunction(int funktion) {
    	switch (funktion) {
        case NEW_GAME:
            newGame();
            break;
        case LOAD_GAME:
            loadGame();
            break;
        case END_PROGRAM:
            System.out.println("You have finished the game. Goodbye.");
            break;
        default:
            System.out.println("Invalid option selected.");
            break;
    }
	}

    /**
	 * Reads the user's selected function.
	 *
	 * @return The selected function as an integer.
	 */
	private int readFunction() {
		return readPositivIntegerInput();
	}

	/**
	 * Displays the main menu options to the user.
	 */
	private void showMenu() {
	    System.out.println("===== Main Menu =====");
	    System.out.println("1. New Game");
        System.out.println("2. Load Game");
	    System.out.println("3. Quit");
	    System.out.print("Please select an option: ");
		
	}
	
	/**
     * Starts a new game based on the user's chosen difficulty level and city name.
     */
	private void newGame() {
		System.out.println("===== New Game =====");
	    System.out.println("Level of difficulty:");
	    System.out.println("1: EASY");
	    System.out.println("2: MEDIUM");
	    System.out.println("3: HARD");
	    int difficulty = 0;
	    do {
	    	System.out.print("Please choose your option: ");
		    difficulty = readPositivIntegerInput();
	    } while(difficulty < EASY || difficulty > HARD);
	    
	    System.out.println("Enter the name of your city:");
	    String cityName = "";
		try {
			cityName = input.next();
		} 
		catch (NoSuchElementException e) {
			System.out.println(e);
		}
		catch(IllegalStateException e) {
			System.out.println(e);
		}
		Game.Difficulty newGameDifficulty = Game.Difficulty.EASY; 
		switch(difficulty) {
			case EASY:
				newGameDifficulty = Game.Difficulty.EASY;
				break;
			case MEDIUM:
				newGameDifficulty = Game.Difficulty.MEDIUM;
				break;
			case HARD:
				newGameDifficulty = Game.Difficulty.HARD;
				break;
			default:
				throw new UnsupportedOperationException("The selected difficulty level "+difficulty+" is not implemented.");
		}
	    currentGame = new Game(java.util.UUID.randomUUID().toString(), cityName, newGameDifficulty, this); //Issue#11
	    currentGame.startGame();
	}
	
	private void saveGame() {
	    if (currentGame == null) {
	        System.out.println("No game to save.");
	        return;
	    }

	    System.out.print("Enter the filename to save the game: ");
	    String filename = input.next();

	    System.out.println("Saving game...");
	    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
	        out.writeObject(currentGame.getCityState());
	        out.writeObject(currentGame.getGameConfig()); 
	        System.out.println("Game saved successfully to " + filename);
	    } catch (IOException e) {
	        System.out.println("Failed to save game: " + e.getMessage());
	    }
    }

    private void loadGame() {
        System.out.print("Enter the filename to load the game from: ");
        String filename = input.next();

        System.out.println("Loading game from " + filename + "...");
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            CityState state = (CityState) in.readObject();
            GameConfig config = (GameConfig) in.readObject(); 
            currentGame = new Game(java.util.UUID.randomUUID().toString(), state, config, this); 
            System.out.println("Game loaded successfully.");
            
    		System.out.println("Year "+state.getYear()+" of your great city "+state.getName()+" is over.");
    		printState(state);
            
            currentGame.startGame();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load game: " + e.getMessage());
        }
    }

	/**
     * Prints the current game state to the screen.
     */
    private void printState(CityState state) {
        System.out.println("--- Current State ---");
        System.out.println("Bushels: " + state.getTotalAmountOfBushels() +
                " (Barley: " + state.getBushels(Game.GrainType.BARLEY) +
                ", Corn: " + state.getBushels(Game.GrainType.CORN) +
                ", Millet: " + state.getBushels(Game.GrainType.MILLET) +
                ", Rice: " + state.getBushels(Game.GrainType.RICE) +
                ", Rye: " + state.getBushels(Game.GrainType.RYE) +
                ", Wheat: " + state.getBushels(Game.GrainType.WHEAT) +
                "), Acres: " + state.getAcres() +
                ", Residents: " + state.getResidents() +
                ", Free Storage: " + state.getFreeStorage());
    }
	
	/**
     * Reads a positive integer input from the user.
     *
     * @return The positive integer entered by the user.
     */
	private int readPositivIntegerInput() {
		int result = -1;
		String line;
		while(result < 0) {
			try {
				line = input.next();
				result = Integer.parseInt(line);
				if(result < 0)
					System.out.println("You entered a negative integer. Please try again.");
			}
			catch(NumberFormatException e) {
				System.out.println("Error – Please enter an integer value");
			}
			catch(NoSuchElementException e) {
				System.out.println(e);
			}
			catch(IllegalStateException e) {
				System.out.println(e);
			}
		}
		return result;
	}
	
	@Override
	public int buy(int pricePerAcre, CityState cityState) {
		System.out.println("\n=== BUY ===");
		printState(cityState);
		System.out.println("Current price per acres: "+pricePerAcre);
		System.out.print("How many acres would you like to buy?");
		System.out.println(">");
		return readPositivIntegerInput();
	}

	@Override
	public int sell(int pricePerAcre, CityState cityState) {
		System.out.println("\n=== SELL ===");
		printState(cityState);
		System.out.println("Current price per acres: "+pricePerAcre);
		System.out.println("How many acres would you like to sell?");
		System.out.println(">");
		return readPositivIntegerInput();
	}

	@Override
	public int feed(int bushelsPerResident, CityState cityState) {
		System.out.println("\n=== FEED ===");
		printState(cityState);
		System.out.println(bushelsPerResident+" bushels per resident required.");
		System.out.println("How many bushels do you would like to feed to your residents? ");
		System.out.println(">");
		return readPositivIntegerInput();
	}

	@Override
	public int[] plant(int bushelsPerAcre, int acrePerResident, CityState cityState) {
		int[] result = new int[6];
		System.out.println("\n=== PLANT ===");
		printState(cityState);
		System.out.println("Now you can plant your acres. "+bushelsPerAcre+" bushels per acre and 1/"+acrePerResident+" residents per acre required.");
		System.out.println("How many acres of barley do you wish to plant? ");
		System.out.println(">");
		result[Game.GrainType.BARLEY.ordinal()] = readPositivIntegerInput();
		System.out.println("How many acres of corn do you wish to plant? ");
		System.out.println(">");
		result[Game.GrainType.CORN.ordinal()] = readPositivIntegerInput();
		System.out.println("How many acres of millet do you wish to plant? ");
		System.out.println(">");
		result[Game.GrainType.MILLET.ordinal()] = readPositivIntegerInput();
		System.out.println("How many acres of rice do you wish to plant? ");
		System.out.println(">");
		result[Game.GrainType.RICE.ordinal()] = readPositivIntegerInput();
		System.out.println("How many acres of rye do you wish to plant? ");
		System.out.println(">");
		result[Game.GrainType.RYE.ordinal()] = readPositivIntegerInput();
		System.out.println("How many acres of wheat do you wish to plant? ");
		System.out.println(">");
		result[Game.GrainType.WHEAT.ordinal()] = readPositivIntegerInput();
		return result;
	}
	
	@Override
	public void turnEnd(TurnResult result) {
		System.out.println("\n=== TURN ENDED ===");
		System.out.println("Year "+result.getYear()+" of your great city "+result.getName()+" is over.");
		System.out.println(result.getStarved()+" people starved.");
		System.out.println(result.getNewResidents()+" people came to your city.");
		System.out.println("Your city now has "+result.getResidents()+" residents.");
		System.out.println("Your city owns "+result.getAcres()+" acres of land.");
		System.out.println("You harvested "+result.getNumberOfBushelsHarvested()+" bushels:");
		for (Game.GrainType grainType : Game.GrainType.values()) {
		    int ordinal = grainType.ordinal();
		    System.out.println("\t-" + result.getBushelsHarvested()[ordinal] + " bushels of " + grainType.toString().toLowerCase());
		}
		
		System.out.println("Rats ate "+result.getAteByRates()+" bushels.");
		System.out.println(result.getBushelsDecayed()+" bushels decayed in the depot during the last year.");
		System.out.println(result.getDepotCapacity() - result.getFreeStorageSpaces()+" of "+ result.getDepotCapacity() + " storage spaces in your depot are filled with "+result.getTotalNumberOfBushels()+" bushels:");
		for (Game.GrainType grainType : Game.GrainType.values()) {
		    int ordinal = grainType.ordinal();
		    System.out.println("\t-" + result.getBushels()[ordinal] + " bushels of " + grainType.toString().toLowerCase());
		}
			
		System.out.println("Here is the detaild information on your depot:");
		System.out.println(result.getDepotState());
		
		System.out.println("Press some key to start the new year. Enter SAVE to save the current game and continue. Enter QUIT to quit the current game. ");
		String in = "";
		try {
			in = input.next().trim();
		} 
		catch (NoSuchElementException e) {
			System.out.println(e);
		}
		catch(IllegalStateException e) {
			System.out.println(e);
		}
		if(in.equals("IDKFA")) 
			this.currentGame.IDKFA();
		if(in.equals("SAVE")) {
			this.saveGame();
			System.out.println("You did quit the game.");
		}
		if(in.equals("QUIT")) {
			this.currentGame.quitGame();
			System.out.println("You did quit the game.");
		}
	}

	@Override
	public void illigleInput(String message) {
		System.out.println(message);
	}

	@Override
	public void gameWon(String message) {
		System.out.println(message);
		System.out.println("Press some key to continue");
		try {
			input.next();
		} 
		catch (NoSuchElementException e) {
			System.out.println(e);
		}
		catch(IllegalStateException e) {
			System.out.println(e);
		}
	}

	@Override
	public void gameLost(String message) {
		System.out.println(message);
		System.out.println("Press some key to continue");
		try {
			input.next();
		} 
		catch (NoSuchElementException e) {
			System.out.println(e);
		}
		catch(IllegalStateException e) {
			System.out.println(e);
		}
		
	}

	@Override
	public int expandDepot(CityState cityState) {
		System.out.println("\n=== EXPAND DEPOT ===");
		printState(cityState);
		System.out.println("How many new silos do you want to build? (cost: 5% of the bushels per silo)?");
		System.out.println(">");
		return readPositivIntegerInput();
	}


}
