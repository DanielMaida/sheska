package br.ufpe.cin.sheska.app;

import java.util.ArrayList;

public class LuceneReturn {

	private int totalMatches;
	private int totalFiles;
	private ArrayList<String> paths;
	public LuceneReturn(int totalMatches, int totalFiles, ArrayList<String> paths){
		this.totalMatches = totalMatches;
		this.totalFiles = totalFiles;
		this.paths = paths;
	}
	public int getTotalMatches() {
		return totalMatches;
	}
	public void setTotalMatches(int totalMatches) {
		this.totalMatches = totalMatches;
	}
	public int getTotalFiles() {
		return totalFiles;
	}
	public void setTotalFiles(int totalFiles) {
		this.totalFiles = totalFiles;
	}
	public ArrayList<String> getPaths() {
		return paths;
	}
	public void setPaths(ArrayList<String> paths) {
		this.paths = paths;
	}
	
	
}
