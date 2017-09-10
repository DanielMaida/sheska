package br.ufpe.cin.sheska.app;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class QueryMaker {

	public LuceneReturn makeQuery(String query, Analyzer analyzer, int mode) throws Exception 
    {
        
        IndexSearcher searcher = createSearcher(mode);
        ArrayList<String> pathList = new ArrayList<String>();
        TopDocs foundDocs = searchInContent(query, searcher, analyzer);
         
        
        System.out.println("Total Matches :: " + foundDocs.totalHits);
        System.out.println("Total Files :: " + foundDocs.scoreDocs.length);
       
        
        for (ScoreDoc sd : foundDocs.scoreDocs) 
        {
            Document d = searcher.doc(sd.doc);
            pathList.add(d.get("path"));
            
        }
        LuceneReturn lr = new LuceneReturn(foundDocs.totalHits,foundDocs.scoreDocs.length,pathList);
        return lr;
    }
	
	private static TopDocs searchInContent(String textToFind, IndexSearcher searcher, Analyzer analyzer) throws Exception
    {
        QueryParser qp = new QueryParser("contents", analyzer);
        Query query = qp.parse(textToFind);
         
        TopDocs hits = searcher.search(query, 205);
        return hits;
    }
 
    private static IndexSearcher createSearcher(int mode) throws IOException 
    {
        Directory dir = FSDirectory.open(Paths.get("indexedFiles/base_" + mode ));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }
}

