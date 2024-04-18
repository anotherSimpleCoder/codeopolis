package de.htwsaar.esch.Codeopolis.DomainModel;

import java.util.Arrays;

/**
 * The `TurnResult` class represents the result of a game turn.
 * It encapsulates various information about the state of the game after a turn.
 */
public class TurnResult {
	private String name; //The name of the city
	private int year; //The year of the city
	private int newResidents; // Number of new residents in the city after the turn
	private int[] bushelsHarvested; // Amount of bushels harvested during the turn
	private int residents; // Current number of residents in the city
	private int[] bushels; // Current number of bushels in the city
	private int starved; // Number of residents who starved during the turn
	private int acres; // Current number of acres owned by the city
	private int ateByRates; // Number of bushels eaten by rats during the turn
	private int starvedPercentage; // Percentage of residents starved during the turn
	private int depotCapacity; // The amount of storage spaces in the depot
	private int freeStorageSpaces; // The amount of free storage spaces in the depot
	private int bushelsDecayed; // The amount of bushels decayed in the depot during the last year. 
	private String depotState; //The state of the depot represented as string.

	
	
	 /**
     * Constructs a `TurnResult` object with the specified values for each attribute.
     *
     * @param name             The name of the city
     * @param year             The year of the city
     * @param newResidents     Number of new residents in the city after the turn
     * @param bushelsHarvested Amount of bushels harvested during the turn
     * @param residents        Current number of residents in the city
     * @param bushels          Current number of bushels in the city
     * @param starved          Number of residents who starved during the turn
     * @param acres            Current number of acres owned by the city
     * @param ateByRates       Number of bushels eaten by rats during the turn
     * @param bushelsDecayed       Amount of bushels that decayed during the turn.
     * @param depotCapacity        The capacity of the city's depot for storing harvests.
     * @param freeStorageSpaces    The number of free storage spaces in the city's depot.
     * @param depotState	The state of the depot represented as string.
     */
     
	public TurnResult(String name, int year, int newResidents, int[] bushelsHarvested, int residents, int[] bushels, int starved, int acres, int ateByRates, int starvedPercentage, int bushelsDecayed, int depotCapacity, int freeStorageSpaces, String depotState) {
		this.name = name;
		this.year = year;
		this.newResidents = newResidents;
		this.bushelsHarvested =bushelsHarvested;
		this.residents = residents;
		this.bushels = bushels;
		this.starved = starved;
		this.acres = acres;
		this.ateByRates = ateByRates;
		this.starvedPercentage = starvedPercentage;
		this.bushelsDecayed = bushelsDecayed;
		this.depotCapacity = depotCapacity;
		this.freeStorageSpaces = freeStorageSpaces;
		this.depotState = depotState;
	}

	 /**
     * Returns the number of new residents in the city after the turn.
     *
     * @return The number of new residents
     */
	public int getNewResidents() {
		return newResidents;
	}

	/**
     * Returns the amount of bushels harvested during the turn.
     *
     * @return The amount of bushels harvested
     */

	public int[] getBushelsHarvested() {
		return bushelsHarvested;
	}

	/**
     * Returns the current number of residents in the city.
     *
     * @return The number of residents
     */
	public int getResidents() {
		return residents;
	}

	/**
     * Returns the current number of bushels in the city.
     *
     * @return The number of bushels
     */
	public int[] getBushels() {
		return bushels;
	}

	/**
     * Returns the number of residents who starved during the turn.
     *
     * @return The number of starved residents
     */
	public int getStarved() {
		return starved;
	}

	/**
     * Returns the current number of acres in the city.
     *
     * @return The number of acres
     */
	public int getAcres() {
		return acres;
	}

	/**
     * Returns the number of bushels eaten by rats during the turn.
     *
     * @return The number of bushels eaten by rats
     */
	public int getAteByRates() {
		return ateByRates;
	}
	
	/**
     * Returns a string representation of the `TurnResult` object.
     *
     * @return A string representation of the object, displaying its attributes and their values
     */
	@Override
	public String toString() {
	    return "TurnResult{" +
	            "name='" + name + '\'' +
	            ", year=" + year +
	            ", newResidents=" + newResidents +
	            ", bushelsHarvested=" + bushelsHarvested +
	            ", residents=" + residents +
	            ", bushels=" + bushels +
	            ", starved=" + starved +
	            ", acres=" + acres +
	            ", ateByRates=" + ateByRates +
	            '}';
	}

	/**
     * Returns the name of the city.
     *
     * @return The name of the city.
     */
	public String getName() {
		return name;
	}

	/**
     * Returns the year of the city.
     *
     * @return The year of the city.
     */
	public int getYear() {
		return year;
	}
	
    /**
     * Indicates whether some other object is equal to this one by means of value equality.
     *
     * @param obj The reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TurnResult other = (TurnResult) obj;
        return year == other.year &&
                newResidents == other.newResidents &&
                Arrays.equals(bushelsHarvested, other.bushelsHarvested) &&
                residents == other.residents &&
                Arrays.equals(bushels, other.bushels) &&
                starved == other.starved &&
                acres == other.acres &&
                ateByRates == other.ateByRates &&
                name.equals(other.name) &&
                starvedPercentage == other.starvedPercentage &&
                depotCapacity == other.depotCapacity &&
                freeStorageSpaces == other.freeStorageSpaces &&
                bushelsDecayed == other.bushelsDecayed;
    }

    /**
     * Returns the percentage of residents starved during this turn.
     *
     * @return Percentage of residents starved during this turn.
     */
	public int getStarvedPercentage() {
		return starvedPercentage;
	}

	/**
	 * The amount of storage spaces in the depot
	 * @return Amount of storage spaces in the depot
	 */
	public int getDepotCapacity() {
		return depotCapacity;
	}

	/**
	 * The amount of free storage spaces in the depot
	 * @return The amount of free storage spaces in the depot
	 */
	public int getFreeStorageSpaces() {
		return freeStorageSpaces;
	}

	/**
	 * Gets the amount of bushels that decayed during the turn.
	 *
	 * @return The amount of bushels that decayed during the turn.
	 */
	public int getBushelsDecayed() {
		return this.bushelsDecayed;
	}
	
	/**
     * Returns the total number of bushels harvested during the turn.
     *
     * @return The total number of bushels harvested.
     */
	public int getNumberOfBushelsHarvested() {
		int result = 0; 
		for(int i = 0; i< this.getBushelsHarvested().length; i++)
			result += this.getBushelsHarvested()[i];
		return result;
	}
	
    /**
     * Returns the total number of bushels in the city after the turn.
     *
     * @return The total number of bushels.
     */
	public int getTotalNumberOfBushels() {
		int result = 0; 
		for(int i = 0; i< this.getBushels().length; i++)
			result += this.getBushels()[i];
		return result;
	}

	/**
	 * Retrieves the state of the depot represented as string.
	 * 
	 * @return The state of the depot.
	 */
	public String getDepotState() {
		return depotState;
	}
}
