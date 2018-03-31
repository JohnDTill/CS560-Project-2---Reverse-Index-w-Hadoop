import java.util.Scanner;
import java.io.*;

public class LinePrepender {
	public static void main(String[] args) throws Exception {
		if (args.length != 0) {
		    System.err.println("Usage: java LinePrepender");
		    System.exit(1);
		}

		prepend();
	}

	private static void prepend() throws UnsupportedEncodingException{
		try{
			Scanner sc = new Scanner(new File("input/shakespeare_works.txt"));
			PrintWriter writer = new PrintWriter("input/shakespeare_works_with_lines.txt", "UTF-8");
			int line_num = 1;
			int old_doc_id = -1;
			sc.nextLine(); //skip the first line with R column information
			while(sc.hasNext()){
			 String line = sc.nextLine();
			 int doc_id = Integer.parseInt(line.split("[\\s]")[0]);
			 if( doc_id != old_doc_id ){
				line_num = 1; //count the line number for the specific work
				old_doc_id = doc_id;
			 }
			 writer.println(line_num + " " + line); //add the line number at the start of the line
			 line_num++;
			}
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("Stop Word file not found");
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
}
