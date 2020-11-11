package soft.komp.maven_specifikacija;

/**
 * Klasa koja postavlja implementaciju koja ce se koristiti
 */
public class ExportManager {
	
	/**
	 * Objekat specifikacije
	 */
	private static Specifikacija specifikacija;
	
	/**
	 * Postavlja specifikaciju na odredjenu implementaciju
	 * @param spec - objekat implementacije
	 */
	public static void registerExporter(Specifikacija spec) {
		specifikacija = spec;		
	}
	
	/**
	 * Vraca objekat specifikacije
	 * @return - metoda vraca objekat specifikacije
	 */
	public static Specifikacija getExporter() {
		return specifikacija;
	}

}
