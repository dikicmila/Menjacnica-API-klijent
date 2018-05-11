import java.awt.EventQueue;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import memnjacnica.util.URLConnectionUtil;
import menjacnica.Zemlja;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;

public class Menjacnica {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private LinkedList<Zemlja> zemlje = new LinkedList<Zemlja>();
	private String[] countries = countries();

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
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(countries));
		comboBox.setBounds(40, 78, 136, 20);
		panel.add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox();
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
			// TODO Auto-generated catch bloc	k
			e.printStackTrace();
		}
		return null;
	}
}
