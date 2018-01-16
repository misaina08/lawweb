/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.contact;

import ejb.GeneriqueBean;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import modeles.contact.Contact;
import modeles.parametres.Titre;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "ajoutContactMB")
@RequestScoped
public class AjoutContactMB {

    @EJB
    private GeneriqueBean generiqueBean;

   

  

    private Titre titreSelected = new Titre();
    private List<Titre> titres;
    private Contact newContact = new Contact();

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
    public AjoutContactMB() {
    }

    public String ajouter() {
        try {
            System.out.println("titre " + titreSelected.getId());
            newContact.setIdTitre(titreSelected.getId());
            generiqueBean.getService().save(newContact);
            MessageUtil.addFlashInfoMessage("Contact ajouté");
            return "/pages/contact/liste.xhtml?faces-redirect=true;";
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.addFlashErrorMessage("Erreur d'ajout");
            return "";
        }

    }

    public Contact getNewContact() {
        return newContact;
    }

    public void setNewContact(Contact newContact) {
        this.newContact = newContact;
    }

    public Converter getTitreConverter() {
        return titreConverter;
    }

    public List<Titre> getTitres() {
        if (titres == null) {
            try {
                titres = (List<Titre>) (List<?>) generiqueBean.getService().find(new Titre());
            } catch (Exception ex) {
                Logger.getLogger(AjoutContactMB.class.getName()).log(Level.SEVERE, null, ex);
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
