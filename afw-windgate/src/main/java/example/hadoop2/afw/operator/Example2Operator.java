package example.hadoop2.afw.operator;

import java.util.List;

import org.apache.hadoop.io.Text;

import com.asakusafw.runtime.core.BatchContext;
import com.asakusafw.runtime.core.Result;
import com.asakusafw.vocabulary.flow.processor.PartialAggregation;
import com.asakusafw.vocabulary.model.Key;
import com.asakusafw.vocabulary.operator.Convert;
import com.asakusafw.vocabulary.operator.GroupSort;
import com.asakusafw.vocabulary.operator.Summarize;
import com.asakusafw.vocabulary.operator.Update;

import example.hadoop2.afw.modelgen.dmdl.model.AmountModel;
import example.hadoop2.afw.modelgen.dmdl.model.InputModel;
import example.hadoop2.afw.modelgen.dmdl.model.NumberModel;
import example.hadoop2.afw.modelgen.dmdl.model.Out1Model;
import example.hadoop2.afw.modelgen.dmdl.model.Out2Model;
import example.hadoop2.afw.modelgen.dmdl.model.Out3Model;
import example.hadoop2.afw.modelgen.dmdl.model.Out4Model;

public abstract class Example2Operator {
	AmountModel amount = new AmountModel();

	@Convert
	public AmountModel convertAmount(InputModel in) {
		amount.setDate(in.getDate());
		amount.setTime(in.getTime());
		amount.setCode(in.getCode());
		amount.setAmount(in.getPrice() * in.getCount());
		return amount;
	}

	@Summarize(partialAggregation = PartialAggregation.PARTIAL)
	public abstract Out1Model sum1(AmountModel in);

	@Summarize(partialAggregation = PartialAggregation.PARTIAL)
	public abstract NumberModel distinctNumber(InputModel in);

	@Summarize(partialAggregation = PartialAggregation.PARTIAL)
	public abstract Out2Model sum2(NumberModel in);

	@Update
	public void updateHH(AmountModel in) {
		in.setTimeAsString(in.getTimeAsString().substring(0, 2));
	}

	@Summarize(partialAggregation = PartialAggregation.PARTIAL)
	public abstract Out3Model sum3(AmountModel in);

	@GroupSort
	public void addTime3(
			@Key(group = { "date" }, order = { "date asc" }) List<Out3Model> list,
			Result<Out3Model> out) {
		Text date = null;

		Out3Model[] table = new Out3Model[24];
		for (Out3Model m : list) {
			int h = Integer.parseInt(m.getTimeAsString());
			table[h] = m;

			if (date == null) {
				date = m.getDate();
			}
		}

		for (int h = 0; h < table.length; h++) {
			Out3Model m = table[h];
			if (m == null) {
				m = new Out3Model();
				m.setDate(date);
				m.setTimeAsString(String.format("%02d", h));
				m.setAmount(0);
			}
			out.add(m);
		}
	}

	@Summarize(partialAggregation = PartialAggregation.PARTIAL)
	public abstract Out4Model sum4(AmountModel in);

	@GroupSort
	public void limit4(
			@Key(group = {}, order = "amount desc") List<Out4Model> list,
			Result<Out4Model> out) {
		int limit = Integer.parseInt(BatchContext.get("LIMIT"));
		int i = 0;
		for (Out4Model value : list) {
			if (++i <= limit) {
				out.add(value);
			} else {
				break;
			}
		}
	}
}
