package example.hadoop2.afw.jobflow;

import com.asakusafw.vocabulary.flow.Export;
import com.asakusafw.vocabulary.flow.FlowDescription;
import com.asakusafw.vocabulary.flow.Import;
import com.asakusafw.vocabulary.flow.In;
import com.asakusafw.vocabulary.flow.JobFlow;
import com.asakusafw.vocabulary.flow.Out;
import com.asakusafw.vocabulary.flow.util.CoreOperatorFactory;

import example.hadoop2.afw.modelgen.dmdl.model.InputModel;
import example.hadoop2.afw.modelgen.dmdl.model.Out1Model;
import example.hadoop2.afw.modelgen.dmdl.model.Out2Model;
import example.hadoop2.afw.modelgen.dmdl.model.Out3Model;
import example.hadoop2.afw.modelgen.dmdl.model.Out4Model;
import example.hadoop2.afw.operator.Example2OperatorFactory;
import example.hadoop2.afw.operator.Example2OperatorFactory.AddTime3;
import example.hadoop2.afw.operator.Example2OperatorFactory.ConvertAmount;
import example.hadoop2.afw.operator.Example2OperatorFactory.DistinctNumber;
import example.hadoop2.afw.operator.Example2OperatorFactory.Limit4;
import example.hadoop2.afw.operator.Example2OperatorFactory.Sum1;
import example.hadoop2.afw.operator.Example2OperatorFactory.Sum2;
import example.hadoop2.afw.operator.Example2OperatorFactory.Sum3;
import example.hadoop2.afw.operator.Example2OperatorFactory.Sum4;
import example.hadoop2.afw.operator.Example2OperatorFactory.UpdateHH;

@JobFlow(name = "Example2AfwJob")
public class Example2Job extends FlowDescription {

	private In<InputModel> in;
	private Out<Out1Model> out1;
	private Out<Out2Model> out2;
	private Out<Out3Model> out3;
	private Out<Out4Model> out4;

	public Example2Job(
			@Import(name = "in", description = InputFromCSV.class) In<InputModel> in,
			@Export(name = "out1", description = Out1ToCSV.class) Out<Out1Model> out1,
			@Export(name = "out2", description = Out2ToCSV.class) Out<Out2Model> out2,
			@Export(name = "out3", description = Out3ToCSV.class) Out<Out3Model> out3,
			@Export(name = "out4", description = Out4ToCSV.class) Out<Out4Model> out4) {
		this.in = in;
		this.out1 = out1;
		this.out2 = out2;
		this.out3 = out3;
		this.out4 = out4;
	}

	@Override
	protected void describe() {
		CoreOperatorFactory core = new CoreOperatorFactory();
		Example2OperatorFactory operators = new Example2OperatorFactory();

		ConvertAmount amount = operators.convertAmount(in);
		core.stop(amount.original);

		{
			Sum1 sum = operators.sum1(amount.out);
			out1.add(sum.out);
		}
		{
			DistinctNumber distinct = operators.distinctNumber(in);
			Sum2 sum = operators.sum2(distinct.out);
			out2.add(sum.out);
		}
		{
			UpdateHH conv = operators.updateHH(amount.out);
			Sum3 sum = operators.sum3(conv.out);
			AddTime3 add = operators.addTime3(sum.out);
			out3.add(add.out);
		}
		{
			Sum4 sum = operators.sum4(amount.out);
			Limit4 limit = operators.limit4(sum.out);
			out4.add(limit.out);
		}
	}
}
