package example.hadoop2.azarea.flow;

import example.hadoop2.azarea.entity.DateEntity;
import example.hadoop2.azarea.entity.DateHhEntity;
import example.hadoop2.azarea.entity.HhTable;
import example.hadoop2.azarea.entity.NumberEntity;
import example.hadoop2.azarea.entity.Result1Entity;
import example.hadoop2.azarea.entity.Result2Entity;
import example.hadoop2.azarea.entity.Result3Entity;
import example.hadoop2.azarea.entity.Result4Entity;
import example.hadoop2.azarea.entity.SalesEntity;
import example.hadoop2.azarea.entity.TimeEntity;
import java.util.List;
import jp.co.cac.azarea.cluster.Main;
import jp.co.cac.azarea.cluster.entity.format.DelimitedEntityFormat;
import jp.co.cac.azarea.cluster.planner.job.EntityFlow;
import jp.co.cac.azarea.cluster.planner.job.SimpleEntityFlowManager;
import jp.co.cac.azarea.cluster.planner.operation.Conversion;
import jp.co.cac.azarea.cluster.planner.operation.EntityFile;
import jp.co.cac.azarea.cluster.planner.operation.Group;
import jp.co.cac.azarea.cluster.planner.operation.GroupSort;
import jp.co.cac.azarea.cluster.planner.operation.Join;
import jp.co.cac.azarea.cluster.planner.operation.UniqueJoin;
import jp.co.cac.azarea.cluster.util.Generated;

@Generated("AZAREA-Cluster 1.0")
public class SalesFlow extends EntityFlow {

	@Override
	protected void initialize() {
		final int LIMIT = getApplicationContext().getInt("LIMIT");
		getEntityManager().defineEntityFormat("SalesEntity.txt",
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
				// 先頭レコードに対してのみ出力する（distinct）
				NumberEntity entity = entities.get(0);
				Result2Entity result = new Result2Entity();
				result.date = entity.date;
				result.count = 1;
				output(result);
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
		GroupSort<SalesEntity, DateEntity> entity7 = new GroupSort<SalesEntity, DateEntity>(
				entity1, "date") {
			@Override
			protected void merge(List<SalesEntity> entities) {
				SalesEntity entity = entities.get(0);
				DateEntity result = new DateEntity();
				result.match_key = "k";
				result.date = entity.date;
				output(result);
			}
		};
		Join<DateEntity, HhTable, DateHhEntity> joinDateHh = new Join<DateEntity, HhTable, DateHhEntity>(
				entity7, entity6, "match_key") {
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
				int n = 0;
				for (Result4Entity entity : entities) {
					if (n++ >= LIMIT) {
						break;
					}
					Result4Entity result = new Result4Entity();
					result.copyFrom(entity);
					output(result);
				}
			}
		};
		setOutput(entity9);
	}

	public static void main(String[] args) throws Exception {
		Main.execute(SimpleEntityFlowManager.class.getName(),
				SalesFlow.class.getName(), args);
	}

}
