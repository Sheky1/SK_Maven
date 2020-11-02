package main;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import gui.GlavniProzor;

public class Main {

	public static void main(String[] args) {
		GlavniProzor.getProzor().setVisible(false);
		
		boolean novoSkladiste = true;
		boolean isAuto = true;
		
		Object[] options = {"Novo skladiste", "Postojece skladiste"};
		int n = JOptionPane.showOptionDialog(
			GlavniProzor.getProzor(),
			"Odakle zelite da povucete podatke?",
			"Skladiste",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,
			options,
			options[1]
		);
		if(n == JOptionPane.NO_OPTION) novoSkladiste = false;
		
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		File folder = null;
		int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            folder = fc.getSelectedFile();
            System.out.println("File: " + folder.getName() + ".");
        } else {
            System.out.println("Open command cancelled by user.");
        }

		if(n == JOptionPane.YES_OPTION) {
			Object[] options2 = {"Automatski", "Manuelno"};
			int m = JOptionPane.showOptionDialog(
				GlavniProzor.getProzor(),
				"Kako zelite da se dodeljuje id?",
				"Dodavanje id-ja",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options2,
				options2[1]
			);
			if(m == JOptionPane.NO_OPTION) isAuto = false;
		}
		
		if(folder != null) {
			GlavniProzor.getProzor().setVisible(true);
			GlavniProzor.getProzor().postaviSkladiste(novoSkladiste, folder, isAuto);
		} else {
			JOptionPane.showMessageDialog(null, "Neoophodno je uneti folder skladista", "Skladiste", JOptionPane.ERROR_MESSAGE);
		}
		

	}

}
