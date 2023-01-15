package com.jsfcourse.koszyk;

import java.io.IOException;
import java.math.BigDecimal;
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

import com.jsf.dao.ProductDAO;
import jsfcourse.entities.Product;
import jsfcourse.entities.Cart;
import com.jsf.dao.UserDAO;

import jsfcourse.entities.User;
import com.jsf.dao.CartDAO;

@Named 
@RequestScoped
public class Koszyk {

	
	private String nazwa;
	private int rozmiar;
	private int ilosc;
	private Boolean czy_jest;
	private BigDecimal sum;
	

	private Product produkt1;


	public BigDecimal getSum() {
		return sum;
	}


	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}


	public Boolean getCzy_jest() {
		return czy_jest;
	}


	public int getRozmiar() {
		return rozmiar;
	}

	public void setRozmiar(int rozmiar) {
		this.rozmiar = rozmiar;
	}


	@EJB
	UserDAO userDAO;
	
	@EJB
	CartDAO cartDAO;


	public String pokaz(Product produkt) {
		
		FacesContext ctx = FacesContext.getCurrentInstance();	
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		session.setAttribute("produkt", produkt);
	
		
		return "/pages/produkty/produkty";
	}
	
      
      public String usun(Cart cart) {
   	     
    	if(cart.getIlosc() == 1) {
    		cartDAO.remove(cart);
    	} else {
    		int ilosc2;
    		ilosc2= cart.getIlosc();
    		cart.setIlosc(--ilosc2);
    		cartDAO.merge(cart);
    	}
    	 
      	return "/pages/koszyk/koszyk?faces-redirect=true";
  	}
      
      
	
    public String dodaj_do_koszyka(){
	 
    	FacesContext ctx = FacesContext.getCurrentInstance();	
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		nazwa=(String) session.getAttribute("uzytkownik");
		produkt1=(Product) session.getAttribute("produkt");
		
		User user= new User();
		Cart cart1 = new Cart();
		Cart cart2 = new Cart();
		
        user= userDAO.getUser(nazwa);
		
        cart2= cartDAO.check(user, produkt1, rozmiar);
       
       if( cart2 == null) {
    	    	   
    	   cart1.setIlosc(1);
           cart1.setRozmiar(rozmiar);
       	   cart1.setProduct(produkt1);
           cart1.setUser(user);

       	
       	  cartDAO.create(cart1);
     
       	  
       }else {
    	      	  
    	   ilosc= cart2.getIlosc();
    	   cart2.setIlosc(++ilosc);
    	   cartDAO.merge(cart2);
    	   
       }
        		
       return "/pages/produkty/produkty";
	}
    
    public String sprawdz_czy_dodane() {
    	
    	FacesContext ctx = FacesContext.getCurrentInstance();	
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		nazwa=(String) session.getAttribute("uzytkownik");
		
		User user= new User();
				
		user= userDAO.getUser(nazwa);
		
		czy_jest= cartDAO.check_is_add(user);
	
		
		return "/pages/koszyk/koszyk";
    }
       
    
    public List<Cart> getList(){
		List<Cart> list = null;
		List<Product> list_prod = null;
		
		FacesContext ctx = FacesContext.getCurrentInstance();	
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		nazwa=(String) session.getAttribute("uzytkownik");
		
		User user= new User();
		
		user= userDAO.getUser(nazwa);
		
		list = cartDAO.getList(user);		
			
		return list;
	}
         
    
}	