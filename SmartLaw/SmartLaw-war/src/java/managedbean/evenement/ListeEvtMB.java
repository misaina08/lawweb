/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.evenement;

import ejb.EvenementBean;
import ejb.FacturationBean;
import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modeles.dossiers.DossierLibelle;
import modeles.evenement.EvenementDossier;
import modeles.evenement.EvtDossierLibelle;
import modeles.evenement.MtTypeTarif;
import modeles.parametres.TypeTarifEvt;
import statiques.ObjetStatique;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "listeEvtMB")
@ViewScoped
public class ListeEvtMB implements Serializable{

    @EJB
    private FacturationBean facturationBean;

    @EJB
    private EvenementBean evenementBean;

    public ListeEvtMB() {
    }
    @EJB
    private GeneriqueBean generiqueBean;
    
    

    private Integer idDossier;
    private DossierLibelle dossier;
    private List<EvtDossierLibelle> evtDossier;
    private Map<Integer, Boolean> checked = new HashMap<Integer, Boolean>();
    private List<MtTypeTarif> totaux;
    private List<EvtDossierLibelle> evtAImprimer;

//    for prefacturation
    private List<MtTypeTarif> totauxTarif;
    private Date dateFacturation = new Date();
    private Float tva = new Float(20);
    private Float totalHT = new Float(0);
    private Float totalTTC = new Float(0);

    public void loadDossier() {
        try {
            DossierLibelle d = new DossierLibelle();
            d.setId(idDossier);
            System.out.println("id dossssssier " + idDossier);
            dossier = (DossierLibelle) generiqueBean.getService().findById(d);
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.addFlashErrorMessage("Erreur de chargement");
        }
    }

    // for prefacturation
    public List<MtTypeTarif> calculerTotauxSelected() {
        try {
            return evenementBean.calculerTotaux(evtAImprimer);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void initializeMt() {

        try {

            for (TypeTarifEvt tte : ObjetStatique.getTypeTarifEvt()) {
                Float m = new Float(0);
                for (EvtDossierLibelle edl : evtAImprimer) {
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

    public String validerFacture() {
        try {
            System.out.println("validation facture");
            System.out.println("taches " + evtAImprimer);
            facturationBean.genererFacture(evtAImprimer, tva);
            MessageUtil.addFlashInfoMessage("Tâches facturées");
            return "/pages/dossier/liste.xhtml?faces-redirect=true;";
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.addFlashErrorMessage(ex.getMessage());
            return "/pages/dossier/evenement/liste.xhtml?idDossier="+idDossier+"&faces-redirect=true;";
            
        }
    }

    // fin prefacturation
    public void genererPreFacturation() {
        try {
            evtAImprimer = new ArrayList<EvtDossierLibelle>();
            for (EvtDossierLibelle e : evtDossier) {
                if (checked.get(e.getId()) != null) {
                    System.out.println(e.getNomInterv());
                    evtAImprimer.add(e);
                }

            }
            initializeMt();

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public void onClickCheckBox() {
        try {
            
            evtAImprimer = new ArrayList<EvtDossierLibelle>();
            for (EvtDossierLibelle e : evtDossier) {
                if (checked.get(e.getId()) != null) {
                    System.out.println("id"+e.getId());
                    evtAImprimer.add(e);
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public void printListeEvt() {
        try {
            System.out.println("ato am printlist");
            evtAImprimer = new ArrayList<EvtDossierLibelle>();
            for (EvtDossierLibelle e : evtDossier) {
                if (checked.get(e.getId()) != null) {
                    System.out.println(e.getNomInterv());
                    evtAImprimer.add(e);
                }

            }
            
            evenementBean.printListeEvt(evtAImprimer, dossier);
            //            checked.clear();
            //            evtAImprimer.clear();
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public void updateChecked(Integer id) {
        System.out.println("id " + id);
    }

    public String supprimerEvt(Integer id) {
        System.out.println("id evt " + id);
        try {
            EvenementDossier ed = new EvenementDossier();
            ed.setId(id);
            generiqueBean.getService().delete(ed);
            totaux = calculerTotaux();
            MessageUtil.addFlashInfoMessage("Tâche supprimée");
            return "/pages/dossier/evenement/liste.xhtml?idDossier="+idDossier+"&faces-redirect=true;";
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.addFlashWarnMessage("Cette tâche ne peut plus être supprimée. Elle est déjà utilisée ailleur");
            return "/pages/dossier/evenement/liste.xhtml?idDossier="+idDossier+"&faces-redirect=true;";
        }
    }

    public List<MtTypeTarif> calculerTotaux() {
        List<MtTypeTarif> res = null;
        try {
            return evenementBean.calculerTotaux(evtDossier);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public void huuhu(){
        System.out.println("asim");
    }

    public Integer getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(Integer idDossier) {
        this.idDossier = idDossier;
    }

    public DossierLibelle getDossier() {
        return dossier;
    }

    public void setDossier(DossierLibelle dossier) {
        this.dossier = dossier;
    }

    public List<EvtDossierLibelle> getEvtDossier() {
        if (evtDossier == null) {
            try {
                EvtDossierLibelle e = new EvtDossierLibelle();
                e.setIdDossier(idDossier);
                e.setAfacturer(Boolean.TRUE);
                evtDossier = (List<EvtDossierLibelle>) (List<?>) generiqueBean.getService().find(e);
                totaux = calculerTotaux();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return evtDossier;
    }

    public void setEvtDossier(List<EvtDossierLibelle> evtDossier) {
        this.evtDossier = evtDossier;
    }

    public Map<Integer, Boolean> getChecked() {
        return checked;
    }

    public void setChecked(Map<Integer, Boolean> checked) {
        this.checked = checked;
    }

    public List<MtTypeTarif> getTotaux() {
        if (totaux == null) {
            totaux = calculerTotaux();
        }
        return totaux;
    }

    public void setTotaux(List<MtTypeTarif> totaux) {
        this.totaux = totaux;
    }

    public List<EvtDossierLibelle> getEvtAImprimer() {
        return evtAImprimer;
    }

    public void setEvtAImprimer(List<EvtDossierLibelle> evtAImprimer) {
        this.evtAImprimer = evtAImprimer;
    }

    //for prefacturation
    public List<MtTypeTarif> getTotauxTarif() {
        if (totauxTarif == null) {
            totauxTarif = calculerTotauxSelected();
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
