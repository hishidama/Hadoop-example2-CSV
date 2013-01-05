package example.hadoop2.azarea.flow;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import jp.co.cac.azarea.cluster.planner.job.SimpleEntityFlowManager;
import jp.co.cac.azarea.cluster.tester.MapReduceJobManagerTester;

public class SalesFlowTest {

	@Test
	public void flow() throws IOException {
		MapReduceJobManagerTester tester = new MapReduceJobManagerTester(
				new SimpleEntityFlowManager());
		tester.getApplicationContext().setInt("LIMIT", 10);
		assertTrue(tester.testAndAssert("../data", "../expected",
				SalesFlow.class.getName()));
	}
}
