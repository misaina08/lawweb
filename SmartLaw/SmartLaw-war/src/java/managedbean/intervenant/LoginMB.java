/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.intervenant;

import ejb.IntervenantBean;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import modeles.intervenants.Intervenant;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "loginMB")
@SessionScoped
public class LoginMB implements Serializable{

    @EJB
    private IntervenantBean intervenantBean;

    private String login;
    private String mdp;

    /**
     * Creates a new instance of LoginMB
     */
    public LoginMB() {
    }

    public String connect() {
        Intervenant intervenant = null;
        try {
            intervenant = intervenantBean.getByIdentification(login, mdp);
            if (intervenant == null) {
                throw new Exception("");
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().getSessionMap().put("intervSession", intervenant);
                return "/pages/accueil.xhtml?faces-redirect=true";

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Login ou mot de passe non valide");
            return "/pages/intervenant/login.xhtml";
        }
    }
    
    public String deconnecter() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            return "/pages/intervenant/login.xhtml?faces-redirect=true;";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

}
