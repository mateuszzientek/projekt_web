package com.jsfcourse.szczegoly;

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

import com.jsf.dao.ZamowieniaSzczegolyDAO;
import com.jsf.dao.ZamowieniaDAO;

import jsfcourse.entities.Cart;
import jsfcourse.entities.Product;
import jsfcourse.entities.User;
import jsfcourse.entities.Zamowienia;
import jsfcourse.entities.ZamowieniaSzczegoly;

@Named
@RequestScoped
public class Szczegoly {

	private int idZamowienia;

	public int getIdZamowienia() {
		return idZamowienia;
	}

	public void setIdZamowienia(int idZamowienia) {
		this.idZamowienia = idZamowienia;
	}

	@EJB
	ZamowieniaSzczegolyDAO zamowieniaSzczegolyDAO;

	public List<ZamowieniaSzczegoly> getList() {

		List<ZamowieniaSzczegoly> list = null;

		list = zamowieniaSzczegolyDAO.getList_id(idZamowienia);

		return list;
	}

}