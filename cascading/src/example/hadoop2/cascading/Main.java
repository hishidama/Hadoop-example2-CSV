package example.hadoop2.cascading;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import cascading.flow.Flow;
import cascading.flow.FlowConnector;
import cascading.operation.Identity;
import cascading.operation.aggregator.Count;
import cascading.operation.aggregator.Sum;
import cascading.operation.filter.Limit;
import cascading.operation.regex.RegexSplitter;
import cascading.pipe.Each;
import cascading.pipe.Every;
import cascading.pipe.GroupBy;
import cascading.pipe.Pipe;
import cascading.scheme.TextDelimited;
import cascading.scheme.TextLine;
import cascading.tap.Hfs;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tuple.Fields;

import static example.hadoop2.cascading.FieldName.*;

public class Main extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		int r = ToolRunner.run(new Main(), args);
		System.exit(r);
	}

	@Override
	public int run(String[] args) throws Exception {
		// 入出力ディレクトリーの指定
		Tap source = new Hfs(new TextLine(new Fields(F_TEXT)),
				makeQualifiedPath(args[0]));
		Tap sink1 = new Hfs(
				new TextDelimited(new Fields(F_DATE, F_AMOUNT), ","),
				makeQualifiedPath(args[1], "out1"), SinkMode.REPLACE);
		Tap sink2 = new Hfs(
				new TextDelimited(new Fields(F_DATE, F_COUNT), ","),
				makeQualifiedPath(args[1], "out2"), SinkMode.REPLACE);
		Tap sink3 = new Hfs(new TextDelimited(new Fields(F_DATE, F_TIME,
				F_AMOUNT), ","), makeQualifiedPath(args[1], "out3"),
				SinkMode.REPLACE);
		Tap sink4 = new Hfs(new TextDelimited(new Fields(F_DATE, F_CODE,
				F_AMOUNT), ","), makeQualifiedPath(args[1], "out4"),
				SinkMode.REPLACE);
		int limit = Integer.parseInt(args[2]);

		// Pipeの初期化
		Pipe pipe = new Pipe("example2-pipe");
		pipe = new Each(pipe, new RegexSplitter(new Fields(F_DATE, F_TIME,
				F_NUMBER, F_CODE, F_PRICE, F_COUNT), ","));
		pipe = new Each(pipe, new Identity(String.class, String.class,
				String.class, String.class, int.class, int.class));
		Pipe amount = new Each(pipe, new AmountFunction());

		// 日付毎の総売上
		Pipe pipe1 = new GroupBy("pipe1", amount, new Fields(F_DATE));
		pipe1 = new Every(pipe1, new Fields(F_AMOUNT), new Sum(new Fields(
				F_AMOUNT), long.class));

		// 日付毎の客数（伝票の個数）
		Pipe pipe2 = new GroupBy("pipe2", pipe, new Fields(F_DATE, F_NUMBER));
		pipe2 = new Every(pipe2, new Count());
		pipe2 = new GroupBy("pipe2", pipe2, new Fields(F_DATE));
		pipe2 = new Every(pipe2, new Count(new Fields(F_COUNT)));

		// 時間帯毎の売上
		Pipe pipe3 = new Each(amount, new ConvertHHFunction());
		pipe3 = new GroupBy("pipe3", pipe3, new Fields(F_DATE, F_TIME));
		pipe3 = new Every(pipe3, new Fields(F_AMOUNT), new Sum(new Fields(
				F_AMOUNT), long.class));
		pipe3 = new GroupBy(pipe3, new Fields(F_DATE));
		pipe3 = new Every(pipe3, new TimeAddBuffer(), Fields.RESULTS);

		// 上位売上の商品
		Pipe pipe4 = new Pipe("pipe4", amount);
		pipe4 = new GroupBy(pipe4, new Fields(F_DATE, F_CODE));
		pipe4 = new Every(pipe4, new Fields(F_AMOUNT), new Sum(new Fields(
				F_AMOUNT), long.class));
		pipe4 = new GroupBy(pipe4, new Fields(F_DATE), new Fields(F_AMOUNT,
				F_CODE), true);
		// TODO 日付asc,金額desc,商品コードascにする方法
		pipe4 = new Each(pipe4, new Limit(limit));

		// 実行
		Map<String, Tap> taps = new HashMap<String, Tap>();
		taps.put(pipe1.getName(), sink1);
		taps.put(pipe2.getName(), sink2);
		taps.put(pipe3.getName(), sink3);
		taps.put(pipe4.getName(), sink4);
		FlowConnector flowConnector = new FlowConnector();
		Flow flow = flowConnector.connect("example1", source, taps, pipe1,
				pipe2, pipe3, pipe4);
		flow.complete();
		return 0;
	}

	// パスの解釈
	public String makeQualifiedPath(String path) throws IOException {
		FileSystem fs = FileSystem.get(getConf());
		return new Path(path).makeQualified(fs).toString();
	}

	public String makeQualifiedPath(String path, String file)
			throws IOException {
		FileSystem fs = FileSystem.get(getConf());
		return new Path(path, file).makeQualified(fs).toString();
	}
}
