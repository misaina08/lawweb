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
import modeles.dossiers.DossierLibelle;
import modeles.planning.Planning;
import modeles.planning.PlanningLibelle;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "listePlanningDossier")
@ViewScoped
public class ListePlanningDossier implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;

    private Integer idDossier;
    private DossierLibelle dossier;
    

    /**
     * Creates a new instance of ListePlanningDossier
     */
    public ListePlanningDossier() {
    }

    private List<PlanningLibelle> planning;

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
    
    public String supprimer(Integer id) {
        try {
            Planning p = new Planning();
            p.setId(id);
            generiqueBean.getService().delete(p);
            MessageUtil.addFlashInfoMessage("Planning supprimé");
            return "/pages/dossier/planning/liste.xhtml?idDossier="+idDossier+"&faces-redirect=true;";
        } catch(Exception ex) {
            ex.printStackTrace();
            MessageUtil.addFlashErrorMessage("Erreur de suppression");
            return "";
        }
    }

    public List<PlanningLibelle> getPlanning() {
        if (planning == null) {
            try {
                PlanningLibelle p = new PlanningLibelle();
                p.setIdDossier(idDossier);
                planning = (List<PlanningLibelle>) (List<?>) generiqueBean.getService().find(p);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return planning;
    }

    public void setPlanning(List<PlanningLibelle> planning) {
        this.planning = planning;
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

}
