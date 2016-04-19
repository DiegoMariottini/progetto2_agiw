package lucene;

public class PatterAssolut {

	//lanciare le pagine in localhost:3000
	//andare in risultati.jsp e mettere la cartella da dove abbiamo avviato il server
	
	static String home="/home/diegomariottini/"; 
	
	static String indexDir= home+"Scrivania/perAgiw/Index/";
	static String doveSiTrovaIlFileNomiCartelle="/home/diegomariottini/Scrivania/perAgiw/Dati/name1.txt";
	static String dataDir=home+"Scrivania/perAgiw/Dati/";
	
	//static String dataDirPrimo=PatterAssolut.getDataDir() + "Alessandro_Cialfi/";
	static String dirDizionario=home+"Scrivania/perAgiw/dizionario/";
	static String dizionario =home+"Scrivania/perAgiw/dizionario/Dizionario - nomi/";
	static String indexDizionario = home+"Scrivania/perAgiw/dizionario/IndexDizionario/";
	static String dirValutazione=home+"Scrivania/";
	
	
	public PatterAssolut() {
	}
public static String getDizionario() {
	return dizionario;
}
public static String getDirValutazione() {
	return dirValutazione;
}
public static String getIndexDizionario() {
	return indexDizionario;
}
	public static String getDataDir() {
		return dataDir;
	}
	public static String getIndexDir() {
		return indexDir;
	}
/*	public static String getDataDirPrimo() {
		return dataDirPrimo;
	}
	*/
	public static String getDoveSiTrovaIlFileNomiCartelle() {
		return doveSiTrovaIlFileNomiCartelle;
	}
	public static String getDirDizionario() {
		return dirDizionario;
	}
	
}
