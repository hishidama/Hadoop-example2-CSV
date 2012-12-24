package example.hadoop2.azarea.flow;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import jp.co.cac.azarea.cluster.planner.job.SimpleEntityFlowManager;
import jp.co.cac.azarea.cluster.tester.MapReduceJobManagerTester;

public class SalesFlowTest {

	@Test
	public void flow1() throws IOException {
		MapReduceJobManagerTester tester = new MapReduceJobManagerTester(
				new SimpleEntityFlowManager());
		assertTrue(tester.testAndAssert("../data", "../expected/sales1",
				Sales1Flow.class.getName()));
	}

	@Test
	public void flow2() throws IOException {
		MapReduceJobManagerTester tester = new MapReduceJobManagerTester(
				new SimpleEntityFlowManager());
		assertTrue(tester.testAndAssert("../data", "../expected/sales2",
				Sales2Flow.class.getName()));
	}

	@Test
	public void flow3() throws IOException {
		MapReduceJobManagerTester tester = new MapReduceJobManagerTester(
				new SimpleEntityFlowManager());
		assertTrue(tester.testAndAssert("../data", "../expected/sales3",
				Sales3Flow.class.getName()));
	}

	@Test
	public void flow4() throws IOException {
		MapReduceJobManagerTester tester = new MapReduceJobManagerTester(
				new SimpleEntityFlowManager());
		tester.getApplicationContext().setInt("LIMIT", 10);
		assertTrue(tester.testAndAssert("../data", "../expected/sales4",
				Sales4Flow.class.getName()));
	}
}