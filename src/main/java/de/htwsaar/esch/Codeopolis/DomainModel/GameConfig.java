package de.htwsaar.esch.Codeopolis.DomainModel;

import java.io.Serializable;
import java.util.Random;

import de.htwsaar.esch.Codeopolis.DomainModel.Game.Difficulty;

/**
 * Represents the configuration settings for a game.
 */
public class GameConfig implements Serializable{
	private int maxAcrePrice; // Maximum price per acre of land.
	private int minArcrPrice; // Minimum price per acre of land. 
	private int bushelsPerResident; // Number of bushels to feed per resident per year.
	private int bushelsPerAcre; // Number of bushels that need to be planted per acre.
	private int acrePerResident; // Number of acres that a resident can farm.
	private int initialAcres; // Initial number of acres
	private int initialResidents; // Initial number of residents
	private int siloCapacity; //Capacity of a silo
	private int[] initialBushels; // Initial number of bushels
	private int numberOfYears; // Number of game turns (years)
	private float harvestFactor; // The harvest factor indicates the maximum harvest rate. 
	private int maxRateInfestation; //The maximum rate infestation indicates the maximum percentage of bushels that may be eaten by rates in one year.
	private Difficulty difficulty; // Difficulty level of the game
	
	public static final double DECAY_PERCENTAGE_PER_YEAR = 0.02;
	public static final double DEPOT_EXPANSION_COST = 0.05;


	
	/**
     * Constructs a new GameConfig object with the specified difficulty level.
     * 
     * @param difficulty The difficulty level of the game.
     */
	public GameConfig(Game.Difficulty difficulty) {
		this.difficulty = difficulty;
		this.initialBushels = new int[Game.GrainType.values().length];
		setup();
	}
	/**
	 * Setup the game config according to the given difficulty.
	 */
	private void setup() {
		int[] grainDistribution = distributeBushelsRandomly(3000, Game.GrainType.values().length);
		switch(this.difficulty) {
		case EASY:
			this.initialAcres = 1000;
			this.initialBushels[Game.GrainType.BARLEY.ordinal()] = grainDistribution[Game.GrainType.BARLEY.ordinal()];
			this.initialBushels[Game.GrainType.CORN.ordinal()] = grainDistribution[Game.GrainType.CORN.ordinal()];
			this.initialBushels[Game.GrainType.MILLET.ordinal()] = grainDistribution[Game.GrainType.MILLET.ordinal()];
			this.initialBushels[Game.GrainType.RICE.ordinal()] = grainDistribution[Game.GrainType.RICE.ordinal()];
			this.initialBushels[Game.GrainType.RYE.ordinal()] = grainDistribution[Game.GrainType.RYE.ordinal()];
			this.initialBushels[Game.GrainType.WHEAT.ordinal()] = grainDistribution[Game.GrainType.WHEAT.ordinal()];
			this.initialResidents = 100;
			this.maxAcrePrice = 30;
			this.minArcrPrice = 10;
			this.bushelsPerResident = 20;
			this.bushelsPerAcre = 1;
			this.acrePerResident = 10;
			this.numberOfYears = 10;
			this.harvestFactor = 6f;
			this.maxRateInfestation = 10;
			this.siloCapacity = 1500;
			break;
		case MEDIUM:
			this.initialAcres = 900;
			this.initialBushels[Game.GrainType.BARLEY.ordinal()] = grainDistribution[Game.GrainType.BARLEY.ordinal()];
			this.initialBushels[Game.GrainType.CORN.ordinal()] = grainDistribution[Game.GrainType.CORN.ordinal()];
			this.initialBushels[Game.GrainType.MILLET.ordinal()] = grainDistribution[Game.GrainType.MILLET.ordinal()];
			this.initialBushels[Game.GrainType.RICE.ordinal()] = grainDistribution[Game.GrainType.RICE.ordinal()];
			this.initialBushels[Game.GrainType.RYE.ordinal()] = grainDistribution[Game.GrainType.RYE.ordinal()];
			this.initialBushels[Game.GrainType.WHEAT.ordinal()] = grainDistribution[Game.GrainType.WHEAT.ordinal()];
			this.initialResidents = 100;
			this.maxAcrePrice = 35;
			this.minArcrPrice = 15;
			this.bushelsPerResident = 22;
			this.bushelsPerAcre = 1;
			this.acrePerResident = 10;
			this.numberOfYears = 10;
			this.harvestFactor = 5f;
			this.maxRateInfestation = 20;
			this.siloCapacity = 1500;
			break;
		case HARD:
			this.initialAcres = 800;
			this.initialBushels[Game.GrainType.BARLEY.ordinal()] = grainDistribution[Game.GrainType.BARLEY.ordinal()];
			this.initialBushels[Game.GrainType.CORN.ordinal()] = grainDistribution[Game.GrainType.CORN.ordinal()];
			this.initialBushels[Game.GrainType.MILLET.ordinal()] = grainDistribution[Game.GrainType.MILLET.ordinal()];
			this.initialBushels[Game.GrainType.RICE.ordinal()] = grainDistribution[Game.GrainType.RICE.ordinal()];
			this.initialBushels[Game.GrainType.RYE.ordinal()] = grainDistribution[Game.GrainType.RYE.ordinal()];
			this.initialBushels[Game.GrainType.WHEAT.ordinal()] = grainDistribution[Game.GrainType.WHEAT.ordinal()];
			this.initialResidents = 100;
			this.maxAcrePrice = 35;
			this.minArcrPrice = 15;
			this.bushelsPerResident = 24;
			this.bushelsPerAcre = 1;
			this.acrePerResident = 10;
			this.numberOfYears = 10;
			this.harvestFactor = 4f;
			this.maxRateInfestation = 25;
			this.siloCapacity = 1500;
			break;
		default:
			throw new UnsupportedOperationException("The difficulty level "+this.difficulty+" is not yet implemented.");
		}
	}
	
	/**
	 * Distributes a given quantity of bushels randomly across different grain types.
	 *
	 * @param x The total quantity of grain to distribute.
	 * @param n The number of grain types.
	 * @return An array representing the distribution of grain among types.
	 */
	private int[] distributeBushelsRandomly(int x, int n) {
	     if (x <= 0 || n <= 0) {
	            throw new IllegalArgumentException();
	        }

	        int[] result = new int[n];
	        Random random = new Random();

	        // Calculate the mean value for the distribution
	        int mean = x / n;

	        // Calculate the sum of the parts distributed so far
	        int sum = 0;

	        for (int i = 0; i < n - 1; i++) {
	            // Generate a random value between mean/2 and 1.5 * mean
	            int randomValue = random.nextInt(mean) + mean / 2;
	            
	            // Ensure that the remaining value doesn't go negative
	            if (sum + randomValue > x) {
	                randomValue = x - sum;
	            }

	            result[i] = randomValue;
	            sum += randomValue;
	        }

	        // The last part gets the remaining value to ensure full distribution
	        result[n - 1] = x - sum;

	        // Optional: Shuffle the array to get a random distribution
	        shuffleArray(result);

	        return result;
    }
	
	/**
	 * Shuffles the elements of an integer array to achieve a random distribution.
	 *
	 * @param array The array to shuffle.
	 */
	private void shuffleArray(int[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
	
	/**
     * Returns the maximum price per acre.
     * 
     * @return The maximum acre price.
     */
	public int getMaxAcrePrice() {
		return maxAcrePrice;
	}

	/**
     * Returns the minimum price per acre.
     * 
     * @return The minimum acre price.
     */
	public int getMinArcrPrice() {
		return minArcrPrice;
	}

	/**
     * Returns the number of bushels per resident.
     * 
     * @return The number of bushels per resident.
     */
	public int getBushelsPerResident() {
		return bushelsPerResident;
	}

	/**
     * Returns the number of bushels per acre.
     * 
     * @return The number of bushels per acre.
     */
	public int getBushelsPerAcre() {
		return bushelsPerAcre;
	}

	/**
     * Returns the number of acres per resident.
     * 
     * @return The number of acres per resident.
     */
	public int getAcrePerResident() {
		return acrePerResident;
	}
	
	/**
     * Returns the difficulty level of the game.
     * 
     * @return The difficulty level.
     */
	public Game.Difficulty getDifficulty(){
		return this.difficulty;
	}
	
	/**
	 * Returns the initial number of acres.
	 *
	 * @return The initial number of acres.
	 */
	public int getInitialAcres() {
		return initialAcres;
	}
	
	/**
	 * Returns the initial number of residents.
	 *
	 * @return The initial number of residents.
	 */
	public int getInitialResidents() {
		return initialResidents;
	}
	
	/**
	 * Retrieves the initial number of bushels for the specified grain type.
	 *
	 * @param grainType The index representing the type of grain (e.g., wheat, rice, etc.).
	 * @return The initial number of bushels for the specified grain type.
	 */
	public int getInitialBushels(int grainType) {
		return initialBushels[grainType];
	}
	
	/**
	 * Returns the number of game turns (years).
	 *
	 * @return The number of game turns (years).
	 */
	public int getNumberOfYears() {
		return numberOfYears;
	}
	
	/**
	 * Returns the harvest factor of the game configuration. The harvest factor indicates the maximum harvest rate. 
	 *
	 * @return The harvest factor of the game configuration.
	 */
	public float getHarvestFactor() {
		return harvestFactor;
	}
	
	/**
	 * Returns the maximum rate infestation. The maximum rate infestation indicates the maximum percentage of bushels that may be eaten by rates in one year.
	 *
	 * @return The rate infestation.
	 */
	public int getMaxRateInfestation() {
		return maxRateInfestation;
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
	    GameConfig other = (GameConfig) obj;
	    return maxAcrePrice == other.maxAcrePrice &&
	            minArcrPrice == other.minArcrPrice &&
	            bushelsPerResident == other.bushelsPerResident &&
	            bushelsPerAcre == other.bushelsPerAcre &&
	            acrePerResident == other.acrePerResident &&
	            initialAcres == other.initialAcres &&
	            initialResidents == other.initialResidents &&
	            initialBushels == other.initialBushels &&
	            numberOfYears == other.numberOfYears &&
	            difficulty == other.difficulty;
	}
	
	/**
	 * Returns the capacity of a silo.
	 *
	 * @return The the capacity of a silo..
	 */
	public int getSiloCapacity() {
		return siloCapacity;
	}

	
}
