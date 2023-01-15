package com.jsfcourse.produkty;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jsf.dao.ProductDAO;

import jsfcourse.entities.Product;

@Named
@RequestScoped
public class Produkty {

	private Product product = new Product();
	private String nazwa;

	@EJB
	ProductDAO productDAO;

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public Product getProduct() {
		return product;
	}

	public String reload() {

		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		session.setAttribute("nazwa", nazwa);

		return "/pages/produkty/produkty?faces-redirect=true";
	}

	public List<Product> getList() {

		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		nazwa = (String) session.getAttribute("nazwa");

		List<Product> list = null;

		Map<String, Object> searchParams = new HashMap<String, Object>();

		if (nazwa != null && nazwa.length() > 0) {
			searchParams.put("nazwa", nazwa);
		}

		list = productDAO.getList(searchParams);

		return list;
	}

}