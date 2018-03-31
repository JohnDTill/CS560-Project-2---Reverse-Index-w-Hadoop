import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;
import java.util.HashSet;
import java.util.Scanner;
import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.io.LongWritable;

import static java.lang.Math.toIntExact;

public class InvertedIndexBuilder {

  public static class InvertedIndexMapper extends Mapper<Object, Text, Text, Text>{

	
    private HashSet<String> stop_words = loadStopWords();
    private Text word = new Text();

    private HashSet<String> loadStopWords(){
	HashSet<String> stop_words = new HashSet<String>();
	
	try{
		Scanner sc = new Scanner(new File("StopWords.txt"));
		while(sc.hasNext()){
		 String line = sc.nextLine();
		 stop_words.add(line);
		}
	} catch (FileNotFoundException e) {
		System.out.println("Stop Word file not found");
		System.exit(1);
	}

	return stop_words;
    }

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {

	String[] lines = value.toString().split("\n");

	for( String line : lines ){
		StringTokenizer itr = new StringTokenizer( line.toString().toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "") );
		String line_num = itr.nextToken();
		String doc_id = itr.nextToken();

		while(itr.hasMoreTokens()) {
			String candidate = itr.nextToken().replaceAll("[0-9]", "");
			if( !stop_words.contains( candidate ) && candidate.length() > 0 ){
				context.write(new Text(candidate), new Text(doc_id + " " + line_num));
			}
		}
	}
    }
  }

  public static class InvertedIndexReducer extends Reducer<Text,Text,Text,Text> {

    public void reduce(Text key, Iterable<Text> values,
                       Context context
                       ) throws IOException, InterruptedException {
      String doc_ids = "";
      String lines = "";
      for (Text val : values) {
	String[] id_line_pair = val.toString().split(" ");

	doc_ids += id_line_pair[0] + " ";
	lines += id_line_pair[1] + " ";
      }
      context.write(key, new Text(doc_ids + "| " + lines));
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(InvertedIndexBuilder.class);
    job.setMapperClass(InvertedIndexMapper.class);
    job.setReducerClass(InvertedIndexReducer.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
