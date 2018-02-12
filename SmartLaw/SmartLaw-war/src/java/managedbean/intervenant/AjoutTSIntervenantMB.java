/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.intervenant;

import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.evenement.EvtTarif;
import modeles.intervenants.Intervenant;
import modeles.parametres.TypeTarifEvt;
import services.IntervenantService;
import statiques.ObjetStatique;
import utilitaire.MessageUtil;
import utilitaire.Util;

/**
 *
 * @author misa
 */
@Named(value = "ajoutTSIntervenantMB")
@ViewScoped
public class AjoutTSIntervenantMB implements Serializable {

    /**
     * Creates a new instance of TarifsNormauxMB
     */
    public AjoutTSIntervenantMB() {
    }

    @EJB
    private GeneriqueBean generiqueBean;

    private Integer idIntervenant;
    private Intervenant intervenant;
    private EvtTarif evtTarif = new EvtTarif();
    private String duree = "00:00";
    private List<TypeTarifEvt> typesTarif;

    public void loadIntervenant() {
        try {
            Intervenant i = new Intervenant();
            i.setId(idIntervenant);
            intervenant = (Intervenant) generiqueBean.getService().findById(i);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String valider() {
        IntervenantService service = null;
        Util util = null;
        try {
            util = new Util();
            evtTarif.setDuree(util.toTime(duree));
            service = new IntervenantService();
            service.saveTarifSpecial(evtTarif, idIntervenant);
            MessageUtil.addFlashInfoMessage("Tarif spécial ajouté");
            return "/pages/intervenant/tarifsSpeciaux.xhtml?idIntervenant=" + idIntervenant + "&faces-redirect=true;";
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur de modification");
            return "";
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

    public EvtTarif getEvtTarif() {
        return evtTarif;
    }

    public void setEvtTarif(EvtTarif evtTarif) {
        this.evtTarif = evtTarif;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public List<TypeTarifEvt> getTypesTarif() {
        if (typesTarif == null) {
            typesTarif = ObjetStatique.getTypeTarifEvt();
        }
        return typesTarif;
    }

}
