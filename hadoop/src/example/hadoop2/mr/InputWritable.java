package example.hadoop2.mr;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class InputWritable implements Writable {
	/** 日付 */
	private String date;
	/** 時刻 */
	private String time;
	/** 伝票番号 */
	private String number;
	/** 商品コード */
	private String code;
	/** 値段 */
	private int price;
	/** 個数 */
	private int count;

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

	/**
	 * 価格取得
	 * 
	 * @return 価格
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * 価格設定
	 * 
	 * @param price
	 *            価格
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * 個数取得
	 * 
	 * @return 個数
	 */
	public int getCount() {
		return count;
	}

	/**
	 * 個数設定
	 * 
	 * @param count
	 *            個数
	 */
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		Text.writeString(out, date);
		Text.writeString(out, time);
		Text.writeString(out, number);
		Text.writeString(out, code);
		out.writeInt(price);
		out.writeInt(count);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		date = Text.readString(in);
		time = Text.readString(in);
		number = Text.readString(in);
		code = Text.readString(in);
		price = in.readInt();
		count = in.readInt();
	}

	public void setString(String text) {
		String[] ss = text.split(",");
		date = ss[0];
		time = ss[1];
		number = ss[2];
		code = ss[3];
		price = Integer.parseInt(ss[4]);
		count = Integer.parseInt(ss[5]);
	}

	@Override
	public String toString() {
		return date + "," + time + "," + number + "," + code + "," + price
				+ "," + count;
	}
}
