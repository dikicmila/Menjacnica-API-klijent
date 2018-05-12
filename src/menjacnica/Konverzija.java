package menjacnica;

import java.text.SimpleDateFormat;

public class Konverzija {

	SimpleDateFormat datumVreme;
	String izValuta;
	String uValuta;
	double kurs;
	
	public SimpleDateFormat getDatumVreme() {
		return datumVreme;
	}
	public void setDatumVreme(SimpleDateFormat datumVreme) {
		this.datumVreme = datumVreme;
	}
	public String getIzValuta() {
		return izValuta;
	}
	public void setIzValuta(String izValuta) {
		this.izValuta = izValuta;
	}
	public String getuValuta() {
		return uValuta;
	}
	public void setuValuta(String uValuta) {
		this.uValuta = uValuta;
	}
	public double getKurs() {
		return kurs;
	}
	public void setKurs(double kurs) {
		this.kurs = kurs;
	}
	
}
