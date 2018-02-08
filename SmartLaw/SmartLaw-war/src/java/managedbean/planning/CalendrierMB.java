/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.planning;

import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.intervenants.Intervenant;
import modeles.planning.PlanningLibelle;

/**
 *
 * @author misa
 */
@Named(value = "calendrierMB")
@ViewScoped
public class CalendrierMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;
    private List<PlanningLibelle> planning;
    private Date now = new Date();
    private List<Intervenant> intervenants;

    public CalendrierMB() {
    }

    public List<PlanningLibelle> getPlanning() {
        if (planning == null) {
            try {
                planning = (List<PlanningLibelle>) (List<?>) generiqueBean.getService().find(new PlanningLibelle());
            } catch (Exception ex) {
                Logger.getLogger(CalendrierMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return planning;
    }

    public void onIntervChange(Integer id) {
        try {

            if (id == 0) {
                planning = (List<PlanningLibelle>) (List<?>) generiqueBean.getService().find(new PlanningLibelle());
            } else {
                PlanningLibelle p = new PlanningLibelle();
                p.setIdIntervenantpl(id);
                planning = (List<PlanningLibelle>) (List<?>) generiqueBean.getService().find(p);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Date getNow() {
        return now;
    }

    public List<Intervenant> getIntervenants() {
        if (intervenants == null) {
            try {
                Intervenant tous = new Intervenant();
                tous.setId(0);
                tous.setNom("Tous");
                intervenants = new ArrayList<Intervenant>();
                intervenants = (List<Intervenant>) (List<?>) generiqueBean.getService().find(new Intervenant());
                intervenants.add(0, tous);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return intervenants;
    }

}
