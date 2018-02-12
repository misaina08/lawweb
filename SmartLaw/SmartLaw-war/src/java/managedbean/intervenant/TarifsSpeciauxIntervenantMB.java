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
import modeles.evenement.EvtTarif;
import modeles.intervenants.Intervenant;
import modeles.intervenants.TarifSpeciaux;
import modeles.parametres.TypeTarifEvt;
import services.IntervenantService;
import statiques.ObjetStatique;
import utilitaire.MessageUtil;
import utilitaire.Util;

/**
 *
 * @author misa
 */
@Named(value = "tarifsSpeciauxIntervenantMB")
@ViewScoped
public class TarifsSpeciauxIntervenantMB implements Serializable {

    /**
     * Creates a new instance of TarifsNormauxMB
     */
    public TarifsSpeciauxIntervenantMB() {
    }

    @EJB
    private GeneriqueBean generiqueBean;

    private Integer idIntervenant;
    private Intervenant intervenant;
    private List<TarifSpeciaux> tarifs;
    private TarifSpeciaux tarif;
    private List<TypeTarifEvt> typesTarif;
    private String duree;

    public void loadIntervenant() {
        try {
            Intervenant i = new Intervenant();
            i.setId(idIntervenant);
            intervenant = (Intervenant) generiqueBean.getService().findById(i);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadTS(TarifSpeciaux t) {
        Util util = new Util();
        try {
            tarif = t;
            duree = util.addPrefix(2, tarif.getDuree().getHours() + "", "0") + ":" + util.addPrefix(2, tarif.getDuree().getMinutes() + "", "0");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void validerModif() {
        try {
            Util util =  new Util();
            EvtTarif aModifier = new EvtTarif();
            aModifier.setId(tarif.getIdevttarif());
            aModifier = (EvtTarif) generiqueBean.getService().findById(aModifier);

            aModifier.setIdTypeTarif(tarif.getIdTypeTarif());
            aModifier.setLibelle(tarif.getLibelle());
            tarif.setDuree(util.toTime(duree));
            aModifier.setCode(tarif.getCode());
            aModifier.setDuree(tarif.getDuree());
            aModifier.setMontant(tarif.getMt());
            generiqueBean.getService().update(aModifier);

            IntervenantService intervenantService = new IntervenantService();
            TarifSpeciaux t = new TarifSpeciaux();
            t.setIdintervenant(idIntervenant);
            t.setType("ts");
            tarifs = intervenantService.findTarifSpeciaux(t);
            MessageUtil.messageInfo("Tarif modifié");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur de modification");
        }
    }

    public Integer getIdIntervenant() {
        return idIntervenant;
    }

    public void setIdIntervenant(Integer idIntervenant) {
        this.idIntervenant = idIntervenant;
    }

    public Intervenant getIntervenant() {
        return intervenant;
    }

    public void setIntervenant(Intervenant intervenant) {
        this.intervenant = intervenant;
    }

    public List<TarifSpeciaux> getTarifs() {
        if (tarifs == null) {
            try {
                IntervenantService intervenantService = new IntervenantService();
                TarifSpeciaux t = new TarifSpeciaux();
                t.setIdintervenant(idIntervenant);
                t.setType("ts");
                tarifs = intervenantService.findTarifSpeciaux(t);
            } catch (Exception ex) {
                Logger.getLogger(TarifsSpeciauxIntervenantMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tarifs;
    }

    public TarifSpeciaux getTarif() {
        return tarif;
    }

    public void setTarif(TarifSpeciaux tarif) {
        this.tarif = tarif;
    }

    public List<TypeTarifEvt> getTypesTarif() {
        if (typesTarif == null) {
            typesTarif = ObjetStatique.getTypeTarifEvt();
        }
        return typesTarif;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

}
