/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.facture;

import ejb.FacturationBean;
import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.dossiers.DossierLibelle;
import modeles.facturation.FactureEvtLibelle;
import modeles.facturation.FactureLibelle;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "evenementsFactureMB")
@ViewScoped
public class EvenementsFactureMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;
    @EJB
    private FacturationBean facturationBean;

    private Integer idFacture;
    private FactureLibelle facture;
    private DossierLibelle dossier;
    private List<FactureEvtLibelle> evenements;

    private Float totalDuree;

    /**
     * Creates a new instance of EvenementsFactureMB
     */
    public EvenementsFactureMB() {
    }

    public void loadFacture() {
        System.out.println("load facture");
        try {
            FactureLibelle f = new FactureLibelle();
            f.setId(idFacture);
            facture = (FactureLibelle) generiqueBean.getService().findById(f);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadDossier() {
        System.out.println("load dossier");
        try {
            DossierLibelle d = new DossierLibelle();
            d.setId(facture.getIdDossier());
            dossier = (DossierLibelle) generiqueBean.getService().findById(d);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void regler() {
        try {
            facturationBean.reglerFacture(idFacture);
            loadFacture();
            MessageUtil.messageInfo("Facture réglée");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur");
        }
    }
    public void print() {
        try {
            facturationBean.printFacture(facture);
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur d'impression");
        }
    }

    public Integer getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(Integer idFacture) {
        this.idFacture = idFacture;
    }

    public FactureLibelle getFacture() {
        return facture;
    }

    public void setFacture(FactureLibelle facture) {
        this.facture = facture;
    }

    public DossierLibelle getDossier() {
        return dossier;
    }

    public void setDossier(DossierLibelle dossier) {
        this.dossier = dossier;
    }

    public List<FactureEvtLibelle> getEvenements() {
        if (evenements == null) {
            try {
                FactureEvtLibelle f = new FactureEvtLibelle();
                f.setIdFacture(facture.getId());
                evenements = (List<FactureEvtLibelle>) (List<?>) generiqueBean.getService().find(f);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return evenements;
    }

    public Float getTotalMt() {
        Float totalMt = new Float(0);
        for (FactureEvtLibelle e : evenements) {
            System.out.println("fal " + e.getMt());
            totalMt = totalMt + e.getMt();
        }
        return totalMt;
    }

    public Float getTotalDuree() {
        return totalDuree;
    }

}
