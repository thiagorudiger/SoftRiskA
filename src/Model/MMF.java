package Model;

import java.util.ArrayList;

public class MMF {
	
	String name;
	ArrayList<CashFlowStream> cashFlowStreams;
	MMF sucessor;
	Float var;
	Float drift;
	
	public Float getVar() {
		return var;
	}

	public void setVar(Float var) {
		this.var = var;
	}

	public Float getDrift() {
		return drift;
	}

	public void setDrift(Float drift) {
		this.drift = drift;
	}

	public MMF(String dependency) {
		this.name = dependency;
		this.cashFlowStreams = new ArrayList<CashFlowStream>();		
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<CashFlowStream> getCashFlowStreams() {
		return cashFlowStreams;
	}
	
	public void setCashFlowStreams(ArrayList<CashFlowStream> cashFlowStream) {
		this.cashFlowStreams = cashFlowStream;
	}
	
	public MMF getSucessor() {
		return sucessor;
	}
	
	public void setSucessor(MMF sucessor) {
		this.sucessor = sucessor;
	}
}
