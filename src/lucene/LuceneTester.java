package lucene;

import java.io.File;
import java.io.IOException;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class LuceneTester {

	static String indexDir = PatterAssolut.getIndexDir();
//	static String dataDir = PatterAssolut.getDataDirPrimo();
	Indexer indexer;
	static Searcher searcher;


	//funzione per generare l'indice
	/* private void createIndex() throws IOException{
      indexer = new Indexer(indexDir);
      int numIndexed;
      long startTime = System.currentTimeMillis();	
      numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
      long endTime = System.currentTimeMillis();
      indexer.close();
      System.out.println(numIndexed+" File indexed, time taken: "
         +(endTime-startTime)+" ms");		
   }
	 */
	
	
	public Map<ScoreDoc, List<File>> search(String searchQuery) throws IOException, ParseException{

		List<File> files = new LinkedList<File>();
		Map<ScoreDoc, List<File>> hits2files = new HashMap<ScoreDoc, List<File>>();
		searcher = new Searcher(indexDir);
		TopDocs hits = searcher.search(searchQuery);
		ScoreDoc score = null;
		for(ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			File f = new File(doc.get(LuceneConstants.FILE_PATH));
			files.add(f);
			score = scoreDoc;
		}
		
		hits2files.put(score, files);
		return hits2files;
	}
	
	public Map<ScoreDoc, List<File>> searchAfter(ScoreDoc score, String searchQuery) throws IOException, ParseException{

		List<File> files = new LinkedList<File>();
		Map<ScoreDoc, List<File>> hits2files = new HashMap<ScoreDoc, List<File>>();
		searcher = new Searcher(indexDir);
		TopDocs hits = searcher.searchAfter(score, searchQuery);
		ScoreDoc scoreLocal = null;
		for(ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			File f = new File(doc.get(LuceneConstants.FILE_PATH));
			files.add(f);
			scoreLocal = scoreDoc;
		}
		hits2files.put(scoreLocal, files);
		return hits2files;
	}
}