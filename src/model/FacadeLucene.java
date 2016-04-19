package model;

import lucene.LuceneTester;
import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.ScoreDoc;

public class FacadeLucene implements Facade {

	@Override
	public Map<ScoreDoc, List<File>> ricerca(String query) throws Exception {
		LuceneTester lt=new LuceneTester();
		Map<ScoreDoc, List<File>> a =lt.search(query);
		return a;
	}
	
	@Override
	public Map<ScoreDoc,List<File>> ricercaAfter(ScoreDoc score, String query) throws Exception {
		LuceneTester lt=new LuceneTester();
		Map<ScoreDoc, List<File>> a =lt.searchAfter(score, query);
		return a;
	}

}
