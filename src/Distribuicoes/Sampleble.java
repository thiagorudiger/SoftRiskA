package Distribuicoes;

import java.util.Random;

/**
 * The interface for all sampable distributions. 
 * 
 * @author nando
 */
public abstract class Sampleble {

	protected Random r;

	public Sampleble() {
		r = new Random();
	}

	public void setSeed(long seed) {
		r = new Random(seed);
	}

	/**
	 * Must be implemented by the concrete distributions.
	 * 
	 * @return A sample of the distribution.
	 */
	public abstract double getSample();

}
