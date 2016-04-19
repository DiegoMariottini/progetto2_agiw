package infoSistema;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import lucene.LuceneConstants;
import lucene.LuceneTester;
import lucene.PatterAssolut;
import lucene.Searcher;
import java.util.ArrayList;

public class ValutazioneSistema {

	static String fileValutazione=PatterAssolut.getDirValutazione()+"valutazione.txt";
	static String fileNomiCaretelle=PatterAssolut.getDoveSiTrovaIlFileNomiCartelle();


	public static void main(String[] args) {
		String dataDir=PatterAssolut.getDataDir();

		try {
			PrintWriter writerValutazione = new PrintWriter(fileValutazione, "UTF-8");
			String frase="nome e cognome \t  positivi totali  \t  file totali restituiti \t Doc.restituiti  \t  precision  \t  recall \t averagePrecision \t";
			for(int i=0; i<11;i++){
				frase = frase + "precision con recall"+(0.1*i)+"\t"; 
			}

			writerValutazione.println(frase);

			//writerValutazione.println("");
			List<String> nomi=new ArrayList<String>(creaListaCartelle());
			List<String> nomiCartella=new ArrayList<String>();
			List<RisultatoQuery> risultatiQuery=new LinkedList<RisultatoQuery>();
			List<Double> avarangePrecisions=new LinkedList<Double>();

			for(String nome:nomi){
				String nomeCartella=nome.replaceAll(" ","_");
				nomeCartella = nomeCartella.replace("\"", "");
				char asc27=27;
				nomeCartella = nomeCartella.replace(""+asc27, "");
				nomeCartella=nomeCartella.substring(0,(nomeCartella.length()));
				File f = new File (dataDir+nomeCartella);
				if(f.exists()){
					nomiCartella.add(nomeCartella);
					int relevantElementsInt=f.listFiles().length;
					int selectedElementsInt=searchForValuation(nome);
					List<Integer> positionPositiv= positiviRestituiti(nome, nomeCartella);

					double relevantElement=relevantElementsInt;
					double selectedElements=selectedElementsInt;
					double truePositiv=positionPositiv.size();

					double precision= truePositiv/selectedElements;
					double recall=truePositiv/relevantElement;


					//ranking
					List<PrecisionRecallPosition> pApforPage= pApPaginaPerPagina(relevantElementsInt,selectedElementsInt,positionPositiv);
					double avarenagePrecisionQuery= AvarangePrecision(pApforPage,selectedElementsInt);
					//applicata anche interpolazione 
					double[] precisionPerValoriRecall= creaRecallToPrecision(pApforPage);


					//mettere i risultati in lista
					risultatiQuery.add(new RisultatoQuery(precision, recall, avarenagePrecisionQuery, precisionPerValoriRecall));
					avarangePrecisions.add(avarenagePrecisionQuery);

					//salvataggio su file esterno
					frase=nome+"\t"+relevantElement+"\t"+selectedElements+"\t"+truePositiv+"\t"+precision+"\t"+recall+"\t"+avarenagePrecisionQuery+"\t";
					for(double i: precisionPerValoriRecall){
						frase=frase+"\t"+i;
					}

					writerValutazione.println(frase);

				}
			}



			writerValutazione.println();
			writerValutazione.println();
			writerValutazione.println();


			writerValutazione.println("Il valore di Map: \t "+MediaAvarangePrecision(avarangePrecisions));
			writerValutazione.println();

			writerValutazione.println("La precision media : \t "+mediaPrecision(risultatiQuery));
			writerValutazione.println();

			writerValutazione.println("La recall media : \t "+mediaRecall(risultatiQuery));
			writerValutazione.println();



			frase="il variare della precision al variare della recall di 0.1 è uguale a:";
			writerValutazione.println(frase);
			frase="";
			double [] precisionPerValoriRecall=mediaRecallToPrecision(risultatiQuery);
			for(double i: precisionPerValoriRecall){
				frase=frase+i+"\t";
			}
			writerValutazione.println(frase);



			writerValutazione.close();
			System.out.println("Finito");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}



	private static double mediaRecall(List<RisultatoQuery> risultatiQuery) {
		int quantiRisultatiConsidero=risultatiQuery.size();
		double Somma=0;
		for(RisultatoQuery query:risultatiQuery){
			double recallQuery=query.getRecall();
			Somma=Somma+recallQuery;
		}
		double media=Somma/quantiRisultatiConsidero;
		return media;
	}



	private static double mediaPrecision(List<RisultatoQuery> risultatiQuery) {
		int quantiRisultatiConsidero=risultatiQuery.size();
		double Somma=0;
		for(RisultatoQuery query:risultatiQuery){
			double precisionQuery=query.getPrecision();
			Somma=Somma+precisionQuery;
		}
		double media=Somma/quantiRisultatiConsidero;
		return media;
	}



	public static List<Integer> positiviRestituiti(String nome,String nomeCartella){
		int positivi=0;
		List<Integer> posizionePositivi= new LinkedList<Integer>();

		try {
			List<File> fileRestituitiLucene= fileTotali(nome);
			List<File> filePositiviLucene= new LinkedList<File>();
			int posizioni=0;
			for(File file :fileRestituitiLucene){
				posizioni=posizioni+1;
				if(isPositive(file,nomeCartella) && (!(filePositiviLucene.contains(file)))){
					filePositiviLucene.add(file);
					posizionePositivi.add(posizioni);
				}


			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return posizionePositivi;

	}

	public static boolean isPositive(File file,String nomeCartella){
		String path=PatterAssolut.getDataDir();
		String stringaDaConsiderare=file.getPath().substring(path.length());
		return stringaDaConsiderare.contains(nomeCartella+"/");
	}
	public static List<File> fileTotali(String nome) throws ParseException, IOException{
		LuceneTester tester=new LuceneTester();
		Map<ScoreDoc, List<File>> mappaScoreToListaFile = tester.search(nome);
		LinkedList<File> files = (LinkedList<File>) mappaScoreToListaFile.values().iterator().next();
		ScoreDoc score=(ScoreDoc) mappaScoreToListaFile.keySet().iterator().next();

		LinkedList<File> nuoviDaAggiungere = (LinkedList<File>)tester.searchAfter(score, nome).values().iterator().next();

		while(!(nuoviDaAggiungere.isEmpty())){
			files.addAll(nuoviDaAggiungere);
			mappaScoreToListaFile=tester.searchAfter(score, nome);

			score=(ScoreDoc) mappaScoreToListaFile.keySet().iterator().next();
			nuoviDaAggiungere =(LinkedList<File>)mappaScoreToListaFile.values().iterator().next();

		}

		return files;

	}

	private static int searchForValuation(String searchQuery) throws IOException, ParseException{
		Searcher searcher = new Searcher(PatterAssolut.getIndexDir());
		TopDocs hits = searcher.search(searchQuery);
		return hits.totalHits;
	}

	public static List<String> creaListaCartelle(){

		LinkedList<String> listaNomi = new LinkedList<String>();

		//String fileNomiCaretelle=PatterAssolut.getDoveSiTrovaIlFileNomiCartelle();

		Scanner readerNomiCartelle;
		try {
			readerNomiCartelle = new Scanner(new FileReader(fileNomiCaretelle));
			while(readerNomiCartelle.hasNext())	{
				String persona= readerNomiCartelle.nextLine();
				listaNomi.add(persona);
			}
			readerNomiCartelle.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listaNomi;
	}

	//calcolo grafico recall precision

	//per valori standard di recall da 0 a 1 per ogni 0,1
	public static double[] creaRecallToPrecision(List<PrecisionRecallPosition> pApPerPagina){
		Collections.sort(pApPerPagina);
		double[] risultato=new double[11];

		for(int i=0;i<11;i++){
			double recallStandard=0.1*i;
			double precisioneMassima=0;

			Iterator<PrecisionRecallPosition> iteratore=pApPerPagina.iterator();
			boolean nonTrovata=true;
			PrecisionRecallPosition tmp=null;
			while(iteratore.hasNext() && nonTrovata){
				tmp= iteratore.next();
				nonTrovata=tmp.getRecall()<recallStandard;
				if(! nonTrovata){
					precisioneMassima=tmp.getPrecision();

				}


			}
			risultato[i]=precisioneMassima;
		} 
		return risultato;

	}

	public static double[] mediaRecallToPrecision(List<RisultatoQuery> risultatiQuery){
		int quantiRisultatiConsidero=risultatiQuery.size();
		double[] risultati=new double[11]; 
		for(int i=0 ;i < 11; i++){
			double Somma=0;	
			for(RisultatoQuery query:risultatiQuery){
				double[] precisionPerValoriRecall=query.getPrecisionPerValoriRecall();
				Somma=Somma+precisionPerValoriRecall[i];
			}
			double media=Somma/quantiRisultatiConsidero;
			risultati[i]=media;
		}
		return risultati;

	}

	//calcolo MAP

	public static double MediaAvarangePrecision(List<Double> avarangePrecisions){
		double somma=0;
		int query=0;
		for(double avarangePrecision : avarangePrecisions){
			somma=somma+avarangePrecision;
			query=query+1;
		}
		return (somma/query);
	}

	public static double AvarangePrecision(List<PrecisionRecallPosition> pApPaginaPerPagina,int selectedElements){
		double sommaPrecision=0;
		for(PrecisionRecallPosition pAp :pApPaginaPerPagina){
			sommaPrecision=sommaPrecision+pAp.getPrecision();			
		}
		return (sommaPrecision/pApPaginaPerPagina.size());
	}

	public static List<PrecisionRecallPosition> pApPaginaPerPagina(int relevantElements,int selectedElements,List<Integer>positionPositiv){
		List<PrecisionRecallPosition> tabellaValori=new ArrayList<PrecisionRecallPosition>();
		//		 double[][] tabellaValori=new double[selectedElements][2];
		int positiviRestituiti=0;
		for(int i=1;i<selectedElements;i++){

			//p@p
			if(positionPositiv.contains(i)){
				positiviRestituiti=positiviRestituiti+1;
			}
			int positiviCheDovevaRestituireAQuellaPosizione=Math.min(relevantElements, i);

			double positiviRestituitiDouble=positiviRestituiti;
			double positiviCheDovevaRestituireAQuellaPosizioneDouble=positiviCheDovevaRestituireAQuellaPosizione;
			double iDouble= i+1;

			double precision= positiviRestituitiDouble/positiviCheDovevaRestituireAQuellaPosizioneDouble;

			double recall=positiviRestituitiDouble/iDouble;
			PrecisionRecallPosition elemento=new PrecisionRecallPosition(precision,recall,i);
			tabellaValori.add(elemento);
			//			 tabellaValori[i][0]=precision;
			//			 tabellaValori[i][1]=recall;

		}
		return tabellaValori;
	}
}
