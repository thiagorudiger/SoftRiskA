package Distribuicoes;

/**
 * Gaussian (Normal) 0-1 distribution
 *  
 * @author nando
 */
public class Normal extends Sampleble {

	@Override
	public double getSample() {
		return r.nextGaussian();
	}

}
