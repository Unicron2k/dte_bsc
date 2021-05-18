package com.example.a8_1_sqlite.entities;

//En enkel "entitetsklasse"
public class Contact {
	private String etterNavn;
	private String forNavn;
	private String fbLink;
	private String email;
	private String telefonnr;
	private int fodselsaar;
	private long id;

	public Contact(String etterNavn, String forNavn, String fbLink, String email, String telefonnr, int fodselsaar, long id) {
		super();
		this.etterNavn = etterNavn;
		this.forNavn = forNavn;
		this.fbLink = fbLink;
		this.email = email;
		this.telefonnr = telefonnr;
		this.fodselsaar = fodselsaar;
		this.id = id;
	}

	public Contact() {
		super();
		this.etterNavn = "";
		this.forNavn = "";
		this.fbLink = "";
		this.email = "";
		this.telefonnr = "";
		this.fodselsaar = 0;
	}

	public String getEtterNavn() {
		return etterNavn;
	}

	public void setEtterNavn(String etterNavn) {
		this.etterNavn = etterNavn;
	}

	public String getForNavn() {
		return forNavn;
	}

	public void setForNavn(String forNavn) {
		this.forNavn = forNavn;
	}

	public String getFbLink() {
		return fbLink;
	}

	public void setFbLink(String fbLink) {
		this.fbLink = fbLink;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefonnr() {
		return telefonnr;
	}

	public void setTelefonnr(String telefonnr) {
		this.telefonnr = telefonnr;
	}

	public int getFodselsaar() {
		return fodselsaar;
	}

	public void setFodselsaar(int fodselsaar) {
		this.fodselsaar = fodselsaar;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return id + " " + etterNavn + " " + forNavn + " " + fodselsaar + " " + email + " " + telefonnr + " " + fbLink;
	}
}
