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

/**
 * Klasa koja upravlja Entitetima u skladistu
 *
 */
public abstract class Specifikacija {

	/**
	 * folder koji predstavlja skladiste
	 */
	private File folder;
	/**
	 * fajl u koji trenutno upisujemo ili koji citamo
	 */
	private File file;
	/**
	 * lista svih Entiteta
	 */
	private ArrayList<Entitet> podaci;
	/**
	 * da li se id dodeljuje automatski ili rucno
	 */
	private boolean isAuto = true;
	/**
	 * max broj Entiteta po fajlu
	 */
	private int maxPoFajlu = 3;
	/**
	 * brojac za autoinkrement
	 */
	private int autoinkrement = 0;
	/**
	 * brojac za Entitete u skladistu
	 */
	private int brojEntiteta = 0;
	
	
	/**
	 * Inicijalizu se nova lista Entiteta
	 */
	public Specifikacija() {
		podaci = new ArrayList<Entitet>();
	}
	/**
	 * upisi() je metoda koju implementacije pozivaju za upisivanje u fajl
	 */
	public abstract void upisi();
	/**
	 * ucitaj() je metoda koju implementacije pozivaju za citanje iz fajla
	 */
	public abstract boolean ucitaj();
	
	/**
	 * postaviSkladiste(boolean, File, boolean) je metoda koja postavlja skladiste prema prosledjenim parametrima
	 * @param novoSkladiste - boolean vrednost da li je odabrano skladiste novo (true) ili postojece (false),
	 * prosledjena od korisnika
	 * @param folder - folder prosledjen od korisnika koji predstavlja skladiste
	 * @param isAuto - boolean vrednost da li se id dodeljuje manuelno (false) ili je autoinkrement (true),
	 * prosledjena od korisnika
	 */
	public boolean postaviSkladiste(boolean novoSkladiste, File folder, boolean isAuto) {
		setFolder(folder);
		
		File file = new File(this.getFolder().getAbsolutePath() + "/config.txt");
		boolean moze = true;
        try {
            file.createNewFile();
            if(novoSkladiste) {
            	postaviConfig(file, isAuto);
            	this.isAuto = isAuto;
            } else {
            	procitajConfig(file);
        		moze = ucitaj();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return moze;
		
		
	}
	/**
	 * procitajConfig(File) je metoda koja prolazi kroz prosledjeni fajl (config.txt) i u zavisnosti od sadrzaja 
	 * setuje vrednosti promenljivih
	 * @param file - prosledjeni fajl (config.txt) kroz koji se prolazi
	 * @throws IOException - metoda baca IOException ukoliko dodje do greske prilkom citanja iz fajla
	 */
	private void procitajConfig(File file) throws IOException {
		BufferedReader buffReader = new BufferedReader(new FileReader(file));
		List<String> parametri = new ArrayList<String>();
		for(int i = 0; i < 4; i++) {
			String line = buffReader.readLine();
			parametri.add(line);
		}
		for (String string : parametri) {
			String[] par = string.split(":");
			if(par[0].equals("maxPoFajlu")) this.maxPoFajlu = Integer.parseInt(par[1]);
			if(par[0].equals("isAuto")) this.isAuto = par[1].equals("true") ? true : false;
			if(par[0].equals("autoinkrement")) this.autoinkrement = Integer.parseInt(par[1]);
			if(par[0].equals("brojEntiteta")) this.brojEntiteta = Integer.parseInt(par[1]);
		}
		buffReader.close();
	}

	/**
	 * postaviConfig(File, boolean) je metoda koja prilikom postavljanja novog skladista upisujuje u 
	 * prosledjeni config.txt fajl default vrednosti za max broj entiteta po fajlu (3), da li je 
	 * dodeljivanje id-ja automatsko (autoinkrement) ili manuelno i postavlja brojace na 0 (za autoinkrement 
	 * i trenutni broj entiteta u fajlu)
	 * @param file - prosledjeni fajl u koji se upisuje
	 * @param isAuto - prosledjena vrednost da li je dodeljivanje id-ja automatsko (true) ili manuelno (false)
	 * @throws IOException - metoda baca IOException ukoliko dodje do greske prilkom upisivanja u fajl
	 */
	private void postaviConfig(File file, boolean isAuto) throws IOException{
		BufferedWriter buffWriter = new BufferedWriter(new FileWriter(file));
		buffWriter.write("maxPoFajlu:3\n");
		buffWriter.write("isAuto:" + isAuto + "\n");
		buffWriter.write("autoinkrement:0" + "\n");
		buffWriter.write("brojEntiteta:0");
		buffWriter.close();
	}

	/**
	 * dodaj(String, String) je metoda koja na osnovu prosledjenih parametara kreira novi Entitet i 
	 * ubacuje ga u listu svih Entiteta i zatim poziva metodu upisi() za cuvanje u fajl.
	 * @param naziv - naziv Entiteta prosledjen od korisnika
	 * @param textArea - podaci o Entitetu prosledjeni od korisnika
	 */
	public void dodaj(String naziv, String textArea) {
		Map<String, String> polja = parsirajTextarea(textArea);
		Entitet noviEntitet = new Entitet(this.getAutoinkrement(), naziv, polja);
		podaci.add(noviEntitet);
		upisi();
	}
	/**
	 * dodaj(String, String, String) je metoda koja na osnovu prosledjenih parametara kreira novi Entitet 
	 * (ukoliko je id validan), a zatim poziva metodu upisi() koja cuva podatke u fajlu. 
	 * @param idString - id Entiteta prosledjen od korisnika
	 * @param naziv - naziv Entiteta prosledjen od korisnika
	 * @param textArea - podaci o Entitetu prosledjeni od korisnika
	 * @return - metoda vraca boolean da li je Entitet uspesno dodat ili nije
	 */
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
	
	/**
	 * dodajUgnjezdeni(String, String, String) je metoda koja na osnovu prosledjenih parametara kreira novi
	 * ugnjezdeni Entitet odgovarajucem Entitetu i dodaje ga u njegovu listu ugnjezdenih Entiteta, a zatim 
	 * poziva metodu upisi() koja cuva podatke u fajlu. 
	 * @param spoljniId - id Entiteta kome dodajemo ugnjezdeni Entitet prosledjen od korisnika
	 * @param naziv - naziv ugnjezdenog Entiteta prosledjen od korisnika
	 * @param textArea - podaci o ugnjezdenom Entitetu prosledjeni od korisnika
	 */
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
	/**
	 * dodajUgnjezdeni(String, String, String, String) je metoda koja na osnovu prosledjenih parametara 
	 * kreira novi ugnjezdeni Entitet odgovarajucem Entitetu (ukoliko je id validan) i dodaje ga u 
	 * njegovu listu ugnjezdenih Entiteta, a zatim poziva metodu upisi() koja cuva podatke u fajlu. 
	 * @param spoljniId - id Entiteta kome dodajemo ugnjezdeni Entitet prosledjen od korisnika
	 * @param idString - id ugnjezdenog Entiteta prosledjen od korisnika
	 * @param naziv - naziv ugnjezdenog Entiteta prosledjen od korisnika
	 * @param textArea - podaci o ugnjezdenom Entitetu prosledjeni od korisnika
	 * @return - metoda vraca boolean da li je ugnjezdeni Entitet uspesno dodat ili nije
	 */
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

	/**
	 * obrisi(String) je metoda koja brise Entitete iz liste svih Entiteta prema prosledjenom parametru (id 
	 * Entiteta), a zatim poziva metodu upisi() koja cuva izmene u fajlu.
	 * @param idString - id Entiteta za brisanje prosledjen od korisnika
	 */
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
	/**
	 * obrisi(String, String) je metoda koja brise Entitete iz liste svih Entiteta prema prosledjenim 
	 * parametrima (nazivu Entiteta i podacima o Entitetu), a zatim poziva metodu upisi() koja cuva 
	 * izmene u fajlu.
	 * @param naziv - naziv Entiteta za brisanje prosledjen od korisnika
	 * @param textArea - podaci o Entitetu za brisanje prosledjeni od korisnika
	 */
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
	
	/**
	 * pretrazi(String, String) je metoda koja pretrazuje Entitete u listi svih Entiteta, prema 
	 * prosledjenim parametrima (nazivu Entiteta i podacacima o Entitetu) i vraca listu pronadjenih Entiteta
	 * @param naziv - naziv Entiteta za pretragu prosledjen od korisnika
	 * @param textArea - podaci o Entitetu za pretragu prosledjeni od korisnika
	 * @return - metoda vraca listu Entiteta sa odgovarajucim vrednostima
	 */
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
		return zaPretragu;
	}
	/**
	 * pretrazi(String) je metoda koja pretrazuje Entitete u listi svih Entiteta, prema prosledjenom parametru
	 * (id Entiteta) i vraca listu sa pronadjenim Entitetom
	 * @param idString - id Entiteta za pretragu prosledjen od korisnika
	 * @return - metoda vraca listu sa pronadjenim Entitetom
	 */
	public List<Entitet> pretrazi(String idString) {
		int id = Integer.parseInt(idString);
		List<Entitet> zaPretragu = new ArrayList<Entitet>();
		for(Entitet entitet: podaci) {
			if(id == entitet.getId()) {
				zaPretragu.add(entitet);
				break;
			}
			
		}
		return zaPretragu;
	}
	
	/**
	 * sortiraj(boolean, boolean, List) je metoda koja sortira Entitete iz prosledjene liste
	 * Entiteta prema prosledjenim parametrima (rastuce ili opadajuce i po id-ju ili po nazivu), na nacin
	 * koji je definisan u compareTo metodi
	 * @param asc - boolean vrednost da li se sortiranje vrsi rastuce (true) ili opadajuce (false)
	 * @param sortById - boolean vrednoost da li se sortiranje vrsi po id-ju (true) ili po nazivu (false)
	 * @param entiteti - lista Entiteta koja se sortira
	 */
	public void sortiraj(boolean asc, boolean sortById, List<Entitet> entiteti) {
		for (Entitet entitet : entiteti) {
			if(asc) entitet.setAsc(true);
			else entitet.setAsc(false);
			if(sortById) entitet.setSortById(true);
			else entitet.setSortById(false);
		}
		Collections.sort(entiteti);
	}
	
	/**
	 * parsirajTextarea(String) je metoda koja parsira uneti tekst i smesta ga u parove kljuc-vrednost,
	 * koje i vraca.
	 * @param tekst - uneti tekst koji se parsira
	 * @return - metoda vraca mapu polja kljuc-vrednost
	 */
	private Map<String, String> parsirajTextarea(String tekst) {
		Map<String, String> polja = new HashMap<String, String>();
		String[] redovi = tekst.split("\n");
		for(int i = 0; i < redovi.length; i++) {
			String[] red = redovi[i].split(":");
			String kljuc = red[0];
			String vrednost = red[1];
			polja.put(kljuc, vrednost);
		}
		return polja;
	}
	
	/**
	 * clearFile() je metoda koja brise ceo sadrzaj fajla
	 */
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

	public int getBrojEntiteta() {
		return brojEntiteta;
	}

	/**
	 * setBrojEntiteta(int) je metoda koja u config.txt fajlu brojac Entiteta postavlja na prosledjenu
	 * vrednost
	 * @param brojEntiteta - broj Entiteta u skladistu
	 */
	public void setBrojEntiteta(int brojEntiteta) {
		this.brojEntiteta = brojEntiteta;
		File file = new File(this.getFolder().getAbsolutePath() + "/config.txt");
        try {
			BufferedWriter buffWriter = new BufferedWriter(new FileWriter(file));
			buffWriter.write("maxPoFajlu:" + this.getMaxPoFajlu() + "\n");
			buffWriter.write("isAuto:" + this.isAuto() + "\n");
			buffWriter.write("autoinkrement:" + autoinkrement + "\n");
			buffWriter.write("brojEntiteta:" + brojEntiteta);
			buffWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * getAutoinkrement() je metoda koja postavlja vrednost autoinkrement brojaca za automatsko
	 * dodeljivanje id-ja (uvek ga povecava za 1)
	 * @return - metoda vraca vrednost autoinkrementa pre povecavanja
	 */
	public int getAutoinkrement() {
		int temp = autoinkrement;
		autoinkrement++;
		File file = new File(this.getFolder().getAbsolutePath() + "/config.txt");
        try {
			BufferedWriter buffWriter = new BufferedWriter(new FileWriter(file));
			buffWriter.write("maxPoFajlu:" + this.getMaxPoFajlu() + "\n");
			buffWriter.write("isAuto:" + this.isAuto() + "\n");
			buffWriter.write("autoinkrement:" + autoinkrement + "\n");
			buffWriter.write("brojEntiteta:" + brojEntiteta);
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
