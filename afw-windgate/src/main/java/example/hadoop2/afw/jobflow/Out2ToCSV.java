package example.hadoop2.afw.jobflow;

import example.hadoop2.afw.modelgen.dmdl.csv.AbstractOut2ModelCsvExporterDescription;

public class Out2ToCSV extends AbstractOut2ModelCsvExporterDescription {

	@Override
	public String getProfileName() {
		return InputFromCSV.PROFILE_NAME;
	}

	@Override
	public String getPath() {
		return "afw/out2.txt";
	}
}
