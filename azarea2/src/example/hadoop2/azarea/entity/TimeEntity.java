package example.hadoop2.azarea.entity;

import jp.co.cac.azarea.cluster.entity.DefaultEntity;
import jp.co.cac.azarea.cluster.entity.EntitySchema;
import jp.co.cac.azarea.cluster.entity.GeneralEntitySchema;
import jp.co.cac.azarea.cluster.entity.column.EntityColumn;
import jp.co.cac.azarea.cluster.entity.column.LongEntityColumn;
import jp.co.cac.azarea.cluster.entity.column.StringEntityColumn;
import jp.co.cac.azarea.cluster.util.Generated;

/**
 *
 */
@Generated("AZAREA-Cluster 1.0")
public class TimeEntity extends DefaultEntity {
	
	/**
	 *
	 */
	private static final EntityColumn dateColumn = new StringEntityColumn("date");
	/**
	 *
	 */
	private static final EntityColumn hhColumn = new StringEntityColumn("hh");
	/**
	 *
	 */
	private static final EntityColumn amountColumn = new LongEntityColumn("amount", null);
	
	private static final EntityColumn[] ALL_COLUMNS = {
		dateColumn,
		hhColumn,
		amountColumn
	};
	
	private static final EntityColumn[] KEY_COLUMNS = {
		
	};
	
	private static final EntitySchema schema = new GeneralEntitySchema("TimeEntity", ALL_COLUMNS, KEY_COLUMNS);
	
	/**
	 *
	 */
	public String date;
	
	/**
	 *
	 */
	public String hh;
	
	/**
	 *
	 */
	public long amount;
	
	
	public TimeEntity() {
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
			return this.hh;
			
			case 2:
			return this.amount;
			
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
			this.hh = (String)value;
			break;
			
			case 2:
			this.amount = (Long)value;
			break;
			
			default:
			throw new IllegalArgumentException("Illegal index : " + index);
		}
	}
	
}
