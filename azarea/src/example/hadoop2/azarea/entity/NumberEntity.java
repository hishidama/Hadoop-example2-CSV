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
public class NumberEntity extends DefaultEntity {
	
	/**
	 *
	 */
	private static final EntityColumn dateColumn = new StringEntityColumn("date");
	/**
	 *
	 */
	private static final EntityColumn numberColumn = new StringEntityColumn("number");
	
	private static final EntityColumn[] ALL_COLUMNS = {
		dateColumn,
		numberColumn
	};
	
	private static final EntityColumn[] KEY_COLUMNS = {
		
	};
	
	private static final EntitySchema schema = new GeneralEntitySchema("NumberEntity", ALL_COLUMNS, KEY_COLUMNS);
	
	/**
	 *
	 */
	public String date;
	
	/**
	 *
	 */
	public String number;
	
	
	public NumberEntity() {
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
			return this.number;
			
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
			this.number = (String)value;
			break;
			
			default:
			throw new IllegalArgumentException("Illegal index : " + index);
		}
	}
	
}
