package de.htwsaar.esch.Codeopolis.DomainModel.Plants;

/**
 * The abstract class `SpringGrain` represents a type of grain that is typically grown in spring.
 * It inherits properties and methods from the `Grain` class and adds specific behavior related to
 * its growth based on environmental conditions.
 */
public abstract class SpringGrain extends Grain{

	// The optimal summer temperature for spring grains.
	private final float OPTIMAL_SUMMER_TEMPERATURE = 18f;
	
	// The heat resistance factor of the spring grain.
	private float heatResistance;
	
    /**
     * Constructs a new `SpringGrain` object with the specified parameters.
     *
     * @param basicYieldRatio            The basic yield ratio of the grain.
     * @param cropFailureDueToBadConditions The crop failure due to bad conditions.
     * @param heatResistance             The heat resistance factor of the grain.
     */
	public SpringGrain(float basicYieldRatio, float cropFailureDueToBadConditions, float heatResistance) {
		super(basicYieldRatio, cropFailureDueToBadConditions);
		this.heatResistance = heatResistance;
	}
	
    /**
     * Overrides the `grow` method from the parent class `Grain` to incorporate the effects of
     * heat resistance on spring grain growth.
     *
     * @param conditions The environmental conditions affecting grain growth.
     */
    @Override
	public void grow(Conditions conditions) {
		super.grow(conditions);
		if(conditions.getAverageTemperatureSummer() > OPTIMAL_SUMMER_TEMPERATURE * (1+heatResistance))
			this.yieldRatio *= (1 - this.getCropFailureDueToBadConditions());
	}

    /**
     * Gets the optimal summer temperature for spring grains.
     *
     * @return The optimal summer temperature.
     */
	protected float getOPTIMAL_SUMMER_TEMPERATURE() {
		return OPTIMAL_SUMMER_TEMPERATURE;
	}

	 /**
     * Gets the heat resistance factor of the spring grain.
     *
     * @return The heat resistance factor.
     */
	protected float getHeatResistance() {
		return heatResistance;
	}
}
