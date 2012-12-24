package example.hadoop2.azarea.app;

import java.io.IOException;

import jp.co.cac.azarea.cluster.tester.MapReduceJobManagerTester;
import jp.co.cac.azarea.cluster.util.Generated;

@Generated("AZAREA-Cluster 1.0")
public class AzareaSales2AppTester {

	public static void main(String[] args) throws IOException {
		MapReduceJobManagerTester tester = new MapReduceJobManagerTester(
				new AzareaSales2App());
		tester.test("../data", "10");
	}

}