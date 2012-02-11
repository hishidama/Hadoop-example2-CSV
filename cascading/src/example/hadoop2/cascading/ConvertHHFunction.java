package example.hadoop2.cascading;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Function;
import cascading.operation.FunctionCall;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

import static example.hadoop2.cascading.FieldName.*;

public class ConvertHHFunction extends BaseOperation<ConvertHHFunction.Context>
		implements Function<ConvertHHFunction.Context> {
	private static final long serialVersionUID = 1L;

	static class Context {
	}

	/** コンストラクター */
	public ConvertHHFunction() {
		// 出力する項目名(と項目の個数)を指定
		super(new Fields(F_DATE, F_TIME, F_AMOUNT));
	}

	@Override
	public void operate(FlowProcess flowProcess,
			FunctionCall<Context> functionCall) {
		TupleEntry entry = functionCall.getArguments(); // 入力データ

		TupleEntry outEntry = new TupleEntry(fieldDeclaration,
				Tuple.size(fieldDeclaration.size()));
		outEntry.set(F_DATE, entry.get(F_DATE));
		String time = entry.getString(F_TIME);
		outEntry.set(F_TIME, time.substring(0, 2));
		outEntry.set(F_AMOUNT, entry.getLong(F_AMOUNT));

		functionCall.getOutputCollector().add(outEntry); // 出力
	}
}
