package de.htwsaar.esch.Codeopolis.DomainModel;

/**
 * The UserInterface interface represents the interface for interactions between the game logic and the user interface.
 * It defines methods for buying, selling, feeding, planting, handling turn end, illegal input,
 * and game over events.
 * 
 * This interface decouples the game logic from the user interface. 
 * Dependencies point from the user interface to the game logic, not vice versa. 
 * In this way, it is possible to replace the user interface without having to change the game logic. 
 */
public interface UserInterface {
	
	/**
	 * Allows the user to expand the depot.
	 * @param cityState The current state of the city.
     * @return The additional capacity of the depot.
	 */
	int expandDepot(CityState cityState);
	
	/**
     * Allows the user to buy acres of land.
     *
     * @param pricePerAcre The price per acre of land.
     * @param cityState The current state of the city.
     * @return The number of acres bought.
     */
	int buy(int pricePerAcre, CityState cityState);
	
	/**
     * Allows the user to sell acres of land.
     *
     * @param pricePerAcre The price per acre of land.
     * @param cityState The current state of the city.
     * @return The number of acres sold.
     */
	int sell(int pricePerAcre, CityState cityState);
	
	/**
     * Allows the user to feed the city's residents.
     *
     * @param bushelsPerResident The amount of bushels required per resident.
     * @param cityState The current state of the city.
     * @return The number of residents fed.
     */
	int feed(int bushelsPerResident, CityState cityState);
	
	/**
     * Allows the user to plant crops on acres of land.
     *
     * @param bushelsPerAcre The amount of bushels required per acre of land.
     * @param acrePerResident The number of acres required per resident.
     * @param cityState The current state of the city.
     * @return The number of acres planted.
     */
	int[] plant(int bushelsPerAcre, int acrePerResident, CityState cityState);
	
	/**
     * Handles the end of a turn.
     *
     * @param result The result of the turn.
     */
	void turnEnd(City.TurnResult result);
	
	/**
     * Handles illegal user input.
     *
     * @param message The error message indicating the illegal input.
     */
	void illigleInput(String message);
	
	/**
     * Handles the game won event.
     * 
     * @param message The message indicating the result of the game.
     */
	void gameWon(String message);
	
	/**
     * Handles the game lost event.
     * 
     * @param message The message indicating the result of the game.
     */
	void gameLost(String message);
}
