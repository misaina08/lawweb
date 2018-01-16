/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.client;

import ejb.ClientBean;
import ejb.GeneriqueBean;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.clients.Client;

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
    
    

    /**
     * Creates a new instance of ContactsClientMB
     */
    public ContactsClientMB() {
    }

    public void loadClient() {
        try {
            Client c = new Client();
            c.setId(idClient);
            client = (Client)generiqueBean.getService().findById(c);
        }catch(Exception ex) {
            ex.printStackTrace();
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

    
}
