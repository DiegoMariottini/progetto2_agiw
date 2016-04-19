package init;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import lucene.Indexer;
import lucene.IndexerSpell;
import lucene.PatterAssolut;
import lucene.TextFileFilter;

public class InitIndex {

	static String dizionario = PatterAssolut.getDizionario();
	static String indexDir = PatterAssolut.getIndexDir();
	static String indexDizionario = PatterAssolut.getIndexDizionario();
	static String dataDir = PatterAssolut.getDataDir();

	public static void main(String[] args) throws IOException {

		List<String> nomi=new ArrayList<String>(creaListaCartelle() );
		//Lo devo iterare per ogni cartella
			try{
				IndexerSpell indexerSpell = new IndexerSpell(indexDizionario);
				indexerSpell.createIndex(dizionario, new TextFileFilter());
				indexerSpell.close();
				for(String nome:nomi){
					String nomeCartella=nome;
					File f = new File (dataDir+nomeCartella);
					if(f.exists()){
						Indexer indexer = new Indexer(indexDir);
						int numIndexed;
						long startTime = System.currentTimeMillis();
						numIndexed = indexer.createIndex(dataDir+nomeCartella, new TextFileFilter());
						long endTime = System.currentTimeMillis();
						indexer.close();
						//System.out.println(numIndexed+" File indexed, time taken: "
						//		+(endTime-startTime)+" ms");	
						//System.out.println(f.listFiles().length);
						
						//System.out.println(nome);
					}
				}
				System.out.println("Finito");
					
			}catch(IOException e)	{
				e.printStackTrace();
				
				}
			}
	

	public static List<String> creaListaCartelle(){

		LinkedList<String> listaNomi = new LinkedList<String>();
		
		String fileNomiCaretelle=PatterAssolut.getDoveSiTrovaIlFileNomiCartelle();
		
		Scanner readerNomiCartelle;
		try {
			readerNomiCartelle = new Scanner(new FileReader(fileNomiCaretelle));
			while(readerNomiCartelle.hasNext())	{
				String persona= readerNomiCartelle.nextLine();
				String personaSenzaSpazi=persona.replaceAll(" ","_");
				personaSenzaSpazi = personaSenzaSpazi.replace("\"", "");
				char asc27=27;
				personaSenzaSpazi = personaSenzaSpazi.replace(""+asc27, "");
				personaSenzaSpazi=personaSenzaSpazi.substring(0,(personaSenzaSpazi.length()));
				listaNomi.add(personaSenzaSpazi);
			}
			readerNomiCartelle.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return listaNomi;
	}

}
