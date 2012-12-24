package example.hadoop2.azarea.entity;

import jp.co.cac.azarea.cluster.entity.DefaultEntity;
import jp.co.cac.azarea.cluster.entity.EntitySchema;
import jp.co.cac.azarea.cluster.entity.GeneralEntitySchema;
import jp.co.cac.azarea.cluster.entity.column.EntityColumn;
import jp.co.cac.azarea.cluster.entity.column.LongEntityColumn;
import jp.co.cac.azarea.cluster.entity.column.StringEntityColumn;
import jp.co.cac.azarea.cluster.util.Generated;

/**
 * 日付毎の客数（伝票の個数）
 */
@Generated("AZAREA-Cluster 1.0")
public class Result2Entity extends DefaultEntity {
	
	/**
	 * 日付
	 */
	private static final EntityColumn dateColumn = new StringEntityColumn("date");
	/**
	 * 客数
	 */
	private static final EntityColumn countColumn = new LongEntityColumn("count", null);
	
	private static final EntityColumn[] ALL_COLUMNS = {
		dateColumn,
		countColumn
	};
	
	private static final EntityColumn[] KEY_COLUMNS = {
		dateColumn
	};
	
	private static final EntitySchema schema = new GeneralEntitySchema("Result2Entity", ALL_COLUMNS, KEY_COLUMNS);
	
	/**
	 * 日付
	 */
	public String date;
	
	/**
	 * 客数
	 */
	public long count;
	
	
	public Result2Entity() {
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
			this.count = (Long)value;
			break;
			
			default:
			throw new IllegalArgumentException("Illegal index : " + index);
		}
	}
	
}
