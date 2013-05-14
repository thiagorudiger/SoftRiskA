package Support;

import java.util.ArrayList;

import Model.MMF;
import Model.Rate;

public class NPV {
	
	public static Rate rate;

	public NPV(Rate r){
		NPV.rate = r;
	}
	
	public double npvMMF(MMF mmf, int start, int periods) {
		//=VALOR/(1+TAXA)^TEMPO
		double sum = 0.0;
		int iShift = 0;
		
		for (int i = 0; i < periods; i++) {
			if (i < start) {
				sum += 0;
			} else {
				//sum += mmf.getCashFlowStream().get(iShift).getValue() / Math.pow((rate.getR() + 1), i+1);
				iShift++;
			}
			
		}
		
		return sum;
	}
	
	public double npvSequence(ArrayList<MMF> sequence, int p) {
		
		double sum = 0.0;
	
		for (int i = 0; i < sequence.size(); i++) {
			sum += npvMMF(sequence.get(i), i,p);
			
		}
		
		return sum;
		
	}
}
