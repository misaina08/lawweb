/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.client;

import ejb.ClientBean;
import ejb.GeneriqueBean;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.clients.Client;
import modeles.clients.ClientLibelle;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "ficheClientMB")
@ViewScoped
public class FicheClientMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;
    @EJB
    private ClientBean clientBean;

    private Integer idClient;
    private ClientLibelle client;

    /**
     * Creates a new instance of FicheClientMB
     */
    public FicheClientMB() {

    }

    public void loadClient() {
        
        try {
            ClientLibelle c = new ClientLibelle();
            c.setId(idClient);
            client = (ClientLibelle) generiqueBean.getService().findById(c);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void print() {
        try {
            clientBean.printFiche(idClient);
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.addFlashErrorMessage("Erreur d'impression");
        }
    }

    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    public ClientLibelle getClient() {
        return client;
    }

    public void setClient(ClientLibelle client) {
        this.client = client;
    }

}
