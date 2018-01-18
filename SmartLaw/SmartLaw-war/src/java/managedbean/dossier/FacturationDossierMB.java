/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.dossier;

import ejb.DossierBean;
import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.contact.Contact;
import modeles.dossiers.ContactDossier;
import modeles.dossiers.ContactDossierLibelle;
import modeles.dossiers.DossierLibelle;
import modeles.parametres.ModeFacturation;
import modeles.parametres.TypeFacturationDossier;
import modeles.parametres.TypeFacture;
import statiques.ObjetStatique;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "facturationDossierMB")
@ViewScoped
public class FacturationDossierMB implements Serializable {

    @EJB
    private DossierBean dossierBean;

    @EJB
    private GeneriqueBean generiqueBean;

    private Integer idDossier;
    private DossierLibelle dossier;
    private TypeFacturationDossier typeFacturation;
    private List<TypeFacture> typeFacture;
    private List<ModeFacturation> modeFacturation;
    private String[] periodicite;
    private String[] langue;
    private List<Contact> contacts;
    private Integer idContactSelected;
    private ContactDossierLibelle adresseFact;

    public FacturationDossierMB() {
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

    public void majFacturation() {
        try {
            generiqueBean.getService().update(typeFacturation);
            MessageUtil.messageInfo("Modification effectuée");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur de modification");
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

    public void majAdFact() {
        try {
            ContactDossier cDoss = new ContactDossier();
            cDoss.setId(adresseFact.getId());
            cDoss.setIdDossier(dossier.getId());
            cDoss.setTypeContact("FACT");
            cDoss.setIdContact(idContactSelected);
            generiqueBean.getService().update(cDoss);
            
            adresseFact = new ContactDossierLibelle();
            adresseFact.setIdDossier(dossier.getId());
            adresseFact.setTypeContact("FACT");
            adresseFact = ((List<ContactDossierLibelle>) (List<?>) generiqueBean.getService().find(adresseFact)).get(0);
            MessageUtil.messageInfo("Adresse de facturation mise à jour");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur de modification");
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

    public TypeFacturationDossier getTypeFacturation() {
        if (typeFacturation == null) {
            try {
                TypeFacturationDossier tf = new TypeFacturationDossier();
                tf.setIdDossier(dossier.getId());
                typeFacturation = (TypeFacturationDossier) generiqueBean.getService().find(tf).get(0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return typeFacturation;
    }

    public void setTypeFacturation(TypeFacturationDossier typeFacturation) {
        this.typeFacturation = typeFacturation;
    }

    public List<TypeFacture> getTypeFacture() {
        if (typeFacture == null) {
            typeFacture = ObjetStatique.getTypeFacture();
        }
        return typeFacture;
    }

    public void setTypeFacture(List<TypeFacture> typeFacture) {
        this.typeFacture = typeFacture;
    }

    public List<ModeFacturation> getModeFacturation() {
        if (modeFacturation == null) {
            modeFacturation = ObjetStatique.getModeFacturation();
        }
        return modeFacturation;
    }

    public void setModeFacturation(List<ModeFacturation> modeFacturation) {
        this.modeFacturation = modeFacturation;
    }

    public String[] getPeriodicite() {
        if (periodicite == null) {
            periodicite = ObjetStatique.getPeriodicite();
        }
        return periodicite;
    }

    public void setPeriodicite(String[] periodicite) {
        this.periodicite = periodicite;
    }

    public String[] getLangue() {
        if (langue == null) {
            langue = ObjetStatique.getLangue();
        }
        return langue;
    }

    public void setLangue(String[] langue) {
        this.langue = langue;
    }

    public ContactDossierLibelle getAdresseFact() {
        if (adresseFact == null) {
            try {
                adresseFact = new ContactDossierLibelle();
                adresseFact.setIdDossier(dossier.getId());
                adresseFact.setTypeContact("FACT");
                adresseFact = ((List<ContactDossierLibelle>) (List<?>) generiqueBean.getService().find(adresseFact)).get(0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return adresseFact;
    }

    public void setAdresseFact(ContactDossierLibelle adresseFact) {
        this.adresseFact = adresseFact;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public Integer getIdContactSelected() {
        if (idContactSelected == null) {
            idContactSelected = getAdresseFact().getIdContact();
        }
        return idContactSelected;
    }

    public void setIdContactSelected(Integer idContactSelected) {
        this.idContactSelected = idContactSelected;
    }

}
