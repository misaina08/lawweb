/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.facture;

import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.facturation.FactureLibelle;

/**
 *
 * @author misa
 */
@Named(value = "listeFactureMB")
@ViewScoped
public class ListeFactureMB implements Serializable {

    private List<FactureLibelle> factures;
    @EJB
    private GeneriqueBean generiqueBean;

    /**
     * Creates a new instance of ListeFactureMB
     */
    public ListeFactureMB() {
    }

    public List<FactureLibelle> getFactures() {
        if (factures == null) {
            try {
                factures = (List<FactureLibelle>) (List<?>) generiqueBean.getService().find(new FactureLibelle());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return factures;
    }

    public void setFactures(List<FactureLibelle> factures) {
        this.factures = factures;
    }

}
