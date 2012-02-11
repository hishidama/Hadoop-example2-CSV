package example.hadoop2.mr;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * 日付毎の客数（伝票の個数）
 */
public class Job2 {
	public static class KeyWritable implements WritableComparable<KeyWritable> {
		/** 日付 */
		private String date;
		/** 伝票番号 */
		private String number;

		/**
		 * 日付取得
		 * 
		 * @return 日付
		 */
		public String getDate() {
			return date;
		}

		/**
		 * 日付設定
		 * 
		 * @param date
		 *            日付
		 */
		public void setDate(String date) {
			this.date = date;
		}

		/**
		 * 伝票番号取得
		 * 
		 * @return 伝票番号
		 */
		public String getNumber() {
			return number;
		}

		/**
		 * 伝票番号設定
		 * 
		 * @param number
		 *            伝票番号
		 */
		public void setNumber(String number) {
			this.number = number;
		}

		@Override
		public void write(DataOutput out) throws IOException {
			Text.writeString(out, date);
			Text.writeString(out, number);
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			date = Text.readString(in);
			number = Text.readString(in);
		}

		@Override
		public int compareTo(KeyWritable o) {
			int c;
			c = date.compareTo(o.date);
			if (c != 0) {
				return c;
			}
			c = number.compareTo(o.number);
			return c;
		}
	}

	public static class Map extends
			Mapper<LongWritable, Text, KeyWritable, NullWritable> {

		private final InputWritable input = new InputWritable();
		private final KeyWritable okey = new KeyWritable();

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			input.setString(value.toString());

			okey.setDate(input.getDate());
			okey.setNumber(input.getNumber());

			context.write(okey, NullWritable.get());
		}
	}

	public static class Combine extends
			Reducer<KeyWritable, NullWritable, KeyWritable, NullWritable> {
		@Override
		protected void reduce(KeyWritable key, Iterable<NullWritable> values,
				Context context) throws IOException, InterruptedException {
			context.write(key, NullWritable.get());
		}
	}

	public static class Reduce extends
			Reducer<KeyWritable, NullWritable, Text, IntWritable> {

		private HashMap<String, IntWritable> map;

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			map = new HashMap<String, IntWritable>();
		}

		@Override
		protected void reduce(KeyWritable key, Iterable<NullWritable> values,
				Context context) throws IOException, InterruptedException {
			String date = key.getDate();
			IntWritable c = map.get(date);
			if (c == null) {
				c = new IntWritable();
				map.put(date, c);
			}
			c.set(c.get() + 1);
		}

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			Text date = new Text();

			for (Entry<String, IntWritable> kv : map.entrySet()) {
				date.set(kv.getKey());

				context.write(date, kv.getValue());
			}
		}
	}

	public int run(Configuration conf, Path src, Path dst) throws IOException,
			InterruptedException, ClassNotFoundException {
		Job job = new Job(conf, "hadoop2-mr2");
		job.setJarByClass(getClass());
		job.setMapOutputKeyClass(KeyWritable.class);
		job.setMapOutputValueClass(NullWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(Map.class);
		job.setCombinerClass(Combine.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, src);
		FileOutputFormat.setOutputPath(job, dst);
		job.getConfiguration().set(Const.CONF_KV_SEP, ",");

		boolean success = job.waitForCompletion(true);
		return success ? 0 : 1;
	}
}
