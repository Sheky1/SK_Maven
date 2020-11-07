package soft.komp.maven_specifikacija;

import java.util.HashMap;
import java.util.Map;

public class Entitet implements Comparable<Entitet> {

	private int id;
	private String naziv;
	private Map<String, String> prostiPodaci;
	private Map<Integer, Entitet> ugnjezdeni;
	private boolean sortById = true;
	private boolean asc = true;
	
	public Entitet() {
	}
	
	public Entitet(int id, String naziv, Map<String, String> prostiPodaci) {
		this.id = id;
		this.naziv = naziv;
		this.prostiPodaci = prostiPodaci;
		ugnjezdeni = new HashMap<Integer, Entitet>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public Map<String, String> getProstiPodaci() {
		return prostiPodaci;
	}

	public void setProstiPodaci(Map<String, String> prostiPodaci) {
		this.prostiPodaci = prostiPodaci;
	}

	public Map<Integer, Entitet> getUgnjezdeni() {
		return ugnjezdeni;
	}

	public void setUgnjezdeni(Map<Integer, Entitet> ugnjezdeni) {
		this.ugnjezdeni = ugnjezdeni;
	}

	public boolean isSortById() {
		return sortById;
	}

	public void setSortById(boolean sortById) {
		this.sortById = sortById;
	}
	
	public boolean isAsc() {
		return asc;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}

	public int stringCompare(String str1, String str2){ 
        int l1 = str1.length(); 
        int l2 = str2.length(); 
        int lmin = Math.min(l1, l2); 
  
        for (int i = 0; i < lmin; i++) { 
            int str1_ch = (int)str1.charAt(i); 
            int str2_ch = (int)str2.charAt(i); 
            if (str1_ch != str2_ch) { 
                return str1_ch - str2_ch; 
            } 
        } 
        if (l1 != l2) { 
            return l1 - l2; 
        } 
        else { 
            return 0; 
        } 
    }

	public int compareTo(Entitet drugi) {
		if(asc) {
			if(sortById) {
				return this.getId() - drugi.getId();
			}
			else {
				return stringCompare(this.getNaziv(), drugi.getNaziv());
			}
		} else {
			if(sortById) {
				return drugi.getId() - this.getId();
			}
			else {
				return stringCompare(drugi.getNaziv(), this.getNaziv());
			}
		}
		
	}

	@Override
	public String toString() {
		if(ugnjezdeni.size() != 0) {
			return "id:" + id + ", naziv:" + naziv + ", prostiPodaci:" + prostiPodaci + ", ugnjezdeni:"
					+ ugnjezdeni;
		}
		else {
			return "id:" + id + ", naziv:" + naziv + ", prostiPodaci:" + prostiPodaci;
		}
		
	} 
	
}
