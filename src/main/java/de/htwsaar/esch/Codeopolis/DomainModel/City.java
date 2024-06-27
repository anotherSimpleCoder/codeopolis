package de.htwsaar.esch.Codeopolis.DomainModel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Predicate;

import de.htwsaar.esch.Codeopolis.DomainModel.Game.GrainType;
import de.htwsaar.esch.Codeopolis.DomainModel.Harvest.*;
import de.htwsaar.esch.Codeopolis.DomainModel.Plants.*;
import de.htwsaar.esch.Codeopolis.Exceptions.*;

/**
 * Represents a city in Codeopolis
 */
public class City extends GameEntity {
	private int acres;
	private int residents;
	private int year = 0;
	private int fed = -1;
	private Random fortune;
	private Grain[] planted;
	private GameConfig config;
	private final String name;
	private Depot depot;

	/**
	 * Constructs a new City object with the specified name and game configuration.
	 *
	 * @param name The name of the city.
	 * @param config The game configuration.
	 */
	public City(String id, String name, GameConfig config) {
		super(id);
		this.name = name;
		this.config = config;
		this.fortune = new Random();
		setupCity();
	}

	/**
	 * Constructs a new City object using the state of an existing city.
	 * This constructor initializes the city's state based on a CityState object.
	 *
	 * @param cityState The state of the city to use for initialization.
	 */
	public City(CityState cityState, GameConfig config) {
		super(cityState.getId());
		this.name = cityState.getName();
		this.config = config;
		this.fortune = new Random();

		this.acres = cityState.getAcres();
		this.planted = new Grain[Game.GrainType.values().length];
		this.residents = cityState.getResidents();
		this.year = cityState.getYear();

		this.depot = new Depot(cityState.getSilos());
	}


	private void setupCity()
	{
		this.depot = new Depot(Game.GrainType.values().length, this.config.getSiloCapacity());
		this.planted = new Grain[Game.GrainType.values().length];
		this.depot.store(Harvest.createHarvest(Game.GrainType.BARLEY, this.config.getInitialBushels(Game.GrainType.BARLEY.ordinal()), this.year, 1f));
		this.depot.store(Harvest.createHarvest(Game.GrainType.CORN, this.config.getInitialBushels(Game.GrainType.CORN.ordinal()), this.year, 1f));
		this.depot.store(Harvest.createHarvest(Game.GrainType.MILLET, this.config.getInitialBushels(Game.GrainType.MILLET.ordinal()), this.year, 1f));
		this.depot.store(Harvest.createHarvest(Game.GrainType.RICE, this.config.getInitialBushels(Game.GrainType.RICE.ordinal()), this.year, 1f));
		this.depot.store(Harvest.createHarvest(Game.GrainType.RYE, this.config.getInitialBushels(Game.GrainType.RYE.ordinal()), this.year, 1f));
		this.depot.store(Harvest.createHarvest(Game.GrainType.WHEAT, this.config.getInitialBushels(Game.GrainType.WHEAT.ordinal()), this.year, 1f));

		this.acres = config.getInitialAcres();
		this.residents = config.getInitialResidents();
	}

	/**
	 * Expands the capacity of the depot in the game by the specified additional capacity.
	 * This method delegates the expansion operation to the underlying Depot object.
	 *
	 * @param numberOfSilos The number of new silos
	 * @param capacityPerSilo The capacity of the new silos
	 * @return true if the expansion was successful, false otherwise
	 */
	public boolean expandDepot(int numberOfSilos, int capacityPerSilo) {
		return this.depot.expand(numberOfSilos, capacityPerSilo);
	}

	/**
	 * Attempts to buy the specified number of acres at the given price.
	 *
	 * @param price The price per acre.
	 * @param acres The number of acres to buy.
	 */
	public void buy(int price, int acres) throws InsufficientResourcesException{
		if(this.acres == 0)
			return;
		if(price * acres > this.depot.getTotalFillLevel())
			throw new InsufficientResourcesException("Insufficient resources to buy " + acres + " acres.", price * acres, this.depot.getTotalFillLevel());
		this.depot.takeOut(price * acres);
		if(this.acres + acres >= 0) //Avoid integer overflow
			this.acres += acres;
		else
			this.acres = Integer.MAX_VALUE;
	}

	/**
	 * Attempts to sell the specified number of acres at the given price.
	 *
	 * @param price The price per acre.
	 * @param acres The number of acres to sell.
	 */
	public void sell(int price, int acres) throws LandOperationException, DepotCapacityExceededException{
		if(acres == 0)
			return;
		if(acres>this.acres)
			throw new LandOperationException("Attempting to sell more acres than available. You own "+this.acres+" acres and try to sell "+acres+" acres");
		if(!this.depot.full()) {
			this.acres -= acres;
			for(Harvest h : buildNewEquallyDistributedHarvest(price * acres))
				this.depot.store(h); //Issue #39
		}
		else {
			throw new DepotCapacityExceededException("Depot is full, cannot proceed with the sale", this.depot.getTotalFillLevel());
		}
	}

	/**
	 * Attempts to feed the residents with the specified number of bushels.
	 *
	 * @param feed The number of bushels to feed the residents.
	 */
	public void feed(int feed) throws InsufficientResourcesException{
		if(feed>this.depot.getTotalFillLevel())
			throw new InsufficientResourcesException("Insufficient resources to feed " + feed + " bushels.", feed, this.depot.getTotalFillLevel());
		this.depot.takeOut(feed);
		this.fed = feed;
	}

	/**
	 * Attempts to plant the specified number of acres.
	 *
	 * @param acres The number of acres to plant.
	 */
	public void plant(int[] acres) throws InsufficientResourcesException, LandOperationException{
		int acresSum = 0;
		for (GrainType grainType : GrainType.values()) {
			int i = grainType.ordinal();
			acresSum += acres[i];
			if(acres[i] * this.config.getBushelsPerAcre() > this.depot.getFillLevel(grainType))
				throw new InsufficientResourcesException(
						"Not enough bushels to plant " + acres[i] + " acres of grain type " + grainType,
						acres[i] * this.config.getBushelsPerAcre(),
						this.depot.getFillLevel(grainType)
				);
		}
		if(acresSum > this.acres)
			throw new LandOperationException(
					"Attempting to plant " + acresSum + " acres, but only " + this.acres + " acres are available."
			);

		if(acresSum > this.config.getAcrePerResident() * this.residents)
			throw new LandOperationException(
					"Not enough residents to plant " + acresSum + " acres. You can plant "+this.config.getAcrePerResident()+" acres per resident."
			);

		Grain seed = null;
		for (GrainType grainType : GrainType.values()) {
			seed = null;
			int grainTypeIndex = grainType.ordinal(); // Get the ordinal index of the enum

			switch (grainType) {
				case BARLEY:
					seed = new Barley();
					break;
				case CORN:
					seed = new Corn();
					break;
				case MILLET:
					seed = new Millet();
					break;
				case RICE:
					seed = new Rice();
					break;
				case RYE:
					seed = new Rye();
					break;
				case WHEAT:
					if (fortune.nextDouble() < 0.1)
						seed = new Wheat(){
							@Override
							protected float getBasicYieldRatio() {
								return super.getBasicYieldRatio()*2;
							}
						};
					else
						seed = new Wheat();
					break;
				// No default case needed as we are covering all enum constants
			}
			if(acres[grainTypeIndex] > 0 && seed != null) {
				seed.plant(acres[grainTypeIndex]);
				this.planted[grainTypeIndex] = seed;
				this.depot.takeOut(acres[grainTypeIndex] * this.config.getBushelsPerAcre(), grainType);
			}
		}
	}


	/**
	 * Builds a new Harvest object with an equal distribution of grain based on the specified amount.
	 *
	 * @param amount The total amount of grain to be equally distributed among different grain types.
	 * @return A new Harvest object with an equal distribution of grain.
	 */
	private Harvest[] buildNewEquallyDistributedHarvest(int amount) {
		int partition = amount / Game.GrainType.values().length;
		int remainder = amount % Game.GrainType.values().length;
		Harvest[] harvests = new Harvest[Game.GrainType.values().length];

		for (Game.GrainType grainType : Game.GrainType.values()) {
			int bushels = partition;
			if (remainder > 0) {
				bushels++;
				remainder--;
			}
			harvests[grainType.ordinal()] = Harvest.createHarvest(grainType, bushels, year, new Random().nextFloat(0.5f,1f) );
		}
		return harvests;
	}




	/**
	 * Runs a turn in the game for the city, updating its state and returning the result.
	 *
	 * @return The result of the turn.
	 */
	public TurnResult runTurn() {
		//Calculate how many inhabitants of the city starved to death:
		int peopleStarved = this.residents- this.fed/this.config.getBushelsPerResident();
		if(peopleStarved < 0)
			peopleStarved = 0;
		int peopleStarvedPercentage = (peopleStarved*100) / this.residents;

		//Calculate the new population size
		int newResidents = 0;
		if(peopleStarvedPercentage < 40)
			newResidents = (this.residents * this.fortune.nextInt(40))/100;
		this.residents -= peopleStarved;
		if(this.residents + newResidents >= 0) //Avoid integer overflow
			this.residents += newResidents;
		else
			this.residents = Integer.MAX_VALUE;

		//Calculation of the harvest:
		int[] harvested = new int[Game.GrainType.values().length];
		Grain.Conditions thisYearsConditions = Grain.Conditions.generateRandomConditions();

		for(int i = 0; i< Game.GrainType.values().length; i++) {
			if(this.planted[i] != null) {
				this.planted[i].grow(thisYearsConditions);
				harvested[i] = this.planted[i].harvest();
			}
		}
		Harvest[] thisYearsHarvest = new Harvest[] {Harvest.createHarvest(Game.GrainType.BARLEY, harvested[Game.GrainType.BARLEY.ordinal()], this.year, thisYearsConditions.calculateConditionScore()),
				Harvest.createHarvest(Game.GrainType.CORN, harvested[Game.GrainType.CORN.ordinal()], this.year, thisYearsConditions.calculateConditionScore()),
				Harvest.createHarvest(Game.GrainType.MILLET, harvested[Game.GrainType.MILLET.ordinal()], this.year, thisYearsConditions.calculateConditionScore()),
				Harvest.createHarvest(Game.GrainType.RICE, harvested[Game.GrainType.RICE.ordinal()], this.year, thisYearsConditions.calculateConditionScore()),
				Harvest.createHarvest(Game.GrainType.RYE, harvested[Game.GrainType.RYE.ordinal()], this.year, thisYearsConditions.calculateConditionScore()),
				Harvest.createHarvest(Game.GrainType.WHEAT, harvested[Game.GrainType.WHEAT.ordinal()], this.year, thisYearsConditions.calculateConditionScore())};

		for(Harvest h : thisYearsHarvest)
			this.depot.store(h); //Issue #40

		//Calculation of how much grain was eaten by rats:
		int ateByRates = 0;
		if(this.depot.getTotalFillLevel() > 0)
			ateByRates = this.fortune.nextInt((this.depot.getTotalFillLevel()*this.config.getMaxRateInfestation())/100);
		this.depot.takeOut(ateByRates);

		//Bushels in the depot decay.
		int bushelsDecayed = this.depot.decay(this.year);

		//Increment the year by 1:
		this.year++;


		return new City.TurnResult(this.name,
				this.year,
				newResidents,
				harvested,
				this.residents,
				this.depot.getBushelsCategorizedByGrainType(),
				peopleStarved,
				this.acres,
				ateByRates,
				peopleStarvedPercentage,
				bushelsDecayed,
				this.depot.totalCapacity(),
				this.depot.totalCapacity() - this.depot.getTotalFillLevel(),
				this.depot.toString());
	}

	/**
	 * Checks if the city is extinct (has no residents).
	 *
	 * @return True if the city is extinct, false otherwise.
	 */
	public boolean cityExtinct() {
		if(this.residents == 0)
			return true;
		else
			return false;
	}

	/**
	 * Returns the current state of the city.
	 *
	 * @return The CityState object representing the current state.
	 */
	public CityState getState() {
		return new CityState(this.name, this.getId(), this.residents, this.depot.getBushelsCategorizedByGrainType(), this.acres, this.year, this.depot.totalCapacity() - this.depot.getTotalFillLevel(), this.depot.getSilos());
	}

	/**
	 * The method sets bushels and acres to max integer when the cheat code IDKFA was entered. Used for testing the game.
	 */
	public void IDKFA() {
		this.depot.store(Harvest.createHarvest(Game.GrainType.BARLEY, 100000, this.year, 1f));
		this.depot.store(Harvest.createHarvest(Game.GrainType.CORN, 100000, this.year, 1f));
		this.depot.store(Harvest.createHarvest(Game.GrainType.MILLET, 100000, this.year, 1f));
		this.depot.store(Harvest.createHarvest(Game.GrainType.RICE, 100000, this.year, 1f));
		this.depot.store(Harvest.createHarvest(Game.GrainType.RYE, 100000, this.year, 1f));
		this.depot.store(Harvest.createHarvest(Game.GrainType.WHEAT, 100000, this.year, 1f));
		this.acres = 1000000;
	}

	/**
	 * Retrieves the depot details based on the provided filter and comparator.
	 *
	 * @param filter The predicate to filter silos (can be null if no filtering is needed).
	 * @param comparator The comparator to sort silos (can be null if no sorting is needed).
	 * @return A string representation of the silos in the depot.
	 */
	public String getDepotDetails(Predicate<Silo> filter, Comparator<Silo> comparator) {
		return depot.toString(filter, comparator);
	}


	public class TurnResult {
		private final String name; //The name of the city
		private final int year; //The year of the city
		private final int newResidents; // Number of new residents in the city after the turn
		private final int[] bushelsHarvested; // Amount of bushels harvested during the turn
		private final int residents; // Current number of residents in the city
		private final int[] bushels; // Current number of bushels in the city
		private final int starved; // Number of residents who starved during the turn
		private final int acres; // Current number of acres owned by the city
		private final int ateByRates; // Number of bushels eaten by rats during the turn
		private final int starvedPercentage; // Percentage of residents starved during the turn
		private final int depotCapacity; // The amount of storage spaces in the depot
		private final int freeStorageSpaces; // The amount of free storage spaces in the depot
		private final int bushelsDecayed; // The amount of bushels decayed in the depot during the last year.
		private final String depotState; //The state of the depot represented as string.



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

		private TurnResult(String name, int year, int newResidents, int[] bushelsHarvested, int residents, int[] bushels, int starved, int acres, int ateByRates, int starvedPercentage, int bushelsDecayed, int depotCapacity, int freeStorageSpaces, String depotState) {
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
			de.htwsaar.esch.Codeopolis.DomainModel.City.TurnResult other = (de.htwsaar.esch.Codeopolis.DomainModel.City.TurnResult) obj;
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

}

