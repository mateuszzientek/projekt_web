package com.jsfcourse.register;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jsf.dao.UserDAO;
import jsfcourse.entities.User;

@Named
@RequestScoped
public class Register {

	private String login;
	private String haslo;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getHaslo() {
		return haslo;
	}

	public void setHaslo(String haslo) {
		this.haslo = haslo;
	}

	@Inject
	UserDAO userDAO;

	public String add() {

		FacesContext ctx = FacesContext.getCurrentInstance();
		User user1 = new User();
		User user2 = new User();

		user1 = userDAO.getUser(login);

		if (user1 == null) {

			user2.setHaslo(haslo);
			user2.setLogin(login);
			user2.setRola("user");

			userDAO.create(user2);
			return "/pages/logowanie";

		} else {
			ctx.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login zajęty, proszę wybrać inny", null));
			return "/public/rejestracja";
		}

	}

}