package de.htwsaar.esch.Codeopolis.DomainModel.Plants;

/**
 * The `Barley` class represents a specific type of winter grain, barley.
 * It inherits properties and methods from the `WinterGrain` class and defines
 * its own response to drought, pest infestation, and disease outbreak.
 */
public class Barley extends WinterGrain{

	/**
     * Constructs a new `Barley` object with predefined characteristics.
     */
	public Barley() {
		super(4, 0.2f, 0.25f);
	}

	/**
     * Overrides the `drought` method from the parent class `WinterGrain` to specify
     * how barley responds to drought conditions by reducing its yield ratio.
     */
    @Override
	public void drought() {
		this.yieldRatio *= 0.8;
	}
	
    /**
     * Overrides the `pestInfestation` method from the parent class `WinterGrain` to specify
     * how barley responds to pest infestation, specifically Barley Gout Fly, by reducing its yield ratio.
     *
     * @param pest       The type of pest infestation.
     * @param conditions The environmental conditions affecting grain growth.
     */
    @Override
	public void pestInfestation(Pests pest, Conditions conditions) {
		switch (pest) {
		case BarleyGoutFly:
			this.yieldRatio *= 0.6;
			break;

		default:
			break;
		}
	}

    /**
     * Overrides the `diseaseOutbreak` method from the parent class `WinterGrain` to specify
     * how barley responds to disease outbreaks, specifically Fusarium, by reducing its yield ratio.
     *
     * @param disease    The type of disease outbreak.
     * @param conditions The environmental conditions affecting grain growth.
     */
    @Override
	public void diseaseOutbreak(Diseases disease, Conditions conditions) {
		switch (disease) {
			case Fusarium:
				this.yieldRatio *= 0.75f;
				break;
			default:
				break;
		}
	}
}
