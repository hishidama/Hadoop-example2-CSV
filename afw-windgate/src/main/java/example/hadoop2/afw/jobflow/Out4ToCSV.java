package example.hadoop2.afw.jobflow;

import example.hadoop2.afw.modelgen.dmdl.csv.AbstractOut4ModelCsvExporterDescription;

public class Out4ToCSV extends AbstractOut4ModelCsvExporterDescription {

	@Override
	public String getProfileName() {
		return InputFromCSV.PROFILE_NAME;
	}

	@Override
	public String getPath() {
		return "afw/out4.txt";
	}
}
