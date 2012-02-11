package example.hadoop2.mr;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import example.hadoop2.mr.Job41.KeyWritable;

/**
 * 上位売上の商品（ソート）
 */
public class Job42 {
	public static class Map extends
			Mapper<KeyWritable, LongWritable, LongWritable, KeyWritable> {

		@Override
		protected void map(KeyWritable key, LongWritable value, Context context)
				throws IOException, InterruptedException {

			context.write(value, key);
		}
	}

	public static class LongReverseComparator implements
			RawComparator<LongWritable> {
		static final WritableComparator LONG_COMPARATOR = new LongWritable.Comparator();

		@Override
		public int compare(LongWritable o1, LongWritable o2) {
			return o2.compareTo(o1);
		}

		@Override
		public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
			return LONG_COMPARATOR.compare(b2, s2, l2, b1, s1, l1);
		}
	}

	public static class Reduce extends
			Reducer<LongWritable, KeyWritable, KeyWritable, LongWritable> {
		private int limit;

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			limit = context.getConfiguration()
					.getInt(Const.CONF_JOB4_LIMIT, 10);
		}

		private int count = 0;

		@Override
		protected void reduce(LongWritable key, Iterable<KeyWritable> values,
				Context context) throws IOException, InterruptedException {
			for (KeyWritable value : values) {
				if (++count > limit) {
					break;
				}
				context.write(value, key);
			}
		}
	}

	public int run(Configuration conf, Path src, Path dst, int limit)
			throws IOException, InterruptedException, ClassNotFoundException {
		Job job = new Job(conf, "hadoop2-mr42");
		job.setJarByClass(getClass());
		job.getConfiguration().setInt(Const.CONF_JOB4_LIMIT, limit);

		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(KeyWritable.class);
		job.setOutputKeyClass(KeyWritable.class);
		job.setOutputValueClass(LongWritable.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		job.setNumReduceTasks(1);

		job.setSortComparatorClass(LongReverseComparator.class);

		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.getConfiguration().set(Const.CONF_KV_SEP, ",");

		FileInputFormat.setInputPaths(job, src);
		FileOutputFormat.setOutputPath(job, dst);

		boolean success = job.waitForCompletion(true);
		return success ? 0 : 1;
	}
}
