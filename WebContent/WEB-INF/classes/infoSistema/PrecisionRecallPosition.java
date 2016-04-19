package infoSistema;

import java.util.List;

public class PrecisionRecallPosition implements Comparable<PrecisionRecallPosition>{
	static double precision;
	static double recall;
	static int position;
	
	public PrecisionRecallPosition(double precisionPassata,double recallPassata,int posizionePassata) {
	precision=precisionPassata;
	recall=recallPassata;
	position=posizionePassata;
	}
	
	//ordiniamo per precision
	public int hashCode() {
		double codice;
		codice=this.getPrecision()*1000;
		codice=(codice+this.getRecall())*1000;
		codice=(codice+this.getPosition())*1000;
		int result=(int) codice;
		
		
		
		return result;
	
		
	}
	@Override
	public int compareTo(PrecisionRecallPosition that){
		return (int)(that.precision*10000000-this.precision*10000000);
		
	}
	
	
	@Override
	public String toString() {
		String visualizzata=""+precision+"\t"+recall+"\t"+position;
		return visualizzata;
	}
	
	
	
	
	
	public int getPosition() {
		return position;
	}
	public double getPrecision() {
		return precision;
	}
	public double getRecall() {
		return recall;
	}
	
	
	
}
