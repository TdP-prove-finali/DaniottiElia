package model;

import java.time.LocalTime;

public class Avvistamento {
	int id;
	String specie;
	String tratta;
	LocalTime orario;
	double punteggio;
	int aggressione;
	String branco;
	int numerositaBranco;

	public Avvistamento(int id, String specie, String tratta, int aggressione, String branco, int numerositaBranco) {
		this.id = id;
		this.specie = specie;
		this.tratta = tratta;
		this.aggressione = aggressione;
		this.branco=branco;
		this.numerositaBranco=numerositaBranco;
	}

	public int getNumerositaBranco() {
		return numerositaBranco;
	}

	public void setNumerositaBranco(int numerositaBranco) {
		this.numerositaBranco = numerositaBranco;
	}
	
	public int getId() {
		return id;
	}
	public String getSpecie() {
		return specie;
	}
	public String getTratta() {
		return tratta;
	}
	public LocalTime getOrario() {
		return orario;
	}
	public void setOrario(LocalTime orario) {
		this.orario = orario;
	}
	public double getPunteggio() {
		return punteggio;
	}
	public void setPunteggio(double punteggio) {
		this.punteggio = punteggio;
	}
	public int getAggressione() {
		return aggressione;
	}
	public String getBranco() {
		return branco;
	}
	
}
