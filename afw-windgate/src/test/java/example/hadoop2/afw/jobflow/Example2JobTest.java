package example.hadoop2.afw.jobflow;

import org.junit.Test;

import com.asakusafw.testdriver.JobFlowTester;

import example.hadoop2.afw.modelgen.dmdl.model.InputModel;
import example.hadoop2.afw.modelgen.dmdl.model.Out1Model;
import example.hadoop2.afw.modelgen.dmdl.model.Out2Model;
import example.hadoop2.afw.modelgen.dmdl.model.Out3Model;
import example.hadoop2.afw.modelgen.dmdl.model.Out4Model;
import example.hadoop2.afw.jobflow.Example2Job;

public class Example2JobTest {

	@Test
	public void testDescribe() {
		JobFlowTester tester = new JobFlowTester(getClass());
		tester.setBatchArg("LIMIT", "10");
		tester.setBatchArg("SRC_FILE", "afw/input/test.txt");

		tester.input("in", InputModel.class).prepare("input_model.xls#input");
		tester.output("out1", Out1Model.class).verify("out1_model.xls#output",
				"out1_model.xls#rule");
		tester.output("out2", Out2Model.class).verify("out2_model.xls#output",
				"out2_model.xls#rule");
		tester.output("out3", Out3Model.class).verify("out3_model.xls#output",
				"out3_model.xls#rule");
		tester.output("out4", Out4Model.class).verify("out4_model.xls#output",
				"out4_model.xls#rule");

		tester.runTest(Example2Job.class);
	}
}
