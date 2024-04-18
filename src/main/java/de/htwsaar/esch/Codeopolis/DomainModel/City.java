package de.htwsaar.esch.Codeopolis.DomainModel;

import java.util.Random;

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
    private String name;
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
    	this.depot.store(Harvest.createHarvest(Game.GrainType.BARLEY, this.config.getInitialBushels(Game.GrainType.BARLEY.ordinal()), this.year));
    	this.depot.store(Harvest.createHarvest(Game.GrainType.CORN, this.config.getInitialBushels(Game.GrainType.CORN.ordinal()), this.year));
    	this.depot.store(Harvest.createHarvest(Game.GrainType.MILLET, this.config.getInitialBushels(Game.GrainType.MILLET.ordinal()), this.year));
    	this.depot.store(Harvest.createHarvest(Game.GrainType.RICE, this.config.getInitialBushels(Game.GrainType.RICE.ordinal()), this.year));
    	this.depot.store(Harvest.createHarvest(Game.GrainType.RYE, this.config.getInitialBushels(Game.GrainType.RYE.ordinal()), this.year));
    	this.depot.store(Harvest.createHarvest(Game.GrainType.WHEAT, this.config.getInitialBushels(Game.GrainType.WHEAT.ordinal()), this.year));
    	
    	this.acres = config.getInitialAcres();
    	this.residents = config.getInitialResidents();
    }

    /**
     * Expands the capacity of the depot in the game by the specified additional capacity.
     * This method delegates the expansion operation to the underlying Depot object.
     *
     * @param additionalCapacity The additional capacity to be added to the depot.
     * @see Depot#expand(int)
     */
    public void expandDepot(int numberOfSilos, int capacityPerSilo) {
    	this.depot.expand(numberOfSilos, capacityPerSilo);
    }
    
	/**
     * Attempts to buy the specified number of acres at the given price.
     * 
     * @param price The price per acre.
     * @param acres The number of acres to buy.
     * @return True if the purchase was successful, false otherwise.
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
     * @return True if the sale was successful, false otherwise.
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
     * @return True if the feeding was successful, false otherwise.
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
     * @return True if the planting was successful, false otherwise.
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
            harvests[grainType.ordinal()] = Harvest.createHarvest(grainType, bushels, year);
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
		Conditions thisYearsConditions = Conditions.generateRandomConditions();

		boolean drought = this.fortune.nextFloat() > 0.8 ? true : false;
		boolean fusarium = this.fortune.nextFloat() > 0.8 ? true : false;
		boolean leafDrought = this.fortune.nextFloat() > 0.8 ? true : false;
		boolean powderyMildew = this.fortune.nextFloat() > 0.8 ? true : false;
		boolean barleyGoutFly = this.fortune.nextFloat() > 0.8 ? true : false;
		boolean deliaFly = this.fortune.nextFloat() > 0.8 ? true : false;
		boolean fritFly = this.fortune.nextFloat() > 0.8 ? true : false;
		
		for(int i = 0; i< Game.GrainType.values().length; i++) {
			if(this.planted[i] != null) {
				this.planted[i].grow(thisYearsConditions);
				if(drought)
					this.planted[i].drought();
				if(fusarium)
					this.planted[i].diseaseOutbreak(Grain.Diseases.Fusarium, thisYearsConditions);
				if(leafDrought)
					this.planted[i].diseaseOutbreak(Grain.Diseases.LeafDrought, thisYearsConditions);
				if(powderyMildew)
					this.planted[i].diseaseOutbreak(Grain.Diseases.PowderyMildew, thisYearsConditions);
				if(barleyGoutFly)
					this.planted[i].pestInfestation(Grain.Pests.BarleyGoutFly, thisYearsConditions);
				if(deliaFly)
					this.planted[i].pestInfestation(Grain.Pests.DeliaFly, thisYearsConditions);
				if(fritFly)
					this.planted[i].pestInfestation(Grain.Pests.FritFly, thisYearsConditions);
				harvested[i] = this.planted[i].harvest();	
			}
		}		
		Harvest[] thisYearsHarvest = new Harvest[] {Harvest.createHarvest(Game.GrainType.BARLEY, harvested[Game.GrainType.BARLEY.ordinal()], this.year), 
		        Harvest.createHarvest(Game.GrainType.CORN, harvested[Game.GrainType.CORN.ordinal()], this.year), 
		        Harvest.createHarvest(Game.GrainType.MILLET, harvested[Game.GrainType.MILLET.ordinal()], this.year), 
		        Harvest.createHarvest(Game.GrainType.RICE, harvested[Game.GrainType.RICE.ordinal()], this.year), 
		        Harvest.createHarvest(Game.GrainType.RYE, harvested[Game.GrainType.RYE.ordinal()], this.year), 
		        Harvest.createHarvest(Game.GrainType.WHEAT, harvested[Game.GrainType.WHEAT.ordinal()], this.year)};
		
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
		
		
		return new TurnResult(this.name, 
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
	    this.depot.expand(6, Integer.MAX_VALUE);
	    this.depot.store(Harvest.createHarvest(Game.GrainType.BARLEY, 100000, this.year)); 
	    this.depot.store(Harvest.createHarvest(Game.GrainType.CORN, 100000, this.year)); 
	    this.depot.store(Harvest.createHarvest(Game.GrainType.MILLET, 100000, this.year));
	    this.depot.store(Harvest.createHarvest(Game.GrainType.RICE, 100000, this.year)); 
	    this.depot.store(Harvest.createHarvest(Game.GrainType.RYE, 100000, this.year)); 
	    this.depot.store(Harvest.createHarvest(Game.GrainType.WHEAT, 100000, this.year));
	    this.acres = 1000000;
	}

}

