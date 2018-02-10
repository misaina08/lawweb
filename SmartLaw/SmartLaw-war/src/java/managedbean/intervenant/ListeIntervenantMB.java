/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.intervenant;

import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.intervenants.Intervenant;

/**
 *
 * @author misa
 */
@Named(value = "listeIntervenantMB")
@ViewScoped
public class ListeIntervenantMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;

    private List<Intervenant> liste;

    /**
     * Creates a new instance of ListeIntervenantMB
     */
    public ListeIntervenantMB() {
    }

    public List<Intervenant> getListe() {
        if (liste == null) {
            try {
                liste = (List<Intervenant>) (List<?>) generiqueBean.getService().find(new Intervenant());
            } catch (Exception ex) {
                Logger.getLogger(ListeIntervenantMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return liste;
    }

    public void setListe(List<Intervenant> liste) {
        this.liste = liste;
    }

}
