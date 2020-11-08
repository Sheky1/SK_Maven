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
		int maxPoFajlu = super.getMaxPoFajlu();
		int n = super.getBrojEntiteta()/maxPoFajlu + 1;
		for(int i = 0; i < n; i++) {
			File file = new File(super.getFolder().getAbsolutePath() + "/skladiste" + i + ".json");
		    try {
				file.createNewFile();
				super.setFile(file);
		        BufferedReader buffReader = new BufferedReader(new FileReader(super.getFile()));
		        String line;
		        Gson gson = new Gson();
		        Type type = new TypeToken<List<Entitet>>() {}.getType();
		        while ((line = buffReader.readLine()) != null) {
		        	super.getPodaci().addAll((Collection<? extends Entitet>) gson.fromJson(line, type));
		        }
		        buffReader.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	    }
	}

	@Override
	public void upisi() {
		int maxPoFajlu = super.getMaxPoFajlu();
		this.setBrojEntiteta(super.getPodaci().size());
		int n = super.getBrojEntiteta()/maxPoFajlu + 1;
		for(int i = 0; i < n; i++) {
			List<Entitet> zaUpis = new ArrayList<Entitet>();
			for(int j = i * maxPoFajlu; j < i * maxPoFajlu + maxPoFajlu; j++) {
				if(super.getPodaci().size() > j) zaUpis.add(super.getPodaci().get(j));
			}
			File file = new File(super.getFolder().getAbsolutePath() + "/skladiste" + i + ".json");
		    try {
				file.createNewFile();
				super.setFile(file);
				clearFile();
		        BufferedWriter buffWriter = new BufferedWriter(new FileWriter(super.getFile(), true));
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
