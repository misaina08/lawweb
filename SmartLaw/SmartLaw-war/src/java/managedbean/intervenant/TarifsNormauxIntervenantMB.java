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
import modeles.intervenants.TarifIntervenant;
import modeles.intervenants.TarifNormaux;
import services.IntervenantService;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "tarifsNormauxIntervenantMB")
@ViewScoped
public class TarifsNormauxIntervenantMB implements Serializable {

    /**
     * Creates a new instance of TarifsNormauxMB
     */
    public TarifsNormauxIntervenantMB() {
    }

    @EJB
    private GeneriqueBean generiqueBean;

    private Integer idIntervenant;
    private Intervenant intervenant;
    private List<TarifNormaux> tarifs;
    private TarifNormaux tarif;

    public void loadIntervenant() {
        try {
            Intervenant i = new Intervenant();
            i.setId(idIntervenant);
            intervenant = (Intervenant) generiqueBean.getService().findById(i);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadTN(TarifNormaux t) {
        try {
            tarif = t;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onChangeTaux() {
        try {
            tarif.setMt(tarif.getTaux() * tarif.getMtevt() / 100);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void validerModif() {
        try {
            TarifIntervenant tar = new TarifIntervenant();
            tar.setId(tarif.getId());
            tar.setMt(tarif.getMt());
            tar.setTaux(tarif.getTaux());
            tar.setIdIntervenant(idIntervenant);
            tar.setIdEvtTarif(tarif.getIdevttarif());
            IntervenantService is = new IntervenantService();
            is.updateTarifIntervenant(tar);

            IntervenantService intervenantService = new IntervenantService();
            TarifNormaux t = new TarifNormaux();
            t.setIdintervenant(getIntervenant().getId());

            tarifs = intervenantService.findTarifNormaux(t);

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

    public List<TarifNormaux> getTarifs() {
        if (tarifs == null) {
            try {
                IntervenantService intervenantService = new IntervenantService();
                TarifNormaux t = new TarifNormaux();
                t.setIdintervenant(getIntervenant().getId());

                tarifs = intervenantService.findTarifNormaux(t);
            } catch (Exception ex) {
                Logger.getLogger(TarifsNormauxIntervenantMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tarifs;
    }

    public TarifNormaux getTarif() {
        return tarif;
    }

    public void setTarif(TarifNormaux tarif) {
        this.tarif = tarif;
    }

}
