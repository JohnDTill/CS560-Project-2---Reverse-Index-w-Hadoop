import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

public class Search {
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
		    System.err.println("Usage: java Search <Words>");
		    System.exit(1);
		}

		for(String query : args){
			String modified_query = query.toLowerCase().replaceAll("[^a-zA-Z]", "");
			System.out.println("Search results for \"" + modified_query + "\"");
			search(modified_query);
			System.out.println("");
		}
	}

	private static void search(String query){
		try{
			Scanner sc = new Scanner(new File("InvertedIndex.txt"));
			while(sc.hasNext()){
			 String line = sc.nextLine();
			 String[] key_value_pair = line.split("\t");
			 if( key_value_pair[0].equals(query) ){
				reportResults(key_value_pair[1]);
				return;
			 }
			}

			System.out.println("Word not found");
		} catch (FileNotFoundException e) {
			System.out.println("Inverted index file not found");
			System.exit(1);
		}
	}

	private static void reportResults(String value){
		String[] doc_ids = value.split("\\|")[0].split(" ");
		String[] lines = value.split("\\|")[1].split(" ");

		System.out.println("DOC_ID\t\tLINE (in specific work)");
		for( int i = 0; i < doc_ids.length; i++ ){
			System.out.println(doc_ids[i] + "\t\t" + lines[i+1]);
		}
	}
}
