package soft.komp.maven_specifikacija;

import java.io.File;

public class ExportManager {
	
	private static Specifikacija specifikacija;
	
	public static void registerExporter(Specifikacija spec) {
		specifikacija = spec;		
	}
	
	public static Specifikacija getExporter(File file) {
		specifikacija.setFile(file);
		return specifikacija;
	}

}
