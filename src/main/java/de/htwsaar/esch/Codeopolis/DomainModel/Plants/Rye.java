package de.htwsaar.esch.Codeopolis.DomainModel.Plants;

import de.htwsaar.esch.Codeopolis.DomainModel.Plants.Grain.Diseases;
import de.htwsaar.esch.Codeopolis.DomainModel.Plants.Grain.Pests;

/**
 * The `Rye` class represents a specific type of winter grain, rye.
 * It inherits properties and methods from the `WinterGrain` class and defines
 * its own response to drought and disease outbreak.
 */
public class Rye extends WinterGrain{

	/**
     * Constructs a new `Rye` object with predefined characteristics.
     */
	public Rye() {
		super(2, 0.1f, 0.45f);
	}

	/**
     * Overrides the `drought` method from the parent class `WinterGrain` to specify
     * how rye responds to drought conditions by reducing its yield ratio.
     */
    @Override
	public void drought() {
		this.yieldRatio *= 0.95;
	}
	
    /**
     * Overrides the `pestInfestation` method from the parent class `WinterGrain` to specify
     * that rye is not vulnerable to pests, and thus, no reduction in yield ratio occurs.
     *
     * @param pest       The type of pest infestation.
     * @param conditions The environmental conditions affecting grain growth.
     */
    @Override
	public void pestInfestation(Pests pest, Conditions conditions) {
		// Rye is not vulnerable to pests
	}

    /**
     * Overrides the `diseaseOutbreak` method from the parent class `WinterGrain` to specify
     * how rye responds to disease outbreaks, specifically Powdery Mildew, based on
     * winter temperature conditions.
     *
     * @param disease    The type of disease outbreak.
     * @param conditions The environmental conditions affecting grain growth.
     */
    @Override
	public void diseaseOutbreak(Diseases disease, Conditions conditions) {
		switch (disease) {
			case PowderyMildew:
				if(conditions.getAverageTemperatureWinter() > this.getOPTIMAL_WINTER_TEMPERATURE() + 3f){
					this.yieldRatio *= 0.85f;
				}
				else {
					this.yieldRatio *= 0.9f;
				}
				break;
			default:
				break;
		}
	}
	
}
