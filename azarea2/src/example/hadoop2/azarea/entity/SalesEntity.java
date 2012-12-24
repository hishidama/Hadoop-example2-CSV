package example.hadoop2.azarea.entity;

import jp.co.cac.azarea.cluster.entity.DefaultEntity;
import jp.co.cac.azarea.cluster.entity.EntitySchema;
import jp.co.cac.azarea.cluster.entity.GeneralEntitySchema;
import jp.co.cac.azarea.cluster.entity.column.EntityColumn;
import jp.co.cac.azarea.cluster.entity.column.IntEntityColumn;
import jp.co.cac.azarea.cluster.entity.column.StringEntityColumn;
import jp.co.cac.azarea.cluster.util.Generated;

/**
 * 売上伝票
 */
@Generated("AZAREA-Cluster 1.0")
public class SalesEntity extends DefaultEntity {
	
	/**
	 * 日付
	 */
	private static final EntityColumn dateColumn = new StringEntityColumn("date");
	/**
	 * 時刻
	 */
	private static final EntityColumn timeColumn = new StringEntityColumn("time");
	/**
	 * 伝票番号
	 */
	private static final EntityColumn numberColumn = new StringEntityColumn("number");
	/**
	 * 商品コード
	 */
	private static final EntityColumn codeColumn = new StringEntityColumn("code");
	/**
	 * 値段
	 */
	private static final EntityColumn priceColumn = new IntEntityColumn("price", null);
	/**
	 * 個数
	 */
	private static final EntityColumn countColumn = new IntEntityColumn("count", null);
	
	private static final EntityColumn[] ALL_COLUMNS = {
		dateColumn,
		timeColumn,
		numberColumn,
		codeColumn,
		priceColumn,
		countColumn
	};
	
	private static final EntityColumn[] KEY_COLUMNS = {
		
	};
	
	private static final EntitySchema schema = new GeneralEntitySchema("SalesEntity", ALL_COLUMNS, KEY_COLUMNS);
	
	/**
	 * 日付
	 */
	public String date;
	
	/**
	 * 時刻
	 */
	public String time;
	
	/**
	 * 伝票番号
	 */
	public String number;
	
	/**
	 * 商品コード
	 */
	public String code;
	
	/**
	 * 値段
	 */
	public int price;
	
	/**
	 * 個数
	 */
	public int count;
	
	
	public SalesEntity() {
		super(schema);
	}
	
	/**
	 * エンティティを初期化する。
	 */
	@Override
	protected void initialize() {
		
		
		super.initialize();
	}
	
	
	@Override
	protected Object innerGetItem(int index) {
		switch(index) {
			case 0:
			return this.date;
			
			case 1:
			return this.time;
			
			case 2:
			return this.number;
			
			case 3:
			return this.code;
			
			case 4:
			return this.price;
			
			case 5:
			return this.count;
			
			default:
			throw new IllegalArgumentException("Illegal index : " + index);
		}
	}
	
	@Override
	protected void innerSetItem(int index, Object value) {
		switch(index) {
			case 0:
			this.date = (String)value;
			break;
			
			case 1:
			this.time = (String)value;
			break;
			
			case 2:
			this.number = (String)value;
			break;
			
			case 3:
			this.code = (String)value;
			break;
			
			case 4:
			this.price = (Integer)value;
			break;
			
			case 5:
			this.count = (Integer)value;
			break;
			
			default:
			throw new IllegalArgumentException("Illegal index : " + index);
		}
	}
	
}
