package lucene;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;



public class Misspelling {

	static String path=PatterAssolut.getDirDizionario();
	static String dizionario = path+"Dizionario - nomi/";

	
	public static String checker(String query) throws IOException	{
	
		String queryModificata=query;
		//modificato
		
		//elimino tutti i doppi spazi
		while(queryModificata.contains("  ")){
			queryModificata=queryModificata.replace("  ", " ");
		}
		
		//tolgo tutti gli enter
		char asc13=13;
		queryModificata=queryModificata.replaceAll(""+asc13, "");
		
		//tolgo tutti gli spazi finali
		
		
		
		
		
		int lunghezzaQueryModificata=queryModificata.length()-1;
		
		char nonFinale=' ';
		
		while(queryModificata.charAt(lunghezzaQueryModificata)==nonFinale){
			lunghezzaQueryModificata=lunghezzaQueryModificata-1;
			}
		
		queryModificata=queryModificata.substring(0, lunghezzaQueryModificata+1);
		query=queryModificata;
		
		
		
		
		
		Directory cartellaDizionario = FSDirectory.open(Paths.get(path+"IndexSpell/"));
		SpellChecker spell= new SpellChecker(cartellaDizionario);
		Analyzer anal = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(anal);
		
		Directory cartellaIndice = FSDirectory.open(Paths.get(path+"IndexDizionario/"));
		IndexReader ir = DirectoryReader.open(cartellaIndice);
		String fieldName = "contents";
		LuceneDictionary dict = new LuceneDictionary( ir, fieldName );
		spell.indexDictionary(dict, config, false);



		String[] parole = parsing(query);
		String[] suggerimenti = new String[parole.length];
		int i =0;
		for(String s : parole)	{
			if(spell.suggestSimilar(s, 1).length!=0){
				String tmp = spell.suggestSimilar(s, 1)[0];
				suggerimenti[i] = tmp;
			}
			else
				suggerimenti[i] = s;
			i++;
		}
		spell.close();
		
		return array2String(suggerimenti);
		
	}

	private static String[] parsing(String query){
		//nessuno spazio nella stringa: una parola
		//numero spazi+1 = numero parole
		int numeroParole = StringUtils.countMatches(query, " ")+1;
		String queryLocale = new String(query);
		String[] parsingQuery = new String[numeroParole];
		int i = 0;
		String parola = null;
		while((!queryLocale.equals(""))&&(i<parsingQuery.length))	{
			int indexSpazio = queryLocale.indexOf(" ");
			if(indexSpazio!=-1)	{
				parola = queryLocale.substring(0, indexSpazio);
				queryLocale = queryLocale.substring(indexSpazio+1);
			}
			else
				parola = queryLocale;
			parsingQuery[i] = parola;
			i++;
		}
		return parsingQuery;

	}

	private static String array2String(String[] a)	{

		String s = new String();
		for(String i : a)	{
			s = s.concat(i);
			s = s.concat(" ");
		}		

		return s;
	}





}
