package soft.komp.maven_custom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soft.komp.maven_specifikacija.Entitet;
import soft.komp.maven_specifikacija.ExportManager;
import soft.komp.maven_specifikacija.Specifikacija;

public class ImplementacijaCustom extends Specifikacija {

	static {
		ExportManager.registerExporter(new ImplementacijaCustom());
	}
	
	@Override
	public boolean ucitaj() {
		int maxPoFajlu = super.getMaxPoFajlu();
		int brojFajlova = super.getBrojEntiteta()/maxPoFajlu + 1;
		for(int i = 0; i < brojFajlova; i++) {
			File file = new File(super.getFolder().getAbsolutePath() + "/skladiste" + i + ".skc");
			if(!file.exists()) return false;
		    try {
				file.createNewFile();
				super.setFile(file);
				parsirajCitanje();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	    }
		return true;
	}

	@Override
	public void upisi() {
		int maxPoFajlu = super.getMaxPoFajlu();
		super.setBrojEntiteta(super.getPodaci().size());
		int brojFajlova = super.getBrojEntiteta()/maxPoFajlu + 1;
		for(int i = 0; i < brojFajlova; i++) {
			List<Entitet> zaUpis = new ArrayList<Entitet>();
			for(int j = i * maxPoFajlu; j < i * maxPoFajlu + maxPoFajlu; j++) {
				if(super.getPodaci().size() > j) zaUpis.add(super.getPodaci().get(j));
			}
			File file = new File(super.getFolder().getAbsolutePath() + "/skladiste" + i + ".skc");
		    try {
				file.createNewFile();
				super.setFile(file);
				clearFile();
		        parsirajPisanje(zaUpis);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}
	
	public void parsirajPisanje(List<Entitet> zaUpis) throws IOException {
		String upis = "(\n";
		for (Entitet entitet : zaUpis) {
			upis += "\t=>\n";
			upis += upisProstih(entitet, 2);
			upis += "\t\tugnjezdeni->" + upisUgnjezdenih(entitet);
			upis += "\t<=\n";
		}
		upis+=")\n";
		BufferedWriter buffWriter = new BufferedWriter(new FileWriter(super.getFile(), true));
        buffWriter.append(upis);
        buffWriter.close();
	}

	public String upisProstih(Entitet entitet, int brojTabova) {
		String tabovi = brojTabova == 2 ? "\t\t" : "\t\t\t\t";
		String upis = "";
		upis += tabovi + "id->" + entitet.getId() + "\n";
		upis += tabovi + "naziv->" + entitet.getNaziv() + "\n";
		upis += tabovi + "prostiPodaci->" + entitet.getProstiPodaci().toString() + "\n";
		return upis;
	}

	public String upisUgnjezdenih(Entitet entitet) {
		String upis = "";
		if(entitet.getUgnjezdeni().size() == 0) return "{}\n";
		else upis += "{\n";
		for(Map.Entry<Integer, Entitet> par: entitet.getUgnjezdeni().entrySet()) {
			upis += "\t\t\t" + par.getKey() + "->=>\n";
			upis += upisProstih(par.getValue(), 4);
			upis += "\t\t\t<=\n";
		}
		upis += "\t\t}\n";
		return upis;
	}

	public void parsirajCitanje() throws IOException {
		List<Entitet> listaEntiteta = new ArrayList<Entitet>();
		
		BufferedReader buffReader = new BufferedReader(new FileReader(super.getFile()));
        
		String line;
		while ((line = buffReader.readLine()) != null) {
        	if(line.equals("\t=>")) {
        		int id = 0; 
        		String naziv = ""; 
        		Map<String, String> prostiPodaci = new HashMap<String, String>();
        		Map<Integer, Entitet> ugnjezdeni = new HashMap<Integer, Entitet>();
        		while(!(line = buffReader.readLine()).equals("\t<=")) {
            		String[] par;
            		par = line.split("->");
            		if(par[0].equals("\t\tid")) id = Integer.parseInt(par[1]);
            		if(par[0].equals("\t\tnaziv")) naziv = par[1];
            		if(par[0].equals("\t\tprostiPodaci") && !(par[1].equals("{}"))) {
            			String mapa = par[1].substring(1, par[1].length() - 1);
            			String[] parovi = mapa.split(", ");
            			for (String string : parovi) {
							String[] isecenString = string.split("=");
							prostiPodaci.put(isecenString[0], isecenString[1]);
						}
            		}
            		if(par[0].equals("\t\tugnjezdeni") && !(par[1].equals("{}"))) {
            			while(!(line = buffReader.readLine()).equals("\t\t}")) {
                    		String[] pocetakUgnj = line.split("->");
            				String idUgnjezdenog = pocetakUgnj[0].substring(3);
                    		int id2 = 0; 
                    		String naziv2 = ""; 
                    		Map<String, String> prostiPodaci2 = new HashMap<String, String>();
            				while(!(line = buffReader.readLine()).equals("\t\t\t<=")) {
            					if(par[0].equals("\t\t\t\tid")) id2 = Integer.parseInt(par[1]);
                        		if(par[0].equals("\t\t\t\tnaziv")) naziv2 = par[1];
                        		if(par[0].equals("\t\t\t\tprostiPodaci") && !(par[1].equals("{}"))) {
                        			String mapa = par[1].substring(1, par[1].length() - 1);
                        			String[] parovi = mapa.split(", ");
                        			for (String string : parovi) {
            							String[] isecenString = string.split("=");
            							prostiPodaci2.put(isecenString[0], isecenString[1]);
            						}
                        		}
            				}
            				Entitet ugnjezdeniEntitet = new Entitet(id2, naziv2, prostiPodaci2);
            				ugnjezdeni.put(Integer.parseInt(idUgnjezdenog), ugnjezdeniEntitet);
            			}
            		}
        		}
        		Entitet noviEntitet = new Entitet(id, naziv, prostiPodaci);
        		noviEntitet.setUgnjezdeni(ugnjezdeni);
        		listaEntiteta.add(noviEntitet);
        	}
        }
        buffReader.close();
        super.getPodaci().addAll(listaEntiteta);
	}
	
}
