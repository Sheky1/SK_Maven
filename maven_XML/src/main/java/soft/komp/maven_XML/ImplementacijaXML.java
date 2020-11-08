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
			int maxPoFajlu = this.getMaxPoFajlu();
			int n = this.getBrojEntiteta()/maxPoFajlu + 1;
			for(int i = 0; i < n; i++) {
				File file = new File(this.getFolder().getAbsolutePath() + "/skladiste" + i + ".yaml");
				file.createNewFile();
				this.setFile(file);
		        List<Entitet> lista = mapper.readValue(this.getFile(), listType);
		    	this.getPodaci().addAll(lista);
			}
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public void upisi() {
    	ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
	    try {
			int maxPoFajlu = this.getMaxPoFajlu();
			this.setBrojEntiteta(this.getPodaci().size());
			int n = this.getBrojEntiteta()/maxPoFajlu + 1;
			for(int i = 0; i < n; i++) {
				List<Entitet> zaUpis = new ArrayList<Entitet>();
				for(int j = i * maxPoFajlu; j < i * maxPoFajlu + maxPoFajlu; j++) {
					if(this.getPodaci().size() > j) zaUpis.add(this.getPodaci().get(j));
				}
				File file = new File(this.getFolder().getAbsolutePath() + "/skladiste" + i + ".yaml");
				file.createNewFile();
				this.setFile(file);
				clearFile();
		    	mapper.writeValue(this.getFile(), zaUpis);
			}
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
}
