package example.hadoop2.azarea.app;

import java.io.IOException;
import java.util.List;

import example.hadoop2.azarea.flow.Sales1Flow;
import example.hadoop2.azarea.flow.Sales2Flow;
import example.hadoop2.azarea.flow.Sales3Flow;
import example.hadoop2.azarea.flow.Sales4Flow;

import jp.co.cac.azarea.cluster.Main;
import jp.co.cac.azarea.cluster.planner.job.EntityFlowManager;
import jp.co.cac.azarea.cluster.util.Generated;

@Generated("AZAREA-Cluster 1.0")
public class AzareaSales2App extends EntityFlowManager {

	@Override
	protected void initializeContext(List<String> args) throws IOException {
		if (args.size() < 1) {
			throw new IllegalArgumentException("引数は1個指定してください。");
		}
		getApplicationContext().setInt("LIMIT", Integer.parseInt(args.get(0)));
	}

	@Override
	protected void initializeEntityFlow() throws IOException {
		addEntityFlow(new Sales1Flow());
		addEntityFlow(new Sales2Flow());
		addEntityFlow(new Sales3Flow());
		addEntityFlow(new Sales4Flow());
	}

	public static void main(String[] args) throws Exception {
		Main.execute(AzareaSales2App.class.getName(), args);
	}

}
