package gui;

import javax.swing.JButton;
import javax.swing.JToolBar;

import liseneri.DodajListener;
import liseneri.DodajUgnjezdeniListener;
import liseneri.ObrisiListener;
import liseneri.PretraziListener;
import liseneri.PrikaziSveListener;
import liseneri.SortirajListener;

public class Toolbar extends JToolBar{
	
	private JButton dodaj;
	private JButton dodajUgjezdeni;
	private JButton obrisi;
	private JButton pretrazi;
	private JButton soritraj;
	private JButton prikaziSve;
	
	protected Toolbar() {
		
		dodaj = new JButton("Dodaj");
		dodajUgjezdeni = new JButton("Dodaj Ugnjezdeni");
		obrisi = new JButton("Obrisi");
		pretrazi = new JButton("Pretrazi");
		soritraj = new JButton("Sort");
		prikaziSve = new JButton("Prikazi sve");
		
		dodaj.addActionListener(new DodajListener());
		dodajUgjezdeni.addActionListener(new DodajUgnjezdeniListener());
		obrisi.addActionListener(new ObrisiListener());
		pretrazi.addActionListener(new PretraziListener());
		soritraj.addActionListener(new SortirajListener());
		prikaziSve.addActionListener(new PrikaziSveListener());
		
		add(dodaj);
		add(dodajUgjezdeni);
		add(obrisi);
		add(pretrazi);
		add(soritraj);
		add(prikaziSve);
	}
	
	
}
