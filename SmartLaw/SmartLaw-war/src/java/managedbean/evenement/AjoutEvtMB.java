/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.evenement;

import ejb.EvenementBean;
import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modeles.dossiers.DossierLibelle;
import modeles.evenement.EvenementDossier;
import modeles.intervenants.Intervenant;
import modeles.intervenants.TarifsNS;
import utilitaire.MessageUtil;
import utilitaire.Util;

/**
 *
 * @author misa
 */
@Named(value = "ajoutEvtMB")
@ViewScoped
public class AjoutEvtMB implements Serializable {

    public AjoutEvtMB() {
        evtDossier.setDaty(new Date());
    }

    @EJB
    private GeneriqueBean generiqueBean;

    @EJB
    private EvenementBean evenementBean;

    private Integer idDossier;
    private DossierLibelle dossier;
    private EvenementDossier evtDossier = new EvenementDossier();
    private List<TarifsNS> tarifsNSInterv;
    private List<Intervenant> intervenants;
    private List<Intervenant> observateurs;
    private Integer idIntervenantSelected;
    private Integer idObservateurSelected;
    private Boolean sansDuree = false;
    private Integer idTarifsNSSelected;

    private TarifsNS tarifNSSelected;

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

    public void selectTarifsNSInterv() throws Exception {
        try {
            System.out.println("id intervenant " + idIntervenantSelected);
            TarifsNS tarifsInterv = new TarifsNS();
            tarifsInterv.setIdintervenant(idIntervenantSelected);
            tarifsNSInterv = new ArrayList<TarifsNS>();
            tarifsNSInterv = (List<TarifsNS>) (List<?>) generiqueBean.getService().find(tarifsInterv);
            if (tarifsNSInterv.size() == 0) {
                MessageUtil.messageWarn("Cet intervenant ne possède aucun tarif");
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public void validEvt() {
        Util util = null;
        try {
            util = new Util();
//            tarifNSSelected.setDuree(util.toTime(util.dateToTimeString(dureeTime)));
            evtDossier.setDuree(tarifNSSelected.getDuree());
            evtDossier.setMt(tarifNSSelected.getMt());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void intervOnChange(AjaxBehaviorEvent event) {
        try {
            tarifNSSelected = null;
            evtDossier.setIdEvtTarif(0);
            Util u= new Util();
            evtDossier.setDuree(u.setTimeToZero(new Date()));
            evtDossier.setMt(new Float(0));

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public void dureeOnChange() {
        Float mt;
        try {
            mt = evenementBean.calculMtInterv(evtDossier.getDuree(), idIntervenantSelected, tarifNSSelected.getIdevttarif());
            System.out.println("montant " + mt);
            evtDossier.setMt(mt);

        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

    public String valider() {
        try {
            evtDossier.setAfacturer(Boolean.TRUE);
            evtDossier.setIdIntervenant(idIntervenantSelected);
            evtDossier.setDemandeur(idObservateurSelected);
            evtDossier.setIdDossier(idDossier);
            evtDossier.setIdEvtTarif(tarifNSSelected.getIdevttarif());
            evtDossier.setImprimer(Boolean.FALSE);
            generiqueBean.getService().save(evtDossier);
            MessageUtil.addFlashInfoMessage("Tâche ajoutée");
            return "/pages/dossier/evenement/liste.xhtml?idDossier=" + idDossier + "&faces-redirect=true;";
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.addFlashErrorMessage("Erreur d'ajout");
            return "";
        }
    }

    private Converter tarifNSConverter = new Converter() {
        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            TarifsNS t = null;
            try {
                t = new TarifsNS();
                t.setId(new Integer(value));
                t = (TarifsNS) generiqueBean.getService().findById(t);
                return t;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Object value) {
            TarifsNS titre = (TarifsNS) value;
            return titre.getId() + "";
        }

    };

    public void sansDureeOnChange() {
        try {
            if (sansDuree) {
                Util u = new Util();
                evtDossier.setMt(new Float(0));
                evtDossier.setDuree(u.setTimeToZero(new Date()));
            } else {
                evtDossier.setDuree(tarifNSSelected.getDuree());
                evtDossier.setMt(tarifNSSelected.getMt());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    
    
    public Converter getTarifNSConverter() {
        return tarifNSConverter;
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

    public EvenementDossier getEvtDossier() {
        return evtDossier;
    }

    public void setEvtDossier(EvenementDossier evtDossier) {
        this.evtDossier = evtDossier;
    }

    public List<TarifsNS> getTarifsNSInterv() {
        return tarifsNSInterv;
    }

    public void setTarifsNSInterv(List<TarifsNS> tarifsNSInterv) {
        this.tarifsNSInterv = tarifsNSInterv;
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

    public List<Intervenant> getObservateurs() {
        if (observateurs == null) {
            try {
                observateurs = (List<Intervenant>) (List<?>) generiqueBean.getService().find(new Intervenant());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return observateurs;
    }

    public void setObservateurs(List<Intervenant> observateurs) {
        this.observateurs = observateurs;
    }

    public Integer getIdIntervenantSelected() {
        return idIntervenantSelected;
    }

    public void setIdIntervenantSelected(Integer idIntervenantSelected) {
        this.idIntervenantSelected = idIntervenantSelected;
    }

    public Integer getIdObservateurSelected() {
        return idObservateurSelected;
    }

    public void setIdObservateurSelected(Integer idObservateurSelected) {
        this.idObservateurSelected = idObservateurSelected;
    }

    public Boolean getSansDuree() {
        return sansDuree;
    }

    public void setSansDuree(Boolean sansDuree) {
        this.sansDuree = sansDuree;
    }

    public Integer getIdTarifsNSSelected() {
        return idTarifsNSSelected;
    }

    public void setIdTarifsNSSelected(Integer idTarifsNSSelected) {
        this.idTarifsNSSelected = idTarifsNSSelected;
    }

    public TarifsNS getTarifNSSelected() {
        return tarifNSSelected;
    }

    public void setTarifNSSelected(TarifsNS tarifNSSelected) {
        this.tarifNSSelected = tarifNSSelected;
    }

}
