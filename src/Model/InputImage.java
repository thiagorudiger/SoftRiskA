package Model;

import java.util.ArrayList;

public class InputImage {
	
	Float rate;
	Integer mmfNum;
	String mmfs;
	Integer periods;
	Integer scenariosNum;
	String simulationType;

	ArrayList<String> sucessors;
	ArrayList<String> valuesThrougTime;
	
	public InputImage() {
		this.sucessors = new ArrayList<String>();
		this.valuesThrougTime = new ArrayList<String>();
	}

	public ArrayList<String> getValuesThrougTime() {
		return this.valuesThrougTime;
	}

	public void setValuesThrougTime(ArrayList<String> valuesThrougTime) {
		this.valuesThrougTime = valuesThrougTime;
	}

	public Float getRate() {
		return this.rate;
	}
	
	public void setRate(Float rate) {
		this.rate = rate;
	}
	
	public Integer getMmfNum() {
		return this.mmfNum;
	}
	
	public void setMmfNum(Integer mmfNum) {
		this.mmfNum = mmfNum;
	}
	
	public ArrayList<String> getSucessors() {
		return this.sucessors;
	}
	
	public void setSucessors(ArrayList<String> sucessors) {
		this.sucessors = sucessors;
	}
	
	public String getMmfs() {
		return this.mmfs;
	}

	public void setMmfs(String mmfs) {
		this.mmfs = mmfs;
	}
	
	public Integer getPeriods() {
		return this.periods;
	}

	public void setPeriods(Integer periods) {
		this.periods = periods;
	}

	public Integer getScenariosNum() {
		return this.scenariosNum;
	}

	public void setScenariosNum(Integer scenariosNum) {
		this.scenariosNum = scenariosNum;
	}
	
	public String getSimulationType() {
		return this.simulationType;
	}

	public void setSimulationType(String simulationType) {
		this.simulationType = simulationType;
	}

}
