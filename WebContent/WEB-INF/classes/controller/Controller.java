package controller;

import java.io.IOException;
import java.util.*;

import action.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Controller
 */
@WebServlet(urlPatterns={"/FrontController"})
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Map<String, String> comando2azione; 
	private Map<String, String> esito2pagina; 

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String prossimaPagina = "/errori/error.jsp";
		ServletContext application  = getServletContext();
		RequestDispatcher rd = application.getRequestDispatcher(prossimaPagina);
		rd.forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String prossimaPagina = null;
		//leggo il comando
		String comando = this.leggiComando(request.getServletPath());
		//vedo quale azione fare
		String nomeAzione = this.comando2azione.get(comando);

		//se nome azione è vuoto
		if (nomeAzione==null) {
			prossimaPagina = "/errori/error.jsp";
		}
		else {
			Azione azione = null;
			try {
				azione = (Azione)Class.forName(nomeAzione).newInstance();
				String esitoAzione = azione.esegui(request);
				prossimaPagina = this.esito2pagina.get(esitoAzione);
			} catch (InstantiationException e) {
				prossimaPagina = "/errori/error.jsp";
			} catch (IllegalAccessException e) {
				prossimaPagina = "/errori/error.jsp";
			} catch (ClassNotFoundException e) {
				prossimaPagina = "/errori/error.jsp";
			}
		}

		ServletContext application = getServletContext();
		RequestDispatcher rd = application.getRequestDispatcher(prossimaPagina);
		rd.forward(request, response);		
	}   	  	    

	//leggi comando permette di leggere il comando e vedere poi a quale comando appartiene
	private String leggiComando(String servletPath) {
		StringBuffer str = new StringBuffer(servletPath);
		return str.substring(1,str.lastIndexOf(".do"));
	}

	// dalla stringa del comando deve corrispondere il file che si trova nel src di java
	//i java le devo anche mettere in WEB-INF-classes-<qualsiasiCartella>
	public void init() {
		this.comando2azione = new HashMap<String, String>();
		this.comando2azione.put("ricerca","action.AzioneRicerca" );
		this.comando2azione.put("ricercaAfter","action.AzioneRicercaNext" );

		//il jsp a cui sono collegati i vari comandi partendo da WebContent
		this.esito2pagina= new HashMap<String, String>();
		this.esito2pagina.put("errore", "/errori/error.jsp");
		this.esito2pagina.put("risultati", "/risultati.jsp");



	}

}
