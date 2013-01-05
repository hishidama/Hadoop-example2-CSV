package example.hadoop2.azarea.flow;

import java.util.List;

import jp.co.cac.azarea.cluster.Main;
import jp.co.cac.azarea.cluster.entity.format.DelimitedEntityFormat;
import jp.co.cac.azarea.cluster.planner.job.EntityFlow;
import jp.co.cac.azarea.cluster.planner.job.SimpleEntityFlowManager;
import jp.co.cac.azarea.cluster.planner.operation.Conversion;
import jp.co.cac.azarea.cluster.planner.operation.EntityFile;
import jp.co.cac.azarea.cluster.planner.operation.Group;
import jp.co.cac.azarea.cluster.planner.operation.GroupSort;
import jp.co.cac.azarea.cluster.util.Generated;
import example.hadoop2.azarea.entity.NumberEntity;
import example.hadoop2.azarea.entity.Result2Entity;
import example.hadoop2.azarea.entity.SalesEntity;

@Generated("AZAREA-Cluster 1.0")
public class Sales2Flow extends EntityFlow {

	@Override
	protected void initialize() {
		getEntityManager().defineEntityFormat("SalesEntity.txt",
				DelimitedEntityFormat.delimiter(","));
		getEntityManager().defineEntityFormat("Result2Entity.txt",
				DelimitedEntityFormat.delimiter(","));

		EntityFile<SalesEntity> entity1 = getInput(SalesEntity.class);
		Conversion<SalesEntity, NumberEntity> entity4 = new Conversion<SalesEntity, NumberEntity>(
				entity1) {
			@Override
			protected void convert(SalesEntity entity) {
				NumberEntity result = new NumberEntity();
				result.copyFrom(entity);
				output(result);
			}
		};
		GroupSort<NumberEntity, Result2Entity> distinct2 = new GroupSort<NumberEntity, Result2Entity>(
				entity4, "date", "number") {
			@Override
			protected void merge(List<NumberEntity> entities) {
				// NOP
			}

			@Override
			protected void merge(NumberEntity entity, boolean isFirst,
					boolean isLast) {
				// 先頭レコードに対してのみ出力する（distinct）
				if (isFirst) {
					Result2Entity result = new Result2Entity();
					result.date = entity.date;
					result.count = 1;
					output(result);
				}
			}
		};
		Group<Result2Entity> count2 = new Group<Result2Entity>(distinct2,
				"date") {
			@Override
			protected void doSummarize(Result2Entity summary,
					Result2Entity another) {
				summary.count += another.count;
			}
		};
		setOutput(count2);
	}

	public static void main(String[] args) throws Exception {
		Main.execute(SimpleEntityFlowManager.class.getName(),
				Sales2Flow.class.getName(), args);
	}

}
