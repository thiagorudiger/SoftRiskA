package Model;

import Distribuicoes.Normal;
import Distribuicoes.Triangular;

public class CFE {
	
	float min;
	float mp;
	float max;
	
	float value;
	
	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}
	
	public CFE(Float min, Float mp, Float max){
		this.min = min;
		this.mp = mp;
		this.max = max;
		
		this.value = value(min, mp, max, "TRIANGULAR");
	}

	public CFE(Float val, String simulationType) {
		
		this.value = value(val, val, val, simulationType);
	}
	
	public CFE(Float prevValue, Float var, Float drift, String simulationType) {
		
		if (simulationType.equals("BROWNIAN_MOTION_G")) {
			this.setValue(valueBMG(prevValue, var, drift));
		} else if (simulationType.equals("BROWNIAN_MOTION_A")) {
			this.setValue(valueBMA(prevValue, var, drift));
		}
		
		if (this.value <= 0.0 ) {
			this.setValue((float) 0.0);
		}
	}
	
	public Float value(Float min, Float mp, Float max, String simulationType) {
		
		if (simulationType.equals("CONSTANT")) {
			//Triangular myTriangular = new Triangular(min, mp, max);
			//return Float.valueOf(String.valueOf(myTriangular.getSample()));
			return Float.valueOf(mp);
			
		} else if (simulationType.equals("TRIANGULAR")) {
			Triangular myTriangular = new Triangular(min, mp, max);
			return Float.valueOf(String.valueOf(myTriangular.getSample()));
			
		} else if ((simulationType.equals("BROWNIAN_MOTION_G")) || (simulationType.equals("BROWNIAN_MOTION_A"))) {
			return Float.valueOf(mp);
			
		} else {
			// Error!
			return null;
		}
	}
	
	private Float valueBMA(Float prevValue, Float var, Float drift) {
		Normal myNormal = new Normal();
		//V(t+1) = V(t) + (<sigma>*V(t)*N(0,1)) + <drift>
		return prevValue + (var*prevValue)*(Float.valueOf(String.valueOf(myNormal.getSample()))) + drift;
	}
	
	public Float valueBMG(Float prevValue, Float var, Float drift){
		Normal myNormal = new Normal();
		//V(t+1) = V(t) + (<sigma>*V(t)*N(0,1)) + <Drift>*V(t)
		return prevValue + (var*prevValue)*(Float.valueOf(String.valueOf(myNormal.getSample()))) + drift*prevValue;
	}
}
