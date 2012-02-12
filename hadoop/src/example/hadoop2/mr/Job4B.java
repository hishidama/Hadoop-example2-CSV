package example.hadoop2.mr;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;

/**
 * 上位売上の商品
 */
public class Job4B {
	public static class KeyWritable implements WritableComparable<KeyWritable> {
		/** 日付 */
		private String date;
		/** 商品コード */
		private String code;

		public KeyWritable() {
		}

		public KeyWritable(KeyWritable o) {
			date = o.getDate();
			code = o.getCode();
		}

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
		 * 商品コード取得
		 * 
		 * @return 商品コード
		 */
		public String getCode() {
			return code;
		}

		/**
		 * 商品コード設定
		 * 
		 * @param code
		 *            商品コード
		 */
		public void setCode(String code) {
			this.code = code;
		}

		@Override
		public void write(DataOutput out) throws IOException {
			Text.writeString(out, date);
			Text.writeString(out, code);
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			date = Text.readString(in);
			code = Text.readString(in);
		}

		@Override
		public int compareTo(KeyWritable o) {
			int c;
			c = date.compareTo(o.date);
			if (c != 0) {
				return c;
			}
			c = code.compareTo(o.code);
			return c;
		}

		@Override
		public String toString() {
			return date + "," + code;
		}
	}

	public static class Map extends
			Mapper<LongWritable, Text, KeyWritable, LongWritable> {

		private final InputWritable input = new InputWritable();
		private final KeyWritable okey = new KeyWritable();
		private final LongWritable amount = new LongWritable();

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			input.setString(value.toString());

			okey.setDate(input.getDate());
			okey.setCode(input.getCode());
			amount.set(input.getPrice() * input.getCount());

			context.write(okey, amount);
		}
	}

	public static class Reduce extends
			Reducer<KeyWritable, LongWritable, KeyWritable, LongWritable> {
		private int limit;

		static class Pair implements Comparable<Pair> {
			public KeyWritable key;
			public long amount;

			public Pair(KeyWritable key, long amount) {
				this.key = new KeyWritable(key);
				this.amount = amount;
			}

			@Override
			public int compareTo(Pair o) {
				int c = key.getDate().compareTo(o.key.getDate());
				if (c != 0) {
					return c;
				}

				if (amount < o.amount) {
					return 1;
				} else if (amount > o.amount) {
					return -1;
				}

				return key.getCode().compareTo(o.key.getCode());
			}
		}

		private List<Pair> list;

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			limit = context.getConfiguration()
					.getInt(Const.CONF_JOB4_LIMIT, 10);
			list = new ArrayList<Pair>(limit + 1);
		}

		@Override
		protected void reduce(KeyWritable key, Iterable<LongWritable> values,
				Context context) throws IOException, InterruptedException {
			long sum = 0;
			for (LongWritable value : values) {
				sum += value.get();
			}
			addList(key, sum);
		}

		protected void addList(KeyWritable key, long value) {
			Pair pair = new Pair(key, value);
			list.add(pair);

			Collections.sort(list);
			if (list.size() > limit) {
				list.remove(list.size() - 1);
			}
		}

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			LongWritable value = new LongWritable();
			for (Pair pair : list) {
				KeyWritable key = pair.key;
				value.set(pair.amount);
				context.write(key, value);
			}

			super.cleanup(context);
		}
	}

	public int run(Configuration conf, Path src, Path dst, int limit)
			throws IOException, InterruptedException, ClassNotFoundException {
		Job job = new Job(conf, "hadoop2-mr4b");
		job.setJarByClass(getClass());
		job.getConfiguration().setInt(Const.CONF_JOB4_LIMIT, limit);

		job.setOutputKeyClass(KeyWritable.class);
		job.setOutputValueClass(LongWritable.class);

		job.setMapperClass(Map.class);
		job.setCombinerClass(LongSumReducer.class);
		job.setReducerClass(Reduce.class);
		job.setNumReduceTasks(1);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, src);
		FileOutputFormat.setOutputPath(job, dst);
		job.getConfiguration().set(Const.CONF_KV_SEP, ",");

		boolean success = job.waitForCompletion(true);
		return success ? 0 : 1;
	}
}
