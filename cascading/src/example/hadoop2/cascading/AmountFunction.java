package example.hadoop2.cascading;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Function;
import cascading.operation.FunctionCall;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

import static example.hadoop2.cascading.FieldName.*;

public class AmountFunction extends BaseOperation<AmountFunction.Context>
		implements Function<AmountFunction.Context> {
	private static final long serialVersionUID = 1L;

	static class Context {
	}

	/** コンストラクター */
	public AmountFunction() {
		// 出力する項目名(と項目の個数)を指定
		super(new Fields(F_DATE, F_TIME, F_CODE, F_AMOUNT));
	}

	@Override
	public void operate(FlowProcess flowProcess,
			FunctionCall<Context> functionCall) {
		TupleEntry entry = functionCall.getArguments(); // 入力データ

		TupleEntry outEntry = new TupleEntry(fieldDeclaration,
				Tuple.size(fieldDeclaration.size()));
		outEntry.set(F_DATE, entry.get(F_DATE));
		outEntry.set(F_TIME, entry.get(F_TIME));
		outEntry.set(F_CODE, entry.get(F_CODE));
		long price = entry.getInteger(F_PRICE);
		long count = entry.getInteger(F_COUNT);
		outEntry.set(F_AMOUNT, price * count);

		functionCall.getOutputCollector().add(outEntry); // 出力
	}
}
