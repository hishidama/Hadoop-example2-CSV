package example.hadoop2.azarea.entity;

import jp.co.cac.azarea.cluster.entity.DefaultEntity;
import jp.co.cac.azarea.cluster.entity.EntitySchema;
import jp.co.cac.azarea.cluster.entity.GeneralEntitySchema;
import jp.co.cac.azarea.cluster.entity.column.EntityColumn;
import jp.co.cac.azarea.cluster.entity.column.StringEntityColumn;
import jp.co.cac.azarea.cluster.util.Generated;

/**
 *
 */
@Generated("AZAREA-Cluster 1.0")
public class DateEntity extends DefaultEntity {
	
	/**
	 *
	 */
	private static final EntityColumn match_keyColumn = new StringEntityColumn("match_key");
	/**
	 *
	 */
	private static final EntityColumn dateColumn = new StringEntityColumn("date");
	
	private static final EntityColumn[] ALL_COLUMNS = {
		match_keyColumn,
		dateColumn
	};
	
	private static final EntityColumn[] KEY_COLUMNS = {
		
	};
	
	private static final EntitySchema schema = new GeneralEntitySchema("DateEntity", ALL_COLUMNS, KEY_COLUMNS);
	
	/**
	 *
	 */
	public String match_key;
	
	/**
	 *
	 */
	public String date;
	
	
	public DateEntity() {
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
			return this.match_key;
			
			case 1:
			return this.date;
			
			default:
			throw new IllegalArgumentException("Illegal index : " + index);
		}
	}
	
	@Override
	protected void innerSetItem(int index, Object value) {
		switch(index) {
			case 0:
			this.match_key = (String)value;
			break;
			
			case 1:
			this.date = (String)value;
			break;
			
			default:
			throw new IllegalArgumentException("Illegal index : " + index);
		}
	}
	
}
