package action;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.lucene.search.ScoreDoc;

import lucene.Misspelling;
import model.Facade;
import model.FacadeLucene;

public class AzioneRicercaNext extends Azione {

	@Override
	public String esegui(HttpServletRequest request) throws ServletException, IOException {
		HttpSession sessione = request.getSession();
		String query=(String)sessione.getAttribute("querySessione");
		Facade sistema =new FacadeLucene();
		sessione.setAttribute("querySessione", query);	
		ScoreDoc score = (ScoreDoc)(sessione.getAttribute("score"));
		
		String miss = Misspelling.checker(query);
		
		if(!miss.equals(query))
			sessione.setAttribute("misspelling", miss);
		
		try {
			Map<ScoreDoc, List<File>> mappa = sistema.ricercaAfter(score, query);
			sessione.setAttribute("risultati", mappa.values().iterator().next());
			sessione.setAttribute("score", mappa.keySet().iterator().next());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "risultati";
	}
	 

}
