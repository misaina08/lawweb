/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.facture;

import ejb.EvenementBean;
import ejb.FacturationBean;
import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modeles.evenement.EvtDossierLibelle;
import modeles.evenement.MtTypeTarif;
import modeles.parametres.TypeTarifEvt;
import statiques.ObjetStatique;

/**
 *
 * @author misa
 */
@Named(value = "preFacturationMB")
@ViewScoped
public class PreFacturationMB  implements Serializable{

    @EJB
    private FacturationBean facturationBean;

    @EJB
    private EvenementBean evenementBean;

    @EJB
    private GeneriqueBean generiqueBean;
    

    private List<EvtDossierLibelle> taches = new ArrayList<EvtDossierLibelle>();
    private List<MtTypeTarif> totauxTarif;
    private Date dateFacturation = new Date();
    private Float tva = new Float(20);
    private Float totalHT = new Float(0);
    private Float totalTTC = new Float(0);

    public PreFacturationMB() {
    }

    public void loadEvtDossier(List<EvtDossierLibelle> evtDossier) {
        try {

            for (EvtDossierLibelle e : evtDossier) {
                System.out.println("libelle " + e.getLibelle());
            }
            taches = new ArrayList<EvtDossierLibelle>();
            taches.addAll(evtDossier);
            totauxTarif = calculerTotaux();
            initializeMt();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void huhu() {
        System.out.println("huhu");
    }
    
    public String validerFacture(List<EvtDossierLibelle> evtDossier) {
        try {
            System.out.println("validation facture");
            System.out.println("taches "+evtDossier);
//            facturationBean.genererFacture(taches, tva);
            return "/pages/dossier/liste.xhtml?faces-redirect=true;";
        } catch(Exception ex) {
            ex.printStackTrace();
            return "/pages/dossier/liste.xhtml";
        }
    } 

    public List<MtTypeTarif> calculerTotaux() {
        try {
            return evenementBean.calculerTotaux(taches);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void initializeMt() {

        try {

            for (TypeTarifEvt tte : ObjetStatique.getTypeTarifEvt()) {
                Float m = new Float(0);
                for (EvtDossierLibelle edl : taches) {
                    if (tte.getId().equals(edl.getIdTypeTarif())) {
                        m += edl.getMt();
                    }
                }
                totalHT += m;
            }
            Float stva = totalHT * tva / 100;
            totalTTC = stva + totalHT;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<EvtDossierLibelle> getTaches() {
        return taches;
    }

    public void setTaches(List<EvtDossierLibelle> taches) {
        this.taches = taches;
    }

    public List<MtTypeTarif> getTotauxTarif() {
        if (totauxTarif == null) {
            totauxTarif = calculerTotaux();
        }
        return totauxTarif;
    }

    public void setTotauxTarif(List<MtTypeTarif> totauxTarif) {
        this.totauxTarif = totauxTarif;
    }

    public Date getDateFacturation() {
        return dateFacturation;
    }

    public void setDateFacturation(Date dateFacturation) {
        this.dateFacturation = dateFacturation;
    }

    public Float getTva() {
        return tva;
    }

    public void setTva(Float tva) {
        this.tva = tva;
    }

    public Float getTotalHT() {
        return totalHT;
    }

    public void setTotalHT(Float totalHT) {
        this.totalHT = totalHT;
    }

    public Float getTotalTTC() {
        return totalTTC;
    }

    public void setTotalTTC(Float totalTTC) {
        this.totalTTC = totalTTC;
    }

}
