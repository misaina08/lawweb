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
import modeles.parametres.TypeTarifEvt;
import services.IntervenantService;
import statiques.ObjetStatique;
import utilitaire.MessageUtil;
import utilitaire.Util;

/**
 *
 * @author misa
 */
@Named(value = "ajoutIntervenantMB")
@ViewScoped
public class AjoutIntervenantMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;

    private Intervenant nouveauIntervenant = new Intervenant();
    private List<Fonction> fonctions;
    private String seuilHoraire = "00:00";
    

    /**
     * Creates a new instance of AjoutIntervenantMB
     */
    public AjoutIntervenantMB() {
    }

    public String valider() {

        try {
            Util util = new Util();
            nouveauIntervenant.setSeuilhoraire(util.toTime(seuilHoraire));

            IntervenantService intServ = new IntervenantService();
            intServ.saveIntervenant(nouveauIntervenant);
            MessageUtil.addFlashInfoMessage("Intervenant ajouté");
            return "/pages/intervenant/liste.xhtml?faces-redirect=true;";
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur d'ajout");
            return "";
        }
    }

    public Intervenant getNouveauIntervenant() {
        return nouveauIntervenant;
    }

    public void setNouveauIntervenant(Intervenant nouveauIntervenant) {
        this.nouveauIntervenant = nouveauIntervenant;
    }

    public List<Fonction> getFonctions() {
        if (fonctions == null) {
            try {
                fonctions = (List<Fonction>) (List<?>) generiqueBean.getService().find(new Fonction());
            } catch (Exception ex) {
                Logger.getLogger(AjoutIntervenantMB.class.getName()).log(Level.SEVERE, null, ex);
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
