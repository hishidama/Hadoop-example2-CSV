package example.hadoop2.azarea.flow;

import java.io.IOException;
import jp.co.cac.azarea.cluster.planner.job.SimpleEntityFlowManager;
import jp.co.cac.azarea.cluster.tester.MapReduceJobManagerTester;
import jp.co.cac.azarea.cluster.util.Generated;

@Generated("AZAREA-Cluster 1.0")
public class SalesFlowTester {

	public static void main(String[] args) throws IOException {
		MapReduceJobManagerTester tester = new MapReduceJobManagerTester(
				new SimpleEntityFlowManager());
		tester.getApplicationContext().setInt("LIMIT", 10);
		tester.test("../data", SalesFlow.class.getName());
	}

}