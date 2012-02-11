package example.hadoop2.mr;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;

/**
 * 時間帯毎の売上
 */
public class Job3 {
	public static class KeyWritable implements WritableComparable<KeyWritable> {
		/** 日付 */
		private String date;
		/** 時刻 */
		private String time;

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
		 * 時刻取得
		 * 
		 * @return 時刻
		 */
		public String getTime() {
			return time;
		}

		/**
		 * 時刻設定
		 * 
		 * @param time
		 *            時刻
		 */
		public void setTime(String time) {
			this.time = time;
		}

		@Override
		public void write(DataOutput out) throws IOException {
			Text.writeString(out, date);
			Text.writeString(out, time);
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			date = Text.readString(in);
			time = Text.readString(in);
		}

		@Override
		public int compareTo(KeyWritable o) {
			int c;
			c = date.compareTo(o.date);
			if (c != 0) {
				return c;
			}
			c = time.compareTo(o.time);
			return c;
		}

		@Override
		public String toString() {
			return date + "," + time;
		}
	}

	public static class Map extends
			Mapper<LongWritable, Text, KeyWritable, LongWritable> {

		private final InputWritable input = new InputWritable();
		private final KeyWritable okey = new KeyWritable();
		private final LongWritable amount = new LongWritable();

		private Set<String> set = new HashSet<String>();

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			input.setString(value.toString());

			// まだ出力していない日付に対しては、全時刻のレコードを出力する
			String date = input.getDate();
			if (!set.contains(date)) {
				set.add(date);

				for (int i = 0; i < 24; i++) {
					String hh = String.format("%02d", i);
					okey.setDate(date);
					okey.setTime(hh);
					amount.set(0);
					context.write(okey, amount);
				}
			}

			okey.setDate(date);
			okey.setTime(input.getTime().substring(0, 2));
			amount.set(input.getPrice() * input.getCount());
			context.write(okey, amount);
		}
	}

	public int run(Configuration conf, Path src, Path dst) throws IOException,
			InterruptedException, ClassNotFoundException {
		Job job = new Job(conf, "hadoop2-mr3");
		job.setJarByClass(getClass());
		job.setOutputKeyClass(KeyWritable.class);
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
