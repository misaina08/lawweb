/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.feuilledetemps;

import ejb.EvenementBean;
import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private List<EvtDossierLibelle> evenements;
    private List<MtTypeTarif> totauxTarif;
    private Util util = new Util();
    private String dateEntre;
    private Integer idIntervSelected = 0;
    // toutes si 0
    // à facturées si 1 (true)
    // facturées si 2 (false)
    private Integer aFactureeSelected = 0;

    /**
     * Creates a new instance of FeuilleDeTempsMB
     */
    public FeuilleDeTempsMB() {
    }

    public void onIntervChange(Integer id) {
        try {
            idIntervSelected = id;
//            if (id == 0) {
//                evenements = (List<EvtDossierLibelle>) (List<?>) generiqueBean.getService().find(new EvtDossierLibelle());
//            } else {
//                EvtDossierLibelle e = new EvtDossierLibelle();
//                e.setIdIntervenant(id);
//                evenements = (List<EvtDossierLibelle>) (List<?>) generiqueBean.getService().find(e);
//
//            }
//            totauxTarif = calculerTotaux();
            selectOnChange();
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

    public void rechercher() {
        System.out.println("date range " + dateEntre);
    }

    public void onLegendeChange(Integer afacturee) {
        try {
            aFactureeSelected = afacturee;
            selectOnChange();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void selectOnChange() {
        try {
            EvtDossierLibelle e = new EvtDossierLibelle();
            if (idIntervSelected != 0) {
                e.setIdIntervenant(idIntervSelected);
            } else {
                e.setIdIntervenant(null);
            }
            if (aFactureeSelected == 0) {
                e.setAfacturer(null);
            } else if (aFactureeSelected == 1) {
                e.setAfacturer(Boolean.TRUE);
            } else if (aFactureeSelected == 2) {
                e.setAfacturer(Boolean.FALSE);
            }
            evenements = (List<EvtDossierLibelle>) (List<?>) generiqueBean.getService().find(e);
            totauxTarif = calculerTotaux();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Intervenant> getIntervenants() {
        if (intervenants == null) {
            try {
                Intervenant tous = new Intervenant();
                tous.setId(0);
                tous.setNom("Tous");
                intervenants = (List<Intervenant>) (List<?>) generiqueBean.getService().find(new Intervenant());
                intervenants.add(0, tous);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return intervenants;
    }

    public List<EvtDossierLibelle> getEvenements() {
        if (evenements == null) {
            try {
                evenements = (List<EvtDossierLibelle>) (List<?>) generiqueBean.getService().find(new EvtDossierLibelle());
                totauxTarif = calculerTotaux();
            } catch (Exception ex) {
                Logger.getLogger(FeuilleDeTempsMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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

    public String getDateEntre() {
        return dateEntre;
    }

    public void setDateEntre(String dateEntre) {
        this.dateEntre = dateEntre;
    }

}
