package example.hadoop2.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Main2 extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		int r = ToolRunner.run(new Main2(), args);
		System.exit(r);
	}

	@Override
	public int run(String[] args) throws Exception {
		Path src = new Path(args[0]);
		Path dir = new Path(args[1]);
		int limit = Integer.parseInt(args[2]);

		Configuration conf = getConf();

		{ // 日付毎の総売上
			Job1 job = new Job1();
			int r = job.run(conf, src, new Path(dir, "out1"));
			if (r != 0) {
				return r;
			}
		}
		{ // 日付毎の客数
			Job2 job = new Job2();
			int r = job.run(conf, src, new Path(dir, "out2"));
			if (r != 0) {
				return r;
			}
		}
		{ // 時間帯毎の売上
			Job3 job = new Job3();
			int r = job.run(conf, src, new Path(dir, "out3"));
			if (r != 0) {
				return r;
			}
		}
		{ // 上位売上の商品
			Job4B job = new Job4B();
			int r = job.run(conf, src, new Path(dir, "out4"), limit);
			if (r != 0) {
				return r;
			}
		}
		return 0;
	}
}
