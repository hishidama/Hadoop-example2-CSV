package example.hadoop2.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Main extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		int r = ToolRunner.run(new Main(), args);
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
			Path temp = new Path(dir, "out4temp");
			Job41 job1 = new Job41();
			int r1 = job1.run(conf, src, temp);
			if (r1 != 0) {
				return r1;
			}
			Job42 job2 = new Job42();
			int r2 = job2.run(conf, temp, new Path(dir, "out4"), limit);
			if (r2 != 0) {
				return r2;
			}
		}
		return 0;
	}
}
