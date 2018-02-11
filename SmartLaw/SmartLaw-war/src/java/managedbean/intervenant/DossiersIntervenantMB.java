/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.intervenant;

import ejb.EvenementBean;
import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.affichage.EvtDossierLibGroupBy;
import modeles.intervenants.IntervenantFonctionLibelle;

/**
 *
 * @author misa
 */
@Named(value = "dossiersIntervenantMB")
@ViewScoped
public class DossiersIntervenantMB implements Serializable {

    @EJB
    private EvenementBean evenementBean;

    @EJB
    private GeneriqueBean generiqueBean;
    

    private Integer idIntervenant;
    private IntervenantFonctionLibelle intervenant;
    private List<EvtDossierLibGroupBy> dossiersEnInterv;
    private List<EvtDossierLibGroupBy> dossiersEnDemandeur;

    /**
     * Creates a new instance of DossiersIntervenantMB
     */
    public DossiersIntervenantMB() {
    }

    public void loadIntervenant() {
        try {
            IntervenantFonctionLibelle i = new IntervenantFonctionLibelle();
            i.setId(idIntervenant);
            intervenant = (IntervenantFonctionLibelle) generiqueBean.getService().findById(i);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Integer getIdIntervenant() {
        return idIntervenant;
    }

    public void setIdIntervenant(Integer idIntervenant) {
        this.idIntervenant = idIntervenant;
    }

    public IntervenantFonctionLibelle getIntervenant() {
        return intervenant;
    }

    public void setIntervenant(IntervenantFonctionLibelle intervenant) {
        this.intervenant = intervenant;
    }

    public List<EvtDossierLibGroupBy> getDossiersEnInterv() {
        if (dossiersEnInterv == null) {
            try {
                dossiersEnInterv = evenementBean.findDossierByRoleInterv(idIntervenant, "intervenant");
            } catch (Exception ex) {
                Logger.getLogger(DossiersIntervenantMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return dossiersEnInterv;
    }

    public List<EvtDossierLibGroupBy> getDossiersEnDemandeur() {
        if (dossiersEnDemandeur == null) {
            try {
                dossiersEnDemandeur = evenementBean.findDossierByRoleInterv(idIntervenant, "demandeur");
            } catch (Exception ex) {
                Logger.getLogger(DossiersIntervenantMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return dossiersEnDemandeur;
    }
    
    
}
