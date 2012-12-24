package example.hadoop2.azarea.flow;

import jp.co.cac.azarea.cluster.Main;
import jp.co.cac.azarea.cluster.entity.format.DelimitedEntityFormat;
import jp.co.cac.azarea.cluster.planner.job.EntityFlow;
import jp.co.cac.azarea.cluster.planner.job.SimpleEntityFlowManager;
import jp.co.cac.azarea.cluster.planner.operation.Conversion;
import jp.co.cac.azarea.cluster.planner.operation.EntityFile;
import jp.co.cac.azarea.cluster.planner.operation.Group;
import jp.co.cac.azarea.cluster.util.Generated;
import example.hadoop2.azarea.entity.Result1Entity;
import example.hadoop2.azarea.entity.SalesEntity;

@Generated("AZAREA-Cluster 1.0")
public class Sales1Flow extends EntityFlow {

	@Override
	protected void initialize() {
		getEntityManager().defineEntityFormat("SalesEntity.txt",
				DelimitedEntityFormat.delimiter(","));
		getEntityManager().defineEntityFormat("Result1Entity.txt",
				DelimitedEntityFormat.delimiter(","));

		EntityFile<SalesEntity> entity1 = getInput(SalesEntity.class);
		Conversion<SalesEntity, Result1Entity> entity2 = new Conversion<SalesEntity, Result1Entity>(
				entity1) {
			@Override
			protected void convert(SalesEntity entity) {
				Result1Entity result = new Result1Entity();
				result.date = entity.date;
				result.amount = entity.price * entity.count;
				output(result);
			}
		};
		Group<Result1Entity> sum1 = new Group<Result1Entity>(entity2, "date") {
			@Override
			protected void doSummarize(Result1Entity summary,
					Result1Entity another) {
				summary.amount += another.amount;
			}
		};
		setOutput(sum1);
	}

	public static void main(String[] args) throws Exception {
		Main.execute(SimpleEntityFlowManager.class.getName(),
				Sales1Flow.class.getName(), args);
	}

}
