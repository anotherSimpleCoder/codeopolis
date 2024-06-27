package de.htwsaar.esch.Codeopolis.DomainModel.Plants;

/**
 * The `Corn` class represents a specific type of spring grain, corn.
 * It inherits properties and methods from the `SpringGrain` class and defines
 * its own response to drought, pest infestation, and disease outbreak.
 */
public class Corn extends SpringGrain{

	/**
	 * Constructs a new `Corn` object with predefined characteristics.
	 */
	public Corn() {
		super(4f, 0.2f, 0.25f);
	}

	/**
	 * Overrides the `drought` method from the parent class `SpringGrain` to specify
	 * how corn responds to drought conditions by reducing its yield ratio.
	 */
	@Override
	public void drought() {
		this.yieldRatio *= 0.7;
	}

	/**
	 * Overrides the `pestInfestation` method from the parent class `SpringGrain` to specify
	 * how corn responds to pest infestation, specifically Frit Fly, by reducing its yield ratio.
	 *
	 * @param pest       The type of pest infestation.
	 */
	@Override
	public void pestInfestation(Pests pest) {
		switch(pest) {
			case FritFly:
				this.yieldRatio *= 0.6f;
				break;
			default:
				break;
		}
	}

	/**
	 * Overrides the `diseaseOutbreak` method from the parent class `SpringGrain` to specify
	 * how corn responds to disease outbreaks, specifically Powdery Mildew, by reducing its yield ratio.
	 *
	 * @param disease    The type of disease outbreak.
	 */
	@Override
	public void diseaseOutbreak(Diseases disease) {
		switch (disease) {
			case PowderyMildew:
				this.yieldRatio *= 0.75f;
				break;
			default:
				break;
		}
	}


}
