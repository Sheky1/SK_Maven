package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import exceptions.ExceptionHandler;
import model.TableModel;
import soft.komp.maven_specifikacija.ExportManager;
import soft.komp.maven_specifikacija.Specifikacija;

@SuppressWarnings("serial")
public class GlavniProzor extends JFrame{
	
	private static GlavniProzor prozor;
	private Toolbar toolbar;
	private JTable tabela;
	private TableModel tableModel;
	private JScrollPane skrol;
	private Specifikacija skladiste;
	private String selektovaniID = "";
	
	private GlavniProzor() {
		try {
			Class.forName("soft.komp.maven_XML.ImplementacijaXML");
			skladiste = ExportManager.getExporter();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		postavi();
	}

	public static GlavniProzor getProzor() {
		if (prozor == null) {
			prozor = new GlavniProzor();
		}
		return prozor;
	}

	private void postavi() {
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimenzija = toolkit.getScreenSize();
		int visina = dimenzija.height;
		int sirina = dimenzija.width;
		setSize(sirina/3*2, visina/3*2);
		
		setTitle("Softverske komponente");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		
		
		toolbar = new Toolbar();
		
		JPanel gornji = new JPanel();
		gornji.setLayout(new BorderLayout());
		gornji.add(toolbar, BorderLayout.NORTH);
		
		tableModel = new TableModel();
		tabela = new Tabela(tableModel);
		
		tabela.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent event) {
				if(tabela.getSelectedRow() == -1) return;
	            Integer id = (Integer) tabela.getValueAt(tabela.getSelectedRow(), 0);
	            GlavniProzor.getProzor().setSelektovaniID(id.toString());
			}
	    });
		
		skrol = new JScrollPane(tabela);
		skrol.setPreferredSize(new Dimension(1000,300));
		
		add(gornji, BorderLayout.NORTH);
		add(skrol, BorderLayout.CENTER);
	}
	
	public void postaviSkladiste(boolean novoSkladiste, File file, boolean isAuto) {
		boolean moze = skladiste.postaviSkladiste(novoSkladiste, file, isAuto);
		System.out.println(moze);
		if(!moze) {
			ExceptionHandler.handle("skladiste");
			this.setVisible(false);
			return;
		}
		this.setVisible(true);
		this.getTableModel().update();
	}

	public Toolbar getToolbar() {
		return toolbar;
	}

	public void setToolbar(Toolbar toolbar) {
		this.toolbar = toolbar;
	}

	public JTable getTabela() {
		return tabela;
	}

	public void setTabela(JTable tabela) {
		this.tabela = tabela;
	}

	public String getSelektovaniID() {
		return selektovaniID;
	}

	public void setSelektovaniID(String selektovaniID) {
		this.selektovaniID = selektovaniID;
	}

	public JScrollPane getSkrol() {
		return skrol;
	}

	public void setSkrol(JScrollPane skrol) {
		this.skrol = skrol;
	}

	public Specifikacija getSkladiste() {
		return skladiste;
	}

	public void setSkladiste(Specifikacija skladiste) {
		this.skladiste = skladiste;
	}

	public TableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(TableModel tableModel) {
		this.tableModel = tableModel;
	}
	
}
