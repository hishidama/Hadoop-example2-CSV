package example.hadoop2.mr;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;

/**
 * 日付毎の総売上
 */
public class Job1 {
	public static class Map extends
			Mapper<LongWritable, Text, Text, LongWritable> {

		private final InputWritable input = new InputWritable();
		private final Text date = new Text();
		private final LongWritable amount = new LongWritable();

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			input.setString(value.toString());

			date.set(input.getDate());
			amount.set(input.getPrice() * input.getCount());

			context.write(date, amount);
		}
	}

	public int run(Configuration conf, Path src, Path dst) throws IOException,
			InterruptedException, ClassNotFoundException {
		Job job = new Job(conf, "hadoop2-mr1");
		job.setJarByClass(getClass());
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

		job.setMapperClass(Map.class);
		job.setCombinerClass(LongSumReducer.class);
		job.setReducerClass(LongSumReducer.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, src);
		FileOutputFormat.setOutputPath(job, dst);
		job.getConfiguration().set(Const.CONF_KV_SEP, ",");

		boolean success = job.waitForCompletion(true);
		return success ? 0 : 1;
	}
}
