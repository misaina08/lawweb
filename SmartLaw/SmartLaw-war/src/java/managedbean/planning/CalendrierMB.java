/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.planning;

import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.planning.PlanningLibelle;

/**
 *
 * @author misa
 */
@Named(value = "calendrierMB")
@ViewScoped
public class CalendrierMB implements Serializable{

    @EJB
    private GeneriqueBean generiqueBean;
    private List<PlanningLibelle> planning;
    private Date now = new Date();
    
    public CalendrierMB() {
    }

    public List<PlanningLibelle> getPlanning() {
        if (planning == null) {
            try {
                planning = (List<PlanningLibelle>)(List<?>) generiqueBean.getService().find(new PlanningLibelle());
            } catch (Exception ex) {
                Logger.getLogger(CalendrierMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return planning;
    }

    public Date getNow() {
        return now;
    }
    
    
}
