package example.hadoop2.afw.jobflow;

import example.hadoop2.afw.modelgen.dmdl.csv.AbstractInputModelCsvImporterDescription;

public class InputFromCSV extends AbstractInputModelCsvImporterDescription {
	public static final String PROFILE_NAME = "asakusa";

	@Override
	public String getProfileName() {
		return PROFILE_NAME;
	}

	@Override
	public String getPath() {
		return "${SRC_FILE}";
	}
}
