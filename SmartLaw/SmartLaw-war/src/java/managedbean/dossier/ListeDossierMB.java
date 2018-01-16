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

/**
 *
 * @author misa
 */
@Named(value = "listeDossierMB")
@ViewScoped
public class ListeDossierMB implements Serializable{

    @EJB
    private GeneriqueBean generiqueBean;

    
    private List<DossierLibelle> dossiers; 
    /**
     * Creates a new instance of ListeDossierMB
     */
    public ListeDossierMB() {
    }

    public List<DossierLibelle> getDossiers() {
        if (dossiers == null) {
            try {
                dossiers = (List<DossierLibelle>)(List<?>) generiqueBean.getService().find(new DossierLibelle());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return dossiers;
    }

    public void setDossiers(List<DossierLibelle> dossiers) {
        this.dossiers = dossiers;
    }
    
    
}
