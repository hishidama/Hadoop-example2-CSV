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
import example.hadoop2.azarea.entity.Result4Entity;
import example.hadoop2.azarea.entity.SalesEntity;

@Generated("AZAREA-Cluster 1.0")
public class Sales4Flow extends EntityFlow {

	@Override
	protected void initialize() {
		final int LIMIT = getApplicationContext().getInt("LIMIT");
		getEntityManager().defineEntityFormat("SalesEntity.txt",
				DelimitedEntityFormat.delimiter(","));
		getEntityManager().defineEntityFormat("Result4Entity.txt",
				DelimitedEntityFormat.delimiter(","));

		EntityFile<SalesEntity> entity1 = getInput(SalesEntity.class);
		Conversion<SalesEntity, Result4Entity> entity8 = new Conversion<SalesEntity, Result4Entity>(
				entity1) {
			@Override
			protected void convert(SalesEntity entity) {
				Result4Entity result = new Result4Entity();
				result.date = entity.date;
				result.code = entity.code;
				result.amount = entity.price * entity.count;
				output(result);
			}
		};
		Group<Result4Entity> sum4 = new Group<Result4Entity>(entity8, "date",
				"code") {
			@Override
			protected void doSummarize(Result4Entity summary,
					Result4Entity another) {
				summary.amount += another.amount;
			}
		};
		GroupSort<Result4Entity, Result4Entity> entity9 = new GroupSort<Result4Entity, Result4Entity>(
				sum4, "date", GroupSort.DELIMITER, "amount DESC", "code") {
			@Override
			protected void merge(List<Result4Entity> entities) {
				// NOP
			}

			private int n;

			@Override
			protected void merge(Result4Entity entity, boolean isFirst,
					boolean isLast) {
				if (isFirst) {
					n = 0;
				}
				if (n++ >= LIMIT) {
					return;
				}
				Result4Entity result = new Result4Entity();
				result.copyFrom(entity);
				output(result);
			}
		};
		setOutput(entity9);
	}

	public static void main(String[] args) throws Exception {
		Main.execute(SimpleEntityFlowManager.class.getName(),
				Sales4Flow.class.getName(), args);
	}

}
