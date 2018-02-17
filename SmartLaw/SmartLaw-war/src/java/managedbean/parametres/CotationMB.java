/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.parametres;

import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.evenement.EvtTarif;
import modeles.evenement.EvtTarifLibelle;
import modeles.parametres.TypeTarifEvt;
import statiques.ObjetStatique;
import utilitaire.MessageUtil;
import utilitaire.Util;

/**
 *
 * @author misa
 */
@Named(value = "cotationMB")
@ViewScoped
public class CotationMB implements Serializable {

    @EJB
    private GeneriqueBean bean;

    private List<EvtTarifLibelle> cotations;
    private List<TypeTarifEvt> types;
    private EvtTarif newTarif = new EvtTarif();
    private EvtTarif toUpdate;
    private String duree = "00:00";

    /**
     * Creates a new instance of CotationMB
     */
    public CotationMB() {
    }

    public void init() {
        try {
            EvtTarifLibelle evttariflib = new EvtTarifLibelle();
            evttariflib.setType("tn");
            cotations = (List<EvtTarifLibelle>) (List<?>) bean.getService().find(evttariflib);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void ajouter() {
        try {
            Util u = new Util();
            newTarif.setDuree(u.toTime(duree));
            newTarif.setType("tn");
            bean.getService().save(newTarif);
            newTarif = new EvtTarif();
            init();
            duree = "00:00";
            MessageUtil.messageInfo("Ajout effectuée");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur d'ajout");
        }
    }

    public void modifier(Integer id) {
        try {
            EvtTarif e = new EvtTarif();
            e.setId(id);
            toUpdate = (EvtTarif) bean.getService().findById(e);
            Util u = new Util();
            duree = u.addPrefix(2, "" + toUpdate.getDuree().getHours(), "0") + ":" + u.addPrefix(2, "" + toUpdate.getDuree().getMinutes(), "0");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void validModif() {
        try {
            Util u = new Util();
            toUpdate.setDuree(u.toTime(duree));
            bean.getService().update(toUpdate);
            init();
            MessageUtil.messageInfo("Modification effectuée");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur de modification");
        }
    }

    public List<EvtTarifLibelle> getCotations() {
        if (cotations == null) {
            init();
        }
        return cotations;
    }

    public List<TypeTarifEvt> getTypes() {
        if (types == null) {
            types = ObjetStatique.getTypeTarifEvt();
        }
        return types;
    }

    public EvtTarif getNewTarif() {
        return newTarif;
    }

    public void setNewTarif(EvtTarif newTarif) {
        this.newTarif = newTarif;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public EvtTarif getToUpdate() {
        return toUpdate;
    }

    public void setToUpdate(EvtTarif toUpdate) {
        this.toUpdate = toUpdate;
    }

}
