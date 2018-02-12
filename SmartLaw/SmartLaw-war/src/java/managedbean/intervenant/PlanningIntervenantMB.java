/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.intervenant;

import ejb.GeneriqueBean;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.intervenants.IntervenantFonctionLibelle;

/**
 *
 * @author misa
 */
@Named(value = "planningIntervenantMB")
@ViewScoped
public class PlanningIntervenantMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;

    private Integer idIntervenant;
    private IntervenantFonctionLibelle intervenant;

    /**
     * Creates a new instance of PlanningIntervenantMB
     */
    public PlanningIntervenantMB() {
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
    
}
