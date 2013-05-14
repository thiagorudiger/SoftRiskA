package Model;

import java.util.ArrayList;
import java.util.Collections;

public class Sequence {
	
	ArrayList<MMF> sequence;
	ArrayList<Double> npvs = new ArrayList<Double>();
	
	public ArrayList<Double> getNpvs() {
		return this.npvs;
	}

	public void setNpvs(ArrayList<Double> npvs) {
		this.npvs = npvs;
	}

	public ArrayList<MMF> getSequence() {
		return sequence;
	}

	public void setSequence(ArrayList<MMF> sequence) {
		this.sequence = sequence;
	}
	
	public void calculateNPV(Integer scenariosNum, String simulationType) {
		
		ArrayList<Double> npvs = new ArrayList<Double>(); 		
		
		if (simulationType.equals("CONSTANT")) {
			Double npv = new Double(0);
			
			for (int i = 0; i < this.sequence.size(); i++) {
				npv += this.sequence.get(i).getCashFlowStreams().get(0).getNpv();
			}

			npvs.add(npv);
			this.setNpvs(npvs);
			
		} else if (simulationType.equals("TRIANGULAR")) {
			for (int j = 0 ; j < scenariosNum ; j++) {
				Double npv = new Double(0);
			
				for (int i = 0; i < this.sequence.size(); i++) {
					npv += this.sequence.get(i).getCashFlowStreams().get(j).getNpv();
				}

				npvs.add(npv);
			}
			
			this.setNpvs(npvs);
			
		} else if ((simulationType.equals("BROWNIAN_MOTION_G")) || (simulationType.equals("BROWNIAN_MOTION_A"))) {
			for (int j = 0 ; j < scenariosNum ; j++) {
				Double npv = new Double(0);
			
				for (int i = 0; i < this.sequence.size(); i++) {
					npv += this.sequence.get(i).getCashFlowStreams().get(j).getNpv();
				}

				npvs.add(npv);
			}
			
			this.setNpvs(npvs);
			
		} else {
			//Error - Todo
		}
	}
	
	public void sortSequence() {
		
		Collections.sort(this.npvs);
		
	}
}
