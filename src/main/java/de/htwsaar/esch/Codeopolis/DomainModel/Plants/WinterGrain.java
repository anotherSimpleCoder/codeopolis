package de.htwsaar.esch.Codeopolis.DomainModel.Plants;

/**
 * The abstract class `WinterGrain` represents a type of grain that is typically grown in winter.
 * It inherits properties and methods from the `Grain` class and adds specific behavior related to
 * its growth based on environmental conditions during winter.
 */
public abstract class WinterGrain extends Grain {

	// The optimal winter temperature for winter grains.
	private final float OPTIMAL_WINTER_TEMPERATURE = 3.3f;
	
	// The cold resistance factor of the winter grain.
	private final float coldResistence;
	
    /**
     * Constructs a new `WinterGrain` object with the specified parameters.
     *
     * @param basicYieldRatio            The basic yield ratio of the grain.
     * @param cropFailureDueToBadConditions The crop failure due to bad conditions.
     * @param coldResistence             The cold resistance factor of the grain.
     */
	public WinterGrain(float basicYieldRatio, float cropFailureDueToBadConditions, float coldResistence) {
		super(basicYieldRatio, cropFailureDueToBadConditions);
		this.coldResistence = coldResistence;
	}
	
    /**
     * Overrides the `grow` method from the parent class `Grain` to incorporate the effects of
     * cold resistance on winter grain growth.
     *
     * @param conditions The environmental conditions affecting grain growth.
     */
    @Override
	public void grow(Conditions conditions) {
		super.grow(conditions);
		if(conditions.getAverageTemperatureWinter() < OPTIMAL_WINTER_TEMPERATURE * (1-coldResistence))
			this.yieldRatio *= (1 - this.getCropFailureDueToBadConditions());

	}

    /**
     * Gets the optimal winter temperature for winter grains.
     *
     * @return The optimal winter temperature.
     */
	protected float getOPTIMAL_WINTER_TEMPERATURE() {
		return OPTIMAL_WINTER_TEMPERATURE;
	}

    /**
     * Gets the cold resistance factor of the winter grain.
     *
     * @return The cold resistance factor.
     */
	protected float getColdResistence() {
		return coldResistence;
	}
}
