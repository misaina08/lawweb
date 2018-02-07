/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.feuilledetemps;

import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.evenement.EvtDossierLibelle;
import modeles.intervenants.Intervenant;

/**
 *
 * @author misa
 */
@Named(value = "feuilleDeTempsMB")
@ViewScoped
public class FeuilleDeTempsMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;

    private List<Intervenant> intervenants;
    private List<EvtDossierLibelle> evenements = new ArrayList<EvtDossierLibelle>();

    /**
     * Creates a new instance of FeuilleDeTempsMB
     */
    public FeuilleDeTempsMB() {
    }

    public void onIntervChange(Integer id) {
        try {
            EvtDossierLibelle e = new EvtDossierLibelle();
            e.setIdIntervenant(id);
            evenements = (List<EvtDossierLibelle>) (List<?>) generiqueBean.getService().find(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    public List<EvtDossierLibelle> getEvenements() {
        return evenements;
    }

}
