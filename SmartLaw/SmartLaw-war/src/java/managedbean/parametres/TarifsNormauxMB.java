/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.parametres;

import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.evenement.EvtTarif;
import modeles.intervenants.FonctionTarifEvt;
import modeles.intervenants.FonctionTarifEvtLibelle;
import modeles.parametres.Fonction;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "tarifsNormauxMB")
@ViewScoped
public class TarifsNormauxMB implements Serializable {
    
    @EJB
    private GeneriqueBean bean;
    
    private List<Fonction> fonctions;
    private List<FonctionTarifEvtLibelle> tarifs = new ArrayList<FonctionTarifEvtLibelle>();
    private Integer idFonctionSelected;
    private Integer idEvtSelected;
    private List<EvtTarif> evtTarifs;

    /**
     * Creates a new instance of TarifsNormauxMB
     */
    public TarifsNormauxMB() {
    }
    
    public void onFonctionChange(Integer idFonction) {
        try {
            FonctionTarifEvtLibelle f = new FonctionTarifEvtLibelle();
            f.setIdFonction(idFonction);
            tarifs = (List<FonctionTarifEvtLibelle>) (List<?>) bean.getService().find(f);
            idFonctionSelected = idFonction;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void supprimerTarif(Integer id) {
        try {
            
            FonctionTarifEvt f = new FonctionTarifEvt();
            f.setId(id);
            
            bean.getService().delete(f);
            onFonctionChange(idFonctionSelected);
            MessageUtil.messageInfo("Suppression effectuée");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur de suppression");
        }
    }
    
    public void loadTarifs() {
        try {
            EvtTarif crit = new EvtTarif();
            crit.setType("tn");
            evtTarifs = (List<EvtTarif>) (List<?>) bean.getService().find(crit);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void ajouter() {
        try {
            FonctionTarifEvt res = new FonctionTarifEvt();
            res.setIdFonction(idFonctionSelected);
            res.setIdEvtTarif(idEvtSelected);
            bean.getService().save(res);
            
            onFonctionChange(idFonctionSelected);
            MessageUtil.messageInfo("Ajout effectué");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageWarn("Erreur d'ajout. Ce tarif est existe déjà");
        }
    }
    
    public List<Fonction> getFonctions() {
        if (fonctions == null) {
            try {
                fonctions = (List<Fonction>) (List<?>) bean.getService().find(new Fonction());
            } catch (Exception ex) {
                Logger.getLogger(TarifsNormauxMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return fonctions;
    }
    
    public List<FonctionTarifEvtLibelle> getTarifs() {
        return tarifs;
    }
    
    public void setTarifs(List<FonctionTarifEvtLibelle> tarifs) {
        this.tarifs = tarifs;
    }
    
    public List<EvtTarif> getEvtTarifs() {
        return evtTarifs;
    }
    
    public void setEvtTarifs(List<EvtTarif> evtTarifs) {
        this.evtTarifs = evtTarifs;
    }
    
    public Integer getIdFonctionSelected() {
        return idFonctionSelected;
    }
    
    public void setIdFonctionSelected(Integer idFonctionSelected) {
        this.idFonctionSelected = idFonctionSelected;
    }
    
    public Integer getIdEvtSelected() {
        return idEvtSelected;
    }
    
    public void setIdEvtSelected(Integer idEvtSelected) {
        this.idEvtSelected = idEvtSelected;
    }
    
}
