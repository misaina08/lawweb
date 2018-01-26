/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.dossier;

import ejb.DossierBean;
import ejb.GeneriqueBean;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.dossiers.Dossier;
import modeles.dossiers.DossierLibelle;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "faitsDossierMB")
@ViewScoped
public class FaitsDossierMB implements Serializable {

    @EJB
    private DossierBean dossierBean;

    @EJB
    private GeneriqueBean generiqueBean;

    private Integer idDossier;
    private DossierLibelle dossier;

    /**
     * Creates a new instance of FaitsDossierMB
     */
    public FaitsDossierMB() {
    }

    public void loadDossier() {
        try {
            DossierLibelle d = new DossierLibelle();
            d.setId(idDossier);
            dossier = (DossierLibelle) generiqueBean.getService().findById(d);
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.addFlashErrorMessage("Erreur de chargement");
        }
    }

    public void valider() {
        try {
            Dossier d = new Dossier();
            d.setId(dossier.getId());
            d = (Dossier) generiqueBean.getService().findById(d);
            d.setFaits(dossier.getFaits());
            generiqueBean.getService().update(d);
            loadDossier();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

}
