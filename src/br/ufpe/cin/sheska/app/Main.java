package br.ufpe.cin.sheska.app;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class Main {
	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);
		Scanner in2 = new Scanner(System.in);
		// Input folder
		String docsPath = "inputFiles";

		// Output folder
		String indexPath = "indexedFiles/base_";

		// Input Path Variable
		final Path docDir = Paths.get(docsPath);

		while (true) {

			boolean isValid = false;
			String opt = "";

			while (!isValid) {

				System.out.println("Bem vindo ao Sheska, o que você deseja fazer?");
				System.out.println("    1 - criar as bases de índice?");
				System.out.println("    2 - fazer uma consulta?");

				opt = in.nextLine();

				if (opt.equals("1") || opt.equals("2")) {
					isValid = true;
				}

			}

			try {
				if (opt.equals("1")) {
					for (int mode = 1; mode < 5; mode++) {
						Indexer indexer = new Indexer(indexPath + mode, getAnalyzer(mode));
						indexer.doIndexing(docDir);
					}
				} else {

					isValid = false;

					while (!isValid) {
						System.out.println("Em qual base você deseja fazer a consulta?");
						System.out.println(" 1 - Sem remoçao de stopwords, Sem Stemming");
						System.out.println(" 2 - Com remocao de stopwords, Sem Stemming");
						System.out.println(" 3 - Sem remocao de stopwords, Com Stemming");
						System.out.println(" 4 - Com remocao de stopwords, Com Stemming");

						int opt2 = in2.nextInt();
						
						if (opt2 > 0 && opt2 < 5 ) {
							System.out.println("Digite sua busca");
							String query = in.nextLine();

							System.out.println("Resultados da busca por " + query + ":");
							QueryMaker qm = new QueryMaker();
							LuceneReturn lr = qm.makeQuery(query, getAnalyzer(opt2), opt2);
							
							System.out.println("Docs: ");
							for(String doc : lr.getPaths()){
								System.out.println(System.getProperty("user.dir") + "/" + doc);
							}
							
							System.out.println("Total Matches :: " + lr.getTotalFiles());

							System.out.println("Deseja fazer outra busca? Y or N");
							String anotherSearch = in.nextLine();

							if (!anotherSearch.equalsIgnoreCase("Y")) {
								isValid = true;
							}
						} else {
							break;
						}
						
					}
				}
			} catch (Exception e) {
				System.out.println("Valor invalido digitado, por favor digite um número");
			}

		}
	}

	private static Analyzer getAnalyzer(int mode) {
		switch (mode) {
		case 1:
			return getEmptyAnalyzer(); // sem stop sem stem
		case 2:
			return new StandardAnalyzer(); // com stop sem stem
		case 3:
			return new EnglishAnalyzer(new CharArraySet(0, true)); // sem stop com stem
		case 4:
			return new EnglishAnalyzer(); // com stop e com stemming
		default:
			return getEmptyAnalyzer(); // sem stop, sem stemming
		}
	}

	private static Analyzer getEmptyAnalyzer() {

		CharArraySet stopWords = new CharArraySet(0, true);

		return new StandardAnalyzer(stopWords);
	}

}
