/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ejb.GeneriqueBean;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.contact.Contact;

/**
 *
 * @author misa
 */
@Named(value = "ficheContactMB")
@ViewScoped
public class FicheContactMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;

    private Contact contact;
    private Integer idContact;

    /**
     * Creates a new instance of FicheContactMB
     */
    public FicheContactMB() {
    }

    public void loadContact() {
        try {
            Contact c = new Contact();
            c.setId(idContact);
            contact = (Contact) generiqueBean.getService().findById(c);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Contact getContact() {
        return contact;
    }

    public Integer getIdContact() {
        return idContact;
    }

    public void setIdContact(Integer idContact) {
        this.idContact = idContact;
    }

}
