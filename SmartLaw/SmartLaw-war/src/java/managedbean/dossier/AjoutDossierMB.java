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
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modeles.clients.Client;
import modeles.dossiers.Dossier;
import modeles.intervenants.Intervenant;
import modeles.intervenants.IntervenantDossier;
import modeles.parametres.Juridiction;
import modeles.parametres.Nature;
import statiques.ObjetStatique;
import utilitaire.MessageUtil;
import utilitaire.Util;

/**
 *
 * @author misa
 */
@Named(value = "ajoutDossierMB")
@ViewScoped
public class AjoutDossierMB implements Serializable {

    @EJB
    private DossierBean dossierBean;

    @EJB
    private GeneriqueBean generiqueBean;

    private Dossier newDossier = new Dossier();
    private List<Juridiction> juridictions;
    private List<Nature> natures;
    private List<Client> clients;
    private List<Intervenant> gestionnaires;
    private List<Intervenant> intervenants;

    private Client clientSelected = new Client();
    private Integer idClientSelected;
    private Integer[] intervenantSelected;

    /**
     * Creates a new instance of AjoutDossierMB
     */
    public AjoutDossierMB() {

    }

    public void selectClient() {
        try {
            if (clients == null) {
                clients = (List<Client>) (List<?>) generiqueBean.getService().find(new Client());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void validClient() {
        try {
            clientSelected = new Client();
            clientSelected.setId(idClientSelected);
            clientSelected = (Client) generiqueBean.getService().findById(clientSelected);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String creer() {
        Util u = null;
        try {
            u = new Util();
            newDossier.setIdClient(idClientSelected);
            Integer iddoss = generiqueBean.getService().getNextVal("dossier_iddossier_seq");
            newDossier.setNumeroDossier(u.dateToString(newDossier.getDateOuverture()).substring(8, 10) + "" + u.addPrefix(4, iddoss.toString(), "0"));
            newDossier.setId(iddoss);
            dossierBean.save(newDossier);

            for (int i = 0; i < intervenantSelected.length; i++) {
                IntervenantDossier intervDoss = new IntervenantDossier();
                intervDoss.setIdDossier(iddoss);
                intervDoss.setIdIntervenant(intervenantSelected[i]);
                generiqueBean.getService().save(intervDoss);
            }

            MessageUtil.addFlashInfoMessage("Dossier créé");
            return "/pages/dossier/liste.xhtml?faces-redirect=true;";
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.addFlashErrorMessage("Erreur d'ajout");
            return "";
        }
    }

    public Dossier getNewDossier() {
        return newDossier;
    }

    public void setNewDossier(Dossier newDossier) {
        this.newDossier = newDossier;
    }

    public List<Juridiction> getJuridictions() {
        if (juridictions == null) {
            juridictions = ObjetStatique.getJuridictions();
        }
        return juridictions;
    }

    public void setJuridictions(List<Juridiction> juridictions) {
        this.juridictions = juridictions;
    }

    public List<Nature> getNatures() {
        if (natures == null) {
            natures = ObjetStatique.getNatures();
        }
        return natures;
    }

    public void setNatures(List<Nature> natures) {
        this.natures = natures;
    }

    public List<Client> getClients() {
        try {
            if (clients == null) {
                System.out.println("get set" + clients);
                clients = (List<Client>) (List<?>) generiqueBean.getService().find(new Client());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public List<Intervenant> getGestionnaires() {
        if (gestionnaires == null) {
            try {
                Intervenant i = new Intervenant();
                i.setGestionnaire(Boolean.TRUE);
                gestionnaires = (List<Intervenant>) (List<?>) generiqueBean.getService().find(i);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return gestionnaires;
    }

    public void setGestionnaires(List<Intervenant> gestionnaires) {
        this.gestionnaires = gestionnaires;
    }

    public List<Intervenant> getIntervenants() {
        if (intervenants == null) {
            try {
                intervenants = (List<Intervenant>) (List<?>) generiqueBean.getService().find(new Intervenant());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return intervenants;
    }

    public void setIntervenants(List<Intervenant> intervenants) {
        this.intervenants = intervenants;
    }

    public Client getClientSelected() {
        return clientSelected;
    }

    public void setClientSelected(Client clientSelected) {
        this.clientSelected = clientSelected;
    }

    public Integer getIdClientSelected() {
        return idClientSelected;
    }

    public void setIdClientSelected(Integer idClientSelected) {
        this.idClientSelected = idClientSelected;
    }

    public Integer[] getIntervenantSelected() {
        return intervenantSelected;
    }

    public void setIntervenantSelected(Integer[] intervenantSelected) {
        this.intervenantSelected = intervenantSelected;
    }

}
