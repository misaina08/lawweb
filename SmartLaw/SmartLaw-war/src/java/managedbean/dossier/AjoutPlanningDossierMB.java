/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.dossier;

import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.dossiers.DossierLibelle;
import modeles.evenement.EvtTarif;
import modeles.intervenants.Intervenant;
import modeles.parametres.Juridiction;
import modeles.planning.Planning;
import statiques.ObjetStatique;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "ajoutPlanningDossierMB")
@ViewScoped
public class AjoutPlanningDossierMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;

    private Integer idDossier;
    private DossierLibelle dossier;
    private Planning planning = new Planning();
    private List<EvtTarif> evt;
    private EvtTarif evtSelected = new EvtTarif();
    private List<Juridiction> juridictions;
    private String[] transport;
    private List<Intervenant> gestionnaires;
    private List<Intervenant> intervenants;

    /**
     * Creates a new instance of AjoutPlanningDossier
     */
    public AjoutPlanningDossierMB() {
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

    public void loadEvt() {
        try {
            System.out.println("load evt");
            if (evt == null) {
                evt = (List<EvtTarif>) (List<?>) generiqueBean.getService().find(new EvtTarif());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void validEvt() {
        try {
            EvtTarif e = new EvtTarif();
            e.setId(planning.getIdEvt());
            evtSelected = (EvtTarif) generiqueBean.getService().findById(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public String creer() {
        try {
            planning.setLibelle(planning.getNotes());
            planning.setPlanifiee(Boolean.TRUE);
            planning.setIdDossier(idDossier);
            planning.setIdTypeProcedure(ObjetStatique.getTypeProcedure().get(0).getId());
            
            generiqueBean.getService().save(planning);
            MessageUtil.addFlashInfoMessage("Création du planning effectuée");
            return "/pages/dossier/planning/liste.xhtml?idDossier="+idDossier+"&faces-redirect=true;";
        } catch(Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur de création du planning");
            return "";
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

    public Planning getPlanning() {
        return planning;
    }

    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    public List<EvtTarif> getEvt() {
        return evt;
    }

    public void setEvt(List<EvtTarif> evt) {
        this.evt = evt;
    }

    public EvtTarif getEvtSelected() {
        return evtSelected;
    }

    public void setEvtSelected(EvtTarif evtSelected) {
        this.evtSelected = evtSelected;
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

    public String[] getTransport() {
        if (transport == null) {
            transport = ObjetStatique.getTransport();
        }
        return transport;
    }

    public void setTransport(String[] transport) {
        this.transport = transport;
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

}
