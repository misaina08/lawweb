/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.client;

import ejb.ClientBean;
import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.clients.Client;
import modeles.contact.Contact;
import modeles.contact.ContactClient;
import modeles.contact.ContactClientLibelle;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "contactsClientMB")
@ViewScoped
public class ContactsClientMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;

    @EJB
    private ClientBean clientBean;
    private Integer idClient;
    private Client client;

    private List<Contact> contacts;
    private List<ContactClientLibelle> liste;
    private ContactClientLibelle contact;
    private Integer idContactSelected;

    /**
     * Creates a new instance of ContactsClientMB
     */
    public ContactsClientMB() {
    }

    public void loadClient() {
        try {
            Client c = new Client();
            c.setId(idClient);
            client = (Client) generiqueBean.getService().findById(c);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void initListe() {
        try {
            ContactClientLibelle contDoss = new ContactClientLibelle();
            contDoss.setIdClient(idClient);
            liste = (List<ContactClientLibelle>) (List<?>) generiqueBean.getService().find(contDoss);
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

    public List<ContactClientLibelle> getListe() {
        if (liste == null) {
            try {
                ContactClientLibelle contDoss = new ContactClientLibelle();
                contDoss.setIdClient(idClient);
                liste = (List<ContactClientLibelle>) (List<?>) generiqueBean.getService().find(contDoss);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return liste;
    }

    public void voir(Integer id) {
        try {
            ContactClientLibelle c = new ContactClientLibelle();
            c.setId(id);
            contact = (ContactClientLibelle) generiqueBean.getService().findById(c);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void supprimer(Integer id) {
        try {
            ContactClient cRemove = new ContactClient();
            cRemove.setId(id);
            generiqueBean.getService().delete(cRemove);
            MessageUtil.addFlashInfoMessage("Contact enlevé");
            initListe();
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.addFlashErrorMessage("Erreur de suppression");
        }
    }

    public void ajouterContact() {
        try {
            ContactClient cDoss = new ContactClient();
            cDoss.setIdClient(idClient);

            cDoss.setIdContact(idContactSelected);
            int mtovy = 0;
            for (ContactClientLibelle cdl : liste) {
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
                MessageUtil.messageWarn("Ce contact est déjà un contact du client");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur d'ajout");
        }
    }

    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ClientBean getClientBean() {
        return clientBean;
    }

    public void setClientBean(ClientBean clientBean) {
        this.clientBean = clientBean;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public ContactClientLibelle getContact() {
        return contact;
    }

    public void setContact(ContactClientLibelle contact) {
        this.contact = contact;
    }

    public Integer getIdContactSelected() {
        return idContactSelected;
    }

    public void setIdContactSelected(Integer idContactSelected) {
        this.idContactSelected = idContactSelected;
    }

}
