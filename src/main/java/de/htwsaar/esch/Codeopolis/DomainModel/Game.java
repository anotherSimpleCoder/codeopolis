package de.htwsaar.esch.Codeopolis.DomainModel;

import java.util.Comparator;
import java.util.Random;
import java.util.function.Predicate;

import de.htwsaar.esch.Codeopolis.Exceptions.*;

/**
 * The Game class represents a game instance of the Codeopolis game.
 */
public class Game extends GameEntity{
	/**
     * The Difficulty enum represents the difficulty level of the game.
     */
    public enum Difficulty {EASY, MEDIUM, HARD}
    
    /**
     * The {@code GrainType} enum represents different types of grains managed within the game.
     * Each enum constant corresponds to a specific type of grain, such as barley, corn, etc.
     * These types are used to categorize and manage the grain resources within the game's economy and agricultural systems.
     */
    public enum GrainType {
        BARLEY, // Represents barley grain
        CORN,   // Represents corn grain
        MILLET, // Represents millet grain
        RICE,   // Represents rice grain
        RYE,    // Represents rye grain
        WHEAT;  // Represents wheat grain
    
        @Override
        public String toString() {
            switch (this) {
                case BARLEY:
                    return "Barley";
                case CORN:
                    return "Corn";
                case MILLET:
                    return "Millet";
                case RICE:
                    return "Rice";
                case RYE:
                    return "Rye";
                case WHEAT:
                    return "Wheat";
                default:
                    return "Unknown";
            }
        }
    }
    
    /**
     * The GameState enum represents the game states.
     */
    private enum GameState {PREPARED, RUNNING, GAMEOVER}
    
    private GameState state;
	private City city;
	private UserInterface ui;
	private Random fortune;
	private GameConfig config;
	
	/**
     * Constructs a new Game object with the specified parameters.
     *
     * @param id         the ID of the game
     * @param name       the name of the game
     * @param difficulty the difficulty level of the game
     * @param ui         the user interface for the game
     */
	public Game(String id, String name, Game.Difficulty difficulty, UserInterface ui){
		super(id); //Issue #4
		this.config = new GameConfig(difficulty);	
		this.city = new City(java.util.UUID.randomUUID().toString(), name, this.config); //Issue #1, Issue #3
		this.ui = ui;
		this.fortune = new Random();
		this.state = GameState.PREPARED;
	}
	
	/**
     * Constructs a Game object with the specified city state.
     *
     * @param id         the ID of the game
     * @param cityState  the state of the game
     * @param gameConfig the config of the game
     * @param ui         the user interface for the game
     */
	public Game(String id, CityState cityState, GameConfig gameConfig, UserInterface ui){
		super(id); 
		this.config = gameConfig;
		this.city = new City(cityState, gameConfig); 
		this.ui = ui;
		this.fortune = new Random();
		this.state = GameState.PREPARED;
	}

	/**
	 * Starts the game by setting the game state to RUNNING and initiating the game loop.
	 */
	public void startGame() {
		this.state = GameState.RUNNING;
		gameLoop();
	}

	/**
	 * Recursively executes the game loop until the game reaches a GAMEOVER state.
	 * During each recursion, it executes core game functions such as expanding the depot, buying, selling,
	 * feeding, and planting. After executing these functions, it checks for end conditions including
	 * city extinction or overwhelming starvation which may lead to game over scenarios.
	 * If none of these conditions are met and the game is not won, it recursively calls itself to continue the game loop.
	 */
	private void gameLoop() {
		if(this.state == GameState.GAMEOVER) {
			return; // Ends the recursion when the game is over
		}

		expandDepot();
		buy();
		sell();
		feed();
		plant();
		City.TurnResult resultOfLastTurn = runturn();
		if(this.city.cityExtinct())
		{
			ui.gameLost("All of your city's residents have starved to death. What a shame. You have ruined the once prosperous city of "+this.city.getState().getName()+".");
			this.state = GameState.GAMEOVER;
		}
		else if(tooManyStarved(resultOfLastTurn)) {
			ui.gameLost("More than half of your city's residents starved to death last year. What a shame. Because of your disastrous mismanagement, you are chased out of the city in disgrace.");
			this.state = GameState.GAMEOVER;
		}
		else if(gameWon()) {
			ui.gameWon("Congratulations, you have led the citizens of your city through "+this.config.getNumberOfYears()+" tough years. You will forever be revered as a hero of the city. A statue of you is be erected in the center of "+this.city.getState().getName()+". ");
			this.state = GameState.GAMEOVER;
		}

		// Recursive call to gameLoop
		if (this.state != GameState.GAMEOVER) {
			gameLoop();
		}
	}
	
	/**
     * Checks if the percentage of residents that starved in the last turn is greater than 50%.
     *
     * @param resultOfLastTurn the result of the last turn
     * @return true if more than 50% of residents starved, false otherwise
     */
	private boolean tooManyStarved(City.TurnResult resultOfLastTurn) {
		if(resultOfLastTurn.getStarvedPercentage() > 50)
			return true;
		return false;
	}

	/**
     * Runs a game turn and returns the result.
     *
     * @return the result of the turn
     */
	private City.TurnResult runturn() {
		City.TurnResult result = this.city.runTurn();
		this.ui.turnEnd(result);
		return result;
	}

	/**
     * Allows the player to plant crops. 
     */
	private void plant() {
		while (true) {
	        try {
	        	this.city.plant(ui.plant(this.config.getBushelsPerAcre(), this.config.getAcrePerResident(), this.city.getState()));
	            return;
	        } catch (InsufficientResourcesException e) {
	            String errorMessage = "Unable to plant crops: " + e.getMessage() + ". You need " + e.getRequired() + " bushels, but only have " + e.getAvailable() + " available.";
	            this.ui.illigleInput(errorMessage);
	        } catch (LandOperationException e) {
	            String errorMessage = "Unable to plant crops: " + e.getMessage();
	            this.ui.illigleInput(errorMessage);
	        }
	    }
	}

	/**
     * Allows the player to feed the residents in the city.
     */
	private void feed() {
		while (true) {
	        try {
	        	this.city.feed(ui.feed(this.config.getBushelsPerResident(), this.city.getState()));
	            return;
	        } catch (InsufficientResourcesException e) {
	            String errorMessage = "Unable to feed residents: You need " + e.getRequired() + " bushels, but only have " + e.getAvailable() + " available.";
	            this.ui.illigleInput(errorMessage);
	        }
	    }
	}
	
	/**
     * Checks if the game has been won by reaching the specified number of years.
     *
     * @return true if the game has been won, false otherwise
     */
	private boolean gameWon() {
		if(this.city.getState().getYear() == this.config.getNumberOfYears())
			return true;
		return false; 
	}
	
	/**
     * Allows the player to expand the depot.
     */
	private void expandDepot() {
		int numberOfNewSilos = ui.expandDepot(this.city.getState());

		while(!this.city.expandDepot(numberOfNewSilos, this.config.getSiloCapacity())) {
			this.ui.illigleInput("You can not afford this silo expansion. An extension of "+numberOfNewSilos+" silos costs " + numberOfNewSilos * this.config.getSiloCapacity() * GameConfig.DEPOT_EXPANSION_COST + " bushels.");
			numberOfNewSilos = ui.expandDepot(this.city.getState());
		}
	}
	
	/**
     * Allows the player to buy acres of land.
     */
	private void buy() {
		 int pricePerAcre = getPricePerAcre();
		    while (true) {
		        try {
		            this.city.buy(pricePerAcre, ui.buy(pricePerAcre, this.city.getState()));
		            return;
		        } catch (InsufficientResourcesException e) {
		            this.ui.illigleInput("You can't afford this. You need " + e.getRequired() + " bushels, but only have " + e.getAvailable() + " available.");
		        }
		    }
	}
	
	/**
     * Allows the player to sell acres of land.
     */
	private void sell() {
		int pricePerAcre = getPricePerAcre();
		while (true) {
	        try {
	        	this.city.sell(pricePerAcre, ui.sell(pricePerAcre, this.city.getState()));
	        	return;
	        }
	        
	        catch (LandOperationException e) {
	            String errorMessage = "Unable to complete sale: " + e.getMessage();
	            this.ui.illigleInput(errorMessage);
	        } catch (DepotCapacityExceededException e) {
	            String errorMessage = "Unable to complete sale: "+e.getMessage()+". Available capacity: " + e.getDepotCapacity();
	            this.ui.illigleInput(errorMessage);
	        }
	    }
	}
	
	 /**
     * Generates a random price per acre within the configured range.
     *
     * @return the generated price per acre
     */
	private int getPricePerAcre() {
		return fortune.nextInt(this.config.getMaxAcrePrice() - this.config.getMinArcrPrice()) + this.config.getMinArcrPrice();
	}
	
	/**
     * Returns the game configuration.
     *
     * @return the game configuration
     */
	public GameConfig getGameConfig() {
		return this.config;
	}
	
	/**
	 * Retrieves the current state of the city.
	 * This method returns a CityState object that encapsulates the current state
	 * of the city, including information about residents, resources, infrastructure, and more.
	 * It provides a snapshot of the city at the time the method is called, 
	 * which can be used for saving the game state or analyzing the city's current status.
	 *
	 * @return A CityState object representing the current state of the city.
	 */
	public CityState getCityState() {
		return this.city.getState();
	}
	
	/**
     * Sets the bushels and acres to the maximum integer value when the cheat code "IDKFA" is entered.
     * Used for testing the game.
     */
	public void IDKFA() {
		this.city.IDKFA();
	}

	/**
     * Quits the game.
     */
	public void quitGame() {
		this.state = GameState.GAMEOVER;
	}

	/**
	 * Retrieves the depot details based on the provided filter and comparator.
	 *
	 * @param filter The predicate to filter silos (can be null if no filtering is needed).
	 * @param comparator The comparator to sort silos (can be null if no sorting is needed).
	 * @return A string representation of the silos in the depot.
	 */
	public String getDepotDetails(Predicate<Silo> filter, Comparator<Silo> comparator) {
		return this.city.getDepotDetails(filter, comparator);
	}
}
