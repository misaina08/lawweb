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
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "modificationClientMB")
@ViewScoped
public class ModificationClientMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;

    @EJB
    private ClientBean clientBean;

    private Integer idClient;
    private Client client;

    /**
     * Creates a new instance of ModificationClientMB
     */
    public ModificationClientMB() {
    }

    public void loadClient() {
        try {
            modeles.clients.Client c = new modeles.clients.Client();
            c.setId(idClient);
            client = (modeles.clients.Client) generiqueBean.getService().findById(c);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public String modifier() {
        try {
            System.out.println("client " + client);
//        client = clientBean.updateClient(client);
            generiqueBean.getService().update(client);
            MessageUtil.addFlashInfoMessage("Modification effectée");
            return "/pages/client/fiche.xhtml?idClient=" + client.getId() + "&faces-redirect=true;";
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.addFlashErrorMessage("Erreur de modification");
            return "";
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
