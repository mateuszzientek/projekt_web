package com.jsfcourse.podsumowanie;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

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

import com.jsf.dao.CartDAO;
import com.jsf.dao.UserDAO;
import com.jsf.dao.ZamowieniaSzczegolyDAO;
import com.jsf.dao.ZamowieniaDAO;
import com.jsf.dao.StatusDAO;

import jsfcourse.entities.Cart;
import jsfcourse.entities.Product;
import jsfcourse.entities.User;
import jsfcourse.entities.Zamowienia;
import jsfcourse.entities.ZamowieniaSzczegoly;
import jsfcourse.entities.Status;

@Named
@RequestScoped
public class Podsumowanie {

	private String imie;
	private String nazwisko;
	private String adres;
	private String telefon;
	private String email;
	private String nazwa;

	private Date currentDate = new Date();
	private User user = new User();
	private int sum;
	private int ilosc1;
	private int id_zam;

	@EJB
	UserDAO userDAO;

	@EJB
	CartDAO cartDAO;

	@EJB
	ZamowieniaDAO zamowieniaDAO;

	@EJB
	StatusDAO statusDAO;

	@EJB
	ZamowieniaSzczegolyDAO zamowieniaSzczegolyDAO;

	public int getId_zam() {
		return id_zam;
	}

	public void setId_zam(int id_zam) {
		this.id_zam = id_zam;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public int getIlosc1() {
		return ilosc1;
	}

	public void setIlosc1(int ilosc1) {
		this.ilosc1 = ilosc1;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	public User getUser() {
		return user;
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

	public String pobierz_adres() {

		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		nazwa = (String) session.getAttribute("uzytkownik");

		user = userDAO.getUser(nazwa);

		imie = user.getImie();
		nazwisko = user.getNazwisko();
		adres = user.getAdres();
		telefon = user.getTelefon();
		email = user.getEmail();

		List<Cart> list = null;

		list = cartDAO.getList(user);

		for (Cart cart : list) {

			sum = sum + (cart.getProduct().getCena() * cart.getIlosc());
			ilosc1 = ilosc1 + cart.getIlosc();

		}

		return "/pages/koszyk/podsumowanie";

	}

	public String zamowienie() {

		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		nazwa = (String) session.getAttribute("uzytkownik");

		user = userDAO.getUser(nazwa);

		List<Cart> list = null;

		list = cartDAO.getList(user);

		Zamowienia zamowienie = new Zamowienia();
		ZamowieniaSzczegoly zamowienieSzczegoly = new ZamowieniaSzczegoly();

		Status status = new Status();

		for (Cart cart : list) {

			sum = sum + (cart.getProduct().getCena() * cart.getIlosc());
			ilosc1 = ilosc1 + cart.getIlosc();

		}

		status = statusDAO.getStatus("W przygotowaniu");

		zamowienie.setCalkowitaCena(sum);
		zamowienie.setCalkowitaIlosc(ilosc1);
		zamowienie.setUser(user);
		zamowienie.setStatus(status);
		zamowienie.setData(currentDate);

		zamowieniaDAO.create(zamowienie);

		int id_zam1 = zamowienie.getIdZamowienia();

		session.setAttribute("id", id_zam1);

		for (Cart cart : list) {

			int ilosc;
			int romziar;

		}

		return "/pages/koszyk/zamowienie?faces-redirect=true";

	}

	public String szczegoly() {

		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		nazwa = (String) session.getAttribute("uzytkownik");
		id_zam = (int) session.getAttribute("id");

		user = userDAO.getUser(nazwa);

		Zamowienia zamowienie = new Zamowienia();

		zamowienie = zamowieniaDAO.get_last_zamowienie(id_zam);

		List<Cart> list = null;

		list = cartDAO.getList(user);

		ZamowieniaSzczegoly zamowienieSzczegoly = new ZamowieniaSzczegoly();

		for (Cart cart : list) {

			String nazwa;
			int rozmiar;
			int cena;
			int ilosc;
			Product produkt;

			nazwa = cart.getProduct().getNazwa();
			rozmiar = cart.getRozmiar();
			cena = cart.getProduct().getCena();
			ilosc = cart.getIlosc();
			produkt = cart.getProduct();

			zamowienieSzczegoly.setZamowienia(zamowienie);
			zamowienieSzczegoly.setProduct(produkt);
			zamowienieSzczegoly.setUser(user);
			zamowienieSzczegoly.setIlosc(ilosc);
			zamowienieSzczegoly.setRozmiar(rozmiar);

			zamowieniaSzczegolyDAO.merge(zamowienieSzczegoly);

		}

		for (Cart cart : list) {

			cartDAO.remove(cart);

		}

		return "/pages/konto/konto?faces-redirect=true";

	}

}