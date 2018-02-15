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
import modeles.clients.ClientLibelle;
import modeles.dossiers.ContactDossier;
import modeles.dossiers.ContactDossierLibelle;
import modeles.parametres.ModeFacturation;
import modeles.parametres.TypeFacturationClient;
import modeles.parametres.TypeFacturationDossier;
import modeles.parametres.TypeFacture;
import statiques.ObjetStatique;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "facturationClientMB")
@ViewScoped
public class FacturationClientMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;
    @EJB
    private ClientBean clientBean;

    private Integer idClient;
    private ClientLibelle client;
    
    private TypeFacturationClient typeFacturation;
    private List<TypeFacture> typeFacture;
    private List<ModeFacturation> modeFacturation;
    private String[] periodicite;
    private String[] langue;

    /**
     * Creates a new instance of FicheClientMB
     */
    public FacturationClientMB() {

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

     public void majFacturation() {
        try {
            generiqueBean.getService().update(typeFacturation);
            MessageUtil.messageInfo("Modification effectuée");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur de modification");
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
public TypeFacturationClient getTypeFacturation() {
        if (typeFacturation == null) {
            try {
                TypeFacturationClient tf = new TypeFacturationClient();
                tf.setIdClient(idClient);
                typeFacturation = (TypeFacturationClient) generiqueBean.getService().find(tf).get(0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return typeFacturation;
    }

    public void setTypeFacturation(TypeFacturationClient typeFacturation) {
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
}
