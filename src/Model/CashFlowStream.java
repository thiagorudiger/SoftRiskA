package Model;

import java.util.ArrayList;

import Support.NPV;

public class CashFlowStream {
	double npv;
	ArrayList<CFE> cashFlowStream;

	public CashFlowStream(ArrayList<CFE> cashFlowStream) {
		//this.cashFlowStream = cashFlowStream;
		this.npv = (double) (new Double(0));
	}

	public Double getNpv() {
		return this.npv;
	}

	public void setNpv(Double npv) {
		this.npv = npv;
	}

	public ArrayList<CFE> getCashFlowStream() {
		return this.cashFlowStream;
	}

	public void setCashFlowStream(ArrayList<CFE> cashFlowStream) {
		this.cashFlowStream = cashFlowStream;
	}

	public void calculateNPV(int periods, Rate r, String simulationType, int iMMF) {
		
		double sum = 0.0;
		NPV myNPV = new NPV(r);
		
		//=VALOR/(1+TAXA)^TEMPO
		
		if (simulationType.equals("CONSTANT")) {
			for (int i = 0; i < periods; i++) {
				if (iMMF > i) {
					sum += 0 / Math.pow((r.getR() + 1), i+1);
				} else {
					sum += this.cashFlowStream.get(i-iMMF).getValue() / Math.pow((r.getR() + 1), i+1);
				}
			}
			this.setNpv(new Double(sum));
		} else if (simulationType.equals("TRIANGULAR")) {
			for (int i = 0; i < periods; i++) {
				if (iMMF > i) {
					sum += 0 / Math.pow((r.getR() + 1), i+1);
				} else {
					sum += this.cashFlowStream.get(i-iMMF).getValue() / Math.pow((r.getR() + 1), i+1);
				}
			}
			this.setNpv(new Double(sum));
		} else if ((simulationType.equals("BROWNIAN_MOTION_G")) || (simulationType.equals("BROWNIAN_MOTION_A"))) {
			for (int i = 0; i < periods; i++) {
				if (iMMF > i) {
					sum += 0 / Math.pow((r.getR() + 1), i+1);
				} else {
					sum += this.cashFlowStream.get(i-iMMF).getValue() / Math.pow((r.getR() + 1), i+1);
				}
			}
			this.setNpv(new Double(sum));
		}
	}
}
