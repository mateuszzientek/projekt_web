package com.jsfcourse.login;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jsf.dao.UserDAO;

import jsfcourse.entities.User;

import javax.faces.simplesecurity.RemoteClient;

@Named
@RequestScoped
public class LoginBB {

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

	public String doLogin() {

		FacesContext ctx = FacesContext.getCurrentInstance();

		User user = new User();

		user = userDAO.get_login_pass(login, haslo);

		if (user == null) {

			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Niepoprawny login lub hasło", null));
			return "/pages/logowanie";

		} else {

			HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
			session.setAttribute("uzytkownik", login);

			RemoteClient<User> client = new RemoteClient<User>();
			client.setDetails(user);
			client.getRoles().add(user.getRola());

			HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
			client.store(request);

			return "/pages/home/home";
		}

	}

	public String doLogout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		session.invalidate();
		return "/public/index";
	}

}
