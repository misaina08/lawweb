/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.feuilledetemps;

import ejb.EvenementBean;
import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.evenement.EvtDossierLibelle;
import modeles.evenement.MtTypeTarif;
import modeles.intervenants.Intervenant;
import utilitaire.Util;

/**
 *
 * @author misa
 */
@Named(value = "feuilleDeTempsMB")
@ViewScoped
public class FeuilleDeTempsMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;
    @EJB
    private EvenementBean evenementBean;

    private List<Intervenant> intervenants;
    private List<EvtDossierLibelle> evenements = new ArrayList<EvtDossierLibelle>();
    private List<MtTypeTarif> totauxTarif;
    private Util util = new Util();

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
            totauxTarif = calculerTotaux();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<MtTypeTarif> calculerTotaux() {
        List<MtTypeTarif> res = null;
        try {
            return evenementBean.calculerTotaux(evenements);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
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

    public List<MtTypeTarif> getTotauxTarif() {
        if (totauxTarif == null) {
            totauxTarif = calculerTotaux();
        }
        return totauxTarif;
    }

    public Util getUtil() {
        return util;
    }

}
