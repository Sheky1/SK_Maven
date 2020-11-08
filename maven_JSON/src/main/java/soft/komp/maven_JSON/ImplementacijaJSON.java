package soft.komp.maven_JSON;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import soft.komp.maven_specifikacija.Entitet;
import soft.komp.maven_specifikacija.ExportManager;
import soft.komp.maven_specifikacija.Specifikacija;

public class ImplementacijaJSON extends Specifikacija {

	static {
		ExportManager.registerExporter(new ImplementacijaJSON());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void ucitaj() {
		int maxPoFajlu = this.getMaxPoFajlu();
		int n = this.getBrojEntiteta()/maxPoFajlu + 1;
		for(int i = 0; i < n; i++) {
			File file = new File(this.getFolder().getAbsolutePath() + "/skladiste" + i + ".json");
		    try {
				file.createNewFile();
				this.setFile(file);
		        BufferedReader buffReader = new BufferedReader(new FileReader(this.getFile()));
		        String line;
		        Gson gson = new Gson();
		        Type type = new TypeToken<List<Entitet>>() {}.getType();
		        while ((line = buffReader.readLine()) != null) {
		        	this.getPodaci().addAll((Collection<? extends Entitet>) gson.fromJson(line, type));
		        }
		        buffReader.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	    }
	}

	@Override
	public void upisi() {
		int maxPoFajlu = this.getMaxPoFajlu();
		this.setBrojEntiteta(this.getPodaci().size());
		int n = this.getBrojEntiteta()/maxPoFajlu + 1;
		for(int i = 0; i < n; i++) {
			List<Entitet> zaUpis = new ArrayList<Entitet>();
			for(int j = i * maxPoFajlu; j < i * maxPoFajlu + maxPoFajlu; j++) {
				if(this.getPodaci().size() > j) zaUpis.add(this.getPodaci().get(j));
			}
			File file = new File(this.getFolder().getAbsolutePath() + "/skladiste" + i + ".json");
		    try {
				file.createNewFile();
				this.setFile(file);
				clearFile();
		        BufferedWriter buffWriter = new BufferedWriter(new FileWriter(this.getFile(), true));
		        Gson gson = new Gson();
		        Type type = new TypeToken<List<Entitet>>() {}.getType();
		        String json = gson.toJson(zaUpis, type);
		        buffWriter.append(json);
		        buffWriter.newLine();
		        buffWriter.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		
	}

}
