/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.contact;



import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.contact.Contact;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "listeContactMB")
@ViewScoped
public class ListeContactMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;


    private List<Contact> contacts;
    

    /**
     * Creates a new instance of ListeContactMB
     */
    public ListeContactMB() {
    }

    public String supprimer(Contact cl) {
        try {
            generiqueBean.getService().delete(cl);
            MessageUtil.addFlashInfoMessage("Contact supprimé");
            return "/pages/contact/liste.xhtml?faces-redirect=true;";
        } catch (Exception ex) {
            MessageUtil.addFlashErrorMessage("Erreur de suppression");
            return "";
        }

    }

    public List<Contact> getContacts() {
        if (contacts == null) {
            try {
                contacts = (List<Contact>)(List<?>)generiqueBean.getService().find(new Contact());
            } catch (Exception ex) {
                Logger.getLogger(ListeContactMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

}
