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
@Named(value = "modificationPlanningMB")
@ViewScoped
public class ModificationPlanningMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;

    private Integer idDossier;
    private Integer idPlanning;
    private Planning planning;

    private List<EvtTarif> evt;
    private EvtTarif evtSelected;
    private List<Juridiction> juridictions;
    private String[] transport;
    private List<Intervenant> gestionnaires;
    private List<Intervenant> intervenants;

    /**
     * Creates a new instance of ModificationPlanningMB
     */
    public ModificationPlanningMB() {
    }

    public void loadPlanning() {
        try {
            Planning p = new Planning();
            p.setId(idPlanning);
            planning = (Planning) generiqueBean.getService().findById(p);

        } catch (Exception ex) {
            ex.printStackTrace();
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
            System.out.println("id avant "+planning.getIdEvt());
            evtSelected = (EvtTarif) generiqueBean.getService().findById(e);
            System.out.println("id apres "+evtSelected.getId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public String modifier() {
        try {
            generiqueBean.getService().update(planning);
            MessageUtil.addFlashInfoMessage("Planning modifié");
            return "/pages/dossier/planning/liste.xhtml?idDossier="+idDossier+"&faces-redirect=true;";
        } catch(Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur de modiication");
            return "";
        }
    }

    public Integer getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(Integer idDossier) {
        this.idDossier = idDossier;
    }

    public Integer getIdPlanning() {
        return idPlanning;
    }

    public void setIdPlanning(Integer idPlanning) {
        this.idPlanning = idPlanning;
    }

    public Planning getPlanning() {
        return planning;
    }

    public void setPlanning(Planning planning) {
        this.planning = planning;
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

    public List<EvtTarif> getEvt() {
        return evt;
    }

    public void setEvt(List<EvtTarif> evt) {
        this.evt = evt;
    }

    public EvtTarif getEvtSelected() {
        if (evtSelected == null) {
            System.out.println("null fona");
            try {
                EvtTarif e = new EvtTarif();
                e.setId(planning.getIdEvt());
                evtSelected = (EvtTarif) generiqueBean.getService().findById(e);
            } catch (Exception ex) {
                Logger.getLogger(ModificationPlanningMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return evtSelected;
    }

    public void setEvtSelected(EvtTarif evtSelected) {
        this.evtSelected = evtSelected;
    }

}
