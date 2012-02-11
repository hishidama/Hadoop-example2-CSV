package example.hadoop2.cascading;

import java.util.Iterator;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Buffer;
import cascading.operation.BufferCall;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;
import cascading.tuple.TupleEntryCollector;

import static example.hadoop2.cascading.FieldName.*;

public class TimeAddBuffer extends BaseOperation<TimeAddBuffer.Context>
		implements Buffer<TimeAddBuffer.Context> {
	private static final long serialVersionUID = 1L;

	public static class Context {
	}

	// コンストラクター
	public TimeAddBuffer() {
		super(new Fields(F_DATE, F_TIME, F_AMOUNT));
	}

	@Override
	public void operate(FlowProcess flowProcess, BufferCall<Context> bufferCall) {
		TupleEntryCollector collector = bufferCall.getOutputCollector();

		TupleEntry[] table = new TupleEntry[24];
		String date = null;

		for (Iterator<TupleEntry> i = bufferCall.getArgumentsIterator(); i
				.hasNext();) {
			TupleEntry entry = i.next();

			TupleEntry outputEntry = new TupleEntry(fieldDeclaration,
					Tuple.size(fieldDeclaration.size()));
			outputEntry.set(F_DATE, entry.get(F_DATE));
			outputEntry.set(F_TIME, entry.get(F_TIME));
			outputEntry.set(F_AMOUNT, entry.get(F_AMOUNT));

			int h = entry.getInteger(F_TIME);
			table[h] = outputEntry;

			if (date == null) {
				date = entry.getString(F_DATE);
			}
		}

		for (int h = 0; h < table.length; h++) {
			TupleEntry entry = table[h];

			if (entry == null) {
				entry = new TupleEntry(fieldDeclaration,
						Tuple.size(fieldDeclaration.size()));
				entry.set(F_DATE, date);
				entry.set(F_TIME, String.format("%02d", h));
				entry.set(F_AMOUNT, 0L);
			}

			collector.add(entry);
		}
	}
}
