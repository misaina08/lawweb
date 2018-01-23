/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.dossier;

import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.contact.Contact;
import modeles.dossiers.ContactDossier;
import modeles.dossiers.ContactDossierLibelle;
import modeles.dossiers.DossierLibelle;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "listeInterlocuteurMB")
@ViewScoped
public class ListeInterlocuteurMB implements Serializable {

    private Integer idDossier;
    private DossierLibelle dossier;
    private List<ContactDossierLibelle> liste;
    private List<Contact> contacts;
    private ContactDossierLibelle contact;
    private Integer idContactSelected;
    @EJB
    private GeneriqueBean generiqueBean;

    /**
     * Creates a new instance of ListeInterlocuteurMB
     */
    public ListeInterlocuteurMB() {
    }

    public void loadDossier() {
        try {
            DossierLibelle d = new DossierLibelle();
            d.setId(idDossier);
            dossier = (DossierLibelle) generiqueBean.getService().findById(d);
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.addFlashErrorMessage("Erreur de chargement");
        }
    }

    public void supprimer(Integer id) {
        try {
            ContactDossier cRemove = new ContactDossier();
            cRemove.setId(id);
            generiqueBean.getService().delete(cRemove);
            MessageUtil.addFlashInfoMessage("Interlocuteur enlevé");
            initListe();
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.addFlashErrorMessage("Erreur de suppression");
        }
    }

    public void initListe() {
        try {
            ContactDossierLibelle contDoss = new ContactDossierLibelle();
            contDoss.setIdDossier(dossier.getId());
            contDoss.setTypeContact("INTR");
            liste = (List<ContactDossierLibelle>) (List<?>) generiqueBean.getService().find(contDoss);
        } catch (Exception ex) {
            Logger.getLogger(ListeInterlocuteurMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void voir(Integer id) {
        try {
            ContactDossierLibelle c = new ContactDossierLibelle();
            c.setId(id);
            contact = (ContactDossierLibelle) generiqueBean.getService().findById(c);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadContacts() {
        try {
            if (contacts == null) {
                contacts = (List<Contact>) (List<?>) generiqueBean.getService().find(new Contact());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void ajouterInterlocuteur() {
        try {
            ContactDossier cDoss = new ContactDossier();
            cDoss.setIdDossier(dossier.getId());
            cDoss.setTypeContact("INTR");
            cDoss.setIdContact(idContactSelected);
            int mtovy = 0;
            for (ContactDossierLibelle cdl : liste) {
                if (cDoss.getIdContact().equals(cdl.getIdContact())) {
                    mtovy++;
                    break;
                }
            }
            if (mtovy == 0) {
                generiqueBean.getService().save(cDoss);
                MessageUtil.messageInfo("Contact ajouté");
                initListe();
            } else {
                MessageUtil.messageWarn("Ce contact est déjà un interlocuteur du dossier");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur d'ajout");
        }
    }

    public Integer getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(Integer idDossier) {
        this.idDossier = idDossier;
    }

    public DossierLibelle getDossier() {
        return dossier;
    }

    public void setDossier(DossierLibelle dossier) {
        this.dossier = dossier;
    }

    public List<ContactDossierLibelle> getListe() {
        if (liste == null) {
            try {
                ContactDossierLibelle contDoss = new ContactDossierLibelle();
                contDoss.setIdDossier(dossier.getId());
                contDoss.setTypeContact("INTR");
                liste = (List<ContactDossierLibelle>) (List<?>) generiqueBean.getService().find(contDoss);
            } catch (Exception ex) {
                Logger.getLogger(ListeInterlocuteurMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return liste;
    }

    public void setListe(List<ContactDossierLibelle> liste) {
        this.liste = liste;
    }

    public ContactDossierLibelle getContact() {
        return contact;
    }

    public void setContact(ContactDossierLibelle contact) {
        this.contact = contact;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public Integer getIdContactSelected() {
        return idContactSelected;
    }

    public void setIdContactSelected(Integer idContactSelected) {
        this.idContactSelected = idContactSelected;
    }
}
