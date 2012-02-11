package example.hadoop2.afw.batch;

import com.asakusafw.vocabulary.batch.Batch;
import com.asakusafw.vocabulary.batch.BatchDescription;

import example.hadoop2.afw.jobflow.Example2Job;

@Batch(name = "Example2AfwBatch")
public class Example2Batch extends BatchDescription {

	@Override
	protected void describe() {
		run(Example2Job.class).soon();
	}
}
