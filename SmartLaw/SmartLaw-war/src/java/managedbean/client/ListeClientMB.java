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
import modeles.clients.ClientLibelle;

/**
 *
 * @author misa
 */
@Named(value = "listeClientMB")
@ViewScoped
public class ListeClientMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;

    @EJB
    private ClientBean clientBean;

    private List<ClientLibelle> clients;
    private List<Client> filteredClients;

    /**
     * Creates a new instance of ListeClientMB
     */
    public ListeClientMB() {
    }

    public List<ClientLibelle> getClients() {
        if (clients == null) {
            try {
                clients = (List<ClientLibelle>) (List<?>) generiqueBean.getService().find(new ClientLibelle());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return clients;
    }

    public void setClients(List<ClientLibelle> clients) {
        this.clients = clients;
    }

    public List<Client> getFilteredClients() {
        return filteredClients;
    }

    public void setFilteredClients(List<Client> filteredClients) {
        this.filteredClients = filteredClients;
    }

}
