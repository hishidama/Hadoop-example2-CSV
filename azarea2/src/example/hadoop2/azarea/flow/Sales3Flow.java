package example.hadoop2.azarea.flow;

import java.util.List;

import jp.co.cac.azarea.cluster.Main;
import jp.co.cac.azarea.cluster.entity.format.DelimitedEntityFormat;
import jp.co.cac.azarea.cluster.planner.job.EntityFlow;
import jp.co.cac.azarea.cluster.planner.job.SimpleEntityFlowManager;
import jp.co.cac.azarea.cluster.planner.operation.Conversion;
import jp.co.cac.azarea.cluster.planner.operation.EntityFile;
import jp.co.cac.azarea.cluster.planner.operation.Group;
import jp.co.cac.azarea.cluster.planner.operation.Join;
import jp.co.cac.azarea.cluster.planner.operation.UniqueJoin;
import jp.co.cac.azarea.cluster.util.Generated;
import example.hadoop2.azarea.entity.DateEntity;
import example.hadoop2.azarea.entity.DateHhEntity;
import example.hadoop2.azarea.entity.HhTable;
import example.hadoop2.azarea.entity.Result3Entity;
import example.hadoop2.azarea.entity.SalesEntity;
import example.hadoop2.azarea.entity.TimeEntity;

@Generated("AZAREA-Cluster 1.0")
public class Sales3Flow extends EntityFlow {

	@Override
	protected void initialize() {
		getEntityManager().defineEntityFormat("SalesEntity.txt",
				DelimitedEntityFormat.delimiter(","));
		getEntityManager().defineEntityFormat("Result3Entity.txt",
				DelimitedEntityFormat.delimiter(","));

		EntityFile<SalesEntity> entity1 = getInput(SalesEntity.class);

		Conversion<SalesEntity, TimeEntity> entity3 = new Conversion<SalesEntity, TimeEntity>(
				entity1) {
			@Override
			protected void convert(SalesEntity entity) {
				TimeEntity result = new TimeEntity();
				result.date = entity.date;
				result.hh = entity.time.substring(0, 2);
				result.amount = entity.price * entity.count;
				output(result);
			}
		};
		Group<TimeEntity> sum3 = new Group<TimeEntity>(entity3, "date", "hh") {
			@Override
			protected void doSummarize(TimeEntity summary, TimeEntity another) {
				summary.amount += another.amount;
			}
		};
		EntityFile<HhTable> entity6 = getInput(HhTable.class);
		Group<SalesEntity> entity7 = new Group<SalesEntity>(entity1, "date") {
			@Override
			protected void doSummarize(SalesEntity summary, SalesEntity another) {
				// NOP（先頭レコードだけ出力する）
			}
		};
		Conversion<SalesEntity, DateEntity> entity2 = new Conversion<SalesEntity, DateEntity>(
				entity7) {
			@Override
			protected void convert(SalesEntity entity) {
				DateEntity result = new DateEntity();
				result.match_key = "k";
				result.date = entity.date;
				output(result);
			}
		};
		Join<DateEntity, HhTable, DateHhEntity> joinDateHh = new Join<DateEntity, HhTable, DateHhEntity>(
				entity2, entity6, "match_key") {
			@Override
			protected void merge(DateEntity main, List<HhTable> subs) {
				for (HhTable sub : subs) {
					DateHhEntity result = new DateHhEntity();
					result.copyFrom(sub);
					result.copyFrom(main);
					output(result);
				}
			}
		};
		UniqueJoin<DateHhEntity, TimeEntity, Result3Entity> entity5 = new UniqueJoin<DateHhEntity, TimeEntity, Result3Entity>(
				joinDateHh, sum3, "date", "hh") {
			@Override
			protected void merge(DateHhEntity main, TimeEntity sub) {
				Result3Entity result = new Result3Entity();
				result.copyFrom(sub);
				output(result);
			}

			@Override
			protected void merge(DateHhEntity main) {
				Result3Entity result = new Result3Entity();
				result.copyFrom(main);
				result.amount = 0;
				output(result);
			}
		};
		setOutput(entity5);
	}

	public static void main(String[] args) throws Exception {
		Main.execute(SimpleEntityFlowManager.class.getName(),
				Sales3Flow.class.getName(), args);
	}

}
