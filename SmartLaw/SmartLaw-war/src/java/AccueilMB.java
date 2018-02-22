/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.evenement.EvtDossierLibelle;
import modeles.intervenants.Intervenant;
import modeles.planning.PlanningLibelle;

/**
 *
 * @author misa
 */
@Named(value = "accueilMB")
@ViewScoped
public class AccueilMB implements Serializable {

    @EJB
    private GeneriqueBean bean;

    private List<EvtDossierLibelle> tachesAVenir;
    private List<PlanningLibelle> planningAVenir;

    /**
     * Creates a new instance of AccueilMB
     */
    public AccueilMB() {
    }

    public List<EvtDossierLibelle> getTachesAVenir() {
        if (tachesAVenir == null) {
            try {
                FacesContext context = FacesContext.getCurrentInstance();
                Intervenant i = (Intervenant) context.getExternalContext().getSessionMap().get("intervSession");
                EvtDossierLibelle evl = new EvtDossierLibelle();
                evl.setIdIntervenant(i.getId());
                evl.setAfacturer(true);

                List<EvtDossierLibelle> taches = (List<EvtDossierLibelle>) (List<?>) bean.getService().find(evl);
                Date dHier = new Date();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                dHier = cal.getTime();

                tachesAVenir = new ArrayList<EvtDossierLibelle>();
                for (EvtDossierLibelle e : taches) {
                    if (e.getDaty().after(dHier)) {
                        tachesAVenir.add(e);
                    }

                }
            } catch (Exception ex) {
                Logger.getLogger(AccueilMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tachesAVenir;
    }

    public List<PlanningLibelle> getPlanningAVenir() {
        if (planningAVenir == null) {
            try {
                FacesContext context = FacesContext.getCurrentInstance();
                Intervenant i = (Intervenant) context.getExternalContext().getSessionMap().get("intervSession");
                PlanningLibelle p = new PlanningLibelle();
               
                p.setIdIntervenantpl(i.getId());

                List<PlanningLibelle> plannings = (List<PlanningLibelle>) (List<?>) bean.getService().find(p);
                Date dHier = new Date();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                dHier = cal.getTime();

                planningAVenir = new ArrayList<PlanningLibelle>();
                for (PlanningLibelle e : plannings) {
                    if (e.getDatePlanning().after(dHier)) {
                        planningAVenir.add(e);
                    }

                }
            } catch (Exception ex) {
                Logger.getLogger(AccueilMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return planningAVenir;
    }

}
