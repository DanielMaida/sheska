package br.ufpe.cin.sheska.app;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {
	
	private IndexWriter writer;
	private ArrayList<File> queue = new ArrayList<File>();
	
	
	public Indexer(String indexDirectoryPath, Analyzer analyzer ) throws IOException{
		Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath).toPath());
		//Directory indexDirectory = FSDirectory.open(Paths.get(Indexer.class.getResource(indexDirectoryPath).getPath()));
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		writer = new IndexWriter(indexDirectory, iwc);
	}
	
	public void doIndexing(Path path) throws IOException{
		if(writer != null)
		{
			indexDocs(path);
			closeWriter();
		}
		else
		{
			System.out.println("Please, initialize the index");
		}
	}
	
	private void closeWriter() throws IOException{
		writer.close();
	}
	
	private void indexDocs(Path path) throws IOException{
		if (Files.isDirectory(path)) //index directory
		{
			Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
				{
					try
					{
						indexDoc(file,attrs.lastModifiedTime().toMillis());
					}
					catch(IOException io){
						io.printStackTrace();
					}
					return FileVisitResult.CONTINUE;
				}
			});
		}
		else
        {
            indexDoc(path, Files.getLastModifiedTime(path).toMillis());
        }	
	}
	
	private void indexDoc(Path file, long lastModified) throws IOException{
		try(InputStream stream = Files.newInputStream(file)){
			Document doc = new Document(); //lucene doc
			doc.add(new StringField("path", file.toString(), Field.Store.YES));
			doc.add(new LongPoint("last_modified", lastModified));
			doc.add(new TextField("contents", new String(Files.readAllBytes(file)), Field.Store.YES));
			
			writer.updateDocument(new Term("path", file.toString()), doc);
		}
	}
	

	
	
}
