package liseneri;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import exceptions.ExceptionHandler;
import gui.GlavniProzor;

public class ObrisiListener implements ActionListener{

	public void actionPerformed(ActionEvent e) {
		
		JTextArea textArea = new JTextArea();

		Object[] options = {"Po ID-ju", "Po parametrima", "Odustani"};
		int n = JOptionPane.showOptionDialog(
			GlavniProzor.getProzor(),
			"Kako zelite da obrisete podatke?",
			"Odabir nacina brisanja",
			JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,
			options,
			options[2]
		);
		
		if(n == JOptionPane.YES_OPTION) {
			String id = JOptionPane.showInputDialog("ID entiteta");
			if(id != null) {
				if(id.equals("")) {
					ExceptionHandler.handle("ID");
					return;
				}
				GlavniProzor.getProzor().getSkladiste().obrisi(id);
				GlavniProzor.getProzor().getTableModel().update();
			}
		}
		else {
			String naziv = JOptionPane.showInputDialog("Naziv entiteta");
			if(naziv.equals("")) {
				ExceptionHandler.handle("naziv");
				return;
			}
			int data = JOptionPane.showConfirmDialog(GlavniProzor.getProzor(), new JScrollPane(textArea), "Podaci entiteta", JOptionPane.YES_NO_OPTION);
			if(data == JOptionPane.YES_OPTION) {
				if(textArea.getText().equals("")) {
					ExceptionHandler.handle("textArea");
					return;
				}
				GlavniProzor.getProzor().getSkladiste().obrisi(naziv, textArea.getText());
				GlavniProzor.getProzor().getTableModel().update();
			}
		}
		
	}

}
