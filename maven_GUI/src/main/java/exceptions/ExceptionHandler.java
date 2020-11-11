package exceptions;

import javax.swing.JOptionPane;

public class ExceptionHandler {

	public static void handle(String text) {
		JOptionPane.showMessageDialog(null, getPoruka(text), "Greska", JOptionPane.ERROR_MESSAGE);
	}
	
	private static String getPoruka(String text) {
		if(text.equals("naziv")) return "Niste uneli naziv.";
		else if(text.equals("ID")) return "Niste uneli ID.";
		else if(text.equals("postojeciID")) return "ID koji ste uneli vec postoji.";
		else if(text.equals("textArea")) return "Morate uneti barem jedan podatak o entitetu.";
		else if(text.equals("nepravilanID")) return "ID mora biti broj.";
		else if(text.equals("skladiste")) return "Nije izabrano validno skladiste.";
		return "Greska";
	}
}
