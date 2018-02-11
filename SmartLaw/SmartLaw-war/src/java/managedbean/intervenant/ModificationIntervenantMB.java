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
import modeles.parametres.Fonction;
import services.IntervenantService;
import utilitaire.MessageUtil;
import utilitaire.Util;

/**
 *
 * @author misa
 */
@Named(value = "modificationIntervenantMB")
@ViewScoped
public class ModificationIntervenantMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;

    private List<Fonction> fonctions;
    private String seuilHoraire;
    private Integer idIntervenant;
    private Intervenant intervenant;

    /**
     * Creates a new instance of ModificationIntervenantMB
     */
    public ModificationIntervenantMB() {
    }

    public void loadIntervenant() {
        try {
            Util util = new Util();

            Intervenant i = new Intervenant();
            i.setId(idIntervenant);
            intervenant = (Intervenant) generiqueBean.getService().findById(i);
            seuilHoraire = util.addPrefix(2, "" + intervenant.getSeuilhoraire().getHours(), "0") + ":"
                    + util.addPrefix(2, "" + intervenant.getSeuilhoraire().getMinutes(), "0");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String valider() {
        try {
            Util util = new Util();
            intervenant.setSeuilhoraire(util.toTime(seuilHoraire));

            IntervenantService intServ = new IntervenantService();
            intServ.update(intervenant);
            MessageUtil.addFlashInfoMessage("Intervenant modifié");
            return "/pages/intervenant/fiche.xhtml?idIntervenant=" + idIntervenant + "&faces-redirect=true;";
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

    public List<Fonction> getFonctions() {
        if (fonctions == null) {
            try {
                fonctions = (List<Fonction>) (List<?>) generiqueBean.getService().find(new Fonction());
            } catch (Exception ex) {
                Logger.getLogger(ModificationIntervenantMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return fonctions;
    }

    public String getSeuilHoraire() {
        return seuilHoraire;
    }

    public void setSeuilHoraire(String seuilHoraire) {
        this.seuilHoraire = seuilHoraire;
    }

}
