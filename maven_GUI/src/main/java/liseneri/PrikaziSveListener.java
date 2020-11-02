package liseneri;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.GlavniProzor;

public class PrikaziSveListener implements ActionListener {

	public void actionPerformed(ActionEvent arg0) {
		GlavniProzor.getProzor().getTableModel().update();
	}

}
