package example.hadoop2.afw.jobflow;

import example.hadoop2.afw.modelgen.dmdl.csv.AbstractOut1ModelCsvExporterDescription;

public class Out1ToCSV extends AbstractOut1ModelCsvExporterDescription {

	@Override
	public String getProfileName() {
		return InputFromCSV.PROFILE_NAME;
	}

	@Override
	public String getPath() {
		return "afw/out1.txt";
	}
}
