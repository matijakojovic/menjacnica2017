package menjacnica.gui;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import menjacnica.Menjacnica;
import menjacnica.MenjacnicaInterface;
import menjacnica.Valuta;
import menjacnica.gui.models.MenjacnicaTableModel;

public class GUIKontroler {
	
	private static MenjacnicaInterface menjacnica;
	private static MenjacnicaGUI menjacnicaGui;
	private static DodajKursGUI dodajKursGui;
	private static ObrisiKursGUI obrisiKursGui;
	private static IzvrsiZamenuGUI izvrsiZamenuGui;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					menjacnicaGui = new MenjacnicaGUI();
					menjacnicaGui.setVisible(true);
					menjacnica = new Menjacnica();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void ugasiAplikaciju() {
		int opcija = JOptionPane.showConfirmDialog(menjacnicaGui,
				"Da li ZAISTA zelite da izadjete iz apliacije", "Izlazak",
				JOptionPane.YES_NO_OPTION);

		if (opcija == JOptionPane.YES_OPTION)
			System.exit(0);
	}
	
	public static void ucitajIzFajla() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(menjacnicaGui);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				menjacnica.ucitajIzFajla(file.getAbsolutePath());
				prikaziSveValute();
			}	
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(menjacnicaGui, e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void sacuvajUFajl() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(menjacnicaGui);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				menjacnica.sacuvajUFajl(file.getAbsolutePath());
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(menjacnicaGui, e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void prikaziAboutProzor(){
		JOptionPane.showMessageDialog(menjacnicaGui,
				"Autor: Bojan Tomic, Verzija 1.0", "O programu Menjacnica",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static void prikaziDodajKursGUI() {
		dodajKursGui = new DodajKursGUI(menjacnicaGui);
		dodajKursGui.setLocationRelativeTo(null);
		dodajKursGui.setVisible(true);
	}
	
	public static void unesiKurs() {
		try {
			Valuta valuta = new Valuta();

			// Punjenje podataka o valuti
			valuta.setNaziv(dodajKursGui.textFieldNaziv.getText());
			valuta.setSkraceniNaziv(dodajKursGui.textFieldSkraceniNaziv.getText());
			valuta.setSifra((Integer)(dodajKursGui.spinnerSifra.getValue()));
			valuta.setProdajni(Double.parseDouble(dodajKursGui.textFieldProdajniKurs.getText()));
			valuta.setKupovni(Double.parseDouble(dodajKursGui.textFieldKupovniKurs.getText()));
			valuta.setSrednji(Double.parseDouble(dodajKursGui.textFieldSrednjiKurs.getText()));
			
			// Dodavanje valute u kursnu listu
			menjacnica.dodajValutu(valuta);

			// Osvezavanje glavnog prozora
			prikaziSveValute();
			
			//Zatvaranje DodajValutuGUI prozora
			dodajKursGui.dispose();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(dodajKursGui, e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void prikaziSveValute() {
		MenjacnicaTableModel model = (MenjacnicaTableModel)(menjacnicaGui.table.getModel());
		model.staviSveValuteUModel(menjacnica.vratiKursnuListu());

	}
	
	public static void prikaziObrisiKursGUI() {
		
		if (menjacnicaGui.table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel)(menjacnicaGui.table.getModel());
			obrisiKursGui = new ObrisiKursGUI(menjacnicaGui,
					model.vratiValutu(menjacnicaGui.table.getSelectedRow()));
			obrisiKursGui.setLocationRelativeTo(menjacnicaGui);
			obrisiKursGui.setVisible(true);
		}	
	}

	public static void obrisiValutu() {
		try{
			menjacnica.obrisiValutu(obrisiKursGui.getValuta());
			
			prikaziSveValute();
			obrisiKursGui.dispose();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(obrisiKursGui, e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}	
	}
	
	public static void prikaziIzvrsiZamenuGUI() {
		if (menjacnicaGui.table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel)(menjacnicaGui.table.getModel());
			izvrsiZamenuGui = new IzvrsiZamenuGUI(menjacnicaGui,
					model.vratiValutu(menjacnicaGui.table.getSelectedRow()));
			izvrsiZamenuGui.setLocationRelativeTo(menjacnicaGui);
			izvrsiZamenuGui.setVisible(true);
		}
	}
	
	public static void izvrsiZamenu(){
		try{
			double konacniIznos = 
					menjacnica.izvrsiTransakciju(izvrsiZamenuGui.getValuta(),
							izvrsiZamenuGui.rdbtnProdaja.isSelected(), 
							Double.parseDouble(izvrsiZamenuGui.textFieldIznos.getText()));
		
			izvrsiZamenuGui.textFieldKonacniIznos.setText(""+konacniIznos);
		} catch (Exception e1) {
		JOptionPane.showMessageDialog(izvrsiZamenuGui, e1.getMessage(),
				"Greska", JOptionPane.ERROR_MESSAGE);
		e1.printStackTrace();
		}
	}
	
}