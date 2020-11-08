package soft.komp.maven_XML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;

import soft.komp.maven_specifikacija.Entitet;
import soft.komp.maven_specifikacija.ExportManager;
import soft.komp.maven_specifikacija.Specifikacija;

public class ImplementacijaXML extends Specifikacija {

	static {
		ExportManager.registerExporter(new ImplementacijaXML());
	}
	
	@Override
	public void ucitaj() {
	    try {
	    	ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	    	mapper.findAndRegisterModules();
	    	CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Entitet.class);
			int maxPoFajlu = super.getMaxPoFajlu();
			int n = super.getBrojEntiteta()/maxPoFajlu + 1;
			for(int i = 0; i < n; i++) {
				File file = new File(super.getFolder().getAbsolutePath() + "/skladiste" + i + ".yaml");
				file.createNewFile();
				super.setFile(file);
		        List<Entitet> lista = mapper.readValue(super.getFile(), listType);
		        super.getPodaci().addAll(lista);
			}
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public void upisi() {
    	ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
	    try {
			int maxPoFajlu = super.getMaxPoFajlu();
			super.setBrojEntiteta(super.getPodaci().size());
			int n = super.getBrojEntiteta()/maxPoFajlu + 1;
			for(int i = 0; i < n; i++) {
				List<Entitet> zaUpis = new ArrayList<Entitet>();
				for(int j = i * maxPoFajlu; j < i * maxPoFajlu + maxPoFajlu; j++) {
					if(super.getPodaci().size() > j) zaUpis.add(super.getPodaci().get(j));
				}
				File file = new File(super.getFolder().getAbsolutePath() + "/skladiste" + i + ".yaml");
				file.createNewFile();
				super.setFile(file);
				clearFile();
		    	mapper.writeValue(super.getFile(), zaUpis);
			}
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
}
