package Model;

public class LinhaOutput {
	int cenario;
	String sequencia;
	double npv;
	
	public LinhaOutput(int cenario, String sequencia, double npv){
	
		this.cenario = cenario;
		this.sequencia = sequencia;
		this.npv = npv;
	}
	
	public int getCenario() {
		return cenario;
	}
	public void setCenario(int cenario) {
		this.cenario = cenario;
	}
	public String getSequencia() {
		return sequencia;
	}
	public void setSequencia(String sequencia) {
		this.sequencia = sequencia;
	}
	public double getNpv() {
		return npv;
	}
	public void setNpv(double npv) {
		this.npv = npv;
	}
	
	
}
