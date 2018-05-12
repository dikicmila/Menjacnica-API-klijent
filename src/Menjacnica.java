import java.awt.EventQueue;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import memnjacnica.util.URLConnectionUtil;
import menjacnica.Konverzija;
import menjacnica.Valuta;
import menjacnica.Zemlja;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Menjacnica {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private LinkedList<Zemlja> zemlje = new LinkedList<Zemlja>();
	private String[] countries = countries();
	JComboBox comboBox;
	JComboBox comboBox_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menjacnica window = new Menjacnica();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Menjacnica() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Iz valute zemlje:");
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel.setBounds(40, 42, 90, 14);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("U valutu zemlje:");
		lblNewLabel_1.setBounds(270, 42, 77, 14);
		panel.add(lblNewLabel_1);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(countries));
		comboBox.setBounds(40, 78, 136, 20);
		panel.add(comboBox);
		
		comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(countries));
		comboBox_1.setBounds(234, 78, 136, 20);
		panel.add(comboBox_1);
		
		JLabel lblIznos = new JLabel("Iznos:");
		lblIznos.setBounds(40, 131, 46, 14);
		panel.add(lblIznos);
		
		JLabel label = new JLabel("Iznos:");
		label.setBounds(270, 131, 46, 14);
		panel.add(label);
		
		textField = new JTextField();
		textField.setBounds(40, 168, 86, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(270, 168, 86, 20);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnKonvertuj = new JButton("Konvertuj");
		btnKonvertuj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String url = "http://free.currencyconverterapi.com/api/v3/convert?q=";
				String zahtevURL = skracenica(comboBox.getSelectedItem().toString())+"_" 
									+ skracenica(comboBox_1.getSelectedItem().toString());
				url += zahtevURL;
				try {
					String content = URLConnectionUtil.getContent(url);
					JsonParser jsonParser = new JsonParser();
					JsonObject jsonObj = jsonParser.parse(content).getAsJsonObject().
							getAsJsonObject("results").getAsJsonObject(zahtevURL);
					
					Gson gson = new GsonBuilder().create();
					Valuta valuta = gson.fromJson(jsonObj, Valuta.class);
					
					if (valuta == null) {
						JOptionPane.showMessageDialog(frame, "Nema podataka za unete valute", "Greska", 
								JOptionPane.ERROR_MESSAGE);
					}
					else {
						konvertuj(valuta);
						dodajKonverziju(valuta, "data/log.json");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnKonvertuj.setBounds(162, 215, 89, 23);
		panel.add(btnKonvertuj);
		
	}
	private String[] countries() {
		try {
			String content = URLConnectionUtil.getContent("http://free.currencyconverterapi.com/api/v3/countries");
			Gson gson = new GsonBuilder().create();
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = jsonParser.parse(content).getAsJsonObject().getAsJsonObject("results");
			
			for (Map.Entry<String, JsonElement> entry: jsonObject.entrySet()) {
				Zemlja zemlja = gson.fromJson(entry.getValue(), Zemlja.class);
				zemlje.add(zemlja);
			}
			
			String[] naziviZemalja = new String[zemlje.size()];
			for (int i = 0; i < zemlje.size(); i++) 
				naziviZemalja[i] = zemlje.get(i).getName();
			return naziviZemalja;	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private String skracenica(String drzava) {
		for (int i = 0; i < zemlje.size(); i++)
			if (zemlje.get(i).getName().equals(drzava))
				return zemlje.get(i).getCurrencyId();
		return null;
	}
	
	private void konvertuj(Valuta valuta) {
		try {
			double iznos =Double.parseDouble(textField.getText())* Double.parseDouble(valuta.getVal());
			String tekst = "" + iznos;
			textField_1.setText(tekst);
		}catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frame, "Morate uneti broj!", 
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	

	private LinkedList<Konverzija> istorijaKonverzija(String fajl){
		LinkedList<Konverzija> pomLista = new LinkedList<Konverzija>();
		try {
			FileReader in = new FileReader(fajl);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonArray jsonArray = gson.fromJson(in, JsonArray.class);
			
			for (int i = 0; i < jsonArray.size(); i++) {
				pomLista.add(gson.fromJson(jsonArray.get(i), Konverzija.class));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return pomLista;
	}
	
	private void dodajKonverziju(Valuta valuta, String fajl) {
		LinkedList<Konverzija> konverzije = istorijaKonverzija(fajl);
		Konverzija k = new Konverzija();
		k.setDatumVreme(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS"));
		k.setIzValuta(valuta.getFr());
		k.setuValuta(valuta.getTo());
		k.setKurs(Double.parseDouble(valuta.getVal()));
		
		if(konverzije == null)
			konverzije = new LinkedList<Konverzija>();
		
		konverzije.add(k);
		
		try {
			FileWriter out = new FileWriter(fajl);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(konverzije, out);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
