package example.hadoop2.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Create {

	public static void main(String[] args) throws Exception {
		Create creater = new Create();
		creater.run(args);
	}

	public void run(String[] args) throws Exception {
		int size = Integer.parseInt(args[0]) * 1024 * 1024; // MB
		File f = new File(args[1]);
		System.out.println("create " + f);

		create(f, size);
	}

	public void create(File f, int size) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(f), "UTF-8"));
		try {
			int i = 0;
			while (i < size) {
				String data = data(i, size);
				bw.write(data);
				i += data.length();
			}
		} finally {
			bw.close();
		}
	}

	// いつ実行しても同じになるようにする為、seedを固定する
	Random random = new Random(123);
	int number = 1;

	static class Data {
		/** 日付 */
		String date;
		/** 時刻 */
		String time;
		/** 伝票番号 */
		int number;
		/** 商品コード */
		String code;
		/** 商品の値段 */
		int price;
		/** 商品の個数 */
		int count;

		@Override
		public String toString() {
			Object[] ss = { date, time, number, code, price, count };
			return mkString(ss);
		}
	}

	String data(int size, int max) {
		String date = "2012-01-15";
		String time = String.format("%tT", new Date(14L * 60 * 60 * 1000
				* (size + 1) / max));
		int number = this.number++;

		int n = random.nextInt(5) + 1;
		TreeMap<String, Data> map = new TreeMap<String, Data>();
		for (int i = 0; i < n; i++) {
			Data d = new Data();
			d.date = date;
			d.time = time;
			d.number = number;
			d.code = String.format("%05x", random.nextInt(0x7fff) + 1);
			d.price = price(d.code);
			d.count = random.nextInt(10) + 1;
			map.put(d.code, d);
		}

		StringBuilder sb = new StringBuilder(128);
		for (Data d : map.values()) {
			sb.append(d.toString());
			sb.append('\n');
		}
		return sb.toString();
	}

	Map<String, Integer> priceMap = new HashMap<String, Integer>();

	int price(String code) {
		Integer price = priceMap.get(code);
		if (price != null) {
			return price;
		}

		int p = (random.nextInt(100) + 1) * 50;
		priceMap.put(code, p);
		return p;
	}

	static String mkString(Object[] ss) {
		StringBuilder sb = new StringBuilder(64);
		for (Object s : ss) {
			sb.append(s);
			sb.append(',');
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
}
