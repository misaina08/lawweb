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
import java.util.Calendar;
import java.util.Date;
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
    private List<EvtDossierLibelle> evenementsAffiches;
    private List<MtTypeTarif> totauxTarif;
    private Util util = new Util();
    private String dateEntre;
    private Integer idIntervSelected = 0;

    private Date dateDebut;
    private Date dateFin;
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
            selectOnChange();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<MtTypeTarif> calculerTotaux() {
        List<MtTypeTarif> res = null;
        try {
            return evenementBean.calculerTotaux(evenementsAffiches);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void rechercher() {
        try {
            selectOnChange();
            calculerTotaux();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
            lister();
            totauxTarif = calculerTotaux();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void lister() {
        try {
            System.out.println("______________________lister");
            evenementsAffiches = new ArrayList<EvtDossierLibelle>();
            
            for (EvtDossierLibelle e : evenements) {
                if (e.getDaty().after(dateDebut) && e.getDaty().before(dateFin)) {
                    evenementsAffiches.add(e);
                }
            }
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

    public Date getDateDebut() {
        if (dateDebut == null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -15);
            dateDebut = cal.getTime();
        }
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        if (dateFin == null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, +15);
            dateFin = cal.getTime();
        }
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public List<EvtDossierLibelle> getEvenementsAffiches() {
        if (evenementsAffiches== null) {
            selectOnChange();
        }
        return evenementsAffiches;
    }

    public void setEvenementsAffiches(List<EvtDossierLibelle> evenementsAffiches) {
        this.evenementsAffiches = evenementsAffiches;
    }
    

}
