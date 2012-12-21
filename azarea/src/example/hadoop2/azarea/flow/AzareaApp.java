package example.hadoop2.azarea.flow;

import java.io.IOException;
import java.util.List;

import jp.co.cac.azarea.cluster.Main;
import jp.co.cac.azarea.cluster.planner.job.EntityFlowManager;
import jp.co.cac.azarea.cluster.util.Generated;

@Generated("AZAREA-Cluster 1.0")
public class AzareaApp extends EntityFlowManager {

	@Override
	protected void initializeContext(List<String> args) throws IOException {
		if (args.size() < 1) {
			throw new IllegalArgumentException("引数は1個指定してください。");
		}
		getApplicationContext().setInt("LIMIT", Integer.parseInt(args.get(0)));
	}

	@Override
	protected void initializeEntityFlow() throws IOException {
		addEntityFlow(new SalesFlow());
	}

	public static void main(String[] args) throws Exception {
		Main.execute(AzareaApp.class.getName(), args);
	}

}
