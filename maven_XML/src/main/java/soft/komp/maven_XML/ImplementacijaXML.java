package soft.komp.maven_XML;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;

import soft.komp.maven_specifikacija.Entitet;
import soft.komp.maven_specifikacija.Specifikacija;

public class ImplementacijaXML extends Specifikacija {

	@Override
	public void namestiBazu(boolean novoSkladiste) {
		if(novoSkladiste) {
			try {
			     File file = new File(this.getFolder().getAbsolutePath() + "/skladiste.yaml");
		         file.createNewFile();
		         this.setFile(file);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		else {
			try {
			     File file = new File(this.getFolder().getAbsolutePath() + "/skladiste.yaml");
		         file.createNewFile();
		         this.setFile(file);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}

	@Override
	public void ucitaj() {
	    try {
	    	ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	    	mapper.findAndRegisterModules();
	    	Entitet entitet = mapper.readValue(this.getFile(), Entitet.class);
	    	System.out.println(entitet);

//	        BufferedReader buffReader = new BufferedReader(new FileReader(this.getFile()));
	        
//	        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader("ID", "Naziv", "Podaci").parse(buffReader);
//	        for (CSVRecord record : records) {
//	            System.out.println("ID: " + record.get("ID"));
//	            System.out.println("Naziv: " + record.get("Naziv"));
//	            System.out.println("Podaci: " + record.get("Podaci"));
//	        }
	        
//	        buffReader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public void upisi() {
		clearFile();
	    try {
	    	ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
	    	mapper.writeValue(this.getFile(), this.getPodaci());

	    	
//	        BufferedWriter buffWriter = new BufferedWriter(new FileWriter(this.getFile(), true));
	        
//	        CSVPrinter printer = CSVFormat.DEFAULT.print(buffWriter);
//
//	        for (Entitet entitet: this.getPodaci()) {
//				int id = entitet.getId();
//				String naziv = entitet.getNaziv();
//				String info = entitet.getProstiPodaci().toString();
//				String infoUgnj = entitet.getUgnjezdeni().toString();
//	            printer.printRecord(id, naziv, info + infoUgnj);
//			}
//
//	        printer.flush();

//	        buffWriter.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
}
