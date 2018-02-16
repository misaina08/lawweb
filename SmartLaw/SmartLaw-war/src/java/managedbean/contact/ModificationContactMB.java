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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.view.ViewScoped;
import modeles.contact.Contact;
import modeles.parametres.Titre;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "modificationContactMB")
@ViewScoped
public class ModificationContactMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;

    private Titre titreSelected = new Titre();
    private List<Titre> titres;
    private Contact contact;
    private Integer idContact;

    public void loadContact() {
        try {
            Contact c = new Contact();
            c.setId(idContact);
            contact = (Contact) generiqueBean.getService().findById(c);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Converter titreConverter = new Converter() {
        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            Titre t = null;
            try {
                t = new Titre();
                t.setId(new Integer(value));
                t = (Titre) generiqueBean.getService().findById(t);
                return t;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Object value) {
            System.out.println("value--" + value);
            Titre titre = (Titre) value;
            return titre.getId() + "";
        }
    };

    /**
     * Creates a new instance of AjoutContactMB
     */
    public ModificationContactMB() {
    }

    public String modifier() {
        try {
            generiqueBean.getService().update(contact);
            MessageUtil.addFlashInfoMessage("Contact modifié");
            return "/pages/contact/fiche.xhtml?idContact="+idContact+"&faces-redirect=true;";
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.addFlashErrorMessage("Erreur de modification");
            return "";
        }

    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Integer getIdContact() {
        return idContact;
    }

    public void setIdContact(Integer idContact) {
        this.idContact = idContact;
    }

    
    public Converter getTitreConverter() {
        return titreConverter;
    }

    public List<Titre> getTitres() {
        if (titres == null) {
            try {
                titres = (List<Titre>) (List<?>) generiqueBean.getService().find(new Titre());
            } catch (Exception ex) {
                Logger.getLogger(ModificationContactMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return titres;
    }

    public void setTitres(List<Titre> titres) {
        this.titres = titres;
    }

    public Titre getTitreSelected() {
        return titreSelected;
    }

    public void setTitreSelected(Titre titreSelected) {
        this.titreSelected = titreSelected;
    }

}
