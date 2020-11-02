package soft.komp.maven_specifikacija;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Specifikacija {

	private File folder;
	private File file;
	private ArrayList<Entitet> podaci;
	private boolean isAuto = true;
	private int maxPoFajlu = 10;
	private int autoinkrement = 0;
	
	public Specifikacija() {
		podaci = new ArrayList<Entitet>();
	}

	public abstract void namestiBazu(boolean novoSkladiste);
	public abstract void upisi();
	public abstract void ucitaj();
	
	public void postaviSkladiste(boolean novoSkladiste, File folder, boolean isAuto) {
		setFolder(folder);
		
		File file = new File(this.getFolder().getAbsolutePath() + "/config.txt");
        try {
            file.createNewFile();
            if(novoSkladiste) {
            	postaviConfig(file, isAuto);
            	this.isAuto = isAuto;
            } else {
            	procitajConfig(file);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		namestiBazu(novoSkladiste);
		ucitaj();
		
	}

	private void procitajConfig(File file) throws IOException {
		BufferedReader buffReader = new BufferedReader(new FileReader(file));
		List<String> parametri = new ArrayList<String>();
		for(int i = 0; i < 3; i++) {
			String line = buffReader.readLine();
			System.out.println(line);
			parametri.add(line);
		}
		for (String string : parametri) {
			String[] par = string.split(":");
			if(par[0].equals("maxPoFajlu")) this.maxPoFajlu = Integer.parseInt(par[1]);
			if(par[0].equals("isAuto")) this.isAuto = par[1].equals("true") ? true : false;
			if(par[0].equals("autoinkrement")) this.autoinkrement = Integer.parseInt(par[1]);
		}
		buffReader.close();
	}

	private void postaviConfig(File file, boolean isAuto) {
		try {
			BufferedWriter buffWriter = new BufferedWriter(new FileWriter(file));
			buffWriter.write("maxPoFajlu:10\n");
			buffWriter.write("isAuto:" + isAuto + "\n");
			buffWriter.write("autoinkrement:0");
			buffWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dodaj(String naziv, String textArea) {
		Map<String, String> polja = parsirajTextarea(textArea);
		Entitet noviEntitet = new Entitet(this.getAutoinkrement(), naziv, polja);
		podaci.add(noviEntitet);
		upisi();
	}
	public boolean dodaj(String idString, String naziv, String textArea) { 
		boolean uspeo = true;
		int id = Integer.parseInt(idString);
		for (Entitet entitet : podaci) {
			if(entitet.getId() == id) uspeo = false;
		}
		if(uspeo) {
			Map<String, String> polja = parsirajTextarea(textArea);
			Entitet noviEntitet = new Entitet(id, naziv, polja);
			podaci.add(noviEntitet);
			upisi();
		}
		return uspeo;
	}
	
	public void dodajUgnjezdeni(String spoljniId, String naziv, String textArea) {
		int id = Integer.parseInt(spoljniId);
		Entitet spoljni = null;
		for (Entitet entitet : podaci) {
			if(id == entitet.getId()) {
				spoljni = entitet;
				break;
			}
		}
		Map<String, String> polja = parsirajTextarea(textArea);
		Entitet noviEntitet = new Entitet(this.getAutoinkrement() ,naziv, polja);
		spoljni.getUgnjezdeni().put(noviEntitet.getId(), noviEntitet);
		upisi();
	}
	public boolean dodajUgnjezdeni(String spoljniId, String idString, String naziv, String textArea) { 
		boolean uspeo = true;
		int id = Integer.parseInt(spoljniId);
		Entitet spoljni = null;
		for (Entitet entitet : podaci) {
			if(id == entitet.getId()) {
				spoljni = entitet;
				break;
			}
		}
		int idUgnj = Integer.parseInt(idString);
		if(!spoljni.getUgnjezdeni().containsKey(idUgnj)) uspeo = false;
		if(uspeo) {
			Map<String, String> polja = parsirajTextarea(textArea);
			Entitet noviEntitet = new Entitet(idUgnj, naziv, polja);
			spoljni.getUgnjezdeni().put(noviEntitet.getId(), noviEntitet);
			upisi();
		}
		return uspeo;
	}

	public void obrisi(String idString) {
		int id = Integer.parseInt(idString);
		int index = -1;
		for (Entitet entitet : podaci) {
			if(id == entitet.getId()) {
				index = podaci.indexOf(entitet);
				break;
			}
		}
		podaci.remove(index);
		upisi();
	}
	public void obrisi(String naziv, String textArea) {
		Map<String, String> polja = parsirajTextarea(textArea);
		List<Entitet> zaBrisanje = new ArrayList<Entitet>();
		for (Entitet entitet : podaci) {
			int neBrisi = 0;
			if((entitet.getNaziv()).equals(naziv)) {
				for(Map.Entry<String, String> par: polja.entrySet()) {
					if(entitet.getProstiPodaci().containsKey(par.getKey())) {
						if(!(entitet.getProstiPodaci().get(par.getKey()).equals(par.getValue()))) {
							neBrisi = 1;
						}
					}
					else {
						neBrisi = 1;
					}
				}
				if(neBrisi == 0) {
					zaBrisanje.add(entitet);
				}
			}
		}
		podaci.removeAll(zaBrisanje);
		upisi();
	}
	
	public List<Entitet> pretrazi(String naziv, String textArea) {
		Map<String, String> polja = parsirajTextarea(textArea);
		List<Entitet> zaPretragu = new ArrayList<Entitet>();
		for(Entitet entitet: podaci) {
			int nePretrazuj = 0;
			if((entitet.getNaziv()).equals(naziv)) {
				for(Map.Entry<String, String> par: polja.entrySet()) {
					if(entitet.getProstiPodaci().containsKey(par.getKey())) {
						if(!(entitet.getProstiPodaci().get(par.getKey()).equals(par.getValue()))) {
							nePretrazuj = 1;
						}
					}
					else {
						nePretrazuj = 1;
					}
				}
				if(nePretrazuj == 0) {
					zaPretragu.add(entitet);
				}
			}
		}
		System.out.println(zaPretragu);
		return zaPretragu;
	}
	public List<Entitet> pretrazi(String idString) {
		int id = Integer.parseInt(idString);
		List<Entitet> zaPretragu = new ArrayList<Entitet>();
		for(Entitet entitet: podaci) {
			if(id == entitet.getId()) {
				zaPretragu.add(entitet);
				break;
			}
			
		}
		System.out.println(zaPretragu);
		return zaPretragu;
	}
	
	public void sortiraj(boolean asc, boolean sortById, List<Entitet> entiteti) {
		for (Entitet entitet : entiteti) {
			if(asc) entitet.setAsc(true);
			else entitet.setAsc(false);
			if(sortById) entitet.setSortById(true);
			else entitet.setSortById(false);
		}
		Collections.sort(entiteti);
	}
	
	public HashMap<String, String> parsirajTextarea(String tekst) {
		HashMap<String, String> polja = new HashMap<String, String>();
		String[] redovi = tekst.split("\n");
		for(int i = 0; i < redovi.length; i++) {
			String[] red = redovi[i].split(":");
			String kljuc = red[0];
			String vrednost = red[1];
			polja.put(kljuc, vrednost);
		}
		System.out.println(polja);
		return polja;
	}
	
	public void clearFile() {
        FileWriter fw;
		try {
			fw = new FileWriter(this.getFile());
	        PrintWriter writer = new PrintWriter(fw, false);
	        writer.print("");
	        writer.flush();
	        writer.close();
	        fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }

	public boolean isAuto() {
		return isAuto;
	}

	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}

	public int getMaxPoFajlu() {
		return maxPoFajlu;
	}

	public void setMaxPoFajlu(int maxPoFajlu) {
		this.maxPoFajlu = maxPoFajlu;
	}

	public int getAutoinkrement() {
		int temp = autoinkrement;
		autoinkrement++;
		File file = new File(this.getFolder().getAbsolutePath() + "/config.txt");
        try {
			BufferedWriter buffWriter = new BufferedWriter(new FileWriter(file));
			buffWriter.write("maxPoFajlu:" + this.getMaxPoFajlu() + "\n");
			buffWriter.write("isAuto:" + this.isAuto() + "\n");
			buffWriter.write("autoinkrement:" + autoinkrement);
			buffWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

	public void setAutoinkrement(int autoinkrement) {
		this.autoinkrement = autoinkrement;
	}

	public File getFolder() {
		return folder;
	}

	public void setFolder(File folder) {
		this.folder = folder;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public ArrayList<Entitet> getPodaci() {
		return podaci;
	}

	public void setPodaci(ArrayList<Entitet> podaci) {
		this.podaci = podaci;
	}

	@Override
	public String toString() {
		return "Podaci:" + podaci + "";
	}
	
}
