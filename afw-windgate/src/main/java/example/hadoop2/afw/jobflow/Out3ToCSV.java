package example.hadoop2.afw.jobflow;

import example.hadoop2.afw.modelgen.dmdl.csv.AbstractOut3ModelCsvExporterDescription;

public class Out3ToCSV extends AbstractOut3ModelCsvExporterDescription {

	@Override
	public String getProfileName() {
		return InputFromCSV.PROFILE_NAME;
	}

	@Override
	public String getPath() {
		return "afw/out3.txt";
	}
}
