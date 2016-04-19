package lucene;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
 
public class Indexer extends AbstractIndexer{

	
	public Indexer(String indexDirectoryPath) throws IOException{
		//this directory will contain the indexes
		Directory indexDirectory = 
				FSDirectory.open(Paths.get(indexDirectoryPath));
		
		//create the indexer
		Analyzer analyzer = new ItalianAnalyzer();
  		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(indexDirectory, iwc);
	}

}