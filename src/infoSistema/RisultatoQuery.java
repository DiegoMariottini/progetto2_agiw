package infoSistema;

public class RisultatoQuery {
	
	static double precision;
	static double recall;
	static double avarangePrecision;
	static double[] precisionPerValoriRecall;
	
	
	public RisultatoQuery(double precision,double recall,double avarangePrecision,double[] precisionPerValoriRecall) {
		this.avarangePrecision=avarangePrecision;
		this.precision=precision;
		this.precisionPerValoriRecall= precisionPerValoriRecall;
		this.recall=recall;
	
	}
	
	public double getAvarangePrecision() {
		return avarangePrecision;
	}
	public double getPrecision() {
		return precision;
	}
	public double[] getPrecisionPerValoriRecall() {
		return precisionPerValoriRecall;
	}
	public double getRecall() {
		return recall;
	}
	
}
