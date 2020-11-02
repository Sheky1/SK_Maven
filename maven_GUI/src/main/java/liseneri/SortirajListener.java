package liseneri;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import gui.GlavniProzor;
import soft.komp.maven_specifikacija.Entitet;

public class SortirajListener implements ActionListener{

	public void actionPerformed(ActionEvent e) {
		
		Object[] options = {"Po ID-ju", "Po nazivu"};
		int n = JOptionPane.showOptionDialog(
			GlavniProzor.getProzor(),
			"Kako zelite da sortirate podatke?",
			"Odabir nacina sortiranja",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,
			options,
			options[1]
		);
		
		Object[] options2 = {"ASC", "DESC"};
		int m = JOptionPane.showOptionDialog(
			GlavniProzor.getProzor(),
			"Po kom poretku?",
			"Odabir nacina sortiranja",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,
			options2,
			options2[1]
		);
		
		boolean poID = true;
		boolean asc = true;
		
		if(n == JOptionPane.NO_OPTION) poID = false;
		if(m == JOptionPane.NO_OPTION) asc = false;
		
		GlavniProzor.getProzor().getSkladiste().sortiraj(asc, poID, (ArrayList<Entitet>) GlavniProzor.getProzor().getTableModel().getLista());
		GlavniProzor.getProzor().getTableModel().updateAgain();
		
	}

}
