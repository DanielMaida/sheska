package br.ufpe.cin.sheska.app;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;


public class Main 
{
	public static void main(String[] args) 
	{
		//Input folder
		String docsPath = "inputFiles";

		//Output folder
		String indexPath = "indexedFiles/base_";

		//Input Path Variable
		final Path docDir = Paths.get(docsPath);

		try 
		{
			for(int mode = 1; mode < 5; mode ++){
				Indexer indexer = new Indexer(indexPath + mode, getAnalyzer(mode));
				indexer.doIndexing(docDir);
			}
			
			int mode = 2;
			QueryMaker qm = new QueryMaker();
			LuceneReturn lr = qm.makeQuery("computer", getAnalyzer(mode), mode);
			System.out.println(lr.getTotalMatches());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private static Analyzer getAnalyzer(int mode){
		switch(mode){
		case 1:
			return getEmptyAnalyzer(); //sem stop sem stem
		case 2:
			return new StandardAnalyzer(); //com stop sem stem
		case 3:
			return new EnglishAnalyzer(new CharArraySet(0, true)); //sem stop, com stem
		case 4: 
			return new EnglishAnalyzer(); //com stop e com stemming
		default:
			return getEmptyAnalyzer(); //sem stop, sem stemming
		}
	}

	private static Analyzer getEmptyAnalyzer(){

		CharArraySet stopWords = new CharArraySet(0, true);

		return new StandardAnalyzer(stopWords);
	}


}
