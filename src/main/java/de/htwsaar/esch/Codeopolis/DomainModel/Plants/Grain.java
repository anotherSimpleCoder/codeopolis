package de.htwsaar.esch.Codeopolis.DomainModel.Plants;

/**
 * The `Grain` class represents an abstract grain plant with common attributes and behaviors.
 * Subclasses of `Grain` define specific types of grain plants.
 */
public abstract class Grain {
	
	public enum Pests{FritFly, DeliaFly, BarleyGoutFly};
	
	public enum Diseases{PowderyMildew, LeafDrought, Fusarium}
	
	private float basicYieldRatio; // The basic yield ratio of the grain.
	
	private boolean harvested = false; // Indicates if the grain has been harvested.
	
	private int acresPlanted; // Number of acres where the grain is planted.
	
	/*
	 * yieldRatio is protected and therefore visible in the whole package. 
	 * Ideally, it would only be visible in the subclasses only. 
	 * However, this is achieved by having a separate package for grain and all its subclasses. 
	 * 
	 * As an alternative, there could also be a private variable in each subclass of Grain. 
	 * Grain could then define an abstract method "public float getYieldRatio", which is also implemented in the subclasses. 
	 * However, this would mean a redundant implementation. 
	 * The getYieldRatio method would be needed since yieldRatio is required in the subclasses as well as in Grain's harvest method. 
	 */
	
    /**
     * The yield ratio represents the ratio of yield obtained from the planted grain.
     * It is affected by the quality of soil conditions and the grain's basic yield ratio.
     */
	protected float yieldRatio = 0f;

    /**
     * Gets the current yield ratio of the grain.
     *
     * @return The current yield ratio.
     */
	public float getYieldRatio() {
		return yieldRatio;
	}

	/**
     * The crop failure due to bad conditions represents the percentage of crop failure
     * caused by adverse environmental conditions, such as drought or pests.
     */
	private float cropFailureDueToBadConditions;
	
    /**
     * Constructs a `Grain` object with the specified basic yield ratio and crop failure due to bad conditions.
     *
     * @param basicYieldRatio             The basic yield ratio of the grain.
     * @param cropFailureDueToBadConditions The crop failure due to bad conditions.
     */
	public Grain(float basicYieldRatio, float cropFailureDueToBadConditions) {
		this.basicYieldRatio = basicYieldRatio;
		this.cropFailureDueToBadConditions = cropFailureDueToBadConditions;
		this.acresPlanted = 0;
	}
	
    /**
     * Plants the grain on a specified number of acres.
     *
     * @param acres The number of acres to plant the grain.
     * @return True if the planting was successful, false otherwise.
     */
	public boolean plant(int acres) {
		if(acresPlanted > 0) // Cannot double-plant a grain.
			return false;
		if(acres<0)
			return false;
		acresPlanted = acres;
		harvested = false;
		return true;
	}
		
    /**
     * Simulates the growth of the grain based on environmental conditions.
     *
     * @param conditions The environmental conditions affecting the grain's growth.
     */
	public void grow(Conditions conditions) {
		this.yieldRatio = conditions.getSoilConditions() * basicYieldRatio;
	}
	
    /**
     * Handles drought conditions for the grain.
     */
	public abstract void drought();
	
    /**
     * Handles pest infestation conditions for the grain.
     *
     * @param pest       The type of pest infestation.
     * @param conditions The environmental conditions affecting the pest infestation.
     */
	public abstract void pestInfestation(Pests pest, Conditions conditions);
	
    /**
     * Handles disease outbreaks for the grain.
     *
     * @param disease    The type of disease outbreak.
     * @param conditions The environmental conditions affecting the disease outbreak.
     */
	public abstract void diseaseOutbreak(Diseases disease, Conditions conditions);
	
    /**
     * Harvests the grain, returning the crop yield.
     *
     * @return The crop yield obtained from harvesting the grain.
     */
	public int harvest() {
		if(harvested)
			return 0;
		int crop =  (int) (Math.round(this.acresPlanted * this.yieldRatio));
		harvested = true;
		acresPlanted = 0;
		return crop;
	}
		
    /**
     * Retrieves the basic yield ratio of the grain.
     *
     * @return The basic yield ratio of the grain.
     */
	protected float getBasicYieldRatio() {
		return basicYieldRatio;
	}

    /**
     * Retrieves the crop failure rate due to bad conditions.
     *
     * @return The crop failure rate due to bad conditions.
     */
	protected float getCropFailureDueToBadConditions() {
		return cropFailureDueToBadConditions;
	}
	
}