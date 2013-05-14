package Distribuicoes;

/**
 * Represents the triangular distribution.
 * This distribuion has:
 * mean: (min + 4*top + max) / 6
 * 
 * @author nando
 */
public class Triangular extends Sampleble {

	private double min;
	private double top;
	private double max;

	public Triangular(double minimumValue, double moreProbable, double maximumValue) {
		min = minimumValue;
		top = moreProbable;
		max = maximumValue;
		
//		System.out.println("triangular:" + min + "-" + top + "-" + max);
	}

	@Override
	public double getSample() {
		double u0 = r.nextDouble();
		
		if (top > 0) {
			if (u0 < (top - min) / (max - min)) {
				return min + Math.sqrt(u0 * (top - min) * (max - min));
			} else {
				return max - Math.sqrt((1 - u0) * (max - min) * (max - top));
			}
		} else {
			if (u0 < (Math.abs(top) - Math.abs(min)) / (Math.abs(max) - Math.abs(min))) {
				return (-1)*(Math.abs(min) + Math.sqrt(u0 * (Math.abs(top) - Math.abs(min)) * (Math.abs(max) - Math.abs(min))));
			} else {
				return (-1)*(Math.abs(max) - Math.sqrt((1 - u0) * (Math.abs(max) - Math.abs(min)) * (Math.abs(max) - Math.abs(top))));
			}
		}
	}

}
