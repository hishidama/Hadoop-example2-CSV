package example.hadoop2.mr;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

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
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;

/**
 * 上位売上の商品（集計）
 */
public class Job41 {
	public static class KeyWritable implements WritableComparable<KeyWritable> {
		/** 日付 */
		private String date;
		/** 商品コード */
		private String code;

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

	public int run(Configuration conf, Path src, Path dst) throws IOException,
			InterruptedException, ClassNotFoundException {
		Job job = new Job(conf, "hadoop2-mr41");
		job.setJarByClass(getClass());
		job.setOutputKeyClass(KeyWritable.class);
		job.setOutputValueClass(LongWritable.class);

		job.setMapperClass(Map.class);
		job.setCombinerClass(LongSumReducer.class);
		job.setReducerClass(LongSumReducer.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		FileInputFormat.setInputPaths(job, src);
		FileOutputFormat.setOutputPath(job, dst);

		boolean success = job.waitForCompletion(true);
		return success ? 0 : 1;
	}
}
