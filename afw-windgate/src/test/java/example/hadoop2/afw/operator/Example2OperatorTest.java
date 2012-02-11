package example.hadoop2.afw.operator;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.asakusafw.runtime.testing.MockResult;
import com.asakusafw.testdriver.OperatorTestEnvironment;

import example.hadoop2.afw.modelgen.dmdl.model.AmountModel;
import example.hadoop2.afw.modelgen.dmdl.model.InputModel;
import example.hadoop2.afw.modelgen.dmdl.model.Out3Model;
import example.hadoop2.afw.modelgen.dmdl.model.Out4Model;

public class Example2OperatorTest {
	@Rule
	public OperatorTestEnvironment environment = new OperatorTestEnvironment();

	@Test
	public void convertAmount() {
		Example2Operator operator = new Example2OperatorImpl();

		InputModel in = new InputModel();
		in.setDateAsString("2012/02/10");
		in.setTimeAsString("12:34:56");
		in.setCodeAsString("c987");
		in.setPrice(123);
		in.setCount(2);

		AmountModel r = operator.convertAmount(in);

		assertThat(r.getDateAsString(), is("2012/02/10"));
		assertThat(r.getTimeAsString(), is("12:34:56"));
		assertThat(r.getCodeAsString(), is("c987"));
		assertThat(r.getAmount(), is(246L));
	}

	@Test
	public void updateHH() {
		Example2Operator operator = new Example2OperatorImpl();

		AmountModel in = new AmountModel();
		in.setDateAsString("2011/02/10");
		in.setTimeAsString("12:34:56");
		in.setCodeAsString("c987");
		in.setAmount(111);

		operator.updateHH(in);

		assertThat(in.getDateAsString(), is("2011/02/10"));
		assertThat(in.getTimeAsString(), is("12"));
		assertThat(in.getCodeAsString(), is("c987"));
		assertThat(in.getAmount(), is(111L));
	}

	@Test
	public void addTime3() {
		Example2Operator operator = new Example2OperatorImpl();

		List<Out3Model> list = new ArrayList<Out3Model>();
		list.add(createOut3("02", 123));
		list.add(createOut3("04", 234));
		list.add(createOut3("05", 345));
		list.add(createOut3("07", 456));
		list.add(createOut3("23", 567));

		MockResult<Out3Model> result = new MockResult<Out3Model>();

		operator.addTime3(list, result);

		List<Out3Model> r = result.getResults();
		assertThat(r.size(), is(24));
		for (int i = 0; i < 24; i++) {
			Out3Model m = r.get(i);
			assertThat(m.getDateAsString(), is("2012/02/10"));
			assertThat(m.getTimeAsString(), is(String.format("%02d", i)));
			switch (i) {
			case 2:
				assertThat(m.getAmount(), is(123L));
				break;
			case 4:
				assertThat(m.getAmount(), is(234L));
				break;
			case 5:
				assertThat(m.getAmount(), is(345L));
				break;
			case 7:
				assertThat(m.getAmount(), is(456L));
				break;
			case 23:
				assertThat(m.getAmount(), is(567L));
				break;
			default:
				assertThat(m.getAmount(), is(0L));
				break;
			}
		}
	}

	Out3Model createOut3(String time, long amount) {
		Out3Model m = new Out3Model();
		m.setDateAsString("2012/02/10");
		m.setTimeAsString(time);
		m.setAmount(amount);

		return m;
	}

	@Test
	public void limit4() {
		int limit = 3;
		environment.setBatchArg("LIMIT", Integer.toString(limit));
		environment.reload();

		Example2Operator operator = new Example2OperatorImpl();
		for (int i = 1; i <= limit + 1; i++) {
			List<Out4Model> list = new ArrayList<Out4Model>();
			for (int j = 0; j < i; j++) {
				Out4Model m = new Out4Model();
				m.setDateAsString("2012/02/10");
				m.setCodeAsString("c" + j);
				m.setAmount(-j);
				list.add(m);
			}
			MockResult<Out4Model> result = new MockResult<Out4Model>();
			operator.limit4(list, result);

			List<Out4Model> r = result.getResults();
			int expected = Math.min(i, limit);
			assertThat(r.size(), is(expected));
			for (int j = 0; j < expected; j++) {
				assertThat(r.get(j), is(list.get(j)));
			}
		}
	}
}
