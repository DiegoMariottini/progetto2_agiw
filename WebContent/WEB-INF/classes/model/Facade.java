package model;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.ScoreDoc;

public interface Facade {

	public Map<ScoreDoc, List<File>> ricerca(String query)throws Exception;
	public Map<ScoreDoc,List<File>> ricercaAfter(ScoreDoc score, String query) throws Exception;

	
}
