package model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import gui.GlavniProzor;
import soft.komp.maven_specifikacija.Entitet;

public class TableModel extends DefaultTableModel {
	
	private List<Entitet> lista;
	
	public TableModel() {
        super(new String[]{"ID","Ime", "Podaci"}, 0);
        lista = new ArrayList<Entitet>();
    }
    
    public void update() {
    	this.setRowCount(0);
        List<Entitet> podaci = GlavniProzor.getProzor().getSkladiste().getPodaci();
    	for (Entitet entitet : podaci) {
			int id = entitet.getId();
			String naziv = entitet.getNaziv();
			String info = entitet.getProstiPodaci().toString();
			String infoUgnj = entitet.getUgnjezdeni().toString();
			
			Object[] data = {id, naziv, info + infoUgnj};
			
			this.addRow(data);
		}
    	setLista(podaci);
    }
    
    public void update(List<Entitet> podaci) {
    	this.setRowCount(0);
    	for (Entitet entitet : podaci) {
			int id = entitet.getId();
			String naziv = entitet.getNaziv();
			String info = entitet.getProstiPodaci().toString();
			
			Object[] data = {id, naziv, info};
			
			this.addRow(data);
		}
    	setLista(podaci);
    }
    
    public void updateAgain() {
    	this.setRowCount(0);
    	for (Entitet entitet : this.getLista()) {
			int id = entitet.getId();
			String naziv = entitet.getNaziv();
			String info = entitet.getProstiPodaci().toString();
			
			Object[] data = {id, naziv, info};
			
			this.addRow(data);
		}
    }

	public List<Entitet> getLista() {
		return lista;
	}

	public void setLista(List<Entitet> lista) {
		this.lista = lista;
	}
    
    
    
}
