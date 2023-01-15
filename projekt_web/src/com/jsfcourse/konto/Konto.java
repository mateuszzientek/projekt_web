package com.jsfcourse.konto;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jsf.dao.UserDAO;
import com.jsf.dao.ZamowieniaDAO;
import com.jsf.dao.ZamowieniaSzczegolyDAO;

import jsfcourse.entities.User;
import jsfcourse.entities.Zamowienia;
import jsfcourse.entities.ZamowieniaSzczegoly;

@Named
@RequestScoped
public class Konto {

	private String imie;
	private String nazwisko;
	private String adres;
	private String telefon;
	private String email;
	private String nazwa;
	private String wyswietl;
	private Boolean wyswietl_zamowienia;

	private User user = new User();
	private List<Zamowienia> listZam;
	private List<ZamowieniaSzczegoly> listZam_szcz;

	@EJB
	UserDAO userDAO;

	@EJB
	ZamowieniaDAO zamowieniaDAO;

	@EJB
	ZamowieniaSzczegolyDAO zamowieniaSzczegolyDAO;

	public List<ZamowieniaSzczegoly> getListZam_szcz() {
		return listZam_szcz;
	}

	public void setListZam_szcz(List<ZamowieniaSzczegoly> listZam_szcz) {
		this.listZam_szcz = listZam_szcz;
	}

	public List<Zamowienia> getListZam() {
		return listZam;
	}

	public void setListZam(List<Zamowienia> listZam) {
		this.listZam = listZam;
	}

	public User getUser() {
		return user;
	}

	public String getWyswietl() {
		return wyswietl;
	}

	public Boolean getWyswietl_zamowienia() {
		return wyswietl_zamowienia;
	}

	public String getImie() {
		return imie;
	}

	public void setImie(String imie) {
		this.imie = imie;
	}

	public String getNazwisko() {
		return nazwisko;
	}

	public void setNazwisko(String nazwisko) {
		this.nazwisko = nazwisko;
	}

	public String getAdres() {
		return adres;
	}

	public void setAdres(String adres) {
		this.adres = adres;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String dane_adresowe() {

		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		nazwa = (String) session.getAttribute("uzytkownik");

		wyswietl = userDAO.sprawdzenie_imiona(nazwa);

		User user = new User();

		user = userDAO.getUser(nazwa);

		user.setAdres(adres);
		user.setImie(imie);
		user.setNazwisko(nazwisko);
		user.setTelefon(telefon);
		user.setEmail(email);

		userDAO.merge(user);

		return "/pages/konto/poprawnie";
	}

	public String dane_adresowe2() {

		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		nazwa = (String) session.getAttribute("uzytkownik");

		wyswietl = userDAO.sprawdzenie_imiona(nazwa);

		User user = new User();

		user = userDAO.getUser(nazwa);

		user.setAdres(adres);
		user.setImie(imie);
		user.setNazwisko(nazwisko);
		user.setTelefon(telefon);
		user.setEmail(email);

		userDAO.merge(user);

		return "/pages/koszyk/podsumowanie?faces-redirect=true";
	}

	public String pobierz() {

		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		nazwa = (String) session.getAttribute("uzytkownik");

		wyswietl = userDAO.sprawdzenie_imiona(nazwa);

		user = userDAO.getUser(nazwa);

		imie = user.getImie();
		nazwisko = user.getNazwisko();
		adres = user.getAdres();
		telefon = user.getTelefon();
		email = user.getEmail();

		return "/pages/konto/edycja_adresowa";

	}

	public String pobierz1() {

		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		nazwa = (String) session.getAttribute("uzytkownik");

		wyswietl = userDAO.sprawdzenie_imiona(nazwa);

		user = userDAO.getUser(nazwa);

		imie = user.getImie();
		nazwisko = user.getNazwisko();
		adres = user.getAdres();
		telefon = user.getTelefon();
		email = user.getEmail();

		return "/pages/koszyk/dane_wysylki";

	}

	public String sprawdz() {

		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		nazwa = (String) session.getAttribute("uzytkownik");

		user = userDAO.getUser(nazwa);

		wyswietl_zamowienia = zamowieniaDAO.check_is_add(user);

		wyswietl = userDAO.sprawdzenie_imiona(nazwa);

		listZam = zamowieniaDAO.getList(user);

		return "/pages/konto/konto";

	}

	public List<User> getList() {

		List<User> list = null;

		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		nazwa = (String) session.getAttribute("uzytkownik");

		list = userDAO.getList(nazwa);

		return list;
	}

	public List<Zamowienia> getList_zamowienia() {

		List<Zamowienia> listZam = null;

		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		nazwa = (String) session.getAttribute("uzytkownik");

		listZam = zamowieniaDAO.getList(user);

		return listZam;
	}

	public List<ZamowieniaSzczegoly> getList_zamowienia_szczegoly(Zamowienia zam) {

		listZam_szcz = zamowieniaSzczegolyDAO.getList(zam);

		return listZam_szcz;
	}

}